package com.chat.app.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.chat.app.application.ChatApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;

/**
 * 公共工具类
 * 
 * @author xs
 * 
 */

public class CommonUtils {

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager != null) {
			NetworkInfo netInfo = connManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 检查是否是WIFI
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = connManager.getActiveNetworkInfo();
		if (netInfo != null) {
			if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;

	}

	/**
	 *  检查是否是移动网络
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = connManager.getActiveNetworkInfo();
		if (netInfo != null) {
			if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}
	
	 /**
     * 安装一个APK文件
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    

    // MD5変換
    public static String Md5(String str) {
        if(str != null && !str.equals("")) {
            try {
                MessageDigest md5=MessageDigest.getInstance("MD5");
                char[] HEX={'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                byte[] md5Byte=md5.digest(str.getBytes("UTF8"));
                StringBuffer sb=new StringBuffer();
                for(int i=0; i < md5Byte.length; i++) {
                    sb.append(HEX[(int)(md5Byte[i] & 0xff) / 16]);
                    sb.append(HEX[(int)(md5Byte[i] & 0xff) % 16]);
                }
                str=sb.toString();
            } catch(NoSuchAlgorithmException e) {

            } catch(Exception e) {
            }
        }
        return str;
    }
    

    /**
     * 获取屏幕宽高
     * @param activity
     * @return
     */
    public static int[] getScreenSize(Activity activity) {
        int[] screens;
        // if (Constants.screenWidth > 0) {
        // return screens;
        // }
        DisplayMetrics dm=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screens=new int[]{dm.widthPixels, dm.heightPixels};
        return screens;
    }
    
    /**
     * 通过外部浏览器打开页面
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String urlText) {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url=Uri.parse(urlText);
        intent.setData(url);
        context.startActivity(intent);
    }
    
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context 上下文，一般为Activity
     * @param dpValue dp数据值
     * @return px像素值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context 上下文，一般为Activity
     * @param pxValue px像素值
     * @return dp数据值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    
    /**
     * 获取应用版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm=context.getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch(NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
    /**
     * 开启提示音
     */
    public static void startHintVoice(){
		ChatApplication.getInstance().getMediaPlayer().start();
	}
}
