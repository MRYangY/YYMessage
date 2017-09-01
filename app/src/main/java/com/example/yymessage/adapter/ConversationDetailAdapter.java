package com.example.yymessage.adapter;

import com.example.yymessage.R;
import com.example.yymessage.bean.Sms;
import com.example.yymessage.globle.Constant;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationDetailAdapter extends CursorAdapter {
	static final int DURATION=3*60*1000;
	ListView lv;
	public ConversationDetailAdapter(Context context, Cursor c,ListView lv) {
		super(context, c);
		// TODO Auto-generated constructor stub
		this.lv=lv;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_conversation_detail, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		//�������ȫ��holder��
		ViewHolder holder=getHolder(view);
		//����ȫ��sms������
		Sms sms=Sms.createFromCursor(cursor);
		//������ʾ����
		//ʱ��
		//��һ�����Ų���Ҫ�Ƚ�ʱ��
		if(cursor.getPosition()==0){
			holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
			showDate(sms, holder, context);
		}else{
			//�жϵ�ǰ��������һ�����ŵ�ʱ������û�г���������
			long previousDate=getPreviousSmsDate(cursor.getPosition());
			if(sms.getDate()-previousDate>DURATION){
				holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
				showDate(sms, holder, context);
			}else{
				holder.tv_conversation_detail_date.setVisibility(View.GONE);
			}
		}		
		//����
		holder.tv_conversation_detail_receive.setVisibility(sms.getType()==Constant.SMS.TYPE_RECEIVE? View.VISIBLE:View.GONE);
		holder.tv_conversation_detail_send.setVisibility(sms.getType()==Constant.SMS.TYPE_SEND? View.VISIBLE:View.GONE);
		if(sms.getType()==Constant.SMS.TYPE_RECEIVE){
			holder.tv_conversation_detail_receive.setText(sms.getBody());
		}else{
			holder.tv_conversation_detail_send.setText(sms.getBody());
		}
	}
	private ViewHolder getHolder(View view) {
		ViewHolder holder=(ViewHolder) view.getTag();
		if(holder==null){
			holder=new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;

	}
	class ViewHolder{
		private TextView tv_conversation_detail_date;
		private TextView tv_conversation_detail_receive;
		private TextView tv_conversation_detail_send;

		public ViewHolder(View view) {
			tv_conversation_detail_date = (TextView) view.findViewById(R.id.tv_conversation_detail_date);
			tv_conversation_detail_receive = (TextView) view.findViewById(R.id.tv_conversation_detail_receive);
			tv_conversation_detail_send = (TextView) view.findViewById(R.id.tv_conversation_detail_send);
		}
	}
	private void showDate(Sms sms,ViewHolder holder,Context context) {
		if(DateUtils.isToday(sms.getDate())){
			holder.tv_conversation_detail_date.setText(DateFormat.getTimeFormat(context).format(sms.getDate()));
		}else{
			holder.tv_conversation_detail_date.setText(DateFormat.getDateFormat(context).format(sms.getDate()));
		}

	}
	/**
	 * ��ȡ��һ�����ŵ�ʱ��
	 * @param position
	 * @return
	 */
	private long getPreviousSmsDate(int position) {
		Cursor cursor=(Cursor) getItem(position-1);
		Sms sms=Sms.createFromCursor(cursor);
		return sms.getDate();
	}
	
	@Override
	public void changeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		super.changeCursor(cursor);
		//��ListView������ָ������Ŀ��
		lv.setSelection(getCount());
	}
}
