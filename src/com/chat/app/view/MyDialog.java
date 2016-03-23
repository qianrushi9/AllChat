package com.chat.app.view;


import com.chat.app.activity.R;
import com.chat.app.utils.LogUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog {

	private Context context;
	private int content;
	private String contentStr;
	private int confirmButtonText;
	private int cacelButtonText;
	private ClickListenerInterface clickListenerInterface;
	private Button tvConfirm;
	private Button tvCancel;
	private int leftView = View.VISIBLE;
	private int rightView = View.VISIBLE;
	private TextView tvContent;
	private final String TAG = "ConfirmDialog";

	public interface ClickListenerInterface {

		public void doConfirm();

		public void doCancel();
	}
	public MyDialog(Context context , int content, int confirmButtonText, int cacelButtonText) {
		super(context,R.style.tip_dialog);
		this.context = context;
		this.content =content;
		this.confirmButtonText = confirmButtonText;
		this.cacelButtonText = cacelButtonText;
	}

	public MyDialog(Context context , String content, int confirmButtonText, int cacelButtonText) {
		super(context,R.style.tip_dialog);
		this.context = context;
		this.contentStr =content;
		this.confirmButtonText = confirmButtonText;
		this.cacelButtonText = cacelButtonText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void setBtnVisibility(int left,int right){
		this.leftView = left;
		this.rightView = right;
	}

	public void setContent(String msg) {
		this.contentStr = msg;
	}
	
	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.common_dlg, null);
		setContentView(view);

		tvContent = (TextView) view.findViewById(R.id.tv_dlg_content);
		tvConfirm = (Button) view.findViewById(R.id.btn_dlg_confirm);
		tvCancel = (Button) view.findViewById(R.id.btn_dlg_cancel);
	
		tvConfirm.setVisibility(rightView);
		tvCancel.setVisibility(leftView);
		LogUtil.d(TAG, (leftView == View.GONE)+"");
		if(leftView == View.GONE||rightView == View.GONE) {
			LogUtil.d(TAG, "=-=====---");
			tvConfirm.setBackgroundResource(R.drawable.btn_dlg_selector);
			tvCancel.setBackgroundResource(R.drawable.btn_dlg_selector);
		}
		if(TextUtils.isEmpty(contentStr)){
			tvContent.setText(content);
		}else{
			tvContent.setText(contentStr);
		}
		tvConfirm.setText(confirmButtonText);
		tvCancel.setText(cacelButtonText);

		tvConfirm.setOnClickListener(new DlgclickListener());
		tvCancel.setOnClickListener(new DlgclickListener());

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽高
		lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
		dialogWindow.setAttributes(lp);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class DlgclickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_dlg_confirm:
				clickListenerInterface.doConfirm();
				break;
			case R.id.btn_dlg_cancel:
				clickListenerInterface.doCancel();
				break;
			}
		}

	};

}