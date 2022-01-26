package com.prabhutech.prabhupackages.wallet.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.prabhutech.coop.wallet.core.api.utils.auth.JWTDecoder;
import com.prabhutech.prabhupackages.wallet.core.UserCore;

import java.util.HashMap;

public class UserPref extends PrefMaker {
    private static final String NAME = "USER_PREF";
    private static final String NAME_NOTIF = "USER_NOTIF";
    public static final String PREF_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String PREF_REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String PREF_EXPIRE_TOKEN = "EXPIRE_TOKEN";
    private static final String PREF_NOTIF_TOKEN = "NT";
    private static final String PREF_SUPERFLOUS_STATUS = "STATUS";
    private static final String PREF_CUSTOMER_ID = "CUSTOMER_ID";

    /**
     * Store default mobileNumber or email
     */
    public static final String PREF_PHONEOREMAIL = "PREF_PHONEOREMAIL";
    public static final String PREF_PHONE = "PREF_PHONE";
    private static final String CHOOSED_LANGUAGE = "Choosed_Language";
    // TODO: 9/24/19 change all users to NAME
    private static final String SETTING = "Setting";
    private static final String DEVICE_UNIQUE_ID = "UNIQUE_ID";

    /**
     * Store account detail KEY
     */
    public static final String BANK_BRANCH_KEY = "BANK_BRANCH_KEY";
    public static final String ACCOUNT_NO_KEY = "ACCOUNT_NO_KEY";
    public static final String ACCOUNT_NAME_KEY = "ACCOUNT_NAME_KEY";
    public static final String ACCOUNT_TYPE_KEY = "ACCOUNT_TYPE_KEY";
    public static final String ACCOUNT_INTEREST_RATE_KEY = "ACCOUNT_INTEREST_RATE_KEY";
    public static final String ACCOUNT_BALANCE_KEY = "ACCOUNT_BALANCE_KEY";
    public static final String ACCOUNT_POSITION_KEY = "ACCOUNT_POSITION_KEY";

    /**
     * Store visibility of account balance KEY
     */
    public static final String VISIBILITY_KEY = "VISIBILITY_KEY";


    public static SharedPreferences sharedPrefs(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static void setUser(Context context, String key, String value) {
        SharedPreferences.Editor editor = sharedPrefs(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setUserSessionData(Context context, String accessToken, String refreshToken, String expireTime) {
        SharedPreferences.Editor editor = sharedPrefs(context).edit();
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.putString(PREF_REFRESH_TOKEN, refreshToken);
        editor.putString(PREF_EXPIRE_TOKEN, expireTime);
        editor.apply();
    }


    /**
     * gets 'accessToken', 'refreshToken', 'expireTime' from prefs. It is expected that
     * only one instance of these 3 data can exist at a given time therefore the returned data
     * is stored in UserCore. You have to sync pref's data and UserCore's data every time.
     *
     * @param context
     */
    public static void getUserSessionData(Context context) {
        SharedPreferences prefs = sharedPrefs(context);
        UserCore.accessToken = prefs.getString(PREF_ACCESS_TOKEN, "");
        UserCore.refreshToken = prefs.getString(PREF_REFRESH_TOKEN, "");
        UserCore.expireTime = prefs.getString(PREF_EXPIRE_TOKEN, "");
    }

    public static void restoreUserPrefs(Context context) {
        SharedPreferences prefs = sharedPrefs(context);
        UserCore.accessToken = prefs.getString(PREF_ACCESS_TOKEN, "");
        UserCore.refreshToken = prefs.getString(PREF_REFRESH_TOKEN, "");
        UserCore.expireTime = prefs.getString(PREF_EXPIRE_TOKEN, "");
        UserCore.customerId = JWTDecoder.decodeToken(UserCore.accessToken);
    }

    /**
     * Clear the login session after logout
     *
     * @param context
     */
    public static void clearUserSessionData(Context context) {
        removeNotificationToken(context);
        SharedPreferences.Editor editor = sharedPrefs(context).edit();
        editor.putString(PREF_ACCESS_TOKEN, "");
        editor.putString(PREF_REFRESH_TOKEN, "");
        editor.putString(PREF_EXPIRE_TOKEN, "");

        UserCore.userName = null;
        UserCore.password = null;
        UserCore.balance = null;
        UserCore.bonusPoints = null;
        UserCore.topupPoints = null;
        UserCore.customerId = null;
        UserCore.customerImgUrl = null;
        UserCore.fullName = null;
        UserCore.email = null;
        UserCore.country = null;
        UserCore.mobileNumber = null;
        UserCore.qrImageUrl = null;
        UserCore.isKycVerified = false;
        UserCore.kycStatus = null;
        UserCore.showKycInExplore = true;
        editor.apply();

    }

    public static String getUser(Context context, String key) {
        return sharedPrefs(context).getString(key, "");
    }


    public static void setPayment(String req) {

    }

    public static void setLocale(Context context, String lan) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putString(CHOOSED_LANGUAGE, lan);
        editor.apply();
    }

    public static void setUniqueId(Context context, String uniqueId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putString(DEVICE_UNIQUE_ID, uniqueId);
        editor.apply();
    }

    public void setCustomerId(Context context, String customerId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_CUSTOMER_ID, customerId);
        editor.apply();
    }

    public String getCustomerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return prefs.getString(PREF_CUSTOMER_ID, "");
    }


