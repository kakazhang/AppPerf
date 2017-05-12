package perf.android.com.appperf.Case.power;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.UiHelper;
import perf.android.com.appperf.common.AppPerf;

/**
 * Created by shizhy on 17-4-12.
 */

public class TestOfflinePlay {
    //uidevice instance
    private static UiDevice mDevice;
    //QiyiAppPerf instance
    private static AppPerf myAppPerf;

    @BeforeClass
    public static void setUp() throws Exception {
        Log.i(Config.TAG,"set up");
        UiHelper.getArguments();
        UiHelper.init();
        myAppPerf = UiHelper.createAppPerf(Config.sTargetPackage);

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
    }

    @Before
    public void prepare() {
        Log.i(Config.TAG,"prepare");
        myAppPerf.measureAppBootAction();
        UiHelper.snap(Config.MAIN_SCREEN_IDLE_TIMEOUT);

        //enter search screen
        Log.i(Config.TAG,"search");
        myAppPerf.measureAppSearchAction();
        UiHelper.snap(Config.PHONE_SEARCH_IDLE_TIMEOUT);
    }

    @Test
    public void TestOfflinePlay() throws Exception {
        Log.i(Config.TAG,"test");
        myAppPerf.measureAppPlayAction();
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        myAppPerf.measureAppDownloadVideo();
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    @After
    public void post() {
        Log.i(Config.TAG,"take screen shot");
        UiHelper.takeScreenshot("OfflinePlay-"+Config.sTargetPackage);
    }
}
