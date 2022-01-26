package com.prabhutech.prabhupackages.wallet.core.api;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts;
import com.prabhutech.prabhupackages.wallet.core.api.errors.UnNeededException;

import io.reactivex.Single;

public class AppAPI {
    public static Single<JsonObject> checkUpdates(Context context) {
        return com.prabhutech.prabhupackages.wallet.core.api.APICore.send(context, APIContracts.APIName.Apps.CheckUpdate, new JsonObject())
                .map(res -> {
                    if (res.get("success").getAsBoolean()) {
                        JsonArray data = res.get("data").getAsJsonArray();
                        for (JsonElement datum : data) {
                            JsonObject dev = datum.getAsJsonObject();
                            if (dev.get("platform").getAsString().equalsIgnoreCase("android")) {
                                return dev;
                            }
                        }
                        throw new Error(res.get("message").getAsString());
                    } else {
                        throw new UnNeededException("No device update found");
                    }
                }).firstOrError();
    }
}
