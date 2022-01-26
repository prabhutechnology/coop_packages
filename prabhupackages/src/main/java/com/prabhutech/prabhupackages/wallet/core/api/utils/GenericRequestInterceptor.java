package com.prabhutech.prabhupackages.wallet.core.api.utils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class GenericRequestInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println("INTERCEPT: " + request.url().url().toString());
        System.out.println("Method " + request.method());
        System.out.println("Headers " + request.headers());
        System.out.println("Body " + request.body());
        return chain.proceed(request);
    }
}
