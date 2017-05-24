package perf.android.com.appperf;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import perf.android.com.appperf.common.AppPerf;
import perf.android.com.appperf.policy.PolicyManager;

/**
 * Created by kakazhang on 17-4-10.
 */

public class UiHelper {
    private static UiDevice mDevice;
    private static Context mContext;
    private static PackageManager mPm;
    private static final String TAG = "AppPerf";
    private static final String RECORD_PKG = "com.iqiyi.player.nativemediaplayer_sample";

    public static void init() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        mContext = InstrumentationRegistry.getContext();
        mPm = mContext.getPackageManager();
    }

    public static void launchHome() {
        Log.i(Config.TAG, "{Press} Home");

        UiDevice uidevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        uidevice.pressHome();
        String launcherPackage = uidevice.getLauncherPackageName();
        if (!Build.MANUFACTURER.equals("Xiaomi"))
        uidevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), Config.sLaunchTimeout);
    }

    public static boolean launchApp(String packageName) {
        boolean canBeLaunched = false;

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi") && launchForXiaomi(packageName))
            return true;

        Intent intent = mPm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            canBeLaunched = true;

            Log.d(Config.TAG, "intent:" + intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // Make sure each launch is a new task
            mContext.startActivity(intent);
            if (!Build.MANUFACTURER.equals("Xiaomi"))
                mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), Config.sLaunchTimeout);
            else {
                if (packageName.equals(RECORD_PKG))
                    UiHelper.snap(2000);
                else
                    UiHelper.snap(Config.sLaunchTimeout);
            }
        } else {
            String err = String.format("(%s) No launchable Activity.\n", packageName);
            Log.e(Config.TAG, err);
            Bundle bundle = new Bundle();
            bundle.putString("ERROR", err);
            InstrumentationRegistry.getInstrumentation().finish(1, bundle);
        }

        return canBeLaunched;
    }

    public static boolean launchForXiaomi(String packageName) {
        String cmd = null;
        if (packageName.equals(Config.QIYI_PACKAGE_NAME))
            cmd = "am start -n com.qiyi.video/.WelcomeActivity";
        else if (packageName.equals(Config.TENCENT_PACKAGE_NAME))
            cmd = "am start -n com.tencent.qqlive/.ona.activity.WelcomeActivity";
        else if (packageName.equals(Config.YOUKU_PACKAGE_NAME))
            cmd = "am start -n com.youku.phone/.ActivityWelcome";

        if (cmd == null)
            return false;

        try {
            mDevice.executeShellCommand(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UiHelper.snap(Config.sLaunchTimeout);
        return true;
    }

    public static boolean waitResourceGone(String packageName, String resId, long timeout) {
        BySelector view = By.res(packageName, resId);
        UiObject2 ui = mDevice.findObject(view);

        if(ui != null){
            Log.i(TAG, "res exists");
            if(mDevice.wait(Until.gone(view),timeout)) {
                Log.i(TAG, "wait target gone");
                return true;
            }else {
                Log.i(TAG, "wait fail");
                return false;
            }
        }else{
            Log.i(TAG, "no res exists");
            return true;
        }
    }

    public static boolean waitTextGone(String text, long timeout) {
        BySelector view = By.text(text);
        UiObject2 ui = mDevice.findObject(view);

        if(ui != null){
            Log.i(TAG, "text exists:"+text);
            if(mDevice.wait(Until.gone(view),timeout)) {
                Log.i(TAG, "wait text gone");
                return true;
            }else {
                Log.i(TAG, "wait fail");
                return false;
            }
        }else{
            Log.i(TAG, "no text exists");
            return true;
        }
    }

    public static boolean click(String text) {
        UiObject2 series = mDevice.findObject(By.text(text));
        if (series != null) {
            series.click();
            return true;
        } else {
            Log.e(TAG, "find " + text + " failed");
            return false;
        }
    }

    public static boolean clickRes(String packageName,String resId) {
        UiObject2 series = mDevice.findObject(By.res(packageName,resId));
        if (series != null) {
            series.click();
            return true;
        } else {
            Log.e(TAG, "find " + resId + " failed");
            return false;
        }
    }

    public static boolean takeScreenshot(String caseName){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        File file = new File("/sdcard/power/"+date +"-"+caseName+".png");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(mDevice.takeScreenshot(file)){
            return true;
        }else{
            Log.i(TAG,"fail screen shot");
            return false;
        }
    }

    public static void snapshot(String name) {
        File dir = new File("/sdcard/AppPerf");
        if (!dir.exists())
            dir.mkdirs();
        File file = new File("/sdcard/AppPerf/" + name);
        mDevice.takeScreenshot(file, 0.2f, 80);
    }

    public static void snapshotWithShellCmd(String name) {
        File dir = new File("/sdcard/AppPerf");
        if (!dir.exists())
            dir.mkdirs();

        try {
            mDevice.executeShellCommand("screencap " + "/sdcard/AppPerf/" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UiHelper.snap(5000);
    }

    public static void snap(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void getArguments() {
        Bundle args = InstrumentationRegistry.getArguments();
        if (args.getString(Config.TEST_PACKAGE) != null)
            Config.sTargetPackage = args.getString(Config.TEST_PACKAGE);
        if (args.getString("main_idle") != null)
            Config.MAIN_SCREEN_IDLE_TIMEOUT = Long.parseLong(args.getString("main_idle"));
        if (args.getString("search_idle") != null)
            Config.PHONE_SEARCH_IDLE_TIMEOUT = Long.parseLong(args.getString("search_idle"));
        if (args.getString("play_idle") != null)
            Config.PLAY_IDLE_TIMEOUT = Long.parseLong(args.getString("play_idle"));
        if (args.getString("click_timeout") != null)
            Config.CLICK_TIMEOUT  = Long.parseLong(args.getString("click_timeout"));
        if (args.getString("record_timeout") != null)
            Config.RECORD_TIMEOUT = Long.parseLong(args.getString("record_timeout"));
        if (args.getString("nowaitad") != null)
            Config.NOWAITAD = Integer.parseInt(args.getString("nowaitad"));
        if (args.getString("launch_timeout") != null)
            Config.sLaunchTimeout = Long.parseLong(args.getString("launch_timeout"));
        if (args.getString("record_interval") != null)
            Config.RECORD_INTERVAL = Long.parseLong(args.getString("record_interval"));
        if (args.getString("ui_wait") != null)
            Config.UI_WAIT_TIMEOUT = Long.parseLong(args.getString("ui_wait"));
    }

    public static AppPerf createAppPerf(String myPackage) {
        AppPerf myAppPerf = null;

        if (myPackage.equals(Config.QIYI_PACKAGE_NAME))
            myAppPerf = PolicyManager.makeNewPerf(mDevice, Config.QIYI_PACKAGE_NAME);
        else if (myPackage.equals(Config.TENCENT_PACKAGE_NAME))
            myAppPerf = PolicyManager.makeNewPerf(mDevice, Config.TENCENT_PACKAGE_NAME);
        else if (myPackage.equals(Config.YOUKU_PACKAGE_NAME))
            myAppPerf = PolicyManager.makeNewPerf(mDevice, Config.YOUKU_PACKAGE_NAME);

        return myAppPerf;
    }

}
