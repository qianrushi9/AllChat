package com.chat.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import com.chat.app.base.BaseActivity;
import com.chat.app.entity.User;
import com.chat.app.utils.CommonUtils;
import com.chat.app.utils.LogUtil;

public class RegisterActivity extends BaseActivity {
	private EditText et_username;
	private EditText et_password;

	private Button bt_register;
    private Dialog dialog;
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_register;
	}

	@Override
	public void initViews() {
		super.initViews();

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		bt_register = (Button) findViewById(R.id.bt_register);
		setTitleText("注册");
		setBtnVisibility(View.VISIBLE, View.GONE);

	}

	@Override
	protected void onLeftBtnClick() {
		navigateToTask(LoginActivity.class, true);
	}

	@Override
	public void setBtnVisibility(int leftVisibility, int rightVisibility) {
		// TODO Auto-generated method stub
		super.setBtnVisibility(leftVisibility, rightVisibility);
	}

	@Override
	public void setListener() {
		bt_register.setOnClickListener(this);
		mLeftBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_register:
			String username = et_username.getText().toString();
			String password = et_password.getText().toString();
			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
				showShort("用户名或者密码不能为空!");
				LogUtil.d("test", "为空");
				return;
			}
			boolean isNetAvailable = CommonUtils.isNetWorkAvailable(mContext);
			if (!isNetAvailable) {
				showLong("网络不可用,请先检查网络");
				return;
			}
			final ProgressDialog dialog = new ProgressDialog(
					RegisterActivity.this);
			dialog.setMessage("注册中...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			//dialog = createLoadingDialog(mContext, "正在注册...");
			final User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setSex(true);
			user.setDeviceType("android");
			user.setInstallId(BmobInstallation.getInstallationId(mContext));
			user.signUp(RegisterActivity.this, new SaveListener() {

				@Override
				public void onSuccess() {
					LogUtil.d("test", "注册成功");
					// 注册成功
					dialog.dismiss();
					showShort("注册成功");
					// 将设备与username进行绑定
					userManager.bindInstallationForRegister(user.getUsername());
					navigateToTask(LoginActivity.class, true);
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// 注册失败
					dialog.dismiss();
					showShort("注册失败");
				}
			});
			break;

		}
	}
}
