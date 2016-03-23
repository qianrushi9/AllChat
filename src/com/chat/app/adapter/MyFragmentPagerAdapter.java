package com.chat.app.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private FragmentManager fgManager;
    private List<Fragment> fragments;

	public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		fgManager = fm;
		this.fragments = fragments;
	}



	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}}