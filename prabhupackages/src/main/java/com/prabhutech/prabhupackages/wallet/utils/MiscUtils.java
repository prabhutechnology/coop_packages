package com.prabhutech.coop.wallet.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

public class MiscUtils {

    /**
     * Use this to check if movie seat row name is in alphabetical order.
     *
     * @param values
     * @param singleCheck only check first two valid strings and leave
     */
    public static boolean isAlphabetic(List<String> values, boolean singleCheck) {
        if (values.size() == 0) return false;
        if (values.size() == 1 && values.get(0).toLowerCase().startsWith("a")) return true;
        for (int i = 0; i < values.size() - 1; i++) {
            int compare = values.get(i).compareTo(values.get(i + 1));
            // cases like a ... a or  s ... s
            if (compare == 0) {
                continue;
            } else if (compare < 0) { // cases like a ... b , c ... e
                if (singleCheck) return true;
            } else { // cases like b ... a, c ... b
                return false;
            }
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {

        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static ArrayList<String> getFirstsList(List<Pair<String, String>> arrayList) {
        ArrayList<String> temp = new ArrayList<>();
        for (Pair<String, String> item : arrayList) {
            temp.add(item.first);
        }

        return temp;
    }

    public static ArrayList<String> getSecondsList(List<Pair<String, String>> arrayList) {
        ArrayList<String> temp = new ArrayList<>();
        for (Pair<String, String> item : arrayList) {
            temp.add(item.second);
        }

        return temp;
    }

    public static ArrayList<Pair<String, String>> getFirstSecondPair(ArrayList<String> firsts, ArrayList<String> seconds) {

        ArrayList<Pair<String, String>> temp = new ArrayList<>();

        if (firsts == null || seconds == null) return null;
        if (firsts.size() != seconds.size()) return null;

        for (int i = 0; i < firsts.size(); i++) {
            temp.add(new Pair<>(firsts.get(i), seconds.get(i)));
        }

        return temp;
    }
}
