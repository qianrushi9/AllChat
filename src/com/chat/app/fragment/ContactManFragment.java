package com.chat.app.fragment;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import com.chat.app.activity.NewFriendActivity;
import com.chat.app.activity.R;
import com.chat.app.adapter.UserAdapter;
import com.chat.app.application.ChatApplication;
import com.chat.app.base.BaseFragment;
import com.chat.app.entity.User;
import com.chat.app.utils.CollectionUtils;
import com.chat.app.utils.CommonUtils;

public class ContactManFragment extends BaseFragment implements OnItemClickListener{
	private LinearLayout ll_new_friend;
	private ListView lv_contact_man;
	private List<User> friends = new ArrayList<User>();
	private UserAdapter userAdapter;
	private TextView tv_tips;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

    @Override
    public View onCreateView(LayoutInflater inflater,
    		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_contactman;
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		ll_new_friend = (LinearLayout) rootView.findViewById(R.id.ll_new_friend);
		lv_contact_man = (ListView) rootView.findViewById(R.id.lv_contact_man);
		tv_tips = (TextView) rootView.findViewById(R.id.tv_tip);
	    tv_tips.setVisibility(View.GONE);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		filledData(CollectionUtils.map2list(ChatApplication.getInstance().getContactList()));
		userAdapter = new UserAdapter(friends, context);
		lv_contact_man.setAdapter(userAdapter);
		userAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		ll_new_friend.setOnClickListener(this);
		lv_contact_man.setOnItemClickListener(this);
		//lv_contact_man.setOnItemLongClickListener(this);
	}
	
	public void refresh(){
		
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					queryMyfriends();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private void filledData(List<BmobChatUser> datas) {
		friends.clear();
		int total = datas.size();
		for (int i = 0; i < total; i++) {
			BmobChatUser user = datas.get(i);
			User sortModel = new User();
			sortModel.setAvatar(user.getAvatar());
			sortModel.setNick(user.getNick());
			sortModel.setUsername(user.getUsername());
			sortModel.setObjectId(user.getObjectId());
			sortModel.setContacts(user.getContacts());
			friends.add(sortModel);
		}
		
	}
	
	/** ��ȡ�����б�
	  * queryMyfriends
	  * @return void
	  * @throws
	  */
	private void queryMyfriends() {
		//�Ƿ����µĺ�������
		if(BmobDB.create(context).hasNewInvite()){
			tv_tips.setVisibility(View.VISIBLE);
			CommonUtils.startHintVoice();
		}else{
			tv_tips.setVisibility(View.GONE);
		}
		//����������һ�α��صĺ������ݿ�ļ�飬��Ϊ�˱��غ������ݿ����Ѿ�����˶Է������ǽ���ȴû����ʾ����������
		// �����������ڴ��б���ĺ����б�
		ChatApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(getActivity()).getContactList()));
	
		Map<String,BmobChatUser> users = ChatApplication.getInstance().getContactList();
		//��װ�µ�User
		filledData(CollectionUtils.map2list(users));
		if(userAdapter==null){
			userAdapter = new UserAdapter(friends,context);
			lv_contact_man.setAdapter(userAdapter);
		}else{
			userAdapter.notifyDataSetChanged();
		}

	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.ll_new_friend:
			Intent intent = new Intent(context, NewFriendActivity.class);
			intent.putExtra("from", "contact");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
