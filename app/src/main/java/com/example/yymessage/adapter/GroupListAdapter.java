package com.example.yymessage.adapter;

import com.example.yymessage.R;
import com.example.yymessage.bean.Group;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class GroupListAdapter extends CursorAdapter {

	public GroupListAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder=getHolder(view);
		Group group=Group.createFromCursor(cursor);
		holder.tv_group_list_name.setText(group.getName()+" ("+group.getThread_count()+")");
		if(DateUtils.isToday(group.getCreate_date())){
			holder.tv_group_list_date.setText(DateFormat.getTimeFormat(context).format(group.getCreate_date()));
		}else{
			holder.tv_group_list_date.setText(DateFormat.getDateFormat(context).format(group.getCreate_date()));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_group_list, null);
	}
	
	private ViewHolder getHolder(View view){
		ViewHolder holder=(ViewHolder) view.getTag();
		if(holder==null){
			holder=new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	class ViewHolder{
		private TextView tv_group_list_date;
		private TextView tv_group_list_name;
		public ViewHolder(View view) {
			tv_group_list_date = (TextView) view.findViewById(R.id.tv_group_list_date);
			tv_group_list_name = (TextView) view.findViewById(R.id.tv_group_list_name);
		}
	}

}
