package perf.android.com.appperf;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import static perf.android.com.appperf.Config.TAG;

/**
 * Created by linzi on 17-5-2.
 */

public class Record {
    private static final String PKG = "com.iqiyi.player.nativemediaplayer_sample";

    public static void record(UiDevice device, boolean start) {
        String resId = "btn_record";
        UiHelper.launchApp(PKG);

        UiHelper.snap(Config.RECORD_TIMEOUT);

        UiObject2 recordBtn = device.findObject(By.res(PKG, resId));
        if (recordBtn != null) {
            Log.d(TAG, "record");
            recordBtn.click();
        }
    }
}