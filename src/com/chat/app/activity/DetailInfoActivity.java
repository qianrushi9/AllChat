package com.chat.app.activity;

import java.util.List;

import com.chat.app.base.BaseActivity;
import com.chat.app.entity.User;
import com.chat.app.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.listener.FindListener;

public class DetailInfoActivity extends BaseActivity{
    private Button btn_sendmsg;
    private TextView tv_username,tv_nick,tv_sex;
    private ImageView iv_avatar;
    String from = "";
    String username = "";
    User user;
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_set_info;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 from = getIntent().getStringExtra("from");
		 username = getIntent().getStringExtra("username");
		 super.onCreate(savedInstanceState);
		
	}
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		setTitleText("ÏêÏ¸×ÊÁÏ");
		btn_sendmsg = (Button) findViewById(R.id.btn_send);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_nick = (TextView) findViewById(R.id.tv_nick);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		initData();
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		getUserInfo(username);
	}
	private void getUserInfo(String name) {
		// TODO Auto-generated method stub
		userManager.queryUser(name, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if(arg0 != null && arg0.size()>0){
					updateUserInfo(user);
				}
			}

			
		});
	}
	
	private void updateUserInfo(User user) {
		// TODO Auto-generated method stub
		String avatar = user.getAvatar();
		if(avatar != null &&!avatar.equals("")){
			ImageLoader.getInstance().displayImage(avatar, iv_avatar, BitmapUtils.getOptions());
		}else{
			iv_avatar.setImageResource(R.drawable.avatar);
		}
		tv_nick.setText(user.getNick());
		tv_username.setText(user.getUsername());
		tv_sex.setText(user.getSex() == true ? "ÄÐ" : "Å®");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_send:
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();
			break;
			

		default:
			break;
		}
	}

}
