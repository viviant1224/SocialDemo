package com.mengqitv.publishsomething;

import android.app.Application;

import com.mengqitv.publishsomething.utils.SharedPreferencesUtils;


/**
 * Created by xy on 15/12/23.
 */
public class GlobalContext extends Application {
    private static GlobalContext context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SharedPreferencesUtils.config(this);
    }
    public static GlobalContext getInstance() {
        return context;
    }
}
