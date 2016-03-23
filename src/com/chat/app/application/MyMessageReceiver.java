package com.chat.app.application;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.chat.app.activity.MainActivity;
import com.chat.app.activity.NewFriendActivity;
import com.chat.app.activity.R;
import com.chat.app.config.Config;
import com.chat.app.utils.CollectionUtils;
import com.chat.app.utils.CommonUtils;
import com.chat.app.utils.SPUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.config.BmobConstant;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.FindListener;


/**
 * ������Ϣ������
 * @ClassName: MyMessageReceiver
 * @Description: TODO
 * @author xs
 * @date 2016-2-10 ����
 */
public class MyMessageReceiver extends BroadcastReceiver {

	// �¼�����
	public static ArrayList<EventListener> events = new ArrayList<EventListener>();
	
	public static final int NOTIFY_ID = 0x000;
	public static int newMsgNum = 0;//
	BmobUserManager userManager;
	BmobChatUser currentUser;

	//������뷢���Զ����ʽ����Ϣ����ʹ��sendJsonMessage����������Json��ʽ���ַ�����Ȼ���㰴�ո�ʽ�Լ�����������
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String json = intent.getStringExtra("msg");
		BmobLog.i("�յ���message = " + json);
		
		userManager = BmobUserManager.getInstance(context);
		currentUser = userManager.getCurrentUser();
		boolean isNetConnected = CommonUtils.isNetWorkAvailable(context);
		if(isNetConnected){
			handleMessage(context, json);
		}else{
			for (int i = 0; i < events.size(); i++)
				((EventListener) events.get(i)).onNetChange(isNetConnected);
		}
	}

	/** ����Json�ַ���
	  * @Title: parseMessage
	  * @Description: TODO
	  * @param @param context
	  * @param @param json 
	  * @return void
	  * @throws
	  */
	private void handleMessage(final Context context, String json) {
		JSONObject josnObject;
		try {
			josnObject = new JSONObject(json);
			String tag = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TAG);
			if(tag.equals(BmobConfig.TAG_OFFLINE)){//����֪ͨ
				if(currentUser!=null){
					if (events.size() > 0) {// �м�����ʱ�򣬴�����ȥ
						for (EventListener eventListener : events)
							eventListener.onOffline();
					}else{
						//�������
						ChatApplication.getInstance().logout();
					}
				}
			}else{
				String fromId = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TARGETID);  //��Ϣ��Դ�û�objectid--Ŀ���ǽ�����˻���½ͬһ�豸ʱ���޷����յ��ǵ�ǰ��½�û�����Ϣ��
				final String toId = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TOID);  //����Ϣ���շ�
				String msgTime = BmobJsonUtil.getString(josnObject,BmobConstant.PUSH_READED_MSGTIME);  //������ʱ��
				if(fromId!=null && !BmobDB.create(context,toId).isBlackUser(fromId)){//����Ϣ���ͷ���Ϊ�������û�
					if(TextUtils.isEmpty(tag)){//��Я��tag��ǩ--�˿ɽ���İ���˵���Ϣ
						BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {
							
							@Override
							public void onSuccess(BmobMsg msg) {
								// TODO Auto-generated method stub
								if (events.size() > 0) {// �м�����ʱ�򣬴�����ȥ
									for (int i = 0; i < events.size(); i++) {
										((EventListener) events.get(i)).onMessage(msg);
									}
								} else {
									//û�м�����ʱ������
									boolean isAllowNtify = (Boolean) SPUtils.get(context, Config.IS_NOTIFY, true);
									if(isAllowNtify && currentUser!=null && currentUser.getObjectId().equals(toId)){//��ǰ��½�û����ڲ���Ҳ���ڽ��շ�id
										newMsgNum++;
										showMsgNotify(context,msg);
									}
								}
							}
							
							@Override
							public void onFailure(int code, String arg1) {
								// TODO Auto-generated method stub
								BmobLog.i("��ȡ���յ���Ϣʧ�ܣ�"+arg1);
							}
						});
						
					}else{//��tag��ǩ
						if(tag.equals(BmobConfig.TAG_ADD_CONTACT)){  //��Ӻ���
							//���������������أ������º�̨��δ���ֶ�
							BmobInvitation message = BmobChatManager.getInstance(context).saveReceiveInvite(json, toId);
							if(currentUser!=null){//�е�½�û�
								if(toId.equals(currentUser.getObjectId())){
									if (events.size() > 0) {// �м�����ʱ�򣬴�����ȥ
										for (EventListener eventListener : events)
											eventListener.onAddUser(message);
									}else{
										//�����º�������,ָ���������ת��NewFriendActivity
										showOtherNotify(context, message.getFromname(), toId,  message.getFromname()+"������Ӻ���", NewFriendActivity.class);
									}
								}
							}
						}else if(tag.equals(BmobConfig.TAG_ADD_AGREE)){
							String username = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TARGETUSERNAME);
							//�յ��Է���ͬ������֮�󣬾͵���ӶԷ�Ϊ����--��Ĭ�����ͬ�ⷽΪ���ѣ������浽���غ������ݿ�
							BmobUserManager.getInstance(context).addContactAfterAgree(username, new FindListener<BmobChatUser>() {
								
								@Override
								public void onError(int arg0, final String arg1) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onSuccess(List<BmobChatUser> arg0) {
									// TODO Auto-generated method stub
									//���浽�ڴ���
									ChatApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(context).getContactList()));
								}
							});
							//��ʾ֪ͨ,�������ת��MainActivity
							showOtherNotify(context, username, toId,  username+"ͬ�������Ϊ����", MainActivity.class);
							//����һ����ʱ��֤�Ự--�����ڻỰ�����γɳ�ʼ�Ự
							BmobMsg.createAndSaveRecentAfterAgree(context, json);
							
						}else if(tag.equals(BmobConfig.TAG_READED)){//�Ѷ���ִ
							String conversionId = BmobJsonUtil.getString(josnObject,BmobConstant.PUSH_READED_CONVERSIONID);
							if(currentUser!=null){
								//����ĳ����Ϣ��״̬
								BmobChatManager.getInstance(context).updateMsgStatus(conversionId, msgTime);
								if(toId.equals(currentUser.getObjectId())){
									if (events.size() > 0) {// �м�����ʱ�򣬴�����ȥ--�����޸Ľ���
										for (EventListener eventListener : events)
											eventListener.onReaded(conversionId, msgTime);
									}
								}
							}
						}
					}
				}else{//�ں������ڼ����е���Ϣ��Ӧ����Ϊ�Ѷ�����Ȼ��ȡ��������֮���ֿ��Բ�ѯ�ĵ�
					BmobChatManager.getInstance(context).updateMsgReaded(true, fromId, msgTime);
					BmobLog.i("����Ϣ���ͷ�Ϊ�������û�");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//�����ȡ�����п�����web��̨���͸��ͻ��˵���Ϣ��Ҳ�п����ǿ������Զ��巢�͵���Ϣ����Ҫ���������н����ʹ���
			BmobLog.i("parseMessage����"+e.getMessage());
		}
	}
	
	/** 
	 *  ��ʾ������Ϣ��֪ͨ
	  * @Title: showNotify
	  * @return void
	  * @throws
	  */
	public void showMsgNotify(Context context,BmobMsg msg) {
		// ����֪ͨ��
		int icon = R.drawable.app_icon;
		String trueMsg = "";
		if(msg.getMsgType()==BmobConfig.TYPE_TEXT && msg.getContent().contains("\\ue")){
			trueMsg = "[����]";
		}else if(msg.getMsgType()==BmobConfig.TYPE_IMAGE){
			trueMsg = "[ͼƬ]";
		}else if(msg.getMsgType()==BmobConfig.TYPE_VOICE){
			trueMsg = "[����]";
		}else if(msg.getMsgType()==BmobConfig.TYPE_LOCATION){
			trueMsg = "[λ��]";
		}else{
			trueMsg = msg.getContent();
		}
		CharSequence tickerText = msg.getBelongUsername() + ":" + trueMsg;
		String contentTitle = msg.getBelongUsername()+ " (" + newMsgNum + "������Ϣ)";
		
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		boolean isAllowVoice = (Boolean) SPUtils.get(context, Config.IS_VOICE, true);
		boolean isAllowVibrate = (Boolean) SPUtils.get(context, Config.IS_VIBRATE, true);
		
		BmobNotifyManager.getInstance(context).showNotifyWithExtras(isAllowVoice,isAllowVibrate,icon, tickerText.toString(), contentTitle, tickerText.toString(),intent);
	}
	
	
	/** ��ʾ����Tag��֪ͨ
	  * showOtherNotify
	  */
	public void showOtherNotify(Context context,String username,String toId,String ticker,Class<?> cls){
		boolean isAllowNitify = (Boolean) SPUtils.get(context, Config.IS_NOTIFY, true);
		boolean isAllowVoice = (Boolean) SPUtils.get(context, Config.IS_VOICE, true);
		boolean isAllowVibrate = (Boolean) SPUtils.get(context, Config.IS_VIBRATE, true);
		if(isAllowNitify && currentUser!=null && currentUser.getObjectId().equals(toId)){
			//ͬʱ����֪ͨ
			BmobNotifyManager.getInstance(context).showNotify(isAllowVoice,isAllowVibrate,R.drawable.app_icon, ticker,username, ticker.toString(),NewFriendActivity.class);
		}
	}
	
}
