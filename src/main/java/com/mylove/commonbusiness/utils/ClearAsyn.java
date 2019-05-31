package com.mylove.commonbusiness.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

public class ClearAsyn extends AsyncTask<Void,Void,List<ActivityManager.RunningAppProcessInfo>> {
    WeakReference<Activity> mWeakReference;

    public ClearAsyn(Activity activity) {
        mWeakReference=new WeakReference<Activity>(activity);
    }

    @Override
    protected List<ActivityManager.RunningAppProcessInfo> doInBackground(Void... voids) {
        final Activity activity=mWeakReference.get();
        if(activity!=null)
        {
            List<ActivityManager.RunningAppProcessInfo> recycle = SystemUtils.getRunningApp(activity);
            SystemUtils.forceApp(recycle);
            return recycle;
        }
        return null;
    }

    @Override
    protected void onPostExecute(final List<ActivityManager.RunningAppProcessInfo> recycle) {
        super.onPostExecute(recycle);

    }
}
