package com.chat.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.chat.app.base.BaseActivity;
import com.chat.app.config.Config;
import com.chat.app.entity.User;
import com.chat.app.utils.CommonUtils;
import com.chat.app.utils.SPUtils;

public class LoginActivity extends BaseActivity {
	private EditText et_username;
	private EditText et_password;
	private TextView tv_register;
	private TextView tv_forgetpassword;
	private Button bt_login;
	private CheckBox cb_remember_count, cb_auto_login;

	private static final int AUTO_LOGIN = 1003;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case AUTO_LOGIN:
				navigateToTask(MainActivity.class, true);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean isRemember = (Boolean) SPUtils.get(mContext,
				Config.IS_REMEMBER, false);
		if (isRemember) {
			String lastUser = (String) SPUtils.get(mContext, Config.USER_NAME,
					"user");

			cb_remember_count.setChecked(true);
			et_username.setText(lastUser);
			String lastPassword = (String) SPUtils.get(mContext,
					Config.PASSWORD, "pass");
			et_password.setText(lastPassword);

		}
		boolean isAutoLogin = (Boolean) SPUtils.get(mContext,
				Config.IS_AUTOLOGIN, false);
		if (isAutoLogin) {
			ProgressDialog progress = new ProgressDialog(mContext);
			progress.setMessage("自动登录");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			cb_auto_login.setChecked(true);
			handler.sendEmptyMessageDelayed(AUTO_LOGIN, 1200);
		}
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public void initViews() {
		super.initViews();
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_forgetpassword = (TextView) findViewById(R.id.tv_forget_password);
		bt_login = (Button) findViewById(R.id.bt_login);
		cb_auto_login = (CheckBox) findViewById(R.id.cb_auto_login);
		cb_remember_count = (CheckBox) findViewById(R.id.cb_remember_account);
		setTitleText("登录");
		setBtnVisibility(View.GONE, View.GONE);
		cb_auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (cb_auto_login.isChecked()) {
					SPUtils.put(mContext, Config.IS_AUTOLOGIN, true);
				} else {
					SPUtils.put(mContext, Config.IS_AUTOLOGIN, false);
				}

			}
		});
		cb_remember_count
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (cb_remember_count.isChecked()) {
							// 保存登录信息
							SPUtils.put(mContext, Config.IS_REMEMBER, true);

						} else {
							SPUtils.put(mContext, Config.IS_REMEMBER, false);
						}

					}
				});
	}

	@Override
	public void setBtnVisibility(int leftVisibility, int rightVisibility) {
		// TODO Auto-generated method stub
		super.setBtnVisibility(leftVisibility, rightVisibility);
	}

	@Override
	public void setTitleText(String title) {
		// TODO Auto-generated method stub
		super.setTitleText(title);
	}

	@Override
	public void setListener() {
		tv_register.setOnClickListener(this);
		tv_forgetpassword.setOnClickListener(this);
		bt_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 注册
		case R.id.tv_register:
			navigateToTask(RegisterActivity.class, false);
			break;
		case R.id.bt_login:
			final String username = et_username.getText().toString();
			final String password = et_password.getText().toString();
			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
				showShort("用户名或者密码不能为空!");
				return;
			}
			boolean isNetAvailable = CommonUtils.isNetWorkAvailable(mContext);
			if (!isNetAvailable) {
				showLong("网络不可用,请先检查网络");
				return;
			}

			final ProgressDialog dialog = new ProgressDialog(mContext);
			dialog.setMessage("登录中...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			userManager.login(user, new SaveListener() {

				@Override
				public void onSuccess() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							dialog.setMessage("正在获取好友列表...");

						}
					});
					if (cb_remember_count.isChecked()) {
						SPUtils.put(mContext, Config.USER_NAME, username);
						SPUtils.put(mContext, Config.PASSWORD, password);
					}
					updateUserInfos();
					dialog.dismiss();
					navigateToTask(MainActivity.class, true);

				}

				@Override
				public void onFailure(int arg0, String errMsg) {
					dialog.dismiss();
					showLong(errMsg);
				}
			});
			break;
		case R.id.tv_forget_password:

			break;

		}
	}

}
