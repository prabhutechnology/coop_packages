package com.prabhutech.prabhupackages.wallet.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class NavigationRedirection {
    /**
     * Replace activity container with fragment
     *
     * @param activity          - Require Activity
     * @param fragmentContainer - Fragment Container View
     * @param fragment          - Action id
     */
    public void replaceFragmentContainerWithFragment(Activity activity, int fragmentContainer, int fragment) {
        Navigation.findNavController(activity, fragmentContainer).navigate(fragment);
    }

    /**
     * Enables to redirect fragment with or without args
     * Accepts NavDirections as well as action id
     *
     * @param v      - View
     * @param action - Action with action id as well as safe args
     */
    public void navigateToFragmentWithAction(View v, Object action) {
        if (Integer.class.equals(action.getClass())) {
            Navigation.findNavController(v).navigate((Integer) action);
        } else
            Navigation.findNavController(v).navigate((NavDirections) action);
    }

    /**
     * Navigation with bundle
     *
     * @param v      - View
     * @param action - Action
     * @param bundle - Bundle
     */
    public void navigateWithBundle(View v, int action, Bundle bundle) {
        Navigation.findNavController(v).navigate(action, bundle);
    }

    /**
     * Navigation with NavController
     *
     * @param navController - NavController
     * @param action        - Action id
     */
    public void navigateWithNavController(NavController navController, int action) {
        navController.navigate(action);
    }

    /**
     * Same as back press
     *
     * @param v - View
     */
    public void navigateBack(View v) {
        Navigation.findNavController(v).navigateUp();
    }

    /**
     * Setup for NavController
     *
     * @param activity          - Activity
     * @param fragmentContainer - Fragment Container for the activity
     * @return NavController
     */
    public NavController setupNavController(Activity activity, int fragmentContainer) {
        return Navigation.findNavController(activity, fragmentContainer);
    }
}
