package com.chat.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

import com.chat.app.adapter.AddFriendAdapter;
import com.chat.app.base.BaseActivity;
import com.chat.app.utils.CollectionUtils;

public class AddFriendActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	
	private EditText et_find_name;
	private Button btn_search;
	private ListView lv_users;
	private List<BmobChatUser> users = new ArrayList<BmobChatUser>();
	private AddFriendAdapter addAdapter;
	private String search_name = null;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
  
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_add_friend;
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		et_find_name = (EditText) findViewById(R.id.et_find_name);
		btn_search = (Button) findViewById(R.id.btn_search);
		lv_users = (ListView) findViewById(R.id.lv_users);
		setTitleText("查找好友");
		setBtnVisibility(View.VISIBLE, View.GONE);
	}
	 @Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}
	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btn_search.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_search:
			users.clear();
			search_name = et_find_name.getText().toString();
			if(search_name!=null && !search_name.equals("")){
				progress = new ProgressDialog(AddFriendActivity.this);
				progress.setMessage("正在搜索...");
				progress.setCanceledOnTouchOutside(true);
				progress.show();
				userManager.queryUserByPage(false, 0, search_name, new FindListener<BmobChatUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						progress.dismiss();
						if(users!=null){
							users.clear();
						}
						showShort("用户不存在");
					}

					@Override
					public void onSuccess(List<BmobChatUser> query_users) {
						progress.dismiss();
						if(CollectionUtils.isNotNull(query_users)){
							showShort("搜索完成");
							users = query_users;
							showLong(users.get(0).getUsername());
							addAdapter = new AddFriendAdapter(query_users, mContext);
							lv_users.setAdapter(addAdapter);
							addAdapter.notifyDataSetChanged();
						}else{
							users.clear();
							showShort("用户不存在");
						}
						
						
					}
				});
			}else{
				showShort("请输入用户名");
			}
			break;

		
		}
	}
	@Override
	protected void onLeftBtnClick() {
         navigateToTask(MainActivity.class, true);
        
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		BmobChatUser user = (BmobChatUser) addAdapter.getItem(position-1);
		Intent intent =new Intent(this,DetailActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startActivity(intent);		
	}
	

}
