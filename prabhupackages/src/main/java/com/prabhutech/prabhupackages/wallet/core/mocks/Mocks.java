package com.prabhutech.prabhupackages.wallet.core.mocks;

import android.content.Context;


import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;

public class Mocks {
    public static class Mocked {
        public static Completable login(Context context) {
            return Completable.create(emitter -> {
                UserPref.setUserSessionData(context, "", "", "");
                emitter.onComplete();
            });
        }

        public static Completable register(Context context) {
            return Completable.create(emitter -> {
                UserCore.customerId = "123123";
                emitter.onComplete();
            });
        }

        public static Completable activate(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Completable resendActivationCode(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Completable refreshToken(Context context) {
            return Completable.create(completableEmitter -> {
                UserPref.setUserSessionData(context, "", "", "");
                completableEmitter.onComplete();
            });
        }

        public static Completable activateCustomer(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Completable resetPassword(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Completable recoverAccount(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Completable updatePassword(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Completable verifyMobileNumber(Context context) {
            return Completable.create(CompletableEmitter::onComplete);
        }

        public static Observable<String> getBalance(Context context) {
            return Observable.create(emitter -> {
                emitter.onNext("666");
                emitter.onComplete();
            });
        }

        public static Completable getProfile(Context context) {
            return Completable.create(emitter -> {
                UserCore.customerId = "todo";
                UserCore.customerImgUrl = "todo";
                UserCore.fullName = "todo";
                UserCore.email = "todo";
                UserCore.country = "todo";
                UserCore.mobileNumber = "todo";
                UserCore.qrImageUrl = "todo";
                UserCore.isKycVerified = false;
                UserCore.isMpinSet = false;
                emitter.onComplete();
            });
        }
    }
}
