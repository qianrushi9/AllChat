package com.chat.app.activity;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;
import com.chat.app.adapter.MyFragmentPagerAdapter;
import com.chat.app.application.MyMessageReceiver;
import com.chat.app.base.BaseActivity;
import com.chat.app.fragment.ContactManFragment;
import com.chat.app.fragment.MessageFragment;
import com.chat.app.fragment.PersonalCenterFragment;
import com.chat.app.receiver.NewMsgBroadCastReceiver;
import com.chat.app.receiver.TagBroadcastReceiver;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends BaseActivity implements
		OnCheckedChangeListener, OnPageChangeListener, EventListener {
    //测试Git提交celkdlfksldkflsdkfldsfsdfsdfsdfs
	//sdfsdgsdgsdg
	private ViewPager mViewPager;
	private RadioButton tab_message, tab_contact, tab_personalcenter; // 底部Tab
	private RadioGroup tabs;
	private SlidingMenu slidingMenu;
	public static final int PAGE_MESSAGE = 0; // 主页的第一个页面的index
	public static final int PAGE_CONTACT = 1;
	public static final int PAGE_PERSONALCENTER = 2;
	public static int mCurrentIndex; // 当前页码
	List<Fragment> fragments = null; // 定义存放Fragment的集合
	MessageFragment msgFragment = null;
	ContactManFragment contactFragment = null;
	PersonalCenterFragment personalFragment = null;
	MyFragmentPagerAdapter fgPagerAdapter = null;
	NewMsgBroadCastReceiver newBroadcast;
	TagBroadcastReceiver tagBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 开启定时检测服务（单位为秒）-在这里检测后台是否还有未读的消息，有的话就取出来
		BmobChat.getInstance(this).startPollService(20);
		newBroadcast = new NewMsgBroadCastReceiver();
		tagBroadcast = new TagBroadcastReceiver();
		registBroadCast(newBroadcast, BmobConfig.BROADCAST_NEW_MESSAGE, 5);
		registBroadCast(tagBroadcast, BmobConfig.BROADCAST_ADD_USER_MESSAGE, 5);
		System.out.println("test git");
	}
       //初始化UI控件
	@SuppressLint("NewApi")
	@Override
	public void initViews() {
		super.initViews();
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		tab_message = (RadioButton) findViewById(R.id.rb_message);
		tab_contact = (RadioButton) findViewById(R.id.rb_contact);
		tab_personalcenter = (RadioButton) findViewById(R.id.rb_personalcenter);
		iv_left_btn.setBackground(getResources().getDrawable(
				R.drawable.sliding_menu_icon));
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
		slidingMenu.setBehindWidth(800);// 设置SlidingMenu菜单的宽度
		slidingMenu.setMenu(R.layout.layout_menu);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		CanvasTransformer ctf = new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// TODO Auto-generated method stub
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		};
		slidingMenu.setBehindCanvasTransformer(ctf);
		tabs = (RadioGroup) findViewById(R.id.tab_bar);
		mViewPager.setCurrentItem(PAGE_MESSAGE);
		tab_message.setChecked(true);
	}

	@Override
	public void registBroadCast(BroadcastReceiver bcr, String action,
			int priority) {
		// TODO Auto-generated method stub
		super.registBroadCast(bcr, action, priority);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 监听推送消息
		MyMessageReceiver.events.add(this);
		// 新消息数目置零
		MyMessageReceiver.newMsgNum = 0;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 取消监听
		MyMessageReceiver.events.remove(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_title_back:
			slidingMenu.toggle(true);
			break;
		case R.id.btn_setting_right:
			switch (mCurrentIndex) {
			case PAGE_CONTACT:
				showShort("添加好友");
				navigateToTask(AddFriendActivity.class, false);
				break;

			case PAGE_PERSONALCENTER:
				break;
			}

		}

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			slidingMenu.toggle(true);
			break;
		// 连续按两次退出程序
		case KeyEvent.KEYCODE_BACK:

			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (System.currentTimeMillis() - exitTime < 2000) {
					finish();
					System.exit(0);
				} else {
					showShort("再按一次退出程序");
					exitTime = System.currentTimeMillis();
				}

			}
			break;
		}
		return false;
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	public void setListener() {
		// TitleBar监听器
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);

		// 其他
		tabs.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void initData() {
		fragments = new ArrayList<Fragment>();
		if (msgFragment == null) {
			msgFragment = new MessageFragment();
		}
		if (contactFragment == null) {
			contactFragment = new ContactManFragment();
		}
		if (personalFragment == null) {
			personalFragment = new PersonalCenterFragment();
		}
		fragments.add(msgFragment);
		fragments.add(contactFragment);
		fragments.add(personalFragment);
		fgPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(fgPagerAdapter);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_message:
			mCurrentIndex = PAGE_MESSAGE;
			mViewPager.setCurrentItem(PAGE_MESSAGE);
			mTitle.setText("消息");
			break;
		case R.id.rb_contact:
			mCurrentIndex = PAGE_CONTACT;
			mViewPager.setCurrentItem(PAGE_CONTACT);
			mTitle.setText("联系人");
//			if(BmobDB.create(mContext).hasNewInvite()){
//				showLong("有新的好友请求");
//			}else{
//				showLong("没有新的好友请求");
//			}
			if(mCurrentIndex == 1){
				//当前页面如果为会话页面，刷新此页面
				if(contactFragment != null){
					contactFragment.refresh();
				}
			}
			break;
		case R.id.rb_personalcenter:
			mCurrentIndex = PAGE_PERSONALCENTER;
			mViewPager.setCurrentItem(PAGE_PERSONALCENTER);
			mTitle.setText("个人中心");
			break;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int index) {
		switch (index) {
		case PAGE_MESSAGE:
			mCurrentIndex = PAGE_MESSAGE;
			tab_message.setChecked(true);
			mTitle.setText("消息");
			mRightBtn.setVisibility(View.GONE);
			break;

		case PAGE_CONTACT:
			mCurrentIndex = PAGE_CONTACT;
			tab_contact.setChecked(true);
			mRightBtn.setVisibility(View.VISIBLE);
			mRightBtn.setBackground(getResources().getDrawable(
					R.drawable.btn_add_friends));
			mTitle.setText("联系人");
			break;
		case PAGE_PERSONALCENTER:
			mCurrentIndex = PAGE_PERSONALCENTER;
			tab_personalcenter.setChecked(true);
			mTitle.setText("个人中心");
			mRightBtn.setVisibility(View.GONE);
			break;

		}

	}

	@Override
	public void onMessage(BmobMsg message) {
		newBroadcast.refreshNewMsg(mContext, message);
		if(mCurrentIndex == 0){
			//当前页面如果为会话页面，刷新此页面
			if(msgFragment != null){
				msgFragment.refresh();
			}
		}
	}

	@Override
	public void onReaded(String conversionId, String msgTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetChange(boolean isNetConnected) {

		if (!isNetConnected) {
			showShort("当前网络不可用,请检查网络!");
		}

	}

	@Override
	public void onAddUser(BmobInvitation message) {
		tagBroadcast.refreshInvite(mContext, message);
		if(mCurrentIndex == 1){
			//当前页面如果为会话页面，刷新此页面
			if(contactFragment != null){
				contactFragment.refresh();
			}
		}
	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub

	}

}
