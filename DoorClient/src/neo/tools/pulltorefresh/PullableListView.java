package neo.tools.pulltorefresh;

import com.example.communityfunction.pulltorefreshlistview.view.OnRefreshListener;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 支持上下拉刷新的listView
 */
public class PullableListView extends ListView implements Pullable
{
	public static final int INIT = 0;
	public static final int LOADING = 1;
	public static final int NO_MORE_DATA = 2;
	private OnRefreshListener mRefreshListener;
	private View mLoadmoreView;
	private ImageView mLoadingView;
	private TextView mStateTextView;
	private int state = INIT;
	private boolean canLoad = true;
	private boolean autoLoad = true;
	private boolean hasMoreData = true;
	private AnimationDrawable mLoadAnim;
	
	private boolean isCanPullDown = true;

	public PullableListView(Context context)
	{
		super(context);
		init(context);
	}

	public PullableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context)
	{
		mLoadmoreView = LayoutInflater.from(context).inflate(R.layout.footer_view,null);    // 加载下滑的view
		mLoadingView = (ImageView) mLoadmoreView.findViewById(R.id.loading_icon);           // 图片
		mLoadingView.setBackgroundResource(R.anim.loading_anim);                       		// 设置动画
		mLoadAnim = (AnimationDrawable) mLoadingView.getBackground();
		mStateTextView = (TextView) mLoadmoreView.findViewById(R.id.loadstate_tv);
		mLoadmoreView.setOnClickListener(new OnClickListener()
		{		
			@Override
			public void onClick(View v)
			{
				// 点击加载
				if(state != LOADING && hasMoreData){           // 处于非加载状态, 并有更多数据
					load();
				}
			}
		});
		addFooterView(mLoadmoreView, null, false);             // 添加下面的view
	}
	
	/**
	 * 是否开启自动加载
	 * @param enable true启用，false禁用
	 */
	public void enableAutoLoad(boolean enable){
		autoLoad = enable;
	}
	
	/**
	 * 是否显示加载更多
	 * @param v true显示，false不显示
	 */
	public void setLoadmoreVisible(boolean v){
		if(v){
			if (getFooterViewsCount() == 0) {
				addFooterView(mLoadmoreView, null, false);
			}
		}
		else {
			removeFooterView(mLoadmoreView);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked()){
		
		case MotionEvent.ACTION_DOWN:
			// 按下的时候禁止自动加载
			canLoad = false;
			break;
		case MotionEvent.ACTION_UP:
			// 松开手判断是否自动加载
			canLoad = true;
			checkLoad();
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		// 在滚动中判断是否满足自动加载条件
		checkLoad();
	}

	/**
	 * 判断是否满足自动加载条件
	 */
	private void checkLoad()
	{
		if (reachBottom() 
			&& mRefreshListener != null 
			&& state != LOADING
			&& canLoad 
			&& autoLoad 
			&& hasMoreData)
			
			load();
	}
	
	/**
	 * 加载数据
	 */
	private void load(){
		changeState(LOADING); 
		mRefreshListener.onLoadingMore();           // 具体类实现怎么加载数据
	}

	/**
	 * 改变状态
	 * @param state 状态
	 */
	private void changeState(int state)
	{
		this.state = state;
		switch (state)
		{
		case INIT:
			mLoadAnim.stop();
			mLoadingView.setVisibility(View.INVISIBLE);
			mStateTextView.setText(R.string.more);
			break;

		case LOADING:
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadAnim.start();
			mStateTextView.setText(R.string.loading);
			break;
			
		case NO_MORE_DATA:
			mLoadAnim.stop();
			removeFooterView(mLoadmoreView);
			break;
		}
	}

	/**
	 * 完成加载
	 */
	public void finishLoading()
	{
		changeState(INIT);
	}

	
	/**
	 * 设置加载监听器
	 * @param listener 监听器
	 */
	public void setOnLoadListener(OnRefreshListener listener)
	{
		this.mRefreshListener = listener;
	}

	/**
	 * 是否到达底部
	 * @return footerview可见时返回true，否则返回false
	 */
	private boolean reachBottom()
	{
		if (getCount() == 0){
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))      // 到达底部
		{
			// 滑到底部，且頂部不是第0个，也就是说item数超过一屏后才能自动加载，否则只能点击加载
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt( getLastVisiblePosition() - getFirstVisiblePosition()).getTop() < getMeasuredHeight() 
					&& !canPullDown())
				return true;
		}
		return false;
	}

	/**
	 * 是否有更多的加载数据
	 * @return
	 */
	public boolean isHasMoreData() {
		return hasMoreData;
	}

	/**
	 * 设置是否还有更多的加载数据
	 * @param hasMoreData
	 */
	public void setHasMoreData(boolean hasMoreData) {
		this.hasMoreData = hasMoreData;
		if (!hasMoreData) {
			changeState(NO_MORE_DATA);
		}else{
			changeState(INIT);
		}
	}

	public interface OnLoadListener
	{
		/**
		 * 下滑刷新
		 * @param pullableListView 支持下滑刷新的listView
		 */
		void onLoad(PullableListView pullableListView);
	}
	
	@Override
	public boolean canPullDown()
	{
		if(isCanPullDown){                     // 可以下拉刷新
			if (getCount() == 0){
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0){
				// 滑到ListView的顶部了
				return true;
			} else
				return false;
		}else{
			return false;                      // 不可下拉刷新
		}
	}
	
	

	@Override
	public boolean canPullUp() {
		
		return false;
	}
	
	public void setCanPullDown(boolean b){
		isCanPullDown = b;
	}
}
