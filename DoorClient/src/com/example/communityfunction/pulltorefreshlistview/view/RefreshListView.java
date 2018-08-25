package com.example.communityfunction.pulltorefreshlistview.view;

import java.text.SimpleDateFormat;

import com.neo.huikaimen.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener {

//	private static final String TAG = "RefreshListView";
	private int firstVisibleItemPosition; // ��Ļ��ʾ�ڵ�һ����item������
	private int downY; // ����ʱy���ƫ����
	private int headerViewHeight; // ͷ���ֵĸ߶�
	private int layoutHeight;
	private int paddingTopFirst;//�ʼ��ʱ��ͷ���ֵ�paddingTop
	private View headerView; // ͷ���ֵĶ���
	private LinearLayout layout;//�Ϸ������Ĳ˵�view

	private final int DOWN_PULL_REFRESH = 0; // ����ˢ��״̬
	private final int RELEASE_REFRESH = 1; // �ɿ�ˢ��
	private final int REFRESHING = 2; // ����ˢ����
	private int currentState = DOWN_PULL_REFRESH; // ͷ���ֵ�״̬: Ĭ��Ϊ����ˢ��״̬
	
	private int direction;//��������

	private Animation upAnimation; // ������ת�Ķ���
	private Animation downAnimation; // ������ת�Ķ���
	
	private ObjectAnimator mAnimator1, mAnimator2;//��ʾ�����ز˵�view�Ķ���
	private AnimatorSet set;

	private ImageView ivArrow; // ͷ���ֵļ�ͷ
	private ProgressBar mProgressBar; // ͷ���ֵĽ�����
	private TextView tvState; // ͷ���ֵ�״̬
//	private TextView tvLastUpdateTime; // ͷ���ֵ�������ʱ��

	private OnRefreshListener mOnRefershListener;
	private boolean isScrollToBottom; // �Ƿ񻬶����ײ�
	private boolean mShow=true;//�Ƿ���ʾ�Ϸ��Ĳ˵�view
	private View footerView; // �Ų��ֵĶ���
	private int footerViewHeight; // �Ų��ֵĸ߶�
	private boolean isLoadingMore = false; // �Ƿ����ڼ��ظ�����

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		this.setOnScrollListener(this);
	}

	/**
	 * ��ʼ���Ų���
	 */
	private void initFooterView() {
		footerView = View.inflate(getContext(), R.layout.listview_footer, null);
		footerView.measure(0, 0);
		footerViewHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		this.addFooterView(footerView);
	}

	/**
	 * ��ʼ��ͷ����
	 */
	public void initHeaderView() {
		headerView = View.inflate(getContext(), R.layout.listview_header, null);
		
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_arrow);
		mProgressBar = (ProgressBar) headerView.findViewById(R.id.pb_listview_header);
		tvState = (TextView) headerView.findViewById(R.id.tv_listview_header_state);
//		tvLastUpdateTime = (TextView) headerView.findViewById(R.id.tv_listview_header_last_update_time);
		
		headerViewHeight=ivArrow.getLayoutParams().height;

		View head=View.inflate(getContext(), R.layout.fragment_community, null);
		View searchBar=head.findViewById(R.id.search_bar);
		layout=(LinearLayout) head.findViewById(R.id.liner);
		
		layoutHeight=searchBar.getLayoutParams().height+layout.getLayoutParams().height;
		
		// �������ˢ��ʱ��
