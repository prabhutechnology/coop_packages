package com.prabhutech.prabhupackages.wallet.views;

import java.util.HashMap;
import java.util.Map;

public class CallbackCollection {
    private Map<String, ChangeCallback> cbs;

    private CallbackCollection() {
        cbs = new HashMap<>();
    }

    public static CallbackCollection __() {
        return new CallbackCollection();
    }

    public void addToggler(String name, ChangeCallback callback) {
        cbs.put(name, callback);
    }

    public void removeToggler(String name) {
        cbs.remove(name);
    }

    /**
     * Enable hide method. Hide the view you dont want to show
     */
    public void nameCall(String name) {
        ChangeCallback callback = cbs.get(name);
        if (callback != null) callback.onChange(name);
    }
}
