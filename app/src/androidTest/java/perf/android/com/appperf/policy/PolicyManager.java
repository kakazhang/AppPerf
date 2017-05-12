package perf.android.com.appperf.policy;

import android.support.test.uiautomator.UiDevice;

import perf.android.com.appperf.common.AppPerf;

/**
 * Created by kakazhang on 17-4-10.
 */

public class PolicyManager {
    private static final String POLICY_IMPL_CLASS_NAME =
            "perf.android.com.appperf.policy.Policy";

    private static final IPolicy sPolicy;

    static {
        // Pull in the actual implementation of the policy at run-time
        try {
            Class policyClass = Class.forName(POLICY_IMPL_CLASS_NAME);
            sPolicy = (IPolicy)policyClass.newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(
                    POLICY_IMPL_CLASS_NAME + " could not be loaded", ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(
                    POLICY_IMPL_CLASS_NAME + " could not be instantiated", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(
                    POLICY_IMPL_CLASS_NAME + " could not be instantiated", ex);
        }
    }

    public static AppPerf makeNewPerf(UiDevice device, String packageName) {
        return sPolicy.makeNewPerf(device, packageName);
    }
}
