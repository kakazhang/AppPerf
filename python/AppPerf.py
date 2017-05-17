#!/usr/bin/env python
# coding=utf-8

##The App performance benchmark tool
# 
# @author : kakzhang
# @file   : AppPerf.py
#

# ######################################################################################
# imports
#
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals
from optparse import OptionParser
import os
import sys
import time
import subprocess
import ConfigParser
import threading
from datetime import datetime

# ######################################################################################
# default action values
#
ACTION_LAUNCH="startup"
ACTION_PLAY_HALFSCREEN="play"
ACTION_PLAY_FULLSCREEN="fullscreen"

DEFAULT_RECORD_TYPE="apprecord"
CMD_RECORD="screenrecord"
CMD_CAMERA="camera"
# ######################################################################################
#  screend record
#
class RecordThread(threading.Thread):
    def __init__(self, outname, timeout) :
        threading.Thread.__init__(self)
        self.outname = outname
        self.timeout = timeout

    def run(self):
       subprocess.Popen("adb shell screenrecord --bugreport --t %ld /sdcard/%s.mp4" %(long(self.timeout), self.outname),
       stdout = subprocess.PIPE, shell = True).communicate()  


def doScreenRecord(outname,timeout) :
    recorder = RecordThread(outname,timeout)
    recorder.start()
    return recorder

def rename_file(package, action) :
    origin_str="%s/%s/%s.mp4" %(package, action, action)
    new_str="%s/%s/%s.mp4" %(package,action, datetime.now().strftime("%Y%m%d_%H%M%S"))
    
    os.rename(origin_str, new_str)

    origin_str="%s/%s/%s.png" %(package, action, action)
    new_str="%s/%s/%s.png" %(package,action, datetime.now().strftime("%Y%m%d_%H%M%S"))
    
    os.rename(origin_str, new_str)

# ######################################################################################
# do app perf
#
def doLaunchAppPerf(package,source,timeout):
    #start record
    if source == CMD_RECORD:
        doScreenRecord(ACTION_LAUNCH,timeout)

    time.sleep(1)

    # start it
    subprocess.Popen("adb shell am instrument -w -r -e debug false -e packageName %s -e class perf.android.com.appperf.Case.TestAppLaunch -e main_idle 5000 -e record_timeout 2000 perf.android.com.appperf.test/android.support.test.runner.AndroidJUnitRunner" %(package),
       stdout = subprocess.PIPE, shell = True).communicate()

# ######################################################################################
# do app halfscreen play
#
def dohalfscreenPlay(package,source,timeout):
    if source == CMD_RECORD:
        doScreenRecord(ACTION_PLAY_HALFSCREEN,timeout)

    # start it
    
    subprocess.Popen("adb shell am instrument -w -r -e debug false -e packageName %s -e main_idle 10000 -e search_idle 2000 -e play_idle 5000 -e launch_timeout 5000 -e record_timeout 2000 -e nowaitid 1 -e class perf.android.com.appperf.Case.TestAppPlay perf.android.com.appperf.test/android.support.test.runner.AndroidJUnitRunner" %(package),
       stdout = subprocess.PIPE, shell = True).communicate()

# ######################################################################################
# kill apps
#
def killApps(package,source) :
    record_pkg = "com.iqiyi.player.nativemediaplayer_sample"
    #stop target test app
    subprocess.Popen("adb shell am force-stop %s" %(package), stdout = subprocess.PIPE, shell = True).communicate()

    #stop record app
    if source == DEFAULT_RECORD_TYPE:
       subprocess.Popen("adb shell am force-stop %s" %(record_pkg), stdout = subprocess.PIPE, shell = True).communicate()

# ######################################################################################
# pull files
#
def pullFiles(package, action, source) :
    print("package:%s,action:%s,source:%s" %(package,action,source))
   #pull png file
    subprocess.Popen("adb pull /sdcard/AppPerf/%s.png %s/%s/%s.png" %(action, package,action, action), stdout = subprocess.PIPE, shell = True).communicate()

    #subprocess.Popen("adb shell rm -r /sdcard/AppPerf/%s*.png" %(action), stdout = subprocess.PIPE, shell = True).communicate()

    #pull record video
    if source == DEFAULT_RECORD_TYPE :
        subprocess.Popen("adb  pull /sdcard/camera-test.mp4 %s/%s/%s.mp4" %(package,action,action), stdout = subprocess.PIPE, shell = True).communicate()
    elif source == CMD_RECORD :
        subprocess.Popen("adb  pull /sdcard/%s.mp4 %s/%s/%s.mp4" %(action,package,action,action), stdout = subprocess.PIPE, shell = True).communicate()


# ######################################################################################
#  prepare
#
def prepare(package,source) :
   killApps(package, source)

# ######################################################################################
# post
#
def post(package,action,source) :
    killApps(package, source)

    pullFiles(package, action,source)

    time.sleep(2)

    #os.system("python main.py %s %s %s/%s/%s.mp4" %(package,action,package, action, action))
    rename_file(package, action)

# ######################################################################################
# do app performance action
#
def doAppPerf(package, action, source, timeout) :
    #prepare for the test
    prepare(package,source)

    #launch app perf
    if action == ACTION_LAUNCH :
        doLaunchAppPerf(package,source,timeout)
    elif action == ACTION_PLAY_HALFSCREEN :
        dohalfscreenPlay(package,source, timeout)

    #post
    time.sleep(5)
    post(package, action, source)

# ######################################################################################
# main
#
def main():
    # init options
    parser = OptionParser("usage: %prog [options]")
    parser.add_option("-p", "--package", dest = "package", help = "set the package name, .e.g com.qiyi.video")
    parser.add_option("-a", "--action", dest = "action", help = "set the action name, .e.g .launch ,play.")
    parser.add_option("-t", "--timeout", dest = "timeout", help = "set the screnrecord timeout, .e.g 30s.")
    parser.add_option("-s", "--source", dest = "source", help = "set test tools, .e.g screenrecord")
    parser.add_option("-o", "--os", dest = "os", help = "set test device os")
    parser.add_option("-n", "--count", dest = "count", help = "set the count")

    (options, args) = parser.parse_args()

    pkg=options.package if options.package else "com.qiyi.video"
    action=options.action if options.action else "startup"
    timeout=options.timeout if options.timeout else 30
    source=options.source if options.source else "apprecord"
    count = long(options.count) if options.count else 1

    for i in range(count) :
        doAppPerf(pkg,action,source,timeout)

    return 0

# python entry
if __name__ == "__main__":
    sys.exit(main())

