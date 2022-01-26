package com.prabhutech.prabhupackages.wallet.core.repo;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.api.APICore;
import com.prabhutech.prabhupackages.wallet.core.api.AppAPI;
import com.prabhutech.prabhupackages.wallet.core.api.errors.UnNeededException;
import com.prabhutech.coop.wallet.core.api.utils.reflection.DynamicRepoGet;
import com.prabhutech.coop.wallet.core.api.utils.reflection.RepoGet;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;

public class AppRepo {
    /**
     * Checks for update comparing VersionCode.
     * Timeout of 2 seconds.
     *
     * @param context activity
     * @return returns if update is needed { isForced, updateFeatures } else return UnNeededException or Error
     */
    @RepoGet("CheckUpdates")
    public static Single<JsonObject> checkUpdates(Context context) {
        return AppAPI.checkUpdates(context).timeout(10, TimeUnit.SECONDS)
                .map(res -> {
                    String versionCode = res.get("versionCode").getAsString();
                    String message = res.get("message").getAsString();
                    boolean forced = res.get("isForce").getAsBoolean();

                    // DON'T USE BuildConfig.VERSION_CODE
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    if (pInfo.versionCode < Integer.parseInt(versionCode.trim())) {
                        JsonObject r = new JsonObject();
                        r.addProperty("isForced", forced);
                        r.addProperty("updateFeatures", message);
                        return r;
                    } else {
                        throw new UnNeededException("No update needed.");
                    }
                });
    }

    /**
     * METHOD POST = 1
     */
    @DynamicRepoGet("DynamicRepo1")
    public static Observable<JsonObject> sendRequest1(Context context, String urlName, boolean body, boolean header, JsonObject req) {
        return APICore.send(context, urlName, req, 1, body, header)
                .map(res -> res);
    }

    /**
     * METHOD GET = 0
     */
    @DynamicRepoGet("DynamicRepo0")
    public static Observable<JsonObject> sendRequest0(Context context, String urlName, boolean body, boolean header, JsonObject req) {
        return APICore.send(context, urlName, req, 0, body, header)
                .map(res -> res);
    }
}
