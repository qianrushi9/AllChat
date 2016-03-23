package com.chat.app.receiver;

import com.chat.app.activity.NewFriendActivity;
import com.chat.app.activity.R;
import com.chat.app.config.Config;
import com.chat.app.utils.CommonUtils;
import com.chat.app.utils.SPUtils;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * ��ǩ��Ϣ�㲥������
 * @author xs
 *
 */
public class TagBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		BmobInvitation msg = (BmobInvitation) intent.getSerializableExtra("invite");
		refreshInvite(context,msg);
		
		abortBroadcast();

	}
	
	public void refreshInvite(Context context,BmobInvitation msg){
		//��ʾ����
				boolean isNotify = (Boolean) SPUtils.get(context, Config.IS_NOTIFY, true);
				if(isNotify){
					CommonUtils.startHintVoice();
				}
				//ͬʱ����֪ͨ
				String tickerText = msg.getFromname()+"������Ӻ���";
				boolean isAllowVibrate = (Boolean) SPUtils.get(context, Config.IS_VIBRATE, true);
				BmobNotifyManager.getInstance(context).showNotify(isNotify,isAllowVibrate,R.drawable.app_icon, tickerText, msg.getFromname(), tickerText.toString(),NewFriendActivity.class);
	}

}
