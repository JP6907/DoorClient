package com.example.communityfunction.myView;

import java.util.ArrayList;

import com.neo.huikaimen.R;
import com.example.communityfunction.bbs.PostBbsActivity;
import com.example.communityfunction.tool.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangyu
 *	�������������ⰴť�ϵĵ������̳���PopupWindow��
 */
public class TitlePopup extends PopupWindow {
	private Context mContext;

	//�б����ļ��
	protected final int LIST_PADDING = 10;
	
	//ʵ����һ������
	private Rect mRect = new Rect();
	
	//�����λ�ã�x��y��
	private final int[] mLocation = new int[2];
	
	//��Ļ�Ŀ�Ⱥ͸߶�
	private int mScreenWidth,mScreenHeight;

	//�ж��Ƿ���Ҫ��ӻ�����б�������
	private boolean mIsDirty;
	
	//λ�ò�������
	private int popupGravity = Gravity.NO_GRAVITY;	
	
	//����������ѡ��ʱ�ļ���
	private OnItemOnClickListener mItemOnClickListener;
	
	//�����б����
	private ListView mListView;
	
	//���嵯���������б�
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();			
	/**
	 * 
	 * @param context������
	 */
	public TitlePopup(Context context){
		//���ò��ֵĲ���
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
	}
	/**
	 * 
	 * @param context ������
	 * @param width   ��
	 * @param height  ��
	 */
	public TitlePopup(Context context, int width, int height){
		this.mContext = context;
		
		//���ÿ��Ի�ý���
		setFocusable(true);
		//���õ����ڿɵ��
		setTouchable(true);	
		//���õ�����ɵ��
		setOutsideTouchable(true);
		
		//�����Ļ�Ŀ�Ⱥ͸߶�
		mScreenWidth = Util.getScreenWidth(mContext);
		mScreenHeight = Util.getScreenHeight(mContext);
		
		//���õ����Ŀ�Ⱥ͸߶�
		setWidth(width);
		setHeight(height);
		
		setBackgroundDrawable(new BitmapDrawable());
		
		//���õ����Ĳ��ֽ���
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.title_popup, null));
		
		initUI();
	}
		
	/**
	 * ��ʼ�������б�
	 */
	private void initUI(){
		mListView = (ListView) getContentView().findViewById(R.id.title_list);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//���������󣬵�����ʧ
				dismiss();
				
				if(mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(position), position);
				
				
				//*****************************
				String s = mActionItems.get(position).mTitle.toString().trim();
				if(s.equals("����"))
				{
					Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show(); 
//					Intent intent = new Intent(mContext,PostBbsActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					mContext.startActivity(intent);
//					((Activity) mContext).startActivityForResult(intent,1);
				}else if(s.equals("������")){
					Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
				}else if(s.equals("�ղ�")){
					Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
				}else if(s.equals("ֻ��¥��")){
					Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
				}else if(s.equals("ת��")){
					Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
				}else if(s.equals("�ղ�")){
					Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
				}
				
			}
		}); 
	}
	
	/**
	 * ��ʾ�����б����
	 */
	public void show(View view){
		//��õ����Ļ��λ������
		view.getLocationOnScreen(mLocation);
		
		//���þ��εĴ�С
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),mLocation[1] + view.getHeight());
		
		//�ж��Ƿ���Ҫ��ӻ�����б�������
		if(mIsDirty){
			populateActions();
		}
		
		//��ʾ������λ��
		showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth()/2), mRect.bottom);
	}
	
	/**
	 * ���õ����б�����
	 */
	private void populateActions(){
		mIsDirty = false;
		
		//�����б��������
		mListView.setAdapter(new BaseAdapter() {			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;
				
				if(convertView == null){
					textView = new TextView(mContext);
					textView.setTextColor(mContext.getResources().getColor(android.R.color.white));
					textView.setTextSize(14);
					//�����ı�����
					textView.setGravity(Gravity.CENTER);
					//�����ı���ķ�Χ
					textView.setPadding(0, 10, 0, 10);
					//�����ı���һ������ʾ�������У�
					textView.setSingleLine(true);
				}else{
					textView = (TextView) convertView;
				}
				
				ActionItem item = mActionItems.get(position);
				
				//�����ı�����
				textView.setText(item.mTitle);
				//����������ͼ��ļ��
				textView.setCompoundDrawablePadding(10);
				//���������ֵ���߷�һ��ͼ��
                textView.setCompoundDrawablesWithIntrinsicBounds(item.mDrawable, null , null, null);
				
                return textView;
			}
			

			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}
			
			@Override
			public int getCount() {
				return mActionItems.size();
			}
		}) ;
	}
	
	/**
	 * ���������
	 */
	public void addAction(ActionItem action){
		if(action != null){
			mActionItems.add(action);
			mIsDirty = true;
		}
	}
	
	/**
	 * ���������
	 */
	public void cleanAction(){
		if(mActionItems.isEmpty()){
			mActionItems.clear();
			mIsDirty = true;
		}
	}
	
	/**
	 * ����λ�õõ�������
	 */
	public ActionItem getAction(int position){
		if(position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}			
	
	/**
	 * ���ü����¼�
	 */
	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
		this.mItemOnClickListener = onItemOnClickListener;
		//Toast.makeText(mContext, "hello", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * @author yangyu
	 *	�������������������ť�����¼�
	 */
	public static interface OnItemOnClickListener{
		public void onItemClick(ActionItem item , int position);
	}
}
