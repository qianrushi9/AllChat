package com.chat.app.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;

import com.chat.app.activity.R;
import com.chat.app.base.MyBaseAdapter;
import com.chat.app.utils.BitmapUtils;
import com.chat.app.utils.FaceTextUtils;
import com.chat.app.utils.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageAdapter extends MyBaseAdapter<BmobRecent> {

	public MessageAdapter(List<BmobRecent> datas, Context context) {
		super(datas, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView,
			ViewGroup viewGroup) {
		BmobRecent item = datas.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_item_message, viewGroup, false);
		}
		
		ImageView avatar = ViewHolder.get(convertView, R.id.iv_msg_avatar);
		TextView name = ViewHolder.get(convertView, R.id.tv_msg_name);
		TextView time = ViewHolder.get(convertView, R.id.tv_msg_time);
		TextView msg = ViewHolder.get(convertView, R.id.tv_recent_msg);
		TextView unread = ViewHolder.get(convertView, R.id.tv_msg_unread);
		
		String avatar_url = item.getAvatar();
		if(avatar_url != null && !avatar_url.equals("")){
			ImageLoader.getInstance().displayImage(avatar_url, avatar, BitmapUtils.getOptions());
		}else{
			avatar.setImageResource(R.drawable.avatar);
		}
		
		name.setText(item.getNick());
		time.setText(TimeUtils.getChatTime(item.getTime()));
		switch (item.getType()) {
		case BmobConfig.TYPE_TEXT:
			SpannableString spString = FaceTextUtils.toSpannableString(context, item.getMessage());
			msg.setText(spString);
			break;
        case BmobConfig.TYPE_IMAGE:
			msg.setText("[Í¼Æ¬]");
			break;
        case BmobConfig.TYPE_VOICE:
        	msg.setText("[ÓïÒô]");
	        break;

		
		}
		
		int unread_num = BmobDB.create(context).getUnreadCount(item.getTargetid());
		if(unread_num > 0){
			unread.setVisibility(View.VISIBLE);
			unread.setText(unread_num+"");
		}else{
			unread.setVisibility(View.GONE);
		}
		return convertView;
	}

}
