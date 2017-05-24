#!/usr/bin/python2.7
# -*- coding: utf-8 -*-
import os
import sys
import string
import datetime
import ftplib
import subprocess
import fcntl
import traceback
import urllib
class ApkDownloader:
    def __init__(self, apk_dir):
    
        self.ftp_host = '10.121.76.50'
        self.ftp_port = '21'
        self.ftp_user = 'mobile_ftp'
        self.ftp_pass = 'mobile_ftp'
        self.ftp_timeout = 20
        self.qiyi_apk_path = '/android/phone/app/com.qiyi.video/debug/daily/qos/'
        
        self.apk_dir = apk_dir

        # create dir recursively
        if not os.path.isdir(apk_dir):
            os.makedirs(apk_dir)
    #end of __init__()
    
    def get_latest_apk_path(self, ftp):
        files = ftp.nlst(self.qiyi_apk_path)
        latest_month = self.get_latest_dir(files)
        month_dir = self.qiyi_apk_path + latest_month
        
        day_files = ftp.nlst(month_dir)
        latest_day = self.get_latest_dir(day_files)
        time_dir = month_dir + "/" + latest_day
        
        time_files = ftp.nlst(time_dir)
        latest_time = self.get_latest_dir(time_files)
        self.qiyi_apk_buildtime = latest_month + latest_day + latest_time
        
        latest_path = time_dir + "/" + latest_time + "/"
        print "latest apk path dir:" + latest_path
        return latest_path
        
    def get_qiyi_buildtime():
        return self.qiyi_apk_buildtime
        
        
    def get_latest_dir(self, dirs):
        latest = 0
        for file in dirs:
            file_date = int(file.split('/')[-1])
            if file_date > latest:
                latest = file_date
        print "latest date dir:" + str(latest)
        return str(latest)
        

    def check_ftp_file(self, ftp, name):
        filename = name + '.gz'
        remote_dir = "/" + name
        remote_files = ftp.nlst(remote_dir)
        if filename in remote_files and '_md5' in remote_files:
            return True
        else:
            return False

    def get_log_names(self, ftp):
        print("==========")
        print("check logs")
        print("==========")
        names = []
        name = ''
        n = ''
        t = self.start_time
        while t < self.end_time:
            #try 1 hour data
            if t.minute == 0 and self.end_time - t >= datetime.timedelta(hours=1):
                n = t.strftime("%Y-%m-%d_%H")
                if self.check_ftp_file(ftp, n) == True:
                    name = n
                    t += datetime.timedelta(hours=1)

            #try 10 minutes data
            #if name == '':
            #    n = t.strftime("%Y-%m-%d_%H_%M")
            #    if self.check_ftp_file(ftp, n) == True:
            #        name = n
            #        t += datetime.timedelta(minutes=10)

            if name == '':
                #t += datetime.timedelta(minutes=10) #try next
                t += datetime.timedelta(hours=1) #try next
                print(n + " ... missing on FTP")
            else:
                if not os.path.isfile(self.log_dir + name + ".gz"):
                    names.append(name)
                    print(name + " ... need to be downloaded")
                else:
                    print(name + " ... OK")
                name = ''
            
        print('')
        return names
    #end of get_log_names()
    
    def download_tencent(self):
        url = "http://mcgi.v.qq.com/commdatav2?cmd=4&confid=848&platform=aphone"
        urllib.urlretrieve(url, self.apk_dir + "/qqlive.apk")

    def download_youku(self):
        url = "http://down2.uc.cn/youku/down.php?pub=ab235fdcb823d83f&spm=a2hmb.20008760.m_221044.5~5~5~5~5~5~P~A"
        urllib.urlretrieve(url, self.apk_dir + "/youku.apk")

    def data_handler(self, data):
        self.file.write(data)
    #end of data_handler()
    
    def go(self):
        lock_pathname = os.path.dirname(os.path.realpath(__file__)) + "/apk.download.lock"
        lock = open(lock_pathname, 'w+')
        fcntl.flock(lock, fcntl.LOCK_EX)
        
        self.download_tencent()
        self.download_youku()

        ftp = ftplib.FTP()

        # ftp connect
        try:
            ftp.connect(self.ftp_host, self.ftp_port, self.ftp_timeout)
        except Exception as e:
            print("FTP connect & login failed: " + str(e))
            lock.close()
            return False

        # ftp login
        try:
            ftp.login(self.ftp_user, self.ftp_pass)
        except Exception as e:
            print("FTP connect & login failed: " + str(e))
            lock.close()
            return False
            
        #download latest apk
        lates_apk_dir = self.get_latest_apk_path(ftp)
        latest_files = ftp.nlst(lates_apk_dir)
        remote_file = ""
        for latest_file in latest_files:
            if latest_file.endswith(".apk"):
                remote_file = latest_file
                
        print "remote file:" + remote_file
        
        local_file = self.apk_dir + "/iqiyi-" + self.qiyi_apk_buildtime  + ".apk"
        downloading_local_file = local_file + ".downloading"
        
        #remove the exist temp gz file
        if os.path.isfile(downloading_local_file):
            os.remove(downloading_local_file)
        try:
            #download apk file
            with open(downloading_local_file, 'wb') as file:
                self.file = file
                ftp.retrbinary('RETR ' + remote_file, self.data_handler)
        except Exception as e:
            sys.stdout.write("failed (" + str(e) + ")\n")
            sys.stdout.flush()
            if os.path.isfile(downloading_local_file):
                os.remove(downloading_local_file)
        else:
            # rename to .apk file
            os.rename(downloading_local_file, local_file)

        try:
            ftp.quit()
        except Exception as e:
            print("FTP quit failed: " + str(e))
            lock.close()
            return False

        lock.close()
        return True
    #end of go()
# end of class


def main(argv):
    downloader = ApkDownloader("./apk/")
    downloader.go()

if __name__ == '__main__':
    main(sys.argv)
