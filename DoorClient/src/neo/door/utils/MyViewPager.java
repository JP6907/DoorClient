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
//		if(this.getCurrentItem()==2)//����������˵�����Fragment
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
//			 * //���������뷵��false����������ACTION_DOWN�¼���
//			 * ���������ACTION_MOVE��ACTION_UP�¼�����ֱ�ӽ�������������
//			 * �¼�û�취�ٴ��ݸ���Ԫ����
//			 */
//			case MotionEvent.ACTION_DOWN:
//				Log.e(TAG, "onInterceptTouchEvent_ACTION_DOWN");
//				isIntercept=false;
//				break;
//				/**
//				 * ������Ҫ�����Ƿ�����
//				 */
//			case MotionEvent.ACTION_MOVE:
//				if (Math.abs(x - mFirstX) > Math.abs(y - mFirstY)) //���һ���
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
//				 * ���뷵��false����ΪACTION_UP����û��̫������
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
