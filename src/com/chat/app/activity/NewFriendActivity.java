package com.chat.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.chat.app.adapter.NewFriendAdapter;
import com.chat.app.base.BaseActivity;
import com.chat.app.view.MyDialog;
import com.chat.app.view.MyDialog.ClickListenerInterface;

public class NewFriendActivity extends BaseActivity implements OnItemLongClickListener{
	private ListView lv_add_request;
	private List<BmobInvitation> invitations = new ArrayList<BmobInvitation>();
	private NewFriendAdapter newAdapter;
	private MyDialog dialog;
	private int delete_item;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleText("新朋友");
		setBtnVisibility(View.VISIBLE, View.GONE);
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		lv_add_request = (ListView) findViewById(R.id.lv_add_request);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		invitations = BmobDB.create(mContext).queryBmobInviteList();
		newAdapter = new NewFriendAdapter(invitations, mContext);
		lv_add_request.setAdapter(newAdapter);
		newAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		lv_add_request.setOnItemLongClickListener(this);
	}
	
	@Override
	public void setTitleText(String title) {
		// TODO Auto-generated method stub
		super.setTitleText(title);
	}
	
	@Override
	public void setBtnVisibility(int leftVisibility, int rightVisibility) {
		// TODO Auto-generated method stub
		super.setBtnVisibility(leftVisibility, rightVisibility);
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activivty_newfriend;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		   dialog = onCreateDlg("确定要删除该条请求吗", R.string.common_confirm, R.string.common_cancel);
		   delete_item = position;
           dialog.setClicklistener(new ClickListenerInterface() {
			
			@Override
			public void doConfirm() {
				
				BmobInvitation invitation = invitations.get(delete_item);
				invitations.remove(delete_item);
				newAdapter.notifyDataSetChanged();
				BmobDB.create(mContext).deleteInviteMsg(invitation.getFromid(), Long.toString(invitation.getTime()));
			}
			
			@Override
			public void doCancel() {
				dialog.dismiss();
				
			}
		});
		dialog.show();
		return false;
	}

}
