package com.prabhutech.prabhupackages.wallet.core.api.utils;

import android.util.Pair;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMeta {
    public String url;
    public String method;
    public String appendPath;
    public boolean authorization;
    public JsonObject extraBody;
    /**
     * If authHeader is set to null then it would be a universal understanding that auth is not needed for this and thus
     * other auth handlers like ReAuthInterceptors will not try to re auth that given url.
     */
    public Map<String, String> authHeader = new HashMap<>();
    public List<Pair<String, String>> queries = new ArrayList<>();
    public boolean streaming = false;

    public RequestMeta() {
    }

    public RequestMeta(String url, String method, String appendPath, boolean authorization, JsonObject extraBody) {
        this.url = url;
        this.method = method;
        this.appendPath = appendPath;
        this.authorization = authorization;
        this.extraBody = extraBody;
    }
}
