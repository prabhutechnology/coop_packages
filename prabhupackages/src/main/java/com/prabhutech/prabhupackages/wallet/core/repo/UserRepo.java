package com.prabhutech.prabhupackages.wallet.core.repo;

import android.accounts.AuthenticatorException;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.utils.customviews.EmailPhoneInputView;
import com.prabhutech.prabhupackages.BuildConfig;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.api.APICore;
import com.prabhutech.prabhupackages.wallet.core.api.UserAPI;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts;
import com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils;
import com.prabhutech.prabhupackages.wallet.core.mocks.Mocks;
import com.prabhutech.prabhupackages.wallet.core.models.UserLogin;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class UserRepo {
    public static final String TAG = "UserRepo";

    public static Completable getAppToken(@NonNull Context context, JsonObject requestBody) {
        if (BuildConfig.MOCKED) return Mocks.Mocked.login(context);
        return APICore.send(context, APIContracts.APIName.User.GET_TOKEN, requestBody)
                .map((Function<JsonObject, Object>) res -> {
                    if (res.get("ErrorCode").getAsInt() == 0) {
                        Log.e(TAG, "Token: " + res.get("Message").toString());
                        UserCore.accessToken = res.get("Id").getAsString();
                        UserPref.setUserSessionData(context, UserCore.accessToken, "", "");
                        return res;
                    } else {
                        Log.w(TAG, "login: maybe exceptional -> " + res);
                        throw new AuthenticatorException(res.get("Message").getAsString());
                    }
                })
                .ignoreElements();
    }

    /**
     * Login the user.
     * Sets session data to prefs and core.
     * Decodes customerId and sets to core.
     *
     * @param context   - Context
     * @param userLogin containing { extraValue, password, rememberme }
     * @return - API response
     */
    public static Completable login(@NonNull Context context, UserLogin userLogin) {
        Log.i(TAG, "login: ");
        if (BuildConfig.MOCKED) return Mocks.Mocked.login(context);

        JsonObject req = JsonUtils.gson.toJsonTree(userLogin).getAsJsonObject();
        return APICore.send(context, APIContracts.APIName.User.CBS_USER_LOGIN, req)
                .map((Function<JsonObject, Object>) res -> {
                    Log.e(TAG, "login: " + res);
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            JsonObject resultCommon = res.get("ResultCommon").getAsJsonObject();
                            Log.e(TAG, "login: " + resultCommon.toString());
                            UserCore.userName = resultCommon.get("UserName").getAsString();
                            UserCore.fullName = resultCommon.get("FullName").getAsString();
                            UserCore.customerId = resultCommon.get("CBSCustomerId").getAsString();

                            UserPref.setUserSessionData(context, UserCore.accessToken, UserCore.refreshToken, UserCore.expireTime);

                            String oldUser = UserPref.getUser(context, UserPref.PREF_PHONEOREMAIL);
                            if (!TextUtils.isEmpty(oldUser)) {
                                // if new user and previously saved user are different, clear fingerprint preference
                                String newUser = userLogin.username;
                                if (!newUser.equals(oldUser)) {
                                    UserPref.setFingerprintEnabledForLogin(context, false);
                                    UserPref.setFingerprintEnabledForTransaction(context, false);
                                }
                            }
                            UserPref.setUser(context, UserPref.PREF_PHONEOREMAIL, userLogin.username);

                            if (EmailPhoneInputView.isPhone(userLogin.username)) {
                                UserCore.mobileNumber = userLogin.username;
                                UserPref.setUser(context, UserPref.PREF_PHONE, userLogin.username);
                            } else {
                                UserCore.email = userLogin.username;
                            }
                            return res;
                        } else if (data.get("ErrorCode").getAsInt() == 98) {
                            if (!data.get("Message").getAsString().isEmpty()) {
                                throw new AuthenticatorException(data.get("Message").getAsString());
                            } else
                                throw new AuthenticatorException("Not registered! Please register");
                        } else {
                            Log.w(TAG, "login: maybe exceptional -> " + res);
                            throw new AuthenticatorException(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                })
                .ignoreElements();
    }

    /**
     * @param context - Context
     * @param req     - MobileNo
     * @return Observable with 'CustomerId' or error.
     */
    public static Completable register(Context context, JsonObject req) {
        Log.i(TAG, "register: " + req.toString());
        if (BuildConfig.MOCKED) return Mocks.Mocked.register(context);
        return APICore.send(context, APIContracts.APIName.User.CBS_USER_REGISTRATION, req)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    Log.e(TAG, "register: " + data.toString());
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            UserCore.customerId = data.get("Id").getAsString();
                            return res;
                        } else {
                            Log.w(TAG, "getBalance: maybe exceptional -> " + res);
                            throw new Error(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                })
                .ignoreElements();
    }

    public static Observable<JsonObject> recoverPasswordSendOTP(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.User.FORGOT_CBS_USER_PASSWORD, jsonObject)
                .map(res -> {
                    Log.e(TAG, "recoverPasswordSendOTP: " + res.toString());
                    JsonObject result = res.get("Result").getAsJsonObject();
                    if (!result.isJsonNull()) {
                        if (result.get("ErrorCode").getAsInt() == 0) {
                            UserCore.customerId = result.get("Id").getAsString();
                            return result;
                        } else if (result.get("ErrorCode").getAsInt() == 98) {
                            throw new Error(result.get("Message").getAsString());
                        } else {
                            Log.w(TAG, "recoverPasswordSendOTP: maybe exceptional -> " + result);
                            throw new Error(result.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Completable verifyOTP(Context context, JsonObject req) {
        if (BuildConfig.MOCKED) return Mocks.Mocked.activate(context);

        return APICore.send(context, APIContracts.APIName.User.VERIFY_CBS_OTP, req)
                .map(res -> {
                    Log.e(TAG, "verifyOTP: " + res.toString());
                    JsonObject result = res.get("Result").getAsJsonObject();
                    if (result.get("ErrorCode").getAsInt() == 0) {
                        UserCore.customerId = result.get("Id").getAsString();
                        return result.get("Message").getAsString();
                    } else {
                        throw new Error(result.get("Message").getAsString());
                    }
                })
                .ignoreElements();
    }

    public static Observable<JsonObject> resendOTP(Context context, JsonObject jsonObject) {
        // if (BuildConfig.MOCKED) { return Mocks.Mocked.resendActivationCode(context); }
        return APICore.send(context, APIContracts.APIName.User.RESEND_CBS_OTP, jsonObject)
                .map(res -> {
                    Log.e(TAG, "resendActivationCode: " + res.toString());
                    if (res.isJsonNull()) {
                        if (res.get("ErrorCode").getAsInt() == 0) {
                            UserCore.customerId = res.get("Id").getAsString();
                            return res;
                        } else {
                            Log.w(TAG, "getBalance: maybe exceptional -> " + res);
                            throw new Error(res.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Completable setPassword(Context context, JsonObject req) {
        Log.i(TAG, "activatePassword: " + req.toString());
        if (BuildConfig.MOCKED) return Mocks.Mocked.register(context);
        return APICore.send(context, APIContracts.APIName.User.SET_CBS_USER_PASSWORD, req)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    Log.e(TAG, "activatePassword: " + data.toString());
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            throw new Error(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                })
                .ignoreElements();
    }

    public static Observable<JsonObject> getCustomerDetail(Context context) {
        return APICore.send(context, APIContracts.APIName.User.GET_CBS_CUSTOMER_DETAIL, new JsonObject())
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
//                    Log.e(TAG, "getCustomerDetail: " + data.get("Message").getAsString());
                    Log.e(TAG, "getCustomerDetail: " + data.toString());
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getCustomerDetail: maybe exceptional -> " + res);
                            throw new AuthenticatorException(res.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> getActiveAccountList(Context context) {
        return APICore.send(context, APIContracts.APIName.User.GET_CBS_ACTIVE_ACCOUNT_LIST, new JsonObject())
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getActiveAccountList: maybe exceptional -> " + res);
                            throw new AuthenticatorException(res.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> changePassword(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.User.CHANGE_CBS_USER_PASSWORD, jsonObject)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getLastThirtyDayBalance: maybe exceptional -> " + res);
                            throw new AuthenticatorException(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> getAccountBalance(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSTransaction.GET_CBS_ACCOUNT_BALANCE, jsonObject)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getAccountBalance: maybe exceptional -> " + res);
                            throw new AuthenticatorException(res.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<String> getCBSBalance(Context context, JsonObject jsonObject) {
        if (BuildConfig.MOCKED) return Mocks.Mocked.getBalance(context);
        return APICore.send(context, APIContracts.APIName.CBSTransaction.GET_CBS_ACCOUNT_BALANCE, jsonObject)
                .map(res -> {
                    Log.e(TAG, "getCBSBalance: " + res.toString());
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            JsonArray resultCommon = res.get("ResultCommon").getAsJsonArray();
                            return resultCommon.get(0).getAsJsonObject().get("AvailableBalance").getAsString();
                        } else {
                            Log.w(TAG, "getCBSBalance: maybe exceptional -> " + res);
                            throw new AuthenticatorException(res.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> getLastThirtyDayBalance(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSTransaction.GET_CBS_LAST_THIRTY_DAY_BALANCE, jsonObject)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getLastThirtyDayBalance: maybe exceptional -> " + data);
                            throw new AuthenticatorException(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> getAccountStatement(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSTransaction.GET_CBS_ACCOUNT_STATEMENT, jsonObject)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getAccountStatement: maybe exceptional -> " + res);
                            throw new AuthenticatorException(res.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> getLastSevenTransaction(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSTransaction.GET_CBS_LAST_SEVEN_TRANSACTION, jsonObject)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getLastThirtyDayBalance: maybe exceptional -> " + res);
                            throw new AuthenticatorException(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException(res.get("Message").getAsString());
                    }
                });
    }

    public static Observable<JsonObject> getCBSProductServiceList(Context context) {
        return APICore.send(context, APIContracts.APIName.CBSProduct.GET_CBS_PRODUCT_SERVICE_LIST, new JsonObject())
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "getCBSProductServiceList: maybe exceptional -> " + res);
                            throw new AuthenticatorException(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException(res.get("Message").getAsString());
                    }
                });
    }

    public static Observable<JsonObject> checkMobileNoType(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSTopUp.GET_CHECK_MOBILE_NUMBER_TYPE, jsonObject)
                .map(res -> {
                    JsonObject data = res.get("Result").getAsJsonObject();
                    if (!data.isJsonNull()) {
                        if (data.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "checkMobileNoType: maybe exceptional -> " + res);
                            throw new AuthenticatorException(data.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> cbsMobileTopUp(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSTopUp.CBS_MOBILE_TOP_UP, jsonObject)
                .map(res -> {
                    Log.e(TAG, "cbsMobileTopUp: " + res.toString());
                    if (!res.isJsonNull()) {
                        JsonObject result = res.get("Result").getAsJsonObject();
                        if (result.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "cbsMobileTopUp: maybe exceptional -> " + result);
                            throw new AuthenticatorException(result.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Observable<JsonObject> cbsInternetGetDetail(Context context, JsonObject jsonObject) {
        return APICore.send(context, APIContracts.APIName.CBSInternet.GET_INTERNET_DETAILS, jsonObject)
                .map(res -> {
                    Log.e(TAG, "cbsMobileTopUp: " + res.toString());
                    if (!res.isJsonNull()) {
                        JsonObject result = res.get("Result").getAsJsonObject();
                        if (result.get("ErrorCode").getAsInt() == 0) {
                            return res;
                        } else {
                            Log.w(TAG, "cbsInternetGetDetail: maybe exceptional -> " + result);
                            throw new AuthenticatorException(result.get("Message").getAsString());
                        }
                    } else {
                        throw new AuthenticatorException("Not registered!");
                    }
                });
    }

    public static Completable saveNotificationToken(Context context, String token) {
        return UserAPI.storeDeviceToken(context, token)
                .map(res -> {
                    UserPref.saveNotificationToken(context, token);
                    return res;
                })
                .ignoreElements();
    }
}
