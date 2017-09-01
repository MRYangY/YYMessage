package com.example.yymessage.dialog;

import com.example.yymessage.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListDialog extends BaseDialog {

	private TextView tv_listdialog_title;
	private String title;
	private String[] items;
	private ListView lv_listdialog; 
	private OnListDialogListener onListDialogListener;
	private Context context;
	
	protected ListDialog(Context context,String title,String[] items,OnListDialogListener onListDialogListener) {
		super(context);
		this.context=context;
		this.title=title;
		this.items=items;
		this.onListDialogListener=onListDialogListener;
	}
	public static void showDialog(Context context,String title,String[] items,OnListDialogListener onListDialogListener){
		ListDialog listDialog=new ListDialog(context, title, items, onListDialogListener);
		listDialog.show();
	}
	@Override
	public void initView() {
		setContentView(R.layout.dialog_list);
		tv_listdialog_title = (TextView) findViewById(R.id.tv_listdialog_title);
		lv_listdialog = (ListView) findViewById(R.id.lv_listdialog);
	}

	@Override
	public void initListener() {
		lv_listdialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(onListDialogListener!=null){
					onListDialogListener.onItemClick(parent, view, position, id);
				}
				dismiss();
			}

		
		});

	}

	@Override
	public void initdata() {
		tv_listdialog_title.setText(title);
		lv_listdialog.setAdapter(new MyAdapter());
	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub

	}
	public interface OnListDialogListener{
		void onItemClick(AdapterView<?> parent, View view, int position,
				long id); 
	}
	class MyAdapter extends BaseAdapter{

		private TextView tv_listdialog_item;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=View.inflate(context, R.layout.item_listdialog, null);
			tv_listdialog_item = (TextView) view.findViewById(R.id.tv_listdialog_item);
			tv_listdialog_item.setText(items[position]);
			return view;
		}
		
	}

}
