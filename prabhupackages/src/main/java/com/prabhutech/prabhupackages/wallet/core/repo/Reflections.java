package com.prabhutech.prabhupackages.wallet.core.repo;

import android.content.Context;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.UriContracts;
import com.prabhutech.prabhupackages.wallet.core.api.utils.reflection.Reflect;

import io.reactivex.Observable;

public class Reflections {
    public static Observable<JsonObject> sendRequest1(Context context, JsonObject req, boolean body, boolean header, String urlName) {
        return (Observable<JsonObject>) Reflect.dynamicRepoGet(UriContracts.APP_REPO, "DynamicRepo1", context, urlName, body, header, req);
    }

    public static Observable<JsonObject> sendRequest0(Context context, JsonObject req, boolean body, boolean header, String urlName) {
        return (Observable<JsonObject>) Reflect.dynamicRepoGet(UriContracts.APP_REPO, "DynamicRepo0", context, urlName, body, header, req);
    }
}
