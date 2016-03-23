package com.chat.app.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.chat.app.activity.R;
import com.chat.app.base.MyBaseAdapter;
import com.chat.app.entity.User;
import com.chat.app.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserAdapter extends MyBaseAdapter<User> {

	public UserAdapter(List<User> datas, Context context) {
		super(datas, context);
		
	}

	@Override
	public View getConvertView(int position, View convertView,
			ViewGroup viewGroup) {
		User user = datas.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_item_contact, viewGroup, false);
		}
		TextView name = ViewHolder.get(convertView, R.id.tv_friend_name);
		ImageView avatar = ViewHolder.get(convertView, R.id.img_friend_avatar);
		String img_url = user.getAvatar();
		if(img_url != null && !img_url.equals("")){
			ImageLoader.getInstance().displayImage(img_url, avatar, BitmapUtils.getOptions());
		}else{
			avatar.setImageResource(R.drawable.avatar);
		}
		
		name.setText(user.getUsername());
		return convertView;
	}

}
