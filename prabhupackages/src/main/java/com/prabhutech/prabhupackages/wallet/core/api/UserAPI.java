package com.prabhutech.prabhupackages.wallet.core.api;

import android.content.Context;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;

import io.reactivex.Observable;

public class UserAPI {
    public static Observable<JsonObject> storeDeviceToken(Context context, String token) {
        JsonObject req = new JsonObject();
        // TODO: 10/20/19 need these if was given permission and is able
        req.addProperty("longitude", UserCore.longitude);
        req.addProperty("latitude", UserCore.latitude);
        req.addProperty("deviceToken", token);
        req.addProperty("deviceType", "android");
        req.addProperty("uniqueId", UserPref.getUniqueId(context));
        req.addProperty("badgeId", "0");
        return com.prabhutech.prabhupackages.wallet.core.api.APICore.send(context, APIContracts.APIName.User.StoreDeviceToken, req)
                .map(res -> {
                    if (res.get("success").getAsBoolean()) {
                        // Toast.makeText(context, "StoreDeviceToken success", Toast.LENGTH_SHORT).show();
                        return res;
                    } else {
                        // TODO: 11/3/19 get details if failed
                        // Toast.makeText(context, "StoreDeviceToken failed", Toast.LENGTH_SHORT).show();
                        throw new Error(res.get("message").getAsString());
                    }
                });
    }
}
