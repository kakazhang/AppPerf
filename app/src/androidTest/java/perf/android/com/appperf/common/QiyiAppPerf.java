package perf.android.com.appperf.common;

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

import static perf.android.com.appperf.Config.TAG_IQIYI;

/**
 * Created by kakazhang on 17-4-10.
 */

public class QiyiAppPerf extends AppPerf {
    private UiDevice mDevice;

    public QiyiAppPerf(UiDevice device) {
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
        UiHelper.waitTextGone("会员跳广告", Config.AD_WAIT_TIMEOUT);
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.waitTextGone("会员跳广告", Config.AD_WAIT_TIMEOUT);
    }

    @Override
    public void measureAppPlayAction() {
        searchAndPlay();
    }

    private void enterPhoneSearchActivity() {
        String resId = "txt_left";
        UiObject2 searchText = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME, resId));
        if (searchText != null && searchText.isClickable()) {
            searchText.click();
        } else {
            Log.e(TAG_IQIYI, "enter phone search activity failed");
        }
    }

    private void searchAndPlay() {
        String resId = "phoneSearchKeyword";
        UiObject2 searchEdit = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME, resId));
        if (searchEdit != null) {
            //set text what we want to play
            searchEdit.setText(Config.TV_SEARIES);

            UiHelper.snap(Config.CLICK_TIMEOUT);

            //click tv_series chinese index
            UiHelper.click(Config.TV_SEARIES_CN);
            UiHelper.snap(Config.CLICK_TIMEOUT);

            Record.record(mDevice, true);
            mDevice.pressBack();
            UiHelper.snap(Config.RECORD_INTERVAL);

            if (Build.MANUFACTURER.contains("HUAWEI"))
                UiHelper.snapshotWithShellCmd("play.png");
            else
                UiHelper.snapshot("play.png");

            //play
            UiHelper.click(Config.SERIES_INDEX);
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            if (Config.NOWAITAD == 0) {
                //wait ad gone
                UiHelper.waitResourceGone(Config.QIYI_PACKAGE_NAME,
                    "ads_skipAd_info_area_pre_ad", Config.AD_WAIT_TIMEOUT);
            }
            Record.record(mDevice,false);
        } else {
            Log.e(TAG_IQIYI, "could not find search edit");
        }
    }

    private void enterFullScreen() {
        waitAdGone();
        String resId = "video_view";
        UiObject2 videoView = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME,resId));
        if(videoView != null) {
            String fullResId = "btn_tolandscape";
            BySelector fullBtnSelector = By.res(Config.QIYI_PACKAGE_NAME, fullResId);
            UiObject2 fullBtn = mDevice.findObject(fullBtnSelector);
            if(fullBtn == null) {
                videoView.click();
                UiHelper.snap(Config.CLICK_TIMEOUT);
                if(mDevice.wait(Until.hasObject(fullBtnSelector),Config.UI_WAIT_TIMEOUT)){
                    fullBtn = mDevice.findObject(fullBtnSelector);
                }
            }
            fullBtn.click();
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            closeDanmu();
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            switchResolution("高清");
        }
    }

    private void closeDanmu() {
        String videoLayRes = "video_view";
        UiObject2 videoLay = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME, videoLayRes));
        String danmuRes = "player_landscape_btn_toggle_spitslot";
        String danmuSwitchRes = "danmaku_show_switch";
        BySelector danmuBtnSelector = By.res(Config.QIYI_PACKAGE_NAME, danmuRes);
        BySelector danmuSwitchSelector = By.res(Config.QIYI_PACKAGE_NAME, danmuSwitchRes);
        UiObject2 danmuBtn = mDevice.findObject(danmuBtnSelector);
        UiObject2 danmuSwitchBtn;
        if(danmuBtn == null) {
            videoLay.click();
            UiHelper.snap(Config.CLICK_TIMEOUT);
            if(mDevice.wait(Until.hasObject(danmuBtnSelector),Config.UI_WAIT_TIMEOUT)){
                danmuBtn = mDevice.findObject(danmuBtnSelector);
            }
        }
        danmuBtn.click();
        UiHelper.snap(Config.CLICK_TIMEOUT);
        if(mDevice.wait(Until.hasObject(danmuSwitchSelector),Config.UI_WAIT_TIMEOUT)) {
            danmuSwitchBtn = mDevice.findObject(danmuSwitchSelector);
            if(danmuSwitchBtn.isSelected()) {
                danmuSwitchBtn.click();
            }
            UiHelper.snap(Config.CLICK_TIMEOUT);
            mDevice.pressBack();
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        }
    }

    private void switchResolution(String resolution){
        String videoLayRes = "video_view";
        UiObject2 videoLay = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME, videoLayRes));
        String resSwitch = "player_landscape_coderateTx";
        BySelector resSwitchSelector = By.res(Config.QIYI_PACKAGE_NAME, resSwitch);
        UiObject2 resSwitchBtn = mDevice.findObject(resSwitchSelector);
        if(resSwitchBtn == null){
            videoLay.click();
            UiHelper.snap(Config.CLICK_TIMEOUT);
            if(mDevice.wait(Until.hasObject(resSwitchSelector),Config.UI_WAIT_TIMEOUT)){
                resSwitchBtn = mDevice.findObject(resSwitchSelector);
            }
        }
        resSwitchBtn.click();
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
        UiHelper.click(resolution);
    }


    private void downloadVideo() {
        String resId = "download_new";
        UiObject2 download = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME, resId));
        if(download != null) {
            download.click();
            UiHelper.snap(Config.CLICK_TIMEOUT);
            String rateRes = "rate";
            UiObject2 resBtn = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME, rateRes));
            if(resBtn.getText() != "高清") {
                resBtn.click();
                UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                //UiHelper.click("高清");
                UiHelper.clickRes(Config.QIYI_PACKAGE_NAME,"rate_gq");
                UiHelper.snap(Config.UI_WAIT_TIMEOUT );
            }
            UiHelper.click(Config.SERIES_INDEX);
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.click("查看缓存视频");
            UiHelper.snap(Config.UI_WAIT_TIMEOUT);
            UiHelper.waitTextGone("正在缓存", Config.DOWNLOAD_TIMEOUT);
            String resEpisode = "phone_download_item_avator";
            UiObject2 ep = mDevice.findObject(By.res(Config.QIYI_PACKAGE_NAME,resEpisode));
            if(ep != null){
                ep.click();
                UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                String resEpList = "phone_download_item_title";
                List<UiObject2> epList = mDevice.findObjects(By.res(Config.QIYI_PACKAGE_NAME,resEpList));
                if(epList != null && !epList.isEmpty()){
                    epList.get(0).click();
                    UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                    //wait ad gone
                    UiHelper.waitTextGone("会员跳广告", Config.AD_WAIT_TIMEOUT);
                    UiHelper.snap(Config.UI_WAIT_TIMEOUT);
                    UiHelper.waitTextGone("会员跳广告", Config.AD_WAIT_TIMEOUT);
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
        int count = 0;
        while (!UiHelper.click(Config.LOCAL_VIDEO_NAME_FULL) && count < 10) {
            mDevice.swipe(mDevice.getDisplayWidth()/2,mDevice.getDisplayHeight()/2,
                    mDevice.getDisplayWidth()/2,0,20);
            count++;
            UiHelper.snap(Config.CLICK_TIMEOUT);
        }
        UiHelper.snap(Config.UI_WAIT_TIMEOUT);
    }

}
