package perf.android.com.appperf.policy;

import android.support.test.uiautomator.UiDevice;

import perf.android.com.appperf.common.AppPerf;

/**
 * Created by kakazhang on 17-4-10.
 */

public interface IPolicy {
    //this policy is used for define anay performance policy
    public AppPerf makeNewPerf(UiDevice device, String packageName);
}
