package com.migs.learn.acl.journalapp.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

public class Utils {
    public static void showSnackbar(@StringRes int errorMessageRes, View mRootView) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