    public static String getUniqueId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        String uniqueId = prefs.getString(DEVICE_UNIQUE_ID, "");
        return uniqueId;
    }


    public static void setNotificationDetails(Context context, boolean isKYCNotificationShown, boolean isAppUpdateNotificationShown) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putBoolean("isKYCNotificationShown", isKYCNotificationShown);
        editor.putBoolean("isAppUpdateNotificationShown", isAppUpdateNotificationShown);
        editor.apply();
    }


    public static void setDate(Context context, String date) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putString("SAVED_DATE", date);
        editor.apply();
    }

    public static String getDate(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        String savedDate = prefs.getString("SAVED_DATE", "");
        return savedDate;
    }

    // === NOTIFICATION === //

    /**
     * Save any notification data.
     *
     * @param context
     * @param token
     */
    public static void saveNotificationToken(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME_NOTIF, Context.MODE_PRIVATE).edit();
        editor.putBoolean("upload_success", true);
        editor.putString(PREF_NOTIF_TOKEN, token);
        editor.apply();
    }

    /**
     * Let anyone know that they may have to update notification token.
     *
     * @param context
     * @param token
     */
    public static void setFailedNotificationTokenUpdate(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME_NOTIF, Context.MODE_PRIVATE).edit();
        editor.putBoolean("upload_success", false);
        editor.putString("T", token);
        editor.apply();
    }

    public static String getNotificationToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(NAME_NOTIF, Context.MODE_PRIVATE);
        return pref.getString(PREF_NOTIF_TOKEN, "");
    }

    public static void removeNotificationToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_NOTIF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_NOTIF_TOKEN, "");
        editor.apply();
    }

    // Account settings

    /**
     * Enable/Disable fingerprint for login
     *
     * @param context
     * @param enabled
     */
    public static void setFingerprintEnabledForLogin(Context context, boolean enabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putBoolean("FINGERPRINT_LOGIN", enabled);
        editor.apply();
    }

    /**
     * Enable/Disable fingerprint for transaction
     *
     * @param context
     * @param enabled
     */
    public static void setFingerprintEnabledForTransaction(Context context, boolean enabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putBoolean("FINGERPRINT_TXN", enabled);
        editor.apply();
    }

    public static boolean getFingerprintEnabledForLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("FINGERPRINT_LOGIN", false);
    }

    public static boolean getFingerprintEnabledForTransaction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("FINGERPRINT_TXN", false);
    }

    public static void setFingerprintPinEnabledForTransaction(Context context, boolean enabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putBoolean("PIN_TXN", enabled);
        editor.apply();
    }

    public static boolean getFingerprintPinEnabledForTransaction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("PIN_TXN", false);
    }

    public static void toggleFingerprintPinEnabledForTransaction(Context context) {
        setFingerprintPinEnabledForTransaction(context, !getFingerprintPinEnabledForTransaction(context));
    }

    public static void clearAllSettings(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.clear().apply();
    }

    /**
     * Only put key vals if user
     */
    public enum SuperFlousStatus {
        /**
         * User has subscribed to 'all' topics.
         */
        NOTIFICATION_ALL_TOPIC_SUBSCRIBED,
        NOTIFICATION_TESTER_TOPIC_SUBSCRIBED,
        TOPIC_REJECTED,
        TOPIC_PENDING,
        TOPIC_APPROVED;
    }

    public synchronized static void setUserStatus(Context context, SuperFlousStatus superFlousStatus, boolean toggle) {
        SharedPreferences.Editor editor = sharedPrefs(context).edit();
        editor.putBoolean(superFlousStatus.name(), toggle);
        editor.apply();
    }

    public synchronized static boolean getUserStatus(Context context, SuperFlousStatus superFlousStatus) {
        SharedPreferences preferences = sharedPrefs(context);
        return preferences.getBoolean(superFlousStatus.name(), false);
    }

    /*Account Detail*/

    /**
     * Store account detail in shared preferences
     *
     * @param context             - Context
     * @param bankBranch          - String
     * @param accountNo           - String
     * @param accountName         - String
     * @param accountType         - String
     * @param accountInterestRate - String
     * @param accountBalance      - String
     * @param position            - Position of LIST
     */
    public static void setAccountDetail(Context context, String bankBranch, String accountNo, String accountName, String accountType, String accountInterestRate, String accountBalance, String position) {
        SharedPreferences.Editor editor = sharedPrefs(context).edit();
        editor.putString(BANK_BRANCH_KEY, bankBranch);
        editor.putString(ACCOUNT_NO_KEY, accountNo);
        editor.putString(ACCOUNT_NAME_KEY, accountName);
        editor.putString(ACCOUNT_TYPE_KEY, accountType);
        editor.putString(ACCOUNT_INTEREST_RATE_KEY, accountInterestRate);
        editor.putString(ACCOUNT_BALANCE_KEY, accountBalance);
        editor.putString(ACCOUNT_POSITION_KEY, position);
        editor.apply();
        System.out.println("Account Detail SAVED!!!");
    }

    /**
     * To retrieve account detail
     *
     * @param context - Context
     * @return hashmap of account detail includes bank branch, account no, account name, account type,
     * interest rate, account balance and account detail position
     */
    public static HashMap<String, String> getAccountDetail(Context context) {
        System.out.println("Account Detail RETRIEVED!!!");
        SharedPreferences preferences = sharedPrefs(context);
        HashMap<String, String> accountDetail = new HashMap<>();
        accountDetail.put(BANK_BRANCH_KEY, preferences.getString(BANK_BRANCH_KEY, null));
        accountDetail.put(ACCOUNT_NO_KEY, preferences.getString(ACCOUNT_NO_KEY, null));
        accountDetail.put(ACCOUNT_NAME_KEY, preferences.getString(ACCOUNT_NAME_KEY, null));
        accountDetail.put(ACCOUNT_TYPE_KEY, preferences.getString(ACCOUNT_TYPE_KEY, null));
        accountDetail.put(ACCOUNT_INTEREST_RATE_KEY, preferences.getString(ACCOUNT_INTEREST_RATE_KEY, null));
        accountDetail.put(ACCOUNT_BALANCE_KEY, preferences.getString(ACCOUNT_BALANCE_KEY, null));
        accountDetail.put(ACCOUNT_POSITION_KEY, preferences.getString(ACCOUNT_POSITION_KEY, null));
        return accountDetail;
    }

    /**
     * It simply clears account detail saved in shared preference
     *
     * @param context - Context
     */
    public static void clearAccountDetail(Context context) {
        SharedPreferences.Editor accountDetailEditor = sharedPrefs(context).edit();
        accountDetailEditor.remove(BANK_BRANCH_KEY);
        accountDetailEditor.remove(ACCOUNT_NO_KEY);
        accountDetailEditor.remove(ACCOUNT_NAME_KEY);
        accountDetailEditor.remove(ACCOUNT_TYPE_KEY);
        accountDetailEditor.remove(ACCOUNT_INTEREST_RATE_KEY);
        accountDetailEditor.remove(ACCOUNT_BALANCE_KEY);
        accountDetailEditor.remove(ACCOUNT_POSITION_KEY);
        accountDetailEditor.apply();
        System.out.println("Account Detail deleted!!!");
    }

    /* Balance Visibility */
    public static void setBalanceVisibility(Context context, boolean visibility) {
        SharedPreferences.Editor editor = sharedPrefs(context).edit();
        editor.putBoolean(VISIBILITY_KEY, visibility);
        editor.apply();
        Log.e("TAG", "setBalanceVisibility: " + visibility);
    }

    public static boolean isBalanceVisible(Context context) {
        SharedPreferences preferences = sharedPrefs(context);
        return preferences.getBoolean(VISIBILITY_KEY, true);
    }

    public static void clearBalanceVisibility(Context context) {
        SharedPreferences.Editor visibilityEditor = sharedPrefs(context).edit();
        visibilityEditor.remove(BANK_BRANCH_KEY);
        visibilityEditor.apply();
    }
}
