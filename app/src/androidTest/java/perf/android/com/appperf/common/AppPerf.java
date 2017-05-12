package perf.android.com.appperf.common;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.UiHelper;


/**
 * Created by kakazhang on 17-4-10.
 */

public abstract  class AppPerf implements Runnable {
    public AppPerf() {
    }

    @Override
    public void run() {
        //here we control measure app sequence
        measureAppBootAction();
        //main screen idle timeout
        UiHelper.snap(Config.MAIN_SCREEN_IDLE_TIMEOUT);

        measureAppSearchAction();
        UiHelper.snap(Config.PHONE_SEARCH_IDLE_TIMEOUT);

        measureAppPlayAction();
        //play timeout
        UiHelper.snap(Config.PLAY_IDLE_TIMEOUT);

        //... any other actions can be announced here
    }

    public abstract void measureAppBootAction();

    public abstract void measureAppPlayAction();

    public abstract void measureAppSearchAction();

    public abstract void measureAppFullScreenPlay();

    public abstract void measureAppDownloadVideo();

    public abstract void measureAppPlayLocalVideo();

    public abstract void measureMyPage();

    public abstract void waitAdGone();
}
