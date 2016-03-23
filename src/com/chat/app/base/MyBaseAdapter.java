package com.chat.app.base;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected List<T> datas;
	protected Context context;
	protected LayoutInflater inflater;
	
	public MyBaseAdapter(List<T> datas,Context context){
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	Toast mToast;
	
	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(context, text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});

		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		return getConvertView(position, convertView, viewGroup);
	}
	
	public abstract View getConvertView(int position, View convertView, ViewGroup viewGroup);
	
	

}
