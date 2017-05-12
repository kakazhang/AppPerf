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

public class TestAppPlay {
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
    }

    @Before
    public void prepare() {
        myAppPerf.measureAppBootAction();
        UiHelper.snap(Config.MAIN_SCREEN_IDLE_TIMEOUT);

        //enter search screen
        myAppPerf.measureAppSearchAction();
        UiHelper.snap(Config.PHONE_SEARCH_IDLE_TIMEOUT);
    }

    @Test
    public void TestAppPlay() throws Exception {
        myAppPerf.measureAppPlayAction();
        //UiHelper.snap(Config.PLAY_IDLE_TIMEOUT);
    }

    @After
    public void post() {
        mDevice.pressHome();
    }


}
