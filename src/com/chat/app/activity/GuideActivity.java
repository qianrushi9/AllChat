package com.chat.app.activity;

import java.util.ArrayList;
import java.util.List;
import com.chat.app.adapter.MyViewPagerAdapter;
import com.chat.app.base.BaseActivity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager mGuideViewPager;
	private MyViewPagerAdapter vpAdater;
	private List<View> guideViews;
	private ImageView[] dots;
	private int[] dotIds = { R.id.iv1, R.id.iv2, R.id.iv3 };
	private Button btn_start;

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.guide;
	}

	// 初始化View
	@Override
	public void initViews() {
		// 初始化指示点
		initDots();
		mGuideViewPager = (ViewPager) findViewById(R.id.viewpager);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = inflater.inflate(R.layout.one, null, false);
		View view2 = inflater.inflate(R.layout.two, null, false);
		View view3 = inflater.inflate(R.layout.three, null, false);
		guideViews = new ArrayList<View>();
		guideViews.add(view1);
		guideViews.add(view2);
		guideViews.add(view3);
		vpAdater = new MyViewPagerAdapter(guideViews, this);
		mGuideViewPager.setAdapter(vpAdater);
		mGuideViewPager.setOnPageChangeListener(this);
		btn_start = (Button) view3.findViewById(R.id.start_btn);
		btn_start.setOnClickListener(this);
	}

	// 初始化指示点
	private void initDots() {
		dots = new ImageView[dotIds.length];
		for (int i = 0; i < dots.length; i++) {
			dots[i] = (ImageView) findViewById(dotIds[i]);
		}
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_btn:
			navigateToTask(LoginActivity.class, true);
			break;

		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		for (int i = 0; i < dotIds.length; i++) {
			if (i == position) {
				dots[i].setImageResource(R.drawable.login_point_selected);
			} else {
				dots[i].setImageResource(R.drawable.login_point);
			}
		}
	}

}
