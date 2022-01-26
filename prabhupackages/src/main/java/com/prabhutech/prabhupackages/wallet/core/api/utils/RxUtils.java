package com.prabhutech.prabhupackages.wallet.core.api.utils;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {
    public static <T> ObservableTransformer<T, T> defaultTransformers() {
        return upstream -> upstream.compose(schedulersTransformer());
    }

    public static CompletableTransformer defaultTransformersCompletable() {
        return upstream -> upstream.compose(schedulersTransformerCompletable());
    }

    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> CompletableTransformer schedulersTransformerCompletable() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static DisposableCompletableObserver onlyCompleteObserver() {
        return new DisposableCompletableObserver() {
            @Override public void onComplete() { }
            @Override public void onError(Throwable e) { }
        };
    }
}
