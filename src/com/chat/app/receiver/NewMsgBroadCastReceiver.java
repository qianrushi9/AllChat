package com.chat.app.receiver;


import com.chat.app.config.Config;
import com.chat.app.utils.CommonUtils;
import com.chat.app.utils.SPUtils;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobMsg;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 新消息广播接收者
 * @author xs
 *
 */
public class NewMsgBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		refreshNewMsg(context,null);
		//终结广播
		abortBroadcast();

	}

	public void refreshNewMsg(Context context,BmobMsg msg) {
		//提示声音
		boolean isNotify = (Boolean) SPUtils.get(context, Config.IS_NOTIFY, true);
		if(isNotify){
			CommonUtils.startHintVoice();
		}
		if(msg != null){
			BmobChatManager.getInstance(context).saveReceiveMessage(true, msg);
		}
		
	}

}
