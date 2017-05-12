Description:
     An android performance automation tools.
     policy directory: 
          an abstract factory to create AppPerf instance: PolicyManager.makeNewPerf(UiDevice device, String packageName)
     
     common directory:
          AppPerf announcement and its child classes: QiyiAppPerf, TencentAppPerf, YoukuAppPerf and so on.
     
     case directory:
          Any cases we want to test can be implemented here.


Usage:
     1. build and install apk
         $ adb push app-debug.apk /data/local/tmp/perf.android.com.appperf
         $ adb shell pm install -r "/data/local/tmp/perf.android.com.appperf"

     2. run (for example test tencent qqlive launch):
         adb shell am instrument -w -r   -e debug false -e packageName com.tencent.qqlive -e class perf.android.com.appperf.TestAppLaunch    
               perf.android.com.appperf.test/android.support.test.runner.AndroidJUnitRunner
     
         1) -e PackageName: select packageName we want to test
         2) -e class perf.android.com.appperf.TestAppLaunch: select which case we want to use

