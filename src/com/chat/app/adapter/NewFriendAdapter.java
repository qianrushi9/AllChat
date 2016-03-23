package com.chat.app.adapter;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.UpdateListener;
import com.chat.app.activity.R;
import com.chat.app.application.ChatApplication;
import com.chat.app.base.MyBaseAdapter;
import com.chat.app.utils.BitmapUtils;
import com.chat.app.utils.CollectionUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewFriendAdapter extends MyBaseAdapter<BmobInvitation>{

	public NewFriendAdapter(List<BmobInvitation> datas, Context context) {
		super(datas, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView,
			ViewGroup viewGroup) {
		final BmobInvitation invitation = datas.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_item_addfriend, viewGroup, false);
		}
		
		TextView fromName = ViewHolder.get(convertView, R.id.name);
		ImageView avatar = ViewHolder.get(convertView, R.id.avatar);
		String img_url = invitation.getAvatar();
		if(img_url != null && !img_url.equals("")){
			ImageLoader.getInstance().displayImage(img_url, avatar, BitmapUtils.getOptions());
		}else{
			avatar.setImageResource(R.drawable.avatar);
		}
		fromName.setText(invitation.getFromname());
		final Button btn_agree = ViewHolder.get(convertView, R.id.btn_add);
		btn_agree.setText("ͬ��");
		int status = invitation.getStatus();
		if(status==BmobConfig.INVITE_ADD_NO_VALIDATION||status==BmobConfig.INVITE_ADD_NO_VALI_RECEIVED){

			btn_agree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					BmobLog.i("���ͬ�ⰴť:"+invitation.getFromid());
					final ProgressDialog progress = new ProgressDialog(context);
					progress.setMessage("�������...");
					progress.setCanceledOnTouchOutside(false);
					progress.show();
					try {
						//ͬ����Ӻ���
						BmobUserManager.getInstance(context).agreeAddContact(invitation, new UpdateListener() {
							
							@SuppressWarnings("deprecation")
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								progress.dismiss();
								btn_agree.setText("��ͬ��");
								btn_agree.setBackgroundDrawable(null);
								btn_agree.setTextColor(context.getResources().getColor(R.color.base_color_text_black));
								btn_agree.setEnabled(false);
								//���浽application�з���Ƚ�
								ChatApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(context).getContactList()));	
							}
							
							@Override
							public void onFailure(int arg0, final String arg1) {
								// TODO Auto-generated method stub
								progress.dismiss();
								ShowToast("���ʧ��: " +arg1);
							}
						});
					} catch (final Exception e) {
						progress.dismiss();
						ShowToast("���ʧ��: " +e.getMessage());
					}
				}
			});
		}else if(status==BmobConfig.INVITE_ADD_AGREE){
			btn_agree.setText("��ͬ��");
			btn_agree.setBackgroundDrawable(null);
			btn_agree.setTextColor(context.getResources().getColor(R.color.base_color_text_black));
			btn_agree.setEnabled(false);
		}
		return convertView;
	}

}
