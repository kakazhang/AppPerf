package perf.android.com.appperf.Case;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.Record;
import perf.android.com.appperf.UiHelper;
import perf.android.com.appperf.common.AppPerf;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by kakazhang on 17-4-11.
 */

public class TestAppLaunch {
    //uidevice instance
    private static UiDevice mDevice;
    //QiyiAppPerf instance
    private static AppPerf myAppPerf;

    @BeforeClass
    public static void setUp() {
        UiHelper.getArguments();
        UiHelper.init();
        myAppPerf = UiHelper.createAppPerf(Config.sTargetPackage);

        assertNotNull(myAppPerf);
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Before
    public void prepare() throws Exception {
        //do nothing
    }

    @Test
    public void TestLaunch() throws Exception {
        //start record
        Record.record(mDevice, true);
        UiHelper.launchHome();
        UiHelper.snap(2000);

        if (Build.MANUFACTURER.equals("HUAWEI"))
           UiHelper.snapshotWithShellCmd("startup.png");
        else
           UiHelper.snapshot("startup.png");

        myAppPerf.measureAppBootAction();
        //wait some time before main screen go to idle
        UiHelper.snap(Config.MAIN_SCREEN_IDLE_TIMEOUT);

        //stop record
        Record.record(mDevice, false);
    }

    @After
    public void post() {
        //do nothing
        mDevice.pressHome();
    }
}
