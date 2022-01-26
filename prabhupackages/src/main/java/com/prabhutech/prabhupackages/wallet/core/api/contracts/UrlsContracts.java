package com.prabhutech.prabhupackages.wallet.core.api.contracts;

import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSInternet.GET_INTERNET_DETAILS;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSInternet.INTERNET_MAKE_PAYMENT;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSProduct.GET_CBS_PRODUCT_SERVICE_LIST;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSTopUp.CBS_MOBILE_TOP_UP;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSTopUp.GET_CHECK_MOBILE_NUMBER_TYPE;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSTransaction.GET_CBS_ACCOUNT_BALANCE;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSTransaction.GET_CBS_ACCOUNT_STATEMENT;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSTransaction.GET_CBS_LAST_SEVEN_TRANSACTION;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.CBSTransaction.GET_CBS_LAST_THIRTY_DAY_BALANCE;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.CBS_USER_LOGIN;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.CBS_USER_REGISTRATION;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.CHANGE_CBS_USER_PASSWORD;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.FORGOT_CBS_USER_PASSWORD;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.GET_CBS_ACTIVE_ACCOUNT_LIST;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.GET_CBS_CUSTOMER_DETAIL;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.GET_TOKEN;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.RESEND_CBS_OTP;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.RefreshToken;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.SET_CBS_USER_PASSWORD;
import static com.prabhutech.prabhupackages.wallet.core.api.contracts.APIContracts.APIName.User.VERIFY_CBS_OTP;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.BuildConfig;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.api.utils.DynamicUrls;
import com.prabhutech.prabhupackages.wallet.core.api.utils.RequestMeta;
import com.prabhutech.prabhupackages.wallet.core.api.utils.Urls;

import java.util.HashMap;
import java.util.Map;

public class UrlsContracts {
    public static String TERMS_AND_CONDITIONS = "https://prabhuprabhupackageserative.com.np/#/about-us";
    public static String PRIVACY_POLICY = "https://prabhuprabhupackageserative.com.np/#/about-us";

    // OTPType 1 = registration OTPType 2 = Transaction
    public final static int OTP_TYPE_REGISTRATION = 1;
    public final static int OTP_TYPE_TRANSACTION = 2;
    public final static int OTP_TYPE_FORGOT_PASSWORD = 3;
    public final static int TOP_UP_TYPE = 1001;
    public final static int TOP_UP_DATA_PACKAGE_TYPE = 1002;
    static JsonObject js = new JsonObject();

