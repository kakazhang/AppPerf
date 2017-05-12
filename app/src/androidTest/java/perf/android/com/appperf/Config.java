package perf.android.com.appperf;

/**
 * Created by kakazhang on 17-4-10.
 */

public class Config {
    public static final String TAG = "AppPerf";
    public static final String TAG_IQIYI = TAG+ "Iqiyi";
    public static final String TAG_TENCENT = TAG+ "Tencent";
    public static final String TAG_YOUKU = TAG+ "Youku";

    public static long sLaunchTimeout = 10 * 1000;

    public static final String TEST_PACKAGE = "packageName";

    public static final String QIYI_PACKAGE_NAME = "com.qiyi.video";
    public static final String TENCENT_PACKAGE_NAME = "com.tencent.qqlive";
    public static final String YOUKU_PACKAGE_NAME = "com.youku.phone";

    public static String sTargetPackage = TENCENT_PACKAGE_NAME;

    //search for dramma
    public static final String TV_SEARIES = "renmindemingyi";
    public static final String TV_SEARIES_CN = "人民的名义";
    public static final String SERIES_INDEX = "1";
    public static final String LOCAL_VIDEO_NAME = "720p_h264";
    public static final String LOCAL_VIDEO_NAME_FULL = "720p_h264.f4v";

    //click timeout
    public static long CLICK_TIMEOUT = 1000;
    //no wait ad??
    public static int NOWAITAD = 1;
    //wait for main screen idle timeout
    public static long MAIN_SCREEN_IDLE_TIMEOUT = 30 * 1000;
    //phone search screen idle timeout
    public static long PHONE_SEARCH_IDLE_TIMEOUT = 10 * 1000;
    //play idle timeout
    public static long PLAY_IDLE_TIMEOUT = 120 * 1000;
    //record interval
    public static long RECORD_TIMEOUT = 5 * 1000;
    public static long RECORD_INTERVAL = 2000;
    //ui timeout
    public static long UI_WAIT_TIMEOUT = 15*1000;
    //ad timeout
    public static long AD_WAIT_TIMEOUT = 120*1000;
    //download timeout
    public static long DOWNLOAD_TIMEOUT = 15*60*1000;
}
