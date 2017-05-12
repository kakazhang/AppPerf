package perf.android.com.appperf.Special;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import java.io.IOException;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.UiHelper;
import perf.android.com.appperf.common.AppPerf;

/**
 * Created by linzi on 17-5-5.
 */

public class AppPerfAdapter extends AppPerf {
    private UiDevice mDevice;

    public AppPerfAdapter() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Override
    public void run() {
        //do nothing
    }

    @Override
    public void measureAppBootAction() {
        UiHelper.launchApp(Config.sTargetPackage);
    }

    @Override
    public void measureAppSearchAction() {
        if (Config.sTargetPackage.equals(Config.YOUKU_PACKAGE_NAME)) {
            String cmd = "am start -n com.youku.phone/com.soku.searchsdk.activity.SearchActivity";
            try {
                mDevice.executeShellCommand(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UiHelper.snap(Config.CLICK_TIMEOUT);
        }
    }

    @Override
    public void measureAppPlayAction() {
        if (Config.sTargetPackage.equals(Config.YOUKU_PACKAGE_NAME)) {
            String cmd = "input 人民的名义";
            try {
                mDevice.executeShellCommand(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void measureAppFullScreenPlay() {

    }

    @Override
    public void measureAppDownloadVideo() {

    }

    @Override
    public void measureAppPlayLocalVideo() {

    }

    @Override
    public void measureMyPage() {

    }

    @Override
    public void waitAdGone() {

    }
}
