package perf.android.com.appperf.Case.power;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.UiHelper;
import perf.android.com.appperf.common.AppPerf;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by shizhy on 17-4-12.
 */

public class TestBackgroundStanby {
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
        mDevice.pressHome();
    }

    @Before
    public void prepare() throws Exception {
        myAppPerf.measureAppBootAction();
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    @Test
    public void TestBackgroundStandby() throws Exception {
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        mDevice.drag(mDevice.getDisplayWidth()/2,mDevice.getDisplayHeight()/2,
                mDevice.getDisplayWidth()/2,0,10);
        //do nothing
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        mDevice.pressHome();
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    @After
    public void post() {
        UiHelper.takeScreenshot("BackgroundStandby-"+Config.sTargetPackage);
    }
}
