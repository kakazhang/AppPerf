package perf.android.com.appperf.common;

/**
 * Created by kakazhang on 17-4-10.
 */
import android.os.Build;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.util.List;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.Record;
import perf.android.com.appperf.Special.AppPerfAdapter;
import perf.android.com.appperf.UiHelper;

import static perf.android.com.appperf.Config.TAG_YOUKU;
import static perf.android.com.appperf.Config.UI_WAIT_TIMEOUT;

/**
 * Created by kakazhang on 17-4-10.
 */

public class YoukuAppPerf extends AppPerf {
    private UiDevice mDevice;
    private AppPerfAdapter MiInstance = null;
    public YoukuAppPerf(UiDevice device) {
        mDevice = device;
        if (Build.MANUFACTURER.equals("Xiaomi") || Build.MANUFACTURER.equals("HUAWEI"))
            MiInstance = new AppPerfAdapter();
    }

    @Override
    public void measureAppBootAction() {
        UiHelper.launchApp(Config.sTargetPackage);
    }

    @Override
    public void measureAppPlayAction() {
        searchAndPlay();
    }

    @Override
    public void measureAppSearchAction() {
        if (MiInstance != null)
            MiInstance.measureAppSearchAction();
        else
           enterPhoneSearchActivity();
    }

    @Override
    public void measureAppFullScreenPlay() {
        enterFullScreen();
    }

    @Override
    public void measureAppDownloadVideo() {
        downloadVideo();
    }

    @Override
    public void measureAppPlayLocalVideo() {
        playLocalVideo();
    }

    @Override
    public void measureMyPage() {
        UiHelper.click("我的");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    @Override
    public void waitAdGone() {
        //wait ad gone
        UiHelper.waitTextGone("会员免广告", Config.AD_WAIT_TIMEOUT);
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    private void enterPhoneSearchActivity() {
        String resId = "home_tool_bar_search_frame";
        UiObject2 searchClickable = mDevice.findObject(By.res(Config.YOUKU_PACKAGE_NAME, resId));
        if (searchClickable != null && searchClickable.isClickable()) {
            searchClickable.click();
        } else {
            Log.e(TAG_YOUKU, "find search_layout failed\n");
        }
    }

    private void searchAndPlay() {
        String resId = "et_widget_search_text_soku";
        UiObject2 searchEdit = mDevice.findObject(By.res(Config.YOUKU_PACKAGE_NAME, resId));
        if (searchEdit != null) {
            //set text what we want to play
            searchEdit.setText(Config.TV_SEARIES);

            UiHelper.snap(Config.CLICK_TIMEOUT);

            //click tv_series chinese index
            click(Config.TV_SEARIES_CN);
            UiHelper.snap(Config.CLICK_TIMEOUT);

            Record.record(mDevice, true);
            mDevice.pressBack();
            UiHelper.snap(3000);

            if (Build.MANUFACTURER.contains("HUAWEI"))
                UiHelper.snapshotWithShellCmd("play.png");
            else
                UiHelper.snapshot("play.png");

            //play
            click(Config.SERIES_INDEX);
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);

            if (Config.NOWAITAD == 0) {
                //wait ad gone
                UiHelper.waitTextGone("会员免广告", Config.AD_WAIT_TIMEOUT);
            }
            Record.record(mDevice, false);
        } else {
            Log.e(TAG_YOUKU, "could not find search edit");
        }
    }

    private void click(String text) {
        UiObject2 series = mDevice.findObject(By.text(text));
        if (series != null) {
            series.click();
        } else {
            Log.e(TAG_YOUKU, "find " + text + " failed");
        }
    }

    private void enterFullScreen() {
        waitAdGone();
        String resId = "player_holder_all";
        UiObject2 videoView = mDevice.findObject(By.res(Config.YOUKU_PACKAGE_NAME,resId));
        if(videoView != null) {
            String fullResId = "fullscreen_btn_layout";
            BySelector fullBtnSelector = By.res(Config.YOUKU_PACKAGE_NAME, fullResId);
            UiObject2 fullBtn = mDevice.findObject(fullBtnSelector);
            if(fullBtn != null && fullBtn.isClickable()) {
                Log.i(Config.TAG,"click full button");
                fullBtn.click();
            }else{
                Log.i(Config.TAG,"click video view");
                videoView.click();
                UiHelper.snap(Config.CLICK_TIMEOUT);
                if(mDevice.wait(Until.hasObject(fullBtnSelector),Config.UI_WAIT_TIMEOUT)){
                    fullBtn = mDevice.findObject(fullBtnSelector);
                    fullBtn.click();
                    UiHelper.snap(UI_WAIT_TIMEOUT);
                }else{
                    Log.e(Config.TAG, "fail waiting full button");
                }
            }
        }else{
            Log.e(Config.TAG, "can not find video view");
        }
    }

    private void downloadVideo() {
        String resId = "detail_card_new_download";
        UiObject2 download = mDevice.findObject(By.res(Config.YOUKU_PACKAGE_NAME, resId));
        if(download != null) {
            download.click();
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.click(Config.SERIES_INDEX);
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.click("确定缓存");
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.click("查看缓存");
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);

            UiHelper.waitTextGone("正在缓存", Config.DOWNLOAD_TIMEOUT);
            String resEpisode = "title_layout";
            UiObject2 ep = mDevice.findObject(By.res(Config.YOUKU_PACKAGE_NAME,resEpisode));
            if(ep != null){
                ep.click();
                UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                String resEpList = "title_layout";
                List<UiObject2> epList = mDevice.findObjects(By.res(Config.YOUKU_PACKAGE_NAME,resEpList));
                if(epList != null && !epList.isEmpty()){
                    epList.get(0).click();
                    UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                    //wait ad gone
                    UiHelper.waitTextGone("会员免广告", Config.AD_WAIT_TIMEOUT);
                }
            }
        }
    }

    private void playLocalVideo() {
        UiHelper.click("我的");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.click("离线中心");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.click("观看本地视频");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.click("720p_h264.f4v");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }
}
