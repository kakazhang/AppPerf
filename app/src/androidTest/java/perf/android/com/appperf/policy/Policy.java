package perf.android.com.appperf.policy;

import android.support.test.uiautomator.UiDevice;

import perf.android.com.appperf.Config;
import perf.android.com.appperf.common.AppPerf;
import perf.android.com.appperf.common.QiyiAppPerf;
import perf.android.com.appperf.common.TencentAppPerf;
import perf.android.com.appperf.common.YoukuAppPerf;

/**
 * Created by kakazhang on 17-4-10.
 */

public class Policy implements IPolicy {
    @Override
    public AppPerf makeNewPerf(UiDevice device, String packageName) {

        if (packageName.equals(Config.QIYI_PACKAGE_NAME))
            return new QiyiAppPerf(device);
        else if (packageName.equals(Config.TENCENT_PACKAGE_NAME))
            return new TencentAppPerf(device);
        else if (packageName.equals(Config.YOUKU_PACKAGE_NAME))
            return new YoukuAppPerf(device);

        return null;
    }
}
