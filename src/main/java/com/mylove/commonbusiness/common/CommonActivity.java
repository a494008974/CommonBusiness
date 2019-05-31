package com.mylove.commonbusiness.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;

import com.jess.arms.base.BaseActivity;
import com.mylove.commonbusiness.utils.ClearAsyn;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import me.jessyan.armscomponent.commonres.download.DownloadUtil;
import me.jessyan.armscomponent.commonres.utils.Contanst;
import me.jessyan.armscomponent.commonsdk.utils.FileUtils;

import static me.jessyan.armscomponent.commonres.utils.Contanst.MSG_WHAT_LAUNCHER_NET;
import static me.jessyan.armscomponent.commonres.utils.Contanst.MSG_WHAT_LAUNCHER_TIME;
import static me.jessyan.armscomponent.commonres.utils.Contanst.MSG_WHAT_RECEIVE_APP;

public abstract class CommonActivity<P extends CommonPresenter> extends BaseActivity<P> {
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if(mPresenter != null){
            mPresenter.initLauncher(this);
        }
        DownloadUtil.get().init(this);

        if(Environment.isExternalStorageEmulated()){
            Contanst.path = Environment.getExternalStorageDirectory().getPath()+"/mbox";
        }else{
            Contanst.path = this.getExternalFilesDir(null).getPath()+"/mbox";
        }
        FileUtils.createOrExistsDir(Contanst.path);
        FileUtils.deleteDir(Contanst.path);

        register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
        fixInputMethodManagerLeak(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ClearAsyn(this).execute();
    }

    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) { return; }
        try {
            // 对 mCurRootView mServedView mNextServedView 进行置空...
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }

            Object obj_get = null;
            Field f_mCurRootView = imm.getClass().getDeclaredField("mCurRootView");
            Field f_mServedView = imm.getClass().getDeclaredField("mServedView");
            Field f_mNextServedView = imm.getClass().getDeclaredField("mNextServedView");

            if (f_mCurRootView.isAccessible() == false) {
                f_mCurRootView.setAccessible(true);
            }
            obj_get = f_mCurRootView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mCurRootView.set(imm, null);
            }

            if (f_mServedView.isAccessible() == false) {
                f_mServedView.setAccessible(true);
            }
            obj_get = f_mServedView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mServedView.set(imm, null);
            }

            if (f_mNextServedView.isAccessible() == false) {
                f_mNextServedView.setAccessible(true);
            }
            obj_get = f_mNextServedView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mNextServedView.set(imm, null);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    //=======================广播====================
    public void register() {
        mNetWorkChangeReceiver = new NetWorkChangeReceiver(this);
        IntentFilter filterNECT = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filterNECT.addAction("android.net.wifi.STATE_CHANGE");
        filterNECT.addAction("android.net.ethernet.STATE_CHANGE");
        registerReceiver(mNetWorkChangeReceiver, filterNECT);

        mTimeReceiver = new TimeReceiver();
        IntentFilter filterTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, filterTime);

        mAppReceiver = new AppReceiver();
        IntentFilter filterAPP = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filterAPP.addDataScheme("package");
        filterAPP.addAction(Intent.ACTION_PACKAGE_REMOVED);
        registerReceiver(mAppReceiver, filterAPP);

        mUsbAndSDCardBroadcastReceiver = new UsbAndSDCardBroadcastReceiver();
        IntentFilter mUsbAndSDCardFilter = new IntentFilter();
        mUsbAndSDCardFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        mUsbAndSDCardFilter.addAction("android.intent.action.MEDIA_REMOVED");
        mUsbAndSDCardFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        mUsbAndSDCardFilter.addDataScheme("file");
        registerReceiver(mUsbAndSDCardBroadcastReceiver, mUsbAndSDCardFilter);
    }

    public void unregister() {
        try {
            if (mNetWorkChangeReceiver != null) {
                unregisterReceiver(mNetWorkChangeReceiver);
            }

            if (mTimeReceiver != null) {
                unregisterReceiver(mTimeReceiver);
            }

            if (mAppReceiver != null) {
                unregisterReceiver(mAppReceiver);
            }

            if (mUsbAndSDCardBroadcastReceiver != null) {
                unregisterReceiver(mUsbAndSDCardBroadcastReceiver);
            }
        }catch(Exception e){

        }
    }

    private AppReceiver mAppReceiver;
    public class AppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String packageName = intent.getDataString();
            packageName = packageName.split(":")[1];
            Message msg = Message.obtain();
            msg.what = MSG_WHAT_RECEIVE_APP;
            Bundle bundle = new Bundle();
            bundle.putString("packageName",packageName);
            bundle.putString("action",intent.getAction());
            msg.setData(bundle);
            EventBus.getDefault().post(msg);
        }
    }

    String usbMountedPath = null;
    UsbAndSDCardBroadcastReceiver mUsbAndSDCardBroadcastReceiver;
    class UsbAndSDCardBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if ((intent.getAction().equals("android.intent.action.MEDIA_REMOVED"))
                    || (intent.getAction().equals("android.intent.action.MEDIA_BAD_REMOVAL"))) {

            }
            if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
                usbMountedPath = intent.getData().getPath();
            }
        }
    }

    private TimeReceiver mTimeReceiver;
    public class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                Message msg = Message.obtain();
                msg.what = MSG_WHAT_LAUNCHER_TIME;
                EventBus.getDefault().post(msg);
            }
        }
    }
    boolean done = false;
    NetWorkChangeReceiver mNetWorkChangeReceiver;
    public class NetWorkChangeReceiver extends BroadcastReceiver {

        WeakReference<Activity> mWeakReference;

        private ConnectivityManager connectivityManager;
        private NetworkInfo info;

        public NetWorkChangeReceiver(Activity activity) {
            mWeakReference=new WeakReference<Activity>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                final Activity activity=mWeakReference.get();
                if(activity != null){
                    Message msg = Message.obtain();
                    msg.what = MSG_WHAT_LAUNCHER_NET;

                    connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if(mNetworkInfo != null && mNetworkInfo.isAvailable()){
                        if(mPresenter != null){
                            mPresenter.fetchHomeData();
                            mPresenter.fetchAD();
                        }
                        switch (mNetworkInfo.getType()) {
                            case  ConnectivityManager.TYPE_WIFI:
                                msg.arg1 = 1;
                                break;
                            case  ConnectivityManager.TYPE_ETHERNET:
                                msg.arg1 = 2;
                                break;
                        }
                    }else{
                        msg.arg1 = 3;
                    }
                    EventBus.getDefault().post(msg);
                }
            }
        }
    }
}
