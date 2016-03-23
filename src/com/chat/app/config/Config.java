package com.chat.app.config;

import android.os.Environment;

/**
 * 系统配置类
 * 
 * @author xs
 * 
 */
public class Config {
	/**
	 *  这是Bmob的ApplicationId,用于初始化操作
	 */
	public static String applicationId = "f8185149613d956dd730001fc698bd50";
	
	/**
	 * 自动登录相关
	 */
	public static final String FILE_NAME = "daoge";    //保存在手机中的文件名
	public static final String USER_NAME = "username";  //用户名
	public static final String PASSWORD = "password";   //密码
	public static final String IS_REMEMBER = "is_remember";
	public static final String IS_AUTOLOGIN = "is_autologin";
	
	/**
	 * 设置相关
	 */
	public static final String IS_NOTIFY = "is_notify";  //是否推送
	public static final String IS_VOICE = "is_voice";    //是否开启声音
	public static final String IS_VIBRATE = "is_vibrate";  //是否开启震动
	
	/**
	 * 发送图片的目录
	 */
	public static String BMOB_MSG_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/Xs_Chat_App/Message/image/";
	
	/**
	 * 状态图片的目录
	 */
	public static String BMOB_MOOD_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/Xs_Chat_App/Mood/image/";
	
	/**
	 * 头像保存目录
	 */
	public static String MyAvatarDir = "/sdcard/Xs_Chat_App/avatar/";
	/**
	 * 选取本地图片的回调
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像
	
	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
}
