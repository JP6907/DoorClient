package neo.door.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public abstract class MyBaseFragment extends Fragment 
{
	protected boolean isVisible = false;
	private float mFirstY;//listView滑动时的起始坐标
	private float mCurrentY;//listView滑动到的当前坐标
	private float mTouchSlop;
	private int direction;//滑动方向
	private int searchBarHeight;//搜索框的高度
	private boolean mShow=true;//是否展示上方的View
	private ObjectAnimator mAnimator1, mAnimator2;
	private AnimatorSet set;
	
	private String TAG="MyBaseFragment";
	
//	@Override
//	public void onCreate(Bundle savedInstanceState) 
//	{
//		
//		super.onCreate(savedInstanceState);
//	}

	/**
	 * 判断当前视图是否可见（用于刚刚进去界面刷新）
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) 
	{
		Log.e(TAG, "setUserVisibleHint");
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint())
		{
			Log.e(TAG, "isVisible=true;");
			isVisible=true;
			onVisible();
		}
		else
		{
			Log.e(TAG, "isVisible=false;");
			isVisible=false;
			inVisible();
		}

	}

	private void inVisible() 
	{
		
	}

	private void onVisible() 
	{
		loadData();
	}
	protected abstract void loadData();//加载数据
		
	/**
	 * 上下滑动时隐藏和显示上方的组件
	 * @param linearView
	 * @param listView
	 */
	@SuppressLint("ClickableViewAccessibility")
	protected void  showOrGone(final LinearLayout linearView,final View searchBar, ListView listView)
	{
		mTouchSlop=ViewConfiguration.get(getActivity()).getScaledTouchSlop();//系统认为的最低滑动距离
		
		searchBarHeight=searchBar.getLayoutParams().height;
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) 
				{
				case MotionEvent.ACTION_DOWN:
					mFirstY=event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					mCurrentY=event.getY();
					if(mCurrentY-mFirstY>mTouchSlop)
						direction=0;//listView向下滑动
					else if(mFirstY-mCurrentY>mTouchSlop)
						direction=1;//listView向上滑动
					if(direction==1)
					{
						if(mShow)
						{
							LinearViewAnim(1,linearView);//隐藏上方的view
							mShow=!mShow;
						}
					}
					else if(direction==0)
					{
						if(!mShow)
						{
							LinearViewAnim(0,linearView);//展示上方的view
							mShow=!mShow;
						}
					}
					
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
	}
//	protected void  showOrGone(final LinearLayout linearView,final View searchBar, ListView listView)
//	{
//		listView.setOnScrollListener(new OnScrollListener() 
//		{
//			
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) 
//			{
//				
//			}
//			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
//			{
//				Log.e("asasasas", "{}{}");
//				int lastVisibleItemPosition = 0;
//				if(firstVisibleItem>lastVisibleItemPosition)
//					direction=1;//listView向上滚动
//				else
//					direction=0;//listView向下滚动
//				
//				lastVisibleItemPosition=firstVisibleItem;
//				if (direction == 1) 
//				{
//					if (mShow) 
//					{
//						LinearViewAnim(1, linearView);// 隐藏上方的view
//						mShow = !mShow;
//					}
//				} 
//				else if (direction == 0) 
//				{
//					if (!mShow) 
//					{
//						LinearViewAnim(0, linearView);// 展示上方的view
//						mShow = !mShow;
//					}
//				}
//			}
//		});
//	}
	
	/**
	 * 设置显示还有隐藏动画
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
					"translationY", linearView.getTranslationY(),-(linearView.getHeight()+searchBarHeight));
			mAnimator2=ObjectAnimator.ofFloat(linearView, "alpha", 1f,0f);
			
		}
		set=new AnimatorSet();
		set.playTogether(mAnimator1,mAnimator2);
		set.start();
		
}

}
