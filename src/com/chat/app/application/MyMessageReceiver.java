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
 * 推送消息接收器
 * @ClassName: MyMessageReceiver
 * @Description: TODO
 * @author xs
 * @date 2016-2-10 晚上
 */
public class MyMessageReceiver extends BroadcastReceiver {

	// 事件监听
	public static ArrayList<EventListener> events = new ArrayList<EventListener>();
	
	public static final int NOTIFY_ID = 0x000;
	public static int newMsgNum = 0;//
	BmobUserManager userManager;
	BmobChatUser currentUser;

	//如果你想发送自定义格式的消息，请使用sendJsonMessage方法来发送Json格式的字符串，然后你按照格式自己解析并处理
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String json = intent.getStringExtra("msg");
		BmobLog.i("收到的message = " + json);
		
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

	/** 解析Json字符串
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
			if(tag.equals(BmobConfig.TAG_OFFLINE)){//下线通知
				if(currentUser!=null){
					if (events.size() > 0) {// 有监听的时候，传递下去
						for (EventListener eventListener : events)
							eventListener.onOffline();
					}else{
						//清空数据
						ChatApplication.getInstance().logout();
					}
				}
			}else{
				String fromId = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TARGETID);  //消息来源用户objectid--目的是解决多账户登陆同一设备时，无法接收到非当前登陆用户的消息。
				final String toId = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TOID);  //该消息接收方
				String msgTime = BmobJsonUtil.getString(josnObject,BmobConstant.PUSH_READED_MSGTIME);  //请求发送时间
				if(fromId!=null && !BmobDB.create(context,toId).isBlackUser(fromId)){//该消息发送方不为黑名单用户
					if(TextUtils.isEmpty(tag)){//不携带tag标签--此可接收陌生人的消息
						BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {
							
							@Override
							public void onSuccess(BmobMsg msg) {
								// TODO Auto-generated method stub
								if (events.size() > 0) {// 有监听的时候，传递下去
									for (int i = 0; i < events.size(); i++) {
										((EventListener) events.get(i)).onMessage(msg);
									}
								} else {
									//没有监听的时候推送
									boolean isAllowNtify = (Boolean) SPUtils.get(context, Config.IS_NOTIFY, true);
									if(isAllowNtify && currentUser!=null && currentUser.getObjectId().equals(toId)){//当前登陆用户存在并且也等于接收方id
										newMsgNum++;
										showMsgNotify(context,msg);
									}
								}
							}
							
							@Override
							public void onFailure(int code, String arg1) {
								// TODO Auto-generated method stub
								BmobLog.i("获取接收的消息失败："+arg1);
							}
						});
						
					}else{//带tag标签
						if(tag.equals(BmobConfig.TAG_ADD_CONTACT)){  //添加好友
							//保存好友请求道本地，并更新后台的未读字段
							BmobInvitation message = BmobChatManager.getInstance(context).saveReceiveInvite(json, toId);
							if(currentUser!=null){//有登陆用户
								if(toId.equals(currentUser.getObjectId())){
									if (events.size() > 0) {// 有监听的时候，传递下去
										for (EventListener eventListener : events)
											eventListener.onAddUser(message);
									}else{
										//推送新好友请求,指定点击后跳转到NewFriendActivity
										showOtherNotify(context, message.getFromname(), toId,  message.getFromname()+"请求添加好友", NewFriendActivity.class);
									}
								}
							}
						}else if(tag.equals(BmobConfig.TAG_ADD_AGREE)){
							String username = BmobJsonUtil.getString(josnObject, BmobConstant.PUSH_KEY_TARGETUSERNAME);
							//收到对方的同意请求之后，就得添加对方为好友--已默认添加同意方为好友，并保存到本地好友数据库
							BmobUserManager.getInstance(context).addContactAfterAgree(username, new FindListener<BmobChatUser>() {
								
								@Override
								public void onError(int arg0, final String arg1) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onSuccess(List<BmobChatUser> arg0) {
									// TODO Auto-generated method stub
									//保存到内存中
									ChatApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(context).getContactList()));
								}
							});
							//显示通知,点击后跳转到MainActivity
							showOtherNotify(context, username, toId,  username+"同意添加您为好友", MainActivity.class);
							//创建一个临时验证会话--用于在会话界面形成初始会话
							BmobMsg.createAndSaveRecentAfterAgree(context, json);
							
						}else if(tag.equals(BmobConfig.TAG_READED)){//已读回执
							String conversionId = BmobJsonUtil.getString(josnObject,BmobConstant.PUSH_READED_CONVERSIONID);
							if(currentUser!=null){
								//更改某条消息的状态
								BmobChatManager.getInstance(context).updateMsgStatus(conversionId, msgTime);
								if(toId.equals(currentUser.getObjectId())){
									if (events.size() > 0) {// 有监听的时候，传递下去--便于修改界面
										for (EventListener eventListener : events)
											eventListener.onReaded(conversionId, msgTime);
									}
								}
							}
						}
					}
				}else{//在黑名单期间所有的消息都应该置为已读，不然等取消黑名单之后又可以查询的到
					BmobChatManager.getInstance(context).updateMsgReaded(true, fromId, msgTime);
					BmobLog.i("该消息发送方为黑名单用户");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//这里截取到的有可能是web后台推送给客户端的消息，也有可能是开发者自定义发送的消息，需要开发者自行解析和处理
			BmobLog.i("parseMessage错误："+e.getMessage());
		}
	}
	
	/** 
	 *  显示聊天消息的通知
	  * @Title: showNotify
	  * @return void
	  * @throws
	  */
	public void showMsgNotify(Context context,BmobMsg msg) {
		// 更新通知栏
		int icon = R.drawable.app_icon;
		String trueMsg = "";
		if(msg.getMsgType()==BmobConfig.TYPE_TEXT && msg.getContent().contains("\\ue")){
			trueMsg = "[表情]";
		}else if(msg.getMsgType()==BmobConfig.TYPE_IMAGE){
			trueMsg = "[图片]";
		}else if(msg.getMsgType()==BmobConfig.TYPE_VOICE){
			trueMsg = "[语音]";
		}else if(msg.getMsgType()==BmobConfig.TYPE_LOCATION){
			trueMsg = "[位置]";
		}else{
			trueMsg = msg.getContent();
		}
		CharSequence tickerText = msg.getBelongUsername() + ":" + trueMsg;
		String contentTitle = msg.getBelongUsername()+ " (" + newMsgNum + "条新消息)";
		
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		boolean isAllowVoice = (Boolean) SPUtils.get(context, Config.IS_VOICE, true);
		boolean isAllowVibrate = (Boolean) SPUtils.get(context, Config.IS_VIBRATE, true);
		
		BmobNotifyManager.getInstance(context).showNotifyWithExtras(isAllowVoice,isAllowVibrate,icon, tickerText.toString(), contentTitle, tickerText.toString(),intent);
	}
	
	
	/** 显示其他Tag的通知
	  * showOtherNotify
	  */
	public void showOtherNotify(Context context,String username,String toId,String ticker,Class<?> cls){
		boolean isAllowNitify = (Boolean) SPUtils.get(context, Config.IS_NOTIFY, true);
		boolean isAllowVoice = (Boolean) SPUtils.get(context, Config.IS_VOICE, true);
		boolean isAllowVibrate = (Boolean) SPUtils.get(context, Config.IS_VIBRATE, true);
		if(isAllowNitify && currentUser!=null && currentUser.getObjectId().equals(toId)){
			//同时提醒通知
			BmobNotifyManager.getInstance(context).showNotify(isAllowVoice,isAllowVibrate,R.drawable.app_icon, ticker,username, ticker.toString(),NewFriendActivity.class);
		}
	}
	
}
