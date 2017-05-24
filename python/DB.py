#!/usr/bin/env python
# coding=utf-8

import os
import sys
import MySQLdb
import traceback
import time
from optparse import OptionParser

DB_NAME="DB_APPPERF"
TABLE_NAME="TABLE_APPPERF"

PKG_IQIYI="com.qiyi.video"
PKG_TENCENT="com.tencent.qqlive"
PKG_YOUKU="com.youku.phone"

def checkTableExists(conn, tablename):
    cursor = conn.cursor()
    cursor.execute("""
        SELECT COUNT(*)
        FROM information_schema.tables
        WHERE table_name = '{0}'
        """.format(tablename.replace('\'', '\'\'')))
    if cursor.fetchone()[0] == 1:
        cursor.close()
        return True

    cursor.close()
    return False

def createDB():
    conn = MySQLdb.connect("localhost", "root", "111111")
    #set auto commit
    conn.autocommit(1)
    
    cursor=conn.cursor()

    #cursor.execute("DROP DATABASE IF EXISTS %s" %DB_NAME)
    cursor.execute("CREATE DATABASE IF NOT EXISTS %s" %DB_NAME)
    conn.select_db(DB_NAME)

    sql = """CREATE TABLE TABLE_APPPERF (
        PKG_NAME CHAR(20) NOT NULL,
        ACTION CHAR(20),
        COST FLOAT,
        VERSION CHAR(20),
        TIMESTAMP CHAR(36),
        BUILDNAME CHAR(36)
    )"""
    
    #cursor.execute("DROP TABLE IF EXISTS %s" %TABLE_NAME)
    if checkTableExists(conn, TABLE_NAME) == False :
            print "create table %s" %(TABLE_NAME)
            # Create table as per requirement
            cursor.execute(sql)

    conn.close()

# #####################################################################################
#  save app time
# ####
def saveTime(package, action, cost, version, timestamp,buildversion) :
    db = MySQLdb.connect(host="localhost", user="root", passwd="111111", db=DB_NAME)
    
    # prepare a cursor object using cursor() method
    cursor = db.cursor()
    value=[package,action,float(cost),version, timestamp,buildversion]

    try:
        # Execute the SQL command
        # Commit your changes in the database
        cursor.execute("insert into TABLE_APPPERF values(%s,%s,%s,%s,%s,%s)",value)
        db.commit()

    except Exception, e:
        exstr = traceback.format_exc()
        print exstr
        #Rollback in case there is any error
        db.rollback()

    # disconnect from server
    db.close()

# #####################################################################################
# query time
#
def queryTime(package, action) :
    db = MySQLdb.connect(host="localhost", user="root", passwd="111111", db=DB_NAME)
    
    # prepare a cursor object using cursor() method
    cursor = db.cursor()    

    sql = "SELECT * FROM TABLE_APPPERF"

    try:
	# 执行SQL语句
	cursor.execute(sql)
	# 获取所有记录列表
	results = cursor.fetchall()
	for row in results:
		pkg = row[0]
		action = row[1]
		cost = row[2]
                version=row[3]
                timestamp=row[4]
		# 打印结果
		print "pkg=%s,action=%s,cost=%f,version=%s,timestamp=%s" %(pkg, action, cost,version,timestamp)

    except Exception, e:
	exstr = traceback.format_exc()
	print exstr

    # 关闭数据库连接
    db.close()

def getBuildVersion(package) :
    if package == PKG_IQIYI :
        prefix = "iqiyi"
    elif package == PKG_TENCENT :
        prefix = "qqlive"
    else :
        prefix = "youku"

    files = os.listdir("./apk")
    for file in files :
        if not os.path.isdir(file) and file.find(prefix) != -1 :
            return file

def main() :
    # init options
    parser = OptionParser("usage: %prog [options]")
    parser.add_option("-p", "--package", dest = "package", help = "set the package name, .e.g com.qiyi.video")
    parser.add_option("-a", "--action", dest = "action", help = "set the action name, .e.g .launch ,play.")
    parser.add_option("-t", "--times", dest = "times", help = "set calculated times, .e.g 0.599.")
    parser.add_option("-v", "--version", dest = "version", help = "set apk version, .e.g 1.0.")
    parser.add_option("-i", "--buildversion", dest = "buildversion", help = "set apk build version, .e.g 1.0.")
    (options, args) = parser.parse_args()

    pkg=options.package if options.package else "com.qiyi.video"
    action=options.action if options.action else "startup"
    times=options.times if options.times else 0
    version=options.version if options.version else "8.4.0"
    buildversion=options.buildversion if options.buildversion else getBuildVersion(pkg)

    #try to create DB
    createDB()

    timestamp = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))

    if times == "None" :
        times = 0
    
    saveTime(pkg, action, times, version, timestamp,buildversion)

#queryTime(pkg, action)

    return 0

if __name__ == "__main__" :
    sys.exit(main())

