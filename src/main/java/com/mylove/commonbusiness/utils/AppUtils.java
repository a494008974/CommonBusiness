package com.mylove.commonbusiness.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by TOME .
 * @data on      2018/6/25 11:12
 * @describe ${App相关工具类}
 */

public class AppUtils {

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }


    /**
     * 对比本地与线上的版本号
     * */
    public static boolean needUpdateV2(String local, String online) {


        boolean need = false;

        if (local != null && online != null) {

            String[] onlines = online.split("\\.");
            String[] locals = local.split("\\.");

            // 2.0.0-1.0.0
            // 2.1.0-2.0.0
            // 2.1.1-2.1.0


            //3.0.0-4.0.0
            //3.1.0-3.2.0
            //3.1.1-3.2.0

            if (Integer.parseInt(onlines[0]) > Integer.parseInt(locals[0])) {
                need = true;
//                Logger.e("1-1");

            } else if (Integer.parseInt(onlines[0]) == Integer.parseInt(locals[0])) {
//                Logger.e("1-2");
                if (Integer.parseInt(onlines[1]) > Integer.parseInt(locals[1])) {
                    need = true;
//                    Logger.e("1-2-1");

                } else if (Integer.parseInt(onlines[1]) == Integer.parseInt(locals[1])) {
//                    Logger.e("1-2-2");
                    if (Integer.parseInt(onlines[2]) > Integer.parseInt(locals[2])) {
//                        Logger.e("1-2-2-1");
                        need = true;
                    } else {
//                        Logger.e("1-2-2-2");
                        need = false;
                    }

                } else if (Integer.parseInt(onlines[1]) < Integer.parseInt(locals[1])) {
                    need = false;
//                    Logger.e("1-2-3");
                }


            } else if (Integer.parseInt(onlines[0]) < Integer.parseInt(locals[0])) {
                need = false;
//                Logger.e("1-3");
            }

        }
        return need;


    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    public static boolean openApp(Context ctx, String pkgname){
        try {
            PackageManager pm = ctx.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(pkgname);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
            return true;
        }catch (Exception e){
        }
        return false;
    }

    public static void install(Context ctx, String filename) {
        File apk = new File(filename);
        Uri data;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(ctx, "com.mylove.tvlauncher.install", apk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apk);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    public static void uninstall(Context ctx, String apk) {
        Uri packageURI = Uri.parse("package:" + apk);// "package:com.demo.CanavaCancel"
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(uninstallIntent);
    }

}