    @Urls
    public static synchronized RequestMeta forName(String urlName) {

        Map<String, String> authHeader = new HashMap<>();
        authHeader.put("Authorization", "Bearer " + UserCore.accessToken);

        //For prabhupackages
        Map<String, String> prabhupackagesHeader = new HashMap<>();
        prabhupackagesHeader.put("APIUser", BuildConfig.APIUSER);
        prabhupackagesHeader.put("APIToken", UserCore.accessToken);
        prabhupackagesHeader.put("CBSCode", UserCore.CBSCode);

        //For prabhupackages (TOKEN)
        Map<String, String> tokenHeader = new HashMap<>();
        tokenHeader.put("APIUser", BuildConfig.APIUSER);

        JsonObject extraCustomerIdBody = new JsonObject();
        extraCustomerIdBody.addProperty("customerId", UserCore.customerId);

        JsonObject requestMedium = new JsonObject();
        requestMedium.addProperty("RequestMedium", UserCore.Request_Medium);

        RequestMeta meta = new RequestMeta();
        if (GET_TOKEN.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_TOKEN;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = true;
            meta.extraBody = null;
            meta.authHeader = tokenHeader;
        } else if (CBS_USER_LOGIN.equals(urlName)) {
            meta.url = APIContracts.baseUrl + CBS_USER_LOGIN;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = true;
            meta.extraBody = requestMedium;
            meta.authHeader = prabhupackagesHeader;
        } else if (CBS_USER_REGISTRATION.equals(urlName)) {
            meta.url = APIContracts.baseUrl + CBS_USER_REGISTRATION;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = requestMedium;
            meta.authHeader = prabhupackagesHeader;
        } else if (RefreshToken.equals(urlName)) {
            meta.url = APIContracts.baseUrl + RefreshToken;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("token", UserCore.accessToken);
            extraBody.addProperty("refreshToken", UserCore.refreshToken);
            meta.extraBody = extraBody;
            meta.authHeader = null;
        } else if (FORGOT_CBS_USER_PASSWORD.equals(urlName)) {
            meta.url = APIContracts.baseUrl + FORGOT_CBS_USER_PASSWORD;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = requestMedium;
            meta.authHeader = prabhupackagesHeader;
        } else if (VERIFY_CBS_OTP.equals(urlName)) {
            meta.url = APIContracts.baseUrl + VERIFY_CBS_OTP;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("CustomerId", UserCore.customerId);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (RESEND_CBS_OTP.equals(urlName)) {
            meta.url = APIContracts.baseUrl + RESEND_CBS_OTP;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("CustomerId", UserCore.customerId);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (SET_CBS_USER_PASSWORD.equals(urlName)) {
            meta.url = APIContracts.baseUrl + SET_CBS_USER_PASSWORD;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("CustomerId", UserCore.customerId);
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_CUSTOMER_DETAIL.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_CUSTOMER_DETAIL;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("CustomerId", UserCore.customerId);
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_ACTIVE_ACCOUNT_LIST.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_ACTIVE_ACCOUNT_LIST;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("MobileNo", UserCore.userName);
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (CHANGE_CBS_USER_PASSWORD.equals(urlName)) {
            meta.url = APIContracts.baseUrl + CHANGE_CBS_USER_PASSWORD;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("Username", UserCore.userName);
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_ACCOUNT_BALANCE.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_ACCOUNT_BALANCE;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            meta.extraBody = new JsonObject();
            meta.extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            meta.extraBody.addProperty("username", UserCore.userName);
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_LAST_THIRTY_DAY_BALANCE.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_LAST_THIRTY_DAY_BALANCE;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_ACCOUNT_STATEMENT.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_ACCOUNT_STATEMENT;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_LAST_SEVEN_TRANSACTION.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_LAST_SEVEN_TRANSACTION;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CHECK_MOBILE_NUMBER_TYPE.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CHECK_MOBILE_NUMBER_TYPE;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (CBS_MOBILE_TOP_UP.equals(urlName)) {
            meta.url = APIContracts.baseUrl + CBS_MOBILE_TOP_UP;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestedMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            extraBody.addProperty("TopupType", TOP_UP_TYPE);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_CBS_PRODUCT_SERVICE_LIST.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_CBS_PRODUCT_SERVICE_LIST;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (GET_INTERNET_DETAILS.equals(urlName)) {
            meta.url = APIContracts.baseUrl + GET_INTERNET_DETAILS;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestedMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        } else if (INTERNET_MAKE_PAYMENT.equals(urlName)) {
            meta.url = APIContracts.baseUrl + INTERNET_MAKE_PAYMENT;
            meta.method = "POST";
            meta.appendPath = "";
            meta.authorization = false;
            JsonObject extraBody = new JsonObject();
            extraBody.addProperty("RequestedMedium", UserCore.Request_Medium);
            extraBody.addProperty("username", UserCore.userName);
            meta.extraBody = extraBody;
            meta.authHeader = prabhupackagesHeader;
        }
        return meta;
    }

    @DynamicUrls
    public static synchronized RequestMeta forNames(String urlName, int method, boolean body, boolean header) {

        Map<String, String> authHeader = new HashMap<>();
        authHeader.put("Authorization", "Bearer " + UserCore.accessToken);

        //For prabhupackages
        Map<String, String> prabhupackagesHeader = new HashMap<>();

        if (header) {
            prabhupackagesHeader.put("APIUser", BuildConfig.APIUSER);
            prabhupackagesHeader.put("APIToken", UserCore.accessToken);
            prabhupackagesHeader.put("CBSCode", UserCore.CBSCode);
        } else {
            prabhupackagesHeader = null;
        }

        //For prabhupackages (TOKEN)
        Map<String, String> tokenHeader = new HashMap<>();
        tokenHeader.put("APIUser", BuildConfig.APIUSER);

        JsonObject extraCustomerIdBody = new JsonObject();
        extraCustomerIdBody.addProperty("customerId", UserCore.customerId);

        JsonObject extraBody = new JsonObject();
        if (body) {
            extraBody.addProperty("RequestMedium", UserCore.Request_Medium);
            if (!CBS_USER_LOGIN.equals(urlName))
                extraBody.addProperty("username", UserCore.userName);
            if (urlName.equals(CBS_MOBILE_TOP_UP)) {
                extraBody = js;
            }

        } else {
            extraBody = null;
        }

        JsonObject requestMedium = new JsonObject();
        requestMedium.addProperty("RequestMedium", UserCore.Request_Medium);
        String methods = "";

        if (method == 0) {
            methods = "GET";
        } else {
            methods = "POST";
        }
        RequestMeta meta = new RequestMeta();
        meta.url = APIContracts.baseUrl + urlName;
        meta.method = methods;
        meta.extraBody = extraBody;
        meta.authHeader = prabhupackagesHeader;
        return meta;
    }

    public static void setBody(String requestMedium) {

        js.addProperty(requestMedium, UserCore.Request_Medium);

    }
}
