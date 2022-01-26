package com.prabhutech.prabhupackages.wallet.core.api;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.UriContracts;
import com.prabhutech.prabhupackages.wallet.core.api.utils.GenericRequestInterceptor;
import com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils;
import com.prabhutech.prabhupackages.wallet.core.api.utils.ReauthInterceptor;
import com.prabhutech.prabhupackages.wallet.core.api.utils.RequestMeta;
import com.prabhutech.prabhupackages.wallet.core.api.utils.RxUtils;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public class APICore {
    public static final String TAG = "ApiCore";

    private static Retrofit core;
    private static OkHttpClient client;
    private static GenericDefinition request;

    private static OkHttpClient client(Context context) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new GenericRequestInterceptor())
                    .addInterceptor(new ReauthInterceptor(context))
                    .build();
        }
        return client;
    }

    private static Retrofit core(Context context) {
        if (core == null) {
            try {
                String baseUrl = (String) com.prabhutech.prabhupackages.wallet.core.api.utils.reflection.Reflect.getAPIContractVar(UriContracts.URI_API_CONTRACT, "baseUrl");
                System.out.println("baseUrl " + baseUrl);
                core = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client(context))
                        .addConverterFactory(GsonConverterFactory.create(com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils.gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            } catch (Exception e) {
                Log.e(TAG, "init: ", e);
            }
        }
        return core;
    }

    private static GenericDefinition request(Context context) {
        if (request == null) request = core(context).create(GenericDefinition.class);
        return request;
    }

    /**
     * Send any type of request as described by urlName.
     *
     * @param context requires application context only
     * @param urlName
     * @param req
     * @returReAuth
     */
    public static Observable<JsonObject> send(Context context, String urlName, @NonNull JsonObject req) {
        RequestMeta meta = getRequestMeta(urlName);
        // create request based on current build name
        return requestFactory(context, meta, req);
    }

    public static Observable<JsonObject> send(Context context, String urlName, @NonNull JsonObject req, int method , boolean body , boolean header) {

        RequestMeta meta = getRequestMetaDyanmic(urlName,method,body,header);
        // create request based on current build name
        return requestFactory(context, meta, req);
    }

    private static Observable<JsonObject> requestFactory(Context context, @NonNull RequestMeta meta, JsonObject req) {
        Log.i(TAG, "send: url is " + meta.url);

        // headers
        Map<String, String> headers = getRequestMetaHeaders(context, meta);
        Log.i(TAG, "send: headers are " + headers);

        if (meta.method.equals("POST")) { // or PUT or DELETE
            // add all from extra body
            putExtraBodyInto(context, meta, req);

            // TEST remove these before release
            Log.i(TAG, "send: body are " + req);

            return request(context).post(meta.url, headers, req)
                    .compose(RxUtils.defaultTransformers());
        } else {
            // TEST remove these before release
            Log.i(TAG, "send: queries are " + meta.queries);

            // mapping queries to one that is found in request and meta.queries
            Map<String, String> params = getRequestMetaParams(meta, req);

            return request(context).get(meta.url, params, headers)
                    .compose(RxUtils.defaultTransformers());
        }
    }

    /**
     * Send any type of request as described by urlName.
     *
     * @param context base context
     * @param urlName
     * @param req
     * @return
     */
    public static Observable<JsonObject> send(Context context, String urlName, com.prabhutech.prabhupackages.wallet.kyc.KycDetails req) {
        RequestMeta meta = null;
        try {
            meta = (RequestMeta) com.prabhutech.prabhupackages.wallet.core.api.utils.reflection.Reflect.invokeStaticNoVerify(UriContracts.URI_URLSCONTRACTS, "Urls", urlName);
            if (meta == null)
                throw new Exception("Null meta; RequestMeta was probably not defined");
        } catch (Exception e) {
            throw new Error("No RequestMeta with name " + urlName + " found.");
        }

        // create request based on current build name
        return registerKyc(context, meta, req);
    }

    private static Observable<JsonObject> registerKyc(Context context, RequestMeta meta, com.prabhutech.prabhupackages.wallet.kyc.KycDetails req) {
        Map<String, String> headers = new HashMap<>();

        List<MultipartBody.Part> filePart = req.getFileParts(context);
        Map<String, RequestBody> partMap = req.getMapped();
        partMap.put("CustomerId", com.prabhutech.prabhupackages.wallet.kyc.KycDetails.createPartFromString(UserCore.customerId.replace("\"", "")));

        if (meta.authorization) {
            if (meta.authHeader != null) headers.putAll(meta.authHeader);
            else throw new Error("No Auth Header specified");
        }

        return request(context).post(meta.url, headers, partMap, filePart)
                .compose(RxUtils.defaultTransformers());
    }

    //=== UTILS ===//
    private static RequestMeta getRequestMeta(String urlName) {
        RequestMeta meta;
        try {
            // FIXME: 10/18/19 does not catch exception
            meta = (RequestMeta) com.prabhutech.prabhupackages.wallet.core.api.utils.reflection.Reflect.invokeStaticNoVerify(UriContracts.URI_URLSCONTRACTS, "Urls", urlName);
            if (meta == null)
                throw new Exception("Null meta; RequestMeta was probably not defined");
            return meta;
        } catch (Exception e) {
            throw new Error("No RequestMeta with name " + urlName + " found.");
        }
    }

    //=== UTILS ===//
    private static RequestMeta getRequestMetaDyanmic(String urlName, int method , boolean body , boolean header) {
        RequestMeta meta;
        try {
            // FIXME: 10/18/19 does not catch exception
            meta = (RequestMeta) com.prabhutech.prabhupackages.wallet.core.api.utils.reflection.Reflect.invokeStaticNoVerify(UriContracts.URI_URLSCONTRACTS, "DynamicUrls", urlName, method , body , header);
            if (meta == null)
                throw new Exception("Null meta; RequestMeta was probably not defined");
            return meta;
        } catch (Exception e) {
            throw new Error("No RequestMeta with name " + urlName + " found.");
        }
    }

    private static Map<String, String> getRequestMetaHeaders(Context context, RequestMeta meta) {
        Map<String, String> headers = reworkHeaderOnAuthEmpty(context, meta);
        if (meta.authHeader != null) headers.putAll(meta.authHeader);
        return headers;
    }

    private static void putExtraBodyInto(Context context, RequestMeta meta, JsonObject req) {
        if (meta.extraBody != null)
            for (Map.Entry<String, JsonElement> stringJsonElementEntry : meta.extraBody.entrySet()) {
                try {
                    String cusID = new UserPref().getCustomerId(context);
                    if (!cusID.matches(stringJsonElementEntry.getValue().toString())) {
                        String customerId = stringJsonElementEntry.getValue().toString();
                        customerId = customerId.replace("\"", "");
//                       new UserPref().setCustomerId(context, stringJsonElementEntry.getValue().toString());
                        new UserPref().setCustomerId(context, customerId);
                    }
                } catch (Exception e) {
                    Log.e("CustomerID error", e.toString());
                    new UserPref().setCustomerId(context, stringJsonElementEntry.getValue().toString());

                }
                req.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
            }
    }

    /**
     * Create parameters by getting data from req and using param name from meta.queries.
     *
     * @param meta must contain queries
     * @param req  must contain required data
     * @return
     */
    private static Map<String, String> getRequestMetaParams(RequestMeta meta, JsonObject req) {
        // TODO: 10/20/19  redo this with
        Map<String, String> queries = new HashMap<>();
        for (Pair<String, String> key : meta.queries) {
            String value = JsonUtils.findIn(req, key.first);
            if (value != null) queries.put(key.second, value);
        }
        return queries;
    }

    public interface GenericDefinition {

        @POST()
        Observable<JsonObject> post(@Url String url, @Body JsonObject request);

        @POST()
        Observable<JsonObject> post(@Url String url, @HeaderMap Map<String, String> headers, @Body JsonObject req);

        @GET("{path}")
        Observable<JsonObject> get(@Path("path") String path);

        @GET()
        Observable<JsonObject> get(@Url String url, @QueryMap Map<String, String> querymap, @HeaderMap Map<String, String> headers);

        // TODO: 9/15/19 change this to get download ResponseBody
        @GET
        Observable<ResponseBody> streamGet(@Url String url, @QueryMap Map<String, String> queries, @HeaderMap Map<String, String> headers);

        @Multipart
        @POST()
        Observable<JsonObject> post(@Url String url,
                                    @HeaderMap Map<String, String> headers,
                                    @PartMap Map<String, RequestBody> body,
                                    @Part List<MultipartBody.Part> files);
    }

    private static HashMap<String, String> reworkHeaderOnAuthEmpty(Context context, RequestMeta meta) {
        HashMap<String, String> map = new HashMap<>();
        if (meta.authHeader == null) return map;
        String auth = meta.authHeader.get("Authorization");
        if (auth != null && !auth.isEmpty() && (auth.trim().toLowerCase().equals("bearer") || (UserCore.customerId == null || UserCore.customerId.isEmpty()))) { // is useless
            // try to get from prefs
            UserPref.restoreUserPrefs(context);
            map.put("Authorization", "Bearer " + UserCore.accessToken);
            Log.w(TAG, "reworkHeaderOnAuthEmpty: " + map.toString());
            Log.w(TAG, "reworkHeaderOnAuthEmpty: " + UserCore.customerId);
        } // else drown in sorrow
        return map;
    }
}
