package perf.android.com.appperf;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import perf.android.com.appperf.common.AppPerf;
import perf.android.com.appperf.policy.PolicyManager;

/**
 * Created by kakazhang on 17-4-10.
 */

public class TestMain {
    private static UiDevice mDevice;

    @Before
    public void setUp() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiHelper.init();

        UiHelper.launchHome();
    }

    @Test
    public void testQiyi() throws Exception {
        start();
        snap(1000);
    }

    private void snap(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        //IQIYI app and Tencent App Performance
        AppPerf qiyiPerf = PolicyManager.makeNewPerf(mDevice, Config.QIYI_PACKAGE_NAME);
//        AppPerf qqPerf = PolicyManager.makeNewPerf(mDevice, Config.TENCENT_PACKAGE_NAME);
//        AppPerf youkuPerf = PolicyManager.makeNewPerf(mDevice, Config.YOUKU_PACKAGE_NAME);

        qiyiPerf.run();

//        UiHelper.launchHome();
//        Config.sTargetPackage = Config.TENCENT_PACKAGE_NAME;
//        qqPerf.run();
//
//        UiHelper.launchHome();
//        Config.sTargetPackage = Config.YOUKU_PACKAGE_NAME;
//        youkuPerf.run();
    }

    @After
    public void post() {
        Log.i("AppPerf","Testmain-post");
        mDevice.pressHome();
    }
}
