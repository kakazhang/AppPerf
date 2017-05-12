package perf.android.com.appperf.Case;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.UiHelper;
import perf.android.com.appperf.common.AppPerf;
import perf.android.com.appperf.policy.PolicyManager;

/**
 * Created by kakazhang on 17-4-11.
 */

public class TestAppSearch {
    //uidevice instance
    private static UiDevice mDevice;
    //QiyiAppPerf instance
    private static AppPerf myAppPerf;

    @BeforeClass
    public static void setUp() throws Exception {
        UiHelper.getArguments();
        UiHelper.init();
        myAppPerf = UiHelper.createAppPerf(Config.sTargetPackage);

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
    }

    @Before
    public void prepare() {
        myAppPerf.measureAppBootAction();
        UiHelper.snap(Config.MAIN_SCREEN_IDLE_TIMEOUT);
    }

    @Test
    public void TestAppSearch() throws Exception {
        myAppPerf.measureAppSearchAction();
        //wait for some time before phone search activity go idle
        UiHelper.snap(Config.PHONE_SEARCH_IDLE_TIMEOUT);
    }

    @After
    public void post() {
        mDevice.pressHome();
    }
}
