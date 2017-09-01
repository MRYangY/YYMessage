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
	//记录选择模式下选中哪些条目
	private List<Integer> selectedConversationIds=new ArrayList<Integer>();
	public ConversationListAdaptor(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	//返回的View对象就是ListView的条目
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_conversation_list, null);
	}

	@Override
	//设置ListView每个条目显示的内容
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder=getHolder(view);
		//根据cursor的内容创建会话对象，此时cursor的指针已经移至正确的位置
		Conversation conversation=Conversation.createFromCursor(cursor);
		//判断当前是否进入选择模式
		if(isCheck){
			holder.iv_check.setVisibility(View.VISIBLE);
			//判断集合中是否包含会话ID，从而确定该条目是否被选中
			if(selectedConversationIds.contains(conversation.getThread_id())){
				holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_checked);
			}else{
				holder.iv_check.setBackgroundResource(R.drawable.common_checkbox_normal);
			}
		}else{
			holder.iv_check.setVisibility(View.GONE);
		}
		//设置号码
		//按号码查询是否存有联系人
		String name=ContactDao.getNameByAddress(context.getContentResolver(), conversation.getAddress());
		if(TextUtils.isEmpty(name)){
			holder.tv_conversation_address.setText(conversation.getAddress()+"("+conversation.getMsg_count()+")");
		}else{
			holder.tv_conversation_address.setText(name+"("+conversation.getMsg_count()+")");
		}
		holder.tv_conversation_body.setText(conversation.getSnippet());
		
		
		//设置时间
		//判断短信发送时间，是不是今天发的
		if(DateUtils.isToday(conversation.getDate())){
			//是的话，显示时分
			holder.tv_conversation_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
		}else{
			//不是的话，显示年月日
			holder.tv_conversation_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
		}
		
		//获取联系人头像
		Bitmap avatar=ContactDao.getAvatarByAddress(context.getContentResolver(), conversation.getAddress());
		//判断是否成功拿到头像
		if(avatar==null){
			holder.iv_conversation_avator.setBackgroundResource(R.drawable.img_default_avatar);
		}else{
			holder.iv_conversation_avator.setBackgroundDrawable(new BitmapDrawable(avatar));
		}
	}
	//参数就是条目的View对象
	private ViewHolder getHolder(View view) {
		//先判断条目View中是否有holder
		ViewHolder holder=(ViewHolder) view.getTag();
		if(holder==null){
			//如果没有就创建一个并且存进View对象
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
	 * 把选中的条目放入集合
	 * @param position
	 */
	public void selectSingle(int position){
		Cursor cursor=(Cursor) getItem(position);
		Conversation conversation=Conversation.createFromCursor(cursor);
		if(selectedConversationIds.contains(conversation.getThread_id())){
			//强转为Integer,否者是把参数作为索引而不是要删除的元素
			selectedConversationIds.remove((Integer)conversation.getThread_id());
		}else{
			selectedConversationIds.add(conversation.getThread_id());
		}
		
		
		notifyDataSetChanged();
	}
	public void selectAll(){
		//遍历Cursor取出所有会话Id
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
		//清空集合
		selectedConversationIds.clear();
		notifyDataSetChanged();
	}

	public List<Integer> getSelectedConversationIds() {
		return selectedConversationIds;
	}

	
}
