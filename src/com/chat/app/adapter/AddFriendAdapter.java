package com.chat.app.adapter;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.PushListener;
import com.chat.app.activity.R;
import com.chat.app.base.MyBaseAdapter;
import com.chat.app.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AddFriendAdapter extends MyBaseAdapter<BmobChatUser> {

	public AddFriendAdapter(List<BmobChatUser> datas, Context context) {
		super(datas, context);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("null")
	@Override
	public View getConvertView(int position, View convertView,
			ViewGroup viewGroup) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_item_addfriend, viewGroup, false);
		}
		final BmobChatUser user = datas.get(position);
		ImageView avatar = ViewHolder.get(convertView, R.id.avatar);
		TextView name = ViewHolder.get(convertView, R.id.name);
		Button btn_add = ViewHolder.get(convertView, R.id.btn_add);
		String img_url = user.getAvatar();
		if (img_url != null && !img_url.equals("")) {
			ImageLoader.getInstance().displayImage(img_url, avatar, BitmapUtils.getOptions());
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}
		name.setText(user.getUsername());
		btn_add.setText("添加");
		btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				final ProgressDialog progress = new ProgressDialog(context);
				progress.setMessage("正在添加...");
				progress.setCanceledOnTouchOutside(false);
				progress.show();
				//发送tag请求
				BmobChatManager.getInstance(context).sendTagMessage(BmobConfig.TAG_ADD_CONTACT, user.getObjectId(),new PushListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						progress.dismiss();
						ShowToast("发送请求成功，等待对方验证!");
					}
					
					@Override
					public void onFailure(int arg0, final String arg1) {
						// TODO Auto-generated method stub
						progress.dismiss();
						ShowToast("发送请求失败，请重新添加!");
						
					}
				});
				
			}
		});
		return convertView;
	}

}
