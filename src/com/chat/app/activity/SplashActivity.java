package com.chat.app.activity;

import com.chat.app.base.BaseActivity;
import com.chat.app.config.Config;
import com.chat.app.utils.SPUtils;

import cn.bmob.im.BmobChat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {

	private boolean isFirstIn = false;

	private static final int TIME = 2000;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int GO_LOGIN = 1002;

	private Animation scaleAnimation = null;
	//private Animation translateAnimation = null;
	private TextView tv_welcome;
	private LinearLayout ll_logo;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GO_HOME:
				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));
				finish();
				break;
			case GO_GUIDE:
				startActivity(new Intent(SplashActivity.this,
						GuideActivity.class));
				finish();
				break;
			case GO_LOGIN:
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
				finish();

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BmobChat.DEBUG_MODE = true;
		BmobChat.getInstance(this).init(Config.applicationId);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void init() {
		tv_welcome = (TextView) findViewById(R.id.tv_welcome);
		ll_logo = (LinearLayout) findViewById(R.id.ll_logo);
		scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
//		translateAnimation = AnimationUtils.loadAnimation(this,
//				R.anim.anim_translate);
		tv_welcome.startAnimation(scaleAnimation);
		scaleAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				//ll_logo.startAnimation(translateAnimation);
				//translateAnimation.setFillAfter(true);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
                
				// 确定是不是第一次进入APP
//				SharedPreferences sp = getSharedPreferences("daoge",
//						MODE_PRIVATE);
//				isFirstIn = sp.getBoolean("isFirstIn", true);
				isFirstIn = (Boolean) SPUtils.get(mContext, "isFirstIn", true);
				// 如果是第一次进入
				if (isFirstIn) {
					mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
//					SharedPreferences.Editor editor = sp.edit();
//					editor.putBoolean("isFirstIn", false);
//					editor.commit();
					SPUtils.put(mContext, "isFirstIn", false);
				} else {
//还未添加注销功能
//					if (userManager.getCurrentUser() != null) {
//						updateUserInfos();
//						mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
//					} else {
//						mHandler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
//					}
					updateUserInfos();
					mHandler.sendEmptyMessageDelayed(GO_LOGIN, TIME);

				}

			}
		});

	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_splash;
	}
}
