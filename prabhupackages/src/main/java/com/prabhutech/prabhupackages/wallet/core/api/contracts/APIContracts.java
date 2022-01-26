package com.prabhutech.prabhupackages.wallet.core.api.contracts;


import com.prabhutech.prabhupackages.BuildConfig;

@com.prabhutech.prabhupackages.wallet.core.api.contracts.IBaseAPIContract
public class APIContracts {
    public static final String baseUrl;
    public static final String collectorUrl;
    public static final String votingBaseUrl;
    public static final String appServerBaseUrl = "https://apps.prabhupay.com/api/";
    public static final String TERMS_N_CONDITION_URL = "https://prabhupay.com/termsandconditions.html";
    public static final String ppWebAppUrl;

    // pair of header and operator code
//    public static List<Pair<String, String>> EXCLUDED_PRODUCTS = Arrays.asList(Pair.create("Internet", "16"));

    static {
        if (BuildConfig.SERVER == 2) {
            baseUrl = "https://testmb.creationsoftnepal.com/api/";
            collectorUrl = "https://testmb.creationsoftnepal.com/api/";
            votingBaseUrl = "https://testvoting.prabhupay.com/Mobile/";
            ppWebAppUrl = "https://apps.prabhupay.com/pages/devwebapp/#/";
        } else if (BuildConfig.SERVER == 1) {
            baseUrl = "https://testmb.creationsoftnepal.com/api/";
            collectorUrl = "https://testmb.creationsoftnepal.com/api/";
            votingBaseUrl = "https://testvoting.prabhupay.com/Mobile/";
            ppWebAppUrl = "https://apps.prabhupay.com/pages/devwebapp/#/";
        } else {
            baseUrl = "https://testmb.creationsoftnepal.com/api/";
            collectorUrl = "https://testmb.creationsoftnepal.com/api/";
            votingBaseUrl = "https://voting.prabhupay.com/Mobile/";
            ppWebAppUrl = "https://apps.prabhupay.com/pages/webapp/#/";
        }

        System.out.println("APIContracts: static init");
    }

    public static class APIName {

        public static class User {
            //Auth
            public static final String GET_TOKEN = "auth/token";

            //Login and registration
            public static final String CBS_USER_LOGIN = "User/CBSLogin";
            public static final String CBS_USER_REGISTRATION = "Customer/RegisterCBSCustomer";
            public static final String VERIFY_CBS_OTP = "Customer/VerifyCBSOTP";
            public static final String RESEND_CBS_OTP = "Customer/GetCBSOTP";
            public static final String SET_CBS_USER_PASSWORD = "User/SetCBSUserPassword";
            public static final String CHANGE_CBS_USER_PASSWORD = "User/ChangeCBSUserPassword";
            public static final String CBS_USER_LOGOUT = "Customer/Logout";

            //Forgot password
            public static final String FORGOT_CBS_USER_PASSWORD = "User/ForgotCBSUserPassword";

            //Route after authentication
            public static final String GET_CBS_CUSTOMER_DETAIL = "Customer/GetCBSCustomerDetail";
            public static final String GET_CBS_ACTIVE_ACCOUNT_LIST = "Customer/GetCBSActiveAccountList";

            public static final String RefreshToken = "Token/Refresh/refresh";
            public static final String StoreDeviceToken = "Customer/StoreDeviceToken";
        }

        /**
         * For CBS transactions
         */
        public static class CBSTransaction {
            public static final String GET_CBS_ACCOUNT_BALANCE = "Transaction/GetCBSAccountBalance";
            public static final String GET_CBS_LAST_THIRTY_DAY_BALANCE = "Transaction/GetCBSLastThirtyDayBalance";
            public static final String GET_CBS_ACCOUNT_STATEMENT = "Transaction/GetCBSAccountStatement";
            public static final String GET_CBS_LAST_SEVEN_TRANSACTION = "Transaction/GetCBSLastSevenTransaction";
        }

        public static class CBSProduct {
            public static final String GET_CBS_PRODUCT_SERVICE_LIST = "Product/GetProductList";
        }

        public static class CBSTopUp {
            public static final String GET_CHECK_MOBILE_NUMBER_TYPE = "MobileTopUp/CheckMobileNo";
            public static final String CBS_MOBILE_TOP_UP = "MobileTopUp/CBSMobileTopUp";
        }

        public static class CBSInternet{
            public static final String GET_INTERNET_DETAILS = "Internet/GetDetails";
            public static final String INTERNET_MAKE_PAYMENT = "Internet/MakePayment";
        }

        public static class Kyc {
            public static final String Register = "Kyc/REGISTER";
        }

        public static class Apps {
            public static final String CheckUpdate = "AppVersion/GetList";
        }
    }

    /**
     * Include urls here if you think re-authentication should not be called for them. i.e. refreshing
     * token if token has expired.
     */
    public static final String[] REAUTH_RESTRICTED_URL = new String[]{
            APIName.User.CBS_USER_LOGIN,
            APIName.User.RefreshToken,
            APIName.Kyc.Register,
            APIName.User.SET_CBS_USER_PASSWORD,
            APIName.User.VERIFY_CBS_OTP,
            APIName.User.RESEND_CBS_OTP
    };
}
