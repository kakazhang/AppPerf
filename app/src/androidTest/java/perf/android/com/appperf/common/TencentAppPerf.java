package perf.android.com.appperf.common;

/**
 * Created by kakazhang on 17-4-10.
 */
import android.nfc.Tag;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.util.List;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.Record;
import perf.android.com.appperf.UiHelper;

import static perf.android.com.appperf.Config.TAG_TENCENT;

/**
 * Created by kakazhang on 17-4-10.
 */

public class TencentAppPerf extends AppPerf {
    private UiDevice mDevice;

    public TencentAppPerf(UiDevice device) {

        mDevice = device;
    }

    @Override
    public void measureAppBootAction() {
        UiHelper.launchApp(Config.sTargetPackage);
    }

    @Override
    public void measureAppSearchAction() {
        enterPhoneSearchActivity();
    }

    @Override
    public void measureAppFullScreenPlay() {
        enterFullScreen();
    }

    @Override
    public void measureAppDownloadVideo() {
        downloadVideoAndPlay();
    }

    @Override
    public void measureAppPlayLocalVideo() {
        playLocalVideo();
    }

    @Override
    public void measureMyPage() {
        if(!UiHelper.clickRes(Config.TENCENT_PACKAGE_NAME,"radio_setting")){
            mDevice.click(mDevice.getDisplayWidth()-5,mDevice.getDisplayHeight()-5);
        }
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    @Override
    public void waitAdGone() {
        //wait ad gone
        UiHelper.waitTextGone("VIP可关闭广告", Config.AD_WAIT_TIMEOUT);
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

    @Override
    public void measureAppPlayAction() {
        searchAndPlay();
    }

    private void enterPhoneSearchActivity() {
        String resId = "search_layout";
        UiObject2 searchClickable = mDevice.findObject(By.res(Config.TENCENT_PACKAGE_NAME, resId));
        if (searchClickable != null && searchClickable.isClickable()) {
            Log.i(TAG_TENCENT, "click search");
            searchClickable.click();
        } else {
            Log.e(TAG_TENCENT, "find search_layout failed\n");
        }
    }

    private void searchAndPlay() {
        String resId = "search_edit";
        UiObject2 searchEdit = mDevice.findObject(By.res(Config.TENCENT_PACKAGE_NAME, resId));
        if (searchEdit != null) {
            Log.i(TAG_TENCENT,"set search text");
            //set text what we want to play
            searchEdit.setText(Config.TV_SEARIES);

            UiHelper.snap(Config.CLICK_TIMEOUT);

            //click tv_series chinese index
            click(Config.TV_SEARIES_CN);
            UiHelper.snap(Config.CLICK_TIMEOUT);

            Record.record(mDevice, true);
            mDevice.pressBack();
            UiHelper.snap(Config.RECORD_INTERVAL);

            if (Build.MANUFACTURER.contains("HUAWEI"))
                UiHelper.snapshotWithShellCmd("play.png");
            else
                UiHelper.snapshot("play.png");

            //play
            click(Config.SERIES_INDEX);
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);

            if (Config.NOWAITAD == 0) {
                //wait ad gone
                UiHelper.waitTextGone("VIP可关闭广告", Config.AD_WAIT_TIMEOUT);
            }
            Record.record(mDevice,false);
        } else {
            Log.e(TAG_TENCENT, "could not find search edit");
        }
    }

    private void click(String text) {
        UiObject2 series = mDevice.findObject(By.text(text));
        if (series != null) {
            series.click();
        } else {
            Log.e(TAG_TENCENT, "find " + text + " failed");
        }
    }

    private void enterFullScreen() {
        waitAdGone();
        String resId = "player_controller_view";
        UiObject2 videoView = mDevice.findObject(By.res(Config.TENCENT_PACKAGE_NAME,resId));
        if(videoView != null) {
            String fullResId = "player_full_button";
            BySelector fullBtnSelector = By.res(Config.TENCENT_PACKAGE_NAME, fullResId);
            UiObject2 fullBtn = mDevice.findObject(fullBtnSelector);
            if(fullBtn != null) {
                fullBtn.click();
            }else{
                videoView.click();
                UiHelper.snap(Config.CLICK_TIMEOUT);
                if(mDevice.wait(Until.hasObject(fullBtnSelector),Config.UI_WAIT_TIMEOUT)){
                    fullBtn = mDevice.findObject(fullBtnSelector);
                    fullBtn.click();
                }
            }
        }
    }

    private void downloadVideoAndPlay(){
        String resDownload = "tool_download";
        UiObject2 downloadButton = mDevice.findObject(By.res(Config.TENCENT_PACKAGE_NAME,resDownload));
        if(downloadButton != null){
            downloadButton.click();
            UiHelper.snap(Config.CLICK_TIMEOUT);
            String rateRes = "definition_tv";
            UiObject2 resBtn = mDevice.findObject(By.res(Config.TENCENT_PACKAGE_NAME, rateRes));
            if(resBtn.getText() != "高清") {
                resBtn.click();
                UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                UiHelper.click("高清");
                UiHelper.snap(Config.UI_WAIT_TIMEOUT );
            }

            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.click(Config.SERIES_INDEX);
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.click("已缓存文件");
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.waitTextGone("正在缓存", Config.DOWNLOAD_TIMEOUT);
            String resEpisode = "exposure_layout";
            UiObject2 ep = mDevice.findObject(By.res(Config.TENCENT_PACKAGE_NAME,resEpisode));
            if(ep != null){
                Log.i(Config.TAG,"click episode");
                ep.click();
                UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                String resEpList = "exposure_layout";
                List<UiObject2> epList = mDevice.findObjects(By.res(Config.TENCENT_PACKAGE_NAME,resEpList));
                if(epList != null && !epList.isEmpty()){
                    epList.get(0).click();
                    UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                    //wait ad gone
                    UiHelper.waitTextGone("VIP可关闭广告", Config.AD_WAIT_TIMEOUT);
                }
            }

        }
    }

    private void playLocalVideo() {
        Log.i(Config.TAG,"play local video");
        measureMyPage();
        UiHelper.click("离线缓存");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.click("本地视频");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.click("扫描本地视频");
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        int count = 0;
        while (!UiHelper.click(Config.LOCAL_VIDEO_NAME) && count < 10) {
            mDevice.swipe(mDevice.getDisplayWidth()/2,mDevice.getDisplayHeight()/2,
                    mDevice.getDisplayWidth()/2,0,20);
            count++;
            UiHelper.snap(Config.CLICK_TIMEOUT);
        }
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }
}

