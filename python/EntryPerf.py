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
import re
import sys
import time
import subprocess
import ConfigParser
import threading
import ctypes

PKG_QIYI="com.qiyi.video"
PKG_TENCENT="com.tencent.qqlive"
PKG_YOUKU="com.youku.phone"

ACTION_STARTUP="startup"
ACTION_PLAY="play"
# ######################################################################################
# main
#
def main():
   package=PKG_QIYI

   os.system("python AppPerf.py -p %s -a %s" %(package, ACTION_STARTUP))
   os.system("python AppPerf.py -p %s -a %s" %(package, ACTION_PLAY))

   package=PKG_TENCENT

   os.system("python AppPerf.py -p %s -a %s" %(package, ACTION_STARTUP))
   os.system("python AppPerf.py -p %s -a %s" %(package, ACTION_PLAY))

   package=PKG_YOUKU

   os.system("python AppPerf.py -p %s -a %s" %(package, ACTION_STARTUP))
   os.system("python AppPerf.py -p %s -a %s" %(package, ACTION_PLAY))

   return 0
   
# python entry
if __name__ == "__main__":
    sys.exit(main())
