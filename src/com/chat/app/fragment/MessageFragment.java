package com.chat.app.fragment;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;
import com.chat.app.activity.ChatActivity;
import com.chat.app.activity.R;
import com.chat.app.adapter.MessageAdapter;
import com.chat.app.base.BaseFragment;
import com.chat.app.view.MyDialog;
import com.chat.app.view.MyDialog.ClickListenerInterface;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MessageFragment extends BaseFragment implements OnItemClickListener,OnItemLongClickListener,OnRefreshListener2<ListView>{
	
    private PullToRefreshListView mPultoListView;
    private ListView actualListView;
    private List<BmobRecent> recents = new ArrayList<BmobRecent>();
    private MessageAdapter msgAdapter;
    private MyDialog dialog = null;
    private int delete_item;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater,
    		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_message;
	}
    @Override
    public void initViews() {
    	// TODO Auto-generated method stub
    	super.initViews();
    	mPultoListView = (PullToRefreshListView) rootView.findViewById(R.id.message_pull_refresh_list);
    	actualListView = mPultoListView.getRefreshableView();
    	mPultoListView.setMode(Mode.BOTH);
    	
    }
    
    @Override
    public void initData() {
    	// TODO Auto-generated method stub
    	super.initData();
    	recents = BmobDB.create(context).queryRecents();
    	msgAdapter = new MessageAdapter(recents, context);
    	actualListView.setAdapter(msgAdapter);
    }
    
    @Override
    public void setListener() {
    	actualListView.setOnItemClickListener(this);
    	actualListView.setOnItemLongClickListener(this);
    	mPultoListView.setOnRefreshListener(this);
    }
    
    private boolean hidden;
    
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if(!hidden){
			refresh();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!hidden){
			refresh();
		}
	}
	
    
    public void refresh(){
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() { 
			    	msgAdapter = new MessageAdapter(BmobDB.create(context).queryRecents(), context);
			    	actualListView.setAdapter(msgAdapter);
					msgAdapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
  
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		dialog = onCreateDlg("确定要删除当前会话吗",R.string.common_confirm, R.string.common_cancel);
		delete_item = position;
		dialog.setClicklistener(new ClickListenerInterface() {
			
			@Override
			public void doConfirm() {
				
				BmobRecent recent =  (BmobRecent) msgAdapter.getItem(delete_item);
				recents.remove(delete_item);
				msgAdapter.notifyDataSetChanged();
				BmobDB.create(context).deleteRecent(recent.getTargetid());
				BmobDB.create(context).deleteMessages(recent.getTargetid());
			}
			
			@Override
			public void doCancel() {
				dialog.dismiss();
				
			}
		});
		dialog.show();
		return true;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		BmobRecent recent = (BmobRecent) msgAdapter.getItem(position);
		//重置未读消息
		BmobDB.create(getActivity()).resetUnread(recent.getTargetid());
		//组装聊天对象
		BmobChatUser user = new BmobChatUser();
		user.setAvatar(recent.getAvatar());
		user.setNick(recent.getNick());
		user.setUsername(recent.getUserName());
		user.setObjectId(recent.getTargetid());
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refresh();   
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}
}
