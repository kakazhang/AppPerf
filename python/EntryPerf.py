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
import logging
import subprocess
import ConfigParser
#from apscheduler.schedulers.blocking  import BlockingScheduler

PKG_QIYI="com.qiyi.video"
PKG_TENCENT="com.tencent.qqlive"
PKG_YOUKU="com.youku.phone"

ACTION_STARTUP="startup"
ACTION_PLAY="play"
LOOP_COUNT=1

# ######################################################################################
# download from server
#
def downloadApk() :
    if os.path.isdir("apk") :
        os.system("rm -r ./apk/iqiyi-*.apk")

    #start download
    os.system("python apk_downloader.py")

# ######################################################################################
# download from server
#
def installApk() :
   files = os.listdir("./apk")
   for file in files :
       print("install :%s" %(file))
       os.system("adb install -r apk/%s" %(file))

def appPerf() :
    COUNT=5
    package=PKG_QIYI
        
    os.system("python AppPerf.py -p %s -a %s -n %d" %(package, ACTION_STARTUP,COUNT))
    os.system("python AppPerf.py -p %s -a %s -n %d" %(package, ACTION_PLAY,COUNT))
                
    package=PKG_TENCENT
                    
    os.system("python AppPerf.py -p %s -a %s -n %d" %(package, ACTION_STARTUP,COUNT))
    os.system("python AppPerf.py -p %s -a %s -n %d" %(package, ACTION_PLAY,COUNT))
                            
    package=PKG_YOUKU
                                
    os.system("python AppPerf.py -p %s -a %s -n %d" %(package, ACTION_STARTUP,COUNT))
    os.system("python AppPerf.py -p %s -a %s -n %d" %(package, ACTION_PLAY,COUNT))

# ######################################################################################
# doAppPerf function
#
def doAppPerf() :
    #downloadApk()
    #installApk()
    
    for i in range(long(LOOP_COUNT)) :
         appPerf()

#end of doAppPerf

def main():
    doAppPerf();

    return 0
   
# python entry
if __name__ == "__main__":
    sys.exit(main())
