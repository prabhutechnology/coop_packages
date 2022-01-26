package com.prabhutech.prabhupackages.wallet.core.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;

public class TalkBackUtils {
    public static boolean talkBackActive(Context context){
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am!=null && am.isEnabled()){
            return !am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK).isEmpty();
        }
        return false;
    }
}
