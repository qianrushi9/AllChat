package com.chat.app.base;

import com.chat.app.application.ChatApplication;
import com.chat.app.view.MyDialog;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements OnClickListener
{
	
	public View rootView;
	public Context context;
	public String TAG = "";
	public FragmentManager fgManager;
	public Toast mToast;
	public MyDialog dlg;
	public BmobUserManager userManager;
	public BmobChatManager manager;
	
	public LayoutInflater mInflater;
	public ChatApplication mAppInstance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		TAG = getClass().getSimpleName();
		setRetainInstance(true);
		mAppInstance = ChatApplication.getInstance();
		userManager = BmobUserManager.getInstance(context);
		manager = BmobChatManager.getInstance(context);
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(getLayoutId(), container, false);


		initViews();
		initData();
		setListener();
		return rootView;
	}
	
	protected abstract int getLayoutId();
	

	//初始化View
	public void initViews(){
		
	}
	
	//初始化数据
	public void initData(){
		
	}
	
	//设置监听器
	public void setListener(){
		
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
	}
	
	public void ShowToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}
	
	public MyDialog onCreateDlg(String content, int right, int left) {
		dlg = new MyDialog(context, content, right, left);
		dlg.setBtnVisibility(View.VISIBLE, View.VISIBLE);
		return dlg;
	}

	public void setContent(String msg) {
		dlg.setContent(msg);
	}
	
	
}
