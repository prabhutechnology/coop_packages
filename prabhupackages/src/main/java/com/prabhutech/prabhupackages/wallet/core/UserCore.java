package com.prabhutech.prabhupackages.wallet.core;

public class UserCore {
    public static String userName = "";
    public static String password;
    public static String xToken;
    public static String merchantName;
    public static String merchantId;
    public static String merchantQRCode;
    public static String merchantMobile;
    public static String balance;
    public static String bonusPoints;
    public static String topupPoints;
    public static String accessToken;
    public static String customerId;
    public static String customerImgUrl;
    public static String fullName;
    public static String email;
    public static String country;
    public static String mobileNumber;
    public static String qrImageUrl;
    public static boolean isKycVerified;
    public static boolean hasRegisterEmail;
    public static String kycStatus = "null";
    public static boolean isMPINTrxnEnable = false;
    public static boolean isMpinSet;
    public static boolean isTrxnPinSet;
    public static String refreshToken;
    public static String expireTime;
    public static String currency = "Rs. ";
    public static String longitude = "0";
    public static String latitude = "0";
    public static String pinNumber = "";

    //PRABHU COOP
    public static String CBSCode = "NBSCOOP";
    public static String Request_Medium = "0c973793-4471-4201-98ee-6ce474006908";
    public static String token_username = "PrabhuPay";
    public static String token_password = "{Pr@bHupAy@2k21";

    public static boolean showKycInExplore = true;
    public static String deviceToken;
    public static boolean deviceTokenIsNew = false;

    public static String getValidName() {
        if (UserCore.userName != null && !UserCore.userName.isEmpty()) {
            return UserCore.userName;
        } else if (UserCore.fullName != null && !UserCore.fullName.isEmpty()) {
            return UserCore.fullName;
        } else {
            return "";
        }
    }
}
