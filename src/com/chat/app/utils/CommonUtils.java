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
 * ����������
 * 
 * @author xs
 * 
 */

public class CommonUtils {

	/**
	 * �ж������Ƿ�����
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
	 * ����Ƿ���WIFI
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
	 *  ����Ƿ����ƶ�����
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
     * ��װһ��APK�ļ�
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    

    // MD5��Q
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
     * ��ȡ��Ļ���
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
     * ͨ���ⲿ�������ҳ��
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
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
     * @param context �����ģ�һ��ΪActivity
     * @param dpValue dp����ֵ
     * @return px����ֵ
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
     * @param context �����ģ�һ��ΪActivity
     * @param pxValue px����ֵ
     * @return dp����ֵ
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    
    /**
     * ��ȡӦ�ð汾��
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
     * ������ʾ��
     */
    public static void startHintVoice(){
		ChatApplication.getInstance().getMediaPlayer().start();
	}
}
