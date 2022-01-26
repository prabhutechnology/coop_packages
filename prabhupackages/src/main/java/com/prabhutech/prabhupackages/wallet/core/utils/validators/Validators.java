package com.prabhutech.prabhupackages.wallet.core.utils.validators;

import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {
    public static final int REQUIRED = 1000;
    public static final int ACTIVATIONCODE = 992;
    public static final int NAME = 993;
    public static final int CURRENCY = 994;

    public static boolean isValidURL(String url) {
        String regXn = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
        Pattern pattern = Pattern.compile(regXn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isValidPhone(String s) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        if (s.isEmpty()) {
            return false;
        } else if (s.length() < 10) {
            return false;
        } else if (!matcher.matches()) {
            return false;
        } else {
            switch (s.substring(0, 3)) {
                case "985":
                case "984":
                case "986":
                case "980":
                case "981":
                case "982":
                case "972":
                case "961":
                case "962":
                case "988":
                case "974":
                case "975":
                case "976":
                    return true;
                default:
                    return false;
            }
        }
    }

    public static boolean isValidGlobalPhone(String s) {
        if (s.length() < 5) {
            return false;
        } else return s.length() <= 12;
    }

    public static boolean isValidLandline(String landline) {
        if (landline.isEmpty()) {
            return false;
        } else if ((landline.length() < 6)) {
            return false;
        } else {
            String landline_f3 = landline.substring(0, 3);
            String landline_f5 = landline.substring(3, 4);
            if (landline_f3.equals("014") || landline_f3.equals("015") || landline_f3.equals("016") || landline_f3.equals("012")) {
                switch (landline_f3) {
                    case "012":
                    case "014":
                    case "015":
                    case "016":
                        return true;
                    default:
                        return false;
                }
            } else {
                switch (landline_f5) {
                    case "4":
                    case "5":
                    case "6":
                    case "2":
                        return true;
                    default:
                        return false;

                }
            }

        }
    }

    public static boolean isValidActivationCode(String s) {
        return s.length() == 4;
    }

    public static boolean isValidName(String value) {
        String nameReg = "^[a-zA-Z ,.'-]+$";
        Pattern pattern = Pattern.compile(nameReg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isValidAddress(String address) {
        String regex = "^[a-zA-Z0-9 ,.'-()]+$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    public static boolean isValidSLRECName(String customer) {
        String regex = "^[a-dA-D][0-9]{11}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(customer);
        return matcher.matches();

    }

    public static boolean isNonEmptyInputEditText(TextInputEditText input) {

        if (!input.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNonZeroInputEditText(TextInputEditText input) {
        if (!input.getText().toString().isEmpty()) {
            if (Float.parseFloat(input.getText().toString()) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidNumber(TextInputEditText input) {

        try {
            if (Float.parseFloat(input.getText().toString()) > 0) {
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidNumberString(String input) {

        try {
            if (Float.parseFloat(input) > 0) {
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean luhnCheck(String card) {
        if (card == null)
            return false;
        char checkDigit = card.charAt(card.length() - 1);
        String digit = calculateCheckDigit(card.substring(0, card.length() - 1));
        return checkDigit == digit.charAt(0);


        //luhn algorithm
//        int nDigits = card.length();
//
//        int nSum = 0;
//        boolean isSecond = false;
//        for (int i = nDigits - 1; i >= 0; i--)
//        {
//
//            int d = card.charAt(i) - '0';
//
//            if (isSecond == true)
//                d = d * 2;
//
//            // We add two digits to handle
//            // cases that make two digits
//            // after doubling
//            nSum += d / 10;
//            nSum += d % 10;
//
//            isSecond = !isSecond;
//        }
//        if(nSum % 10 == 0){
//            return true;
//        }else{
//            return  false;
//        }
    }

    private static String calculateCheckDigit(String card) {
        JsonArray arr = new JsonArray();
        if (card == null)
            return null;
        String digit;
        /* convert to array of int for simplicity */
        int[] digits = new int[card.length()];
        for (int i = 0; i < card.length(); i++) {
            digits[i] = Character.getNumericValue(card.charAt(i));
        }


        /* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2) {
            digits[i] += digits[i];

            /* taking the sum of digits grater than 10 - simple trick by substract 9 */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }

        /* multiply by 9 step */
        sum = sum * 9;

        /* convert to string to be easier to take the last digit */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }


    /**
     * @param type  use InputType's declarations, if not available then use Validators's
     * @param value
     * @param <T>
     * @return true if validated, if not found at all then returns true as well.
     */
    public static <T> boolean validateUsingConst(int type, T value) {
        switch (type) {
            case REQUIRED:
                return !((String) value).isEmpty();
            case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                return isValidEmail((String) value);
            case InputType.TYPE_CLASS_PHONE:
                return isValidPhone((String) value);
            case ACTIVATIONCODE:
                return isValidActivationCode((String) value);
            case NAME:
                return isValidName((String) value);

        }
        return true;
    }

    public static boolean isValidPrice(String text) {
        if (isValidNumberString(text) && Float.parseFloat(text) > 9f) {
            return true;
        }
        return false;
    }

    public static boolean validatePassword(EditText password, TextInputLayout textInputLayout) {
        if (TextUtils.isEmpty(password.getText())) {
            textInputLayout.setError("Required");
            return false;
        }

        if (password.length() < 4) {
            textInputLayout.setError("Must be at least 4 characters");
            return false;
        }

        textInputLayout.setError("");
        return true;
    }

    public static boolean isValidIdNumber(String text) {
        return text.matches("^[a-zA-Z0-9 ,.'-()/-]+$");
    }

    public static class ValidCharset {

        public static String NAME = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ. -";
        public static String ADDRESS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ., -0123456789";
        public static final String COMPANY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ., -0123456789/\\&():;'@#_°";
        public static final String YOUTUUBE = "abcdefghijklmnopqustuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ., -0123456789/\\&():;'@#_°";
        public static final String EMAIL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,_!#$%&'*+@/=?^_`{|}~ -0123456789";

    }


    public static boolean isValidNumber(String text, int min, int max) {
        try {
            float num = Float.parseFloat(text);

            if (num >= min && num <= max) return true;
            else return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
