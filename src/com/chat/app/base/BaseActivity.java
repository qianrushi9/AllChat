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
		// �Զ���½״̬�¼���Ƿ��������豸��½
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
			showShort("�����˺����������豸�ϵ�¼!");
			navigateToTask(LoginActivity.class, true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ����״̬�µļ��
		//checkLogin();
	}

	// �õ������ļ�
	// public abstract int getLayoutId();

	public abstract int getLayoutId();

	// ���ü�����
	public void setListener() {
		// TODO Auto-generated method stub

	}

	// ��ʼ��View
	public void initViews() {
		mTitle = (TextView) findViewById(R.id.tv_title);
		mLeftBtn = (RelativeLayout) findViewById(R.id.rl_title_back);
		mRightBtn = (Button) findViewById(R.id.btn_setting_right);
		iv_left_btn = (ImageView) findViewById(R.id.iv_left_btn);

	}

	// ��ʼ������
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
	 * ��button�����
	 */
	protected void onLeftBtnClick() {
	}

	/**
	 * ��button�����
	 */
	protected void onRightBtnClick() {
	}

	/**
	 * ��������button�Ŀɼ���
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
	 * ���ñ�������
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
	 * Activity��ת
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
	 * Activity��ת ������
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
	 * @Description: ��ʱ����Ϣ��ʾ
	 * @param @param msg ��Ϣ����
	 * @return void ��������
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
	 * @Description: ��ʱ����Ϣ��ʾ
	 * @param @param msg ��Ϣ����
	 * @return void ��������
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
	 * @Description: �������
	 * @param @param mEditText
	 * @return void ��������
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
	 * @Description: �ر������
	 * @param @param mEditText
	 * @return void ��������
	 * @author DAO
	 */
	public void closeKeybord(EditText mEditText) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	/**
	 * ���ڵ�½�����Զ���½����µ��û����ϼ��������ϵļ�����
	 * 
	 * @Title: updateUserInfos
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void updateUserInfos() {

		// ��ѯ���û��ĺ����б�(��������б���ȥ���������û���Ŷ),Ŀǰ֧�ֵĲ�ѯ���Ѹ���Ϊ100�������޸����ڵ����������ǰ����BmobConfig.LIMIT_CONTACTS���ɡ�
		// ����Ĭ�ϲ�ȡ���ǵ�½�ɹ�֮�󼴽������б�洢�����ݿ��У������µ���ǰ�ڴ���,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				if (arg0 == BmobConfig.CODE_COMMON_NONE) {

				} else {
					LogUtil.i("update", "��ѯ�����б�ʧ�ܣ�" + arg1);
				}
			}

			@Override
			public void onSuccess(List<BmobChatUser> users) {
				// ���浽application�з���Ƚ�
				ChatApplication.getInstance().setContactList(
						CollectionUtils.list2map(users));
			}
		});
	}

	/**
	 * �õ��Զ����progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog_layout, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		// main.xml�е�ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// ��ʾ����
		// ���ض���
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// ʹ��ImageView��ʾ����
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// ���ü�����Ϣ

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// ���ò���
		return loadingDialog;

	}

	public void registBroadCast(BroadcastReceiver bcr, String action,
			int priority) {
		// ע�������Ϣ�㲥

		IntentFilter filter = new IntentFilter(action);
		// ���ȼ�Ҫ����ChatActivity
		filter.setPriority(priority);
		registerReceiver(bcr, filter);

	}

}