//		tvLastUpdateTime.setText("���ˢ��ʱ��: " + getLastUpdateTime());

		headerView.measure(0, 0); // ϵͳ������ǲ�����headerView�ĸ߶�
		headerViewHeight=headerView.getMeasuredHeight();
		
		paddingTopFirst=layoutHeight-headerViewHeight;//Ҫ��ֹ����head��סlistView
		
		headerView.setPadding(0, paddingTopFirst, 0, 0);
		this.addHeaderView(headerView); // ��ListView�Ķ������һ��view����
		initAnimation();
		Log.e("RefreshListView", ""+paddingTopFirst+"m"+headerViewHeight);
	}

	/**
	 * ���ϵͳ������ʱ��
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public String getLastUpdateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}

	/**
	 * ��ʼ������
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(200);
		upAnimation.setFillAfter(true); // ����������, ͣ���ڽ�����λ����

		downAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(200);
		downAnimation.setFillAfter(true); // ����������, ͣ���ڽ�����λ����
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			// �ƶ��е�y - ���µ�y = ���.
			int diff = (moveY - downY) / 2;
			// -ͷ���ֵĸ߶� + ��� = paddingTop
			int paddingTop = paddingTopFirst + diff;
			// ���: -ͷ���ֵĸ߶� > paddingTop��ֵ ִ��super.onTouchEvent(ev);
			if (firstVisibleItemPosition == 0 && headerView.getPaddingTop()>=paddingTopFirst && paddingTopFirst < paddingTop) {
				if (paddingTop > paddingTopFirst+headerViewHeight && currentState == DOWN_PULL_REFRESH) { // ��ȫ��ʾ��.
				//	Log.i(TAG, "�ɿ�ˢ��");
					currentState = RELEASE_REFRESH;
					refreshHeaderView();
				} else if (paddingTop < paddingTopFirst+headerViewHeight && currentState == RELEASE_REFRESH) { // û����ʾ��ȫ
				//	Log.i(TAG, "����ˢ��");
					currentState = DOWN_PULL_REFRESH;
					refreshHeaderView();
				}
				// ����ͷ����
				headerView.setPadding(0,paddingTop, 0, 0);
				Log.e("RefreshListView", ""+headerView.getPaddingTop());
				Log.e("RefreshListView", "action_move");
				Log.e("RefreshListView", ""+paddingTopFirst+"m"+headerViewHeight);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			// �жϵ�ǰ��״̬���ɿ�ˢ�»�������ˢ��
			if (currentState == RELEASE_REFRESH) {
				//Log.i(TAG, "ˢ������.");
				// ��ͷ��������Ϊ��ȫ��ʾ״̬
				headerView.setPadding(0, paddingTopFirst+headerViewHeight, 0, 0);
				// ���뵽����ˢ����״̬
				currentState = REFRESHING;
				refreshHeaderView();
				
				if (mOnRefershListener != null) {
					mOnRefershListener.onDownPullRefresh(); //����ʹ���ߵļ�������
				}
			} else if (currentState == DOWN_PULL_REFRESH) {
				// ����ͷ����
				headerView.setPadding(0, paddingTopFirst, 0, 0);
				Log.e("RefreshListView", "action_up");
				Log.e("RefreshListView", ""+paddingTopFirst+"m"+headerViewHeight);
			}
			Log.e("RefreshListView", ""+headerView.getPaddingTop());
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ����currentStateˢ��ͷ���ֵ�״̬
	 */
	private void refreshHeaderView() {
		switch (currentState) {
		case DOWN_PULL_REFRESH: // ����ˢ��״̬
			tvState.setText("����ˢ��");
			ivArrow.startAnimation(downAnimation); // ִ��������ת
			break;
		case RELEASE_REFRESH: // �ɿ�ˢ��״̬
			tvState.setText("�ɿ�ˢ��");
			ivArrow.startAnimation(upAnimation); // ִ��������ת
			break;
		case REFRESHING: // ����ˢ����״̬
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			tvState.setText("����ˢ����...");
			break;
		default:
			break;
		}
	}

	//Ϊ�˽����������ˢ����Ӹú���
	public void startRefreshHeaderView() 
	{		
		headerView.setPadding(0,paddingTopFirst+headerViewHeight ,0, 0);
		currentState = REFRESHING;
		ivArrow.clearAnimation();
		ivArrow.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		tvState.setText("����ˢ����...");
	}
	
	/**
	 * ������״̬�ı�ʱ�ص�
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			// �жϵ�ǰ�Ƿ��Ѿ����˵ײ�
			if (isScrollToBottom && !isLoadingMore) {
				isLoadingMore = true;
				// ��ǰ���ײ�
				//Log.i(TAG, "���ظ�������");
				footerView.setPadding(0, 0, 0, 0);
				this.setSelection(this.getCount());

				if (mOnRefershListener != null) {
					mOnRefershListener.onLoadingMore();
				}
			}
		}
	}

	/**
	 * ������ʱ����
	 * 
	 * @param firstVisibleItem
	 *            ��ǰ��Ļ��ʾ�ڶ�����item��position
	 * @param visibleItemCount
	 *            ��ǰ��Ļ��ʾ�˶��ٸ���Ŀ������
	 * @param totalItemCount
	 *            ListView������Ŀ������
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstVisibleItemPosition = firstVisibleItem;

		if (getLastVisiblePosition() == (totalItemCount - 1)) {
			isScrollToBottom = true;
		} else {
			isScrollToBottom = false;
		}
		
//		int lastVisibleItemPosition = 0;
//		if(firstVisibleItem>lastVisibleItemPosition)
//			direction=1;//listView���Ϲ���
//		else
//			direction=0;//listView���¹���
//		
//		lastVisibleItemPosition=firstVisibleItem;
//		if (direction == 1) 
//		{
//			if (mShow) 
//			{
//				LinearViewAnim(1, layout);// �����Ϸ���view
//				mShow = !mShow;
//			}
//		} 
//		else if (direction == 0) 
//		{
//			if (!mShow) 
//			{
//				LinearViewAnim(0, layout);// չʾ�Ϸ���view
//				mShow = !mShow;
//			}
//		}
	}
	
	/**
	 * ������ʾ�������ض���
	 * @param flag
	 * @param linearView
	 */
	protected void LinearViewAnim(int flag,LinearLayout linearView) 
	{
		
		// TODO Auto-generated method stub
		if(set!=null && set.isRunning())
		{
			set.cancel();
		}
		if(flag==0)
		{
		
			mAnimator1=ObjectAnimator.ofFloat(linearView, 
					"translationY", linearView.getTranslationY(),0);
			mAnimator2=ObjectAnimator.ofFloat(linearView, "alpha", 0f,1f);
		}
		else if(flag==1)
		{
			
		
			mAnimator1=ObjectAnimator.ofFloat(linearView, 
					"translationY", linearView.getTranslationY(),-layoutHeight);
			mAnimator2=ObjectAnimator.ofFloat(linearView, "alpha", 1f,0f);
			
		}
		set=new AnimatorSet();
		set.playTogether(mAnimator1,mAnimator2);
		set.start();
		
}

	/**
	 * ����ˢ�¼����¼�
	 * 
	 * @param listener
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mOnRefershListener = listener;
	}

	/**
	 * ����ͷ����
	 */
	public void hideHeaderView() 
	{
		headerView.setPadding(0, paddingTopFirst, 0, 0);
		ivArrow.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		tvState.setText("����ˢ��");
//		tvLastUpdateTime.setText("���ˢ��ʱ��: " + getLastUpdateTime());
		currentState = DOWN_PULL_REFRESH;
	}

	public void hideFooterView() {
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		isLoadingMore = false;
	}
	

	
}
