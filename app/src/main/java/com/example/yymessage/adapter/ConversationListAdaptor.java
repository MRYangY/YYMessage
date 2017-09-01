package com.example.yymessage.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import com.example.yymessage.R;
import com.example.yymessage.bean.Conversation;
import com.example.yymessage.dao.ContactDao;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.provider.ContactsContract.DataUsageFeedback;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationListAdaptor extends CursorAdapter {
	
	private boolean isCheck=false;
	//��¼ѡ��ģʽ��ѡ����Щ��Ŀ
	private List<Integer> selectedConversationIds=new ArrayList<Integer>();
	public ConversationListAdaptor(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	//���ص�View�������ListView����Ŀ
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_conversation_list, null);
	}

	@Override
	//����ListViewÿ����Ŀ��ʾ������
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder=getHolder(view);
		//����cursor�����ݴ����Ự���󣬴�ʱcursor��ָ���Ѿ�������ȷ��λ��
		Conversation conversation=Conversation.createFromCursor(cursor);
		//�жϵ�ǰ�Ƿ����ѡ��ģʽ
		if(isCheck){
			holder.iv_check.setVisibility(View.VISIBLE);
			//�жϼ������Ƿ�����ỰID���Ӷ�ȷ������Ŀ�Ƿ�ѡ��
			if(selectedConversationIds.contains(conversation.getThread_id())){
				holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_checked);
			}else{
				holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_normal);
			}
		}else{
			holder.iv_check.setVisibility(View.GONE);
		}
		//���ú���
		//�������ѯ�Ƿ������ϵ��
		String name=ContactDao.getNameByAddress(context.getContentResolver(), conversation.getAddress());
		if(TextUtils.isEmpty(name)){
			holder.tv_conversation_address.setText(conversation.getAddress()+"("+conversation.getMsg_count()+")");
		}else{
			holder.tv_conversation_address.setText(name+"("+conversation.getMsg_count()+")");
		}
		holder.tv_conversation_body.setText(conversation.getSnippet());
		
		
		//����ʱ��
		//�ж϶��ŷ���ʱ�䣬�ǲ��ǽ��췢��
		if(DateUtils.isToday(conversation.getDate())){
			//�ǵĻ�����ʾʱ��
			holder.tv_conversation_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
		}else{
			//���ǵĻ�����ʾ������
			holder.tv_conversation_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
		}
		
		//��ȡ��ϵ��ͷ��
		Bitmap avatar=ContactDao.getAvatarByAddress(context.getContentResolver(), conversation.getAddress());
		//�ж��Ƿ�ɹ��õ�ͷ��
		if(avatar==null){
			holder.iv_conversation_avator.setBackgroundResource(R.drawable.img_default_avatar);
		}else{
			holder.iv_conversation_avator.setBackgroundDrawable(new BitmapDrawable(avatar));
		}
	}
	//����������Ŀ��View����
	private ViewHolder getHolder(View view) {
		//���ж���ĿView���Ƿ���holder
		ViewHolder holder=(ViewHolder) view.getTag();
		if(holder==null){
			//���û�оʹ���һ�����Ҵ��View����
			holder=new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	public boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	class ViewHolder{
		private ImageView iv_conversation_avator;
		private TextView tv_conversation_address;
		private TextView tv_conversation_body;
		private TextView tv_conversation_date;
		private ImageView iv_check;

		public ViewHolder(View view) {
			iv_conversation_avator = (ImageView) view.findViewById(R.id.iv_conversation_avator);
			tv_conversation_address = (TextView) view.findViewById(R.id.tv_conversation_address);
			tv_conversation_body = (TextView) view.findViewById(R.id.tv_conversation_body);
			tv_conversation_date = (TextView) view.findViewById(R.id.tv_conversation_date);
			iv_check = (ImageView) view.findViewById(R.id.iv_check);
		}
	}
	/**
	 * ��ѡ�е���Ŀ���뼯��
	 * @param position
	 */
	public void selectSingle(int position){
		Cursor cursor=(Cursor) getItem(position);
		Conversation conversation=Conversation.createFromCursor(cursor);
		if(selectedConversationIds.contains(conversation.getThread_id())){
			//ǿתΪInteger,�����ǰѲ�����Ϊ����������Ҫɾ����Ԫ��
			selectedConversationIds.remove((Integer)conversation.getThread_id());
		}else{
			selectedConversationIds.add(conversation.getThread_id());
		}
		
		
		notifyDataSetChanged();
	}
	public void selectAll(){
		//����Cursorȡ�����лỰId
		Cursor cursor=getCursor();
		cursor.moveToPosition(-1);
		selectedConversationIds.clear();
		while(cursor.moveToNext()){
			Conversation conversation=Conversation.createFromCursor(cursor);
			selectedConversationIds.add(conversation.getThread_id());
		}
		notifyDataSetChanged();
	}
	public void cancelSelect(){
		//��ռ���
		selectedConversationIds.clear();
		notifyDataSetChanged();
	}

	public List<Integer> getSelectedConversationIds() {
		return selectedConversationIds;
	}

	
}
