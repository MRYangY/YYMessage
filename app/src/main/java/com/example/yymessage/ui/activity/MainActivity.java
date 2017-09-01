package com.example.yymessage.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.yymessage.R;
import com.example.yymessage.R.id;
import com.example.yymessage.R.layout;
import com.example.yymessage.adapter.MainPagerAdapter;
import com.example.yymessage.base.BaseActivity;
import com.example.yymessage.ui.fragment.ConversationFragment;
import com.example.yymessage.ui.fragment.GroupFragment;
import com.example.yymessage.ui.fragment.SearchFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    


	private ViewPager viewpager;
	private List<Fragment> fragments;
	private TextView tv_tab_search;
	private TextView tv_tab_conversation;
	private TextView tv_tab_group;
	private LinearLayout ll_tab_conversation;
	private LinearLayout ll_tab_group;
	private LinearLayout ll_tab_search;
	private View v_indicate_line;


	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		//�ҵĲ����ļ��е����
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		tv_tab_conversation = (TextView) findViewById(R.id.tv_tab_conversation);
		tv_tab_group = (TextView) findViewById(R.id.tv_tab_group);
		tv_tab_search = (TextView) findViewById(R.id.tv_tab_search);
		ll_tab_conversation = (LinearLayout) findViewById(R.id.ll_tab_conversation);
		ll_tab_group = (LinearLayout) findViewById(R.id.ll_tab_group);
		ll_tab_search = (LinearLayout) findViewById(R.id.ll_tab_search);
		v_indicate_line = findViewById(R.id.v_indicate_line);
	}

	@Override
	public void initListener() {
		//viewpager�����л�ʱ�ᴥ��
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//�л���ɺ���ã�����Ĳ������л�����������
			public void onPageSelected(int position) {
				textLigthAndScale();
				
			}
			
			@Override
			//���������в��ϵ���
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//positionOffsetPixels:λ��ƫ����������ֵ
				//�ڻ��������г����������棬��˺����е�position����ֵΪǰһ�����������
				int intance=positionOffsetPixels/3;
				ViewPropertyAnimator.animate(v_indicate_line).translationX(intance+position*v_indicate_line.getWidth()).setDuration(0);
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
		});
		
		//������linearlayout���õ���¼�
		ll_tab_conversation.setOnClickListener(this);
		ll_tab_group.setOnClickListener(this);
		ll_tab_search.setOnClickListener(this);
		
	}

	@Override
	public void initData() {
		fragments = new ArrayList<Fragment>();
		ConversationFragment fragment1=new ConversationFragment();
		GroupFragment fragment2=new GroupFragment();
		SearchFragment fragment3=new SearchFragment();
		fragments.add(fragment1);
		fragments.add(fragment2);
		fragments.add(fragment3);
		MainPagerAdapter adapter=new MainPagerAdapter(getSupportFragmentManager(), fragments);
		viewpager.setAdapter(adapter);
		textLigthAndScale();
		
		//���ú��ߵĿ��
		computeIndicateLineWidth();
	}

	
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.ll_tab_conversation:
			viewpager.setCurrentItem(0);
			break;
		case R.id.ll_tab_group:
			viewpager.setCurrentItem(1);
			break;
		case R.id.ll_tab_search:
			viewpager.setCurrentItem(2);
			break;	
		}
	}
	
	/**
	 * �ı�ѡ����������ɫ
	 */
	private void textLigthAndScale(){
		//��ȡviewpager��ǰ��ʾ���������
		int item=viewpager.getCurrentItem();
		//����viewPager�Ľ�����������ѡ�����ɫ
		tv_tab_conversation.setTextColor(item==0? 0xFFFFFFFF:0xaa666666);
		tv_tab_group.setTextColor(item==1? 0xFFFFFFFF:0xaa666666);
		tv_tab_search.setTextColor(item==2? 0xFFFFFFFF:0xaa666666);
		//                            Ҫ�����Ķ���
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleX(item==0? 1.2f:1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleX(item==1? 1.2f:1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleX(item==2? 1.2f:1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleY(item==0? 1.2f:1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleY(item==1? 1.2f:1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleY(item==2? 1.2f:1).setDuration(200);
	}
	/**
	 * ���ú��ߵĿ��Ϊ��Ļ������֮һ
	 */
	private void computeIndicateLineWidth(){
		int width=getWindowManager().getDefaultDisplay().getWidth();
		v_indicate_line.getLayoutParams().width=width/3;
	}

    
    
}
