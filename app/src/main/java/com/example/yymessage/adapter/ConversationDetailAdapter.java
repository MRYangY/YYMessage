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
		//组件对象全在holder里
		ViewHolder holder=getHolder(view);
		//数据全在sms对象里
		Sms sms=Sms.createFromCursor(cursor);
		//设置显示内容
		//时间
		//第一条短信不需要比较时间
		if(cursor.getPosition()==0){
			holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
			showDate(sms, holder, context);
		}else{
			//判断当前短信与上一条短信的时间间隔有没有超过三分钟
			long previousDate=getPreviousSmsDate(cursor.getPosition());
			if(sms.getDate()-previousDate>DURATION){
				holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
				showDate(sms, holder, context);
			}else{
				holder.tv_conversation_detail_date.setVisibility(View.GONE);
			}
		}		
		//内容
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
	 * 获取上一条短信的时间
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
		//让ListView滑动到指定的条目上
		lv.setSelection(getCount());
	}
}
