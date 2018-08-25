package neo.door.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import neo.door.usermanager.UserManager;

public class MyViewPager extends ViewPager {
	private int mFirstX =0,mFirstY=0;
	private String TAG = "MyViewPager";

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent event) {
//		if(this.getCurrentItem()==2)//如果滑动到了第三个Fragment
//		{
//			boolean isIntercept=false;
//			int x= 0;
//			int y= 0;
//			try {
//				x = (int) event.getX();
//				y = (int) event.getY();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Log.e(TAG, "onInterceptTouchEvent");
//			switch (event.getAction()) {
//			/**
//			 * //父容器必须返回false，即不拦截ACTION_DOWN事件，
//			 * 否则后续的ACTION_MOVE，ACTION_UP事件都会直接交给父容器处理，
//			 * 事件没办法再传递给子元素了
//			 */
//			case MotionEvent.ACTION_DOWN:
//				Log.e(TAG, "onInterceptTouchEvent_ACTION_DOWN");
//				isIntercept=false;
//				break;
//				/**
//				 * 根据需要觉定是否拦截
//				 */
//			case MotionEvent.ACTION_MOVE:
//				if (Math.abs(x - mFirstX) > Math.abs(y - mFirstY)) //左右滑动
//				{
//						isIntercept = true;
//						if(UserManager.isScroll())
//							isIntercept=false;
//						Log.e(TAG, ""+UserManager.isScroll());
//				} 
//				else 
//				{
//						isIntercept = false;
//				}
//				Log.e(TAG, "onInterceptTouchEvent_ACTION_MOVE");
//				break;
//				/**
//				 * 必须返回false，因为ACTION_UP本身没有太大意义
//				 */
//			case MotionEvent.ACTION_UP:
//				isIntercept=false;
//				Log.e(TAG, "onInterceptTouchEvent_ACTION_UP");
//				break;
//			default:
//				break;
//			}
//
//			mFirstX=x;
//			mFirstY=y;
//			Log.e(TAG, "onInterceptTouchEvent_return");
//			return isIntercept;
//		}
//		else
//			return super.onInterceptTouchEvent(event);
//	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
