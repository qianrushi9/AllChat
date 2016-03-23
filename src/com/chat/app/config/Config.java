package com.chat.app.config;

import android.os.Environment;

/**
 * ϵͳ������
 * 
 * @author xs
 * 
 */
public class Config {
	/**
	 *  ����Bmob��ApplicationId,���ڳ�ʼ������
	 */
	public static String applicationId = "f8185149613d956dd730001fc698bd50";
	
	/**
	 * �Զ���¼���
	 */
	public static final String FILE_NAME = "daoge";    //�������ֻ��е��ļ���
	public static final String USER_NAME = "username";  //�û���
	public static final String PASSWORD = "password";   //����
	public static final String IS_REMEMBER = "is_remember";
	public static final String IS_AUTOLOGIN = "is_autologin";
	
	/**
	 * �������
	 */
	public static final String IS_NOTIFY = "is_notify";  //�Ƿ�����
	public static final String IS_VOICE = "is_voice";    //�Ƿ�������
	public static final String IS_VIBRATE = "is_vibrate";  //�Ƿ�����
	
	/**
	 * ����ͼƬ��Ŀ¼
	 */
	public static String BMOB_MSG_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/Xs_Chat_App/Message/image/";
	
	/**
	 * ״̬ͼƬ��Ŀ¼
	 */
	public static String BMOB_MOOD_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/Xs_Chat_App/Mood/image/";
	
	/**
	 * ͷ�񱣴�Ŀ¼
	 */
	public static String MyAvatarDir = "/sdcard/Xs_Chat_App/avatar/";
	/**
	 * ѡȡ����ͼƬ�Ļص�
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//�����޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//��������޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//ϵͳ�ü�ͷ��
	
	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//����
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//����ͼƬ
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//λ��
}
