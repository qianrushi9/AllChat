package com.chat.app.base;

import java.util.List;

import com.chat.app.activity.LoginActivity;
import com.chat.app.activity.R;
import com.chat.app.application.ChatApplication;
import com.chat.app.utils.CollectionUtils;
import com.chat.app.utils.LogUtil;
import com.chat.app.view.MyDialog;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity implements
		View.OnClickListener {

	public Context mContext;
	public String TAG = "";
	String Test = "";
	protected TextView mTitle;
	protected RelativeLayout mLeftBtn;
	protected ImageView iv_left_btn;
	protected Button mRightBtn;
	protected MyDialog dlg;
	protected BmobUserManager userManager;
	protected BmobChatManager chatManager;
	protected ChatApplication mAppInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutId());
		// 自动登陆状态下检测是否在其他设备登陆
		//checkLogin();
		TAG = getClass().getSimpleName();
		mContext = this;
		userManager = BmobUserManager.getInstance(mContext);
		chatManager = BmobChatManager.getInstance(mContext);
		mAppInstance = ChatApplication.getInstance();
		initViews();
		setListener();
		initData();
	}

	public void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			showShort("您的账号已在其他设备上登录!");
			navigateToTask(LoginActivity.class, true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 锁屏状态下的检测
		//checkLogin();
	}

	// 得到布局文件
	// public abstract int getLayoutId();

	public abstract int getLayoutId();

	// 设置监听器
	public void setListener() {
		// TODO Auto-generated method stub

	}

	// 初始化View
	public void initViews() {
		mTitle = (TextView) findViewById(R.id.tv_title);
		mLeftBtn = (RelativeLayout) findViewById(R.id.rl_title_back);
		mRightBtn = (Button) findViewById(R.id.btn_setting_right);
		iv_left_btn = (ImageView) findViewById(R.id.iv_left_btn);

	}

	// 初始化数据
	public void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_title_back:
			onLeftBtnClick();
			break;

		case R.id.btn_setting_right:
			onRightBtnClick();
			break;
		}
	}

	/**
	 * 左button被点击
	 */
	protected void onLeftBtnClick() {
	}

	/**
	 * 右button被点击
	 */
	protected void onRightBtnClick() {
	}

	/**
	 * 设置左右button的可见性
	 * 
	 * @param leftVisibility
	 * @param rightVisibility
	 */
	public void setBtnVisibility(final int leftVisibility,
			final int rightVisibility) {
		if (mLeftBtn != null) {
			mLeftBtn.setVisibility(leftVisibility);
		}
		if (mRightBtn != null) {
			mRightBtn.setVisibility(rightVisibility);
		}
	}

	/**
	 * 设置标题文字
	 * 
	 * @param titleId
	 */
	public void setTitleText(final int titleId) {
		if (mTitle != null) {
			mTitle.setText(titleId);
		}
	}

	public void setTitleText(final String title) {
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}

	/**
	 * Activity跳转
	 * 
	 * @param cls
	 * @param isFinish
	 */
	public void navigateToTask(Class<?> cls, boolean isFinish) {
		startActivity(new Intent(mContext, cls));
		if (isFinish) {
			finish();
		}
	}

	/**
	 * Activity跳转 带参数
	 * 
	 * @param cls
	 * @param bundle
	 * @param isFinish
	 */
	public void navigateToTask(Class<?> cls, Bundle bundle, boolean isFinish) {
		Intent intent = new Intent(mContext, cls);
		intent.putExtras(bundle);
		startActivity(intent);
		if (isFinish) {
			finish();
		}
	}

	/**
	 * @Title: showShort
	 * @Description: 短时间消息提示
	 * @param @param msg 消息内容
	 * @return void 返回类型
	 * @author
	 */
	public void showShort(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @Title: showShort
	 * @Description: 长时间消息提示
	 * @param @param msg 消息内容
	 * @return void 返回类型
	 * @author DAO
	 */
	public void showLong(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	public MyDialog onCreateDlg(String content, int right, int left) {
		dlg = new MyDialog(mContext, content, right, left);
		dlg.setBtnVisibility(View.VISIBLE, View.VISIBLE);

		return dlg;
	}

	public void setContent(String msg) {
		dlg.setContent(msg);
	}

	/**
	 * @Title: openKeybord
	 * @Description: 打开软键盘
	 * @param @param mEditText
	 * @return void 返回类型
	 * @author DAO
	 */
	public void openKeybord(EditText mEditText) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * @Title: openKeybord
	 * @Description: 关闭软键盘
	 * @param @param mEditText
	 * @return void 返回类型
	 * @author DAO
	 */
	public void closeKeybord(EditText mEditText) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	/**
	 * 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新
	 * 
	 * @Title: updateUserInfos
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void updateUserInfos() {

		// 查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
		// 这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				if (arg0 == BmobConfig.CODE_COMMON_NONE) {

				} else {
					LogUtil.i("update", "查询好友列表失败：" + arg1);
				}
			}

			@Override
			public void onSuccess(List<BmobChatUser> users) {
				// 保存到application中方便比较
				ChatApplication.getInstance().setContactList(
						CollectionUtils.list2map(users));
			}
		});
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog_layout, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	public void registBroadCast(BroadcastReceiver bcr, String action,
			int priority) {
		// 注册接收消息广播

		IntentFilter filter = new IntentFilter(action);
		// 优先级要低于ChatActivity
		filter.setPriority(priority);
		registerReceiver(bcr, filter);

	}

}
