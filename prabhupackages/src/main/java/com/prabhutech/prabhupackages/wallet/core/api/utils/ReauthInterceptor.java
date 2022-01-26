package com.prabhutech.prabhupackages.wallet.core.api.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.activities.starteractivity.TokenExpireActivity;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class ReauthInterceptor implements Interceptor {

    private interface Definition {
        @POST()
        Call<JsonObject> refresh(@Url String url, @Body JsonObject body);
    }

    private final Context context;

    /**
     * @param context application context
     */
    public ReauthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response res = chain.proceed(chain.request());
        Request req = chain.request();

        /*// handle 401 case
        if (res.code() == 401 && !isRestricted(req.url().toString())) {
            System.out.println("ReAuth: 401 is detected");
            // create new core without re-auth client
            redirectToTokenExpireActivity();
        }*/
        if (res.code() == 401) {
            try {
                JSONObject js = new JSONObject(res.body().string());
                DebugMode.log("ReAuth", "ReAuth Value Success" + js.toString());
                redirectToTokenExpireActivity();
                throw new IOException(js.getString("Message"));
            } catch (Exception e) {
                Log.e("ReAuth 401", "intercept: " + e.getMessage());
                redirectToTokenExpireActivity();
                throw new IOException(e.getMessage());
            }
        }

        return res;
    }

    private void redirectToTokenExpireActivity() {
        Intent intent = new Intent(context, TokenExpireActivity.class);
        context.startActivity(intent);
    }

    /**
     * Checks if the given url is included in APIContracts.REAUTH_RESTRICTED_URL. For those
     * url that are not allowed to refresh token.
     *
     * @param url
     * @return
     */
    private boolean isRestricted(String url) {
        for (String restricted : APIContracts.REAUTH_RESTRICTED_URL)
            if ((APIContracts.baseUrl + restricted).equals(url)) return true;
        return false;
    }
}
