#!/usr/bin/env python
# coding=utf-8

import sys 
import MySQLdb
import traceback
import time
from optparse import OptionParser

DB_NAME="DB_APPPERF"
TABLE_NAME="TABLE_APPPERF"
# ####################################################################################
# create a sqlite
#
def createSQL() :
    conn = MySQLdb.connect("localhost", "root", "111111")
    #set auto commit
    conn.autocommit(1)
    
    cursor=conn.cursor()

    #cursor.execute("DROP DATABASE IF EXISTS %s" %DB_NAME)
    cursor.execute("CREATE DATABASE IF NOT EXISTS %s" %DB_NAME)
    conn.select_db(DB_NAME)

    # Create table as per requirement
    sql = """CREATE TABLE TABLE_APPPERF (
        PKG_NAME CHAR(20) NOT NULL,
        ACTION CHAR(20),
        COST FLOAT,
        VERSION CHAR(20),
        TIMESTAMP CHAR(36),
        BUILDNAME CHAR(36))"""

    cursor.execute("DROP TABLE IF EXISTS TABLE_APPPERF")
    
    #create table
    cursor.execute(sql)

    # disconnect from server
    conn.close()

# #####################################################################################
#  save app time
# ####
def saveTime(package, action, cost, version,timestamp) :
    db = MySQLdb.connect(host="localhost", user="root", passwd="111111", db="DB_APPPERF")
    
    # prepare a cursor object using cursor() method
    cursor = db.cursor()
    value=[package,action,float(cost),float(version),timestamp]

    try:
        # Execute the SQL command
        # Commit your changes in the database
        cursor.execute("insert into TABLE_APPPERF values(%s,%s,%s,%s,%s)",value)
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
def queryTime() :
    db = MySQLdb.connect(host="localhost", user="root", passwd="111111", db="DB_APPPERF")
    
    # prepare a cursor object using cursor() method
    cursor = db.cursor()    

    sql="select * from TABLE_APPPERF where timestamp>='2017-05-23 18:00:00' and timestamp<'2017-05-23 20:00:00'"
#sql="select * from TABLE_APPPERF"
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

def drop_db() :
    conn = MySQLdb.connect("localhost", "root", "111111")

    cursor=conn.cursor()
    cursor.execute("DROP DATABASE IF EXISTS %s" %(DB_NAME))

def main() :
    # init options
    parser = OptionParser("usage: %prog [options]")
    parser.add_option("-c", "--createDB", dest = "createDB", help = "create DB")
    parser.add_option("-d", "--dropDB", dest = "dropDB", help = "drop DB")
    parser.add_option("-s", "--saveDB", dest = "saveDB", help = "save DB")
    parser.add_option("-q", "--queryDB", dest = "queryDB", help = "query DB")

    (options, args) = parser.parse_args()
    
    createDB=int(options.createDB) if options.createDB else 0
    dropDB=int(options.dropDB) if options.dropDB else 0
    saveDB=int(options.saveDB) if options.saveDB else 0
    queryDB=int(options.queryDB) if options.queryDB else 0
   
    if createDB == 1 :
       createSQL()

    if dropDB == 1 :
       drop_db()

    if queryDB == 1 :
       queryTime()

    package="com.qiyi.video"
    action="startup"
    timestamp = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
    cost=7.810
    version=1.0
    
    if saveDB == 1:
       saveTime(package,action,cost,version,timestamp)

    return 0

# python entry
if __name__ == "__main__":
        sys.exit(main())
