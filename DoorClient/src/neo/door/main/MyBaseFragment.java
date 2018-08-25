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
	private float mFirstY;//listView����ʱ����ʼ����
	private float mCurrentY;//listView�������ĵ�ǰ����
	private float mTouchSlop;
	private int direction;//��������
	private int searchBarHeight;//������ĸ߶�
	private boolean mShow=true;//�Ƿ�չʾ�Ϸ���View
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
	 * �жϵ�ǰ��ͼ�Ƿ�ɼ������ڸոս�ȥ����ˢ�£�
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
	protected abstract void loadData();//��������
		
	/**
	 * ���»���ʱ���غ���ʾ�Ϸ������
	 * @param linearView
	 * @param listView
	 */
	@SuppressLint("ClickableViewAccessibility")
	protected void  showOrGone(final LinearLayout linearView,final View searchBar, ListView listView)
	{
		mTouchSlop=ViewConfiguration.get(getActivity()).getScaledTouchSlop();//ϵͳ��Ϊ����ͻ�������
		
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
						direction=0;//listView���»���
					else if(mFirstY-mCurrentY>mTouchSlop)
						direction=1;//listView���ϻ���
					if(direction==1)
					{
						if(mShow)
						{
							LinearViewAnim(1,linearView);//�����Ϸ���view
							mShow=!mShow;
						}
					}
					else if(direction==0)
					{
						if(!mShow)
						{
							LinearViewAnim(0,linearView);//չʾ�Ϸ���view
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
//					direction=1;//listView���Ϲ���
//				else
//					direction=0;//listView���¹���
//				
//				lastVisibleItemPosition=firstVisibleItem;
//				if (direction == 1) 
//				{
//					if (mShow) 
//					{
//						LinearViewAnim(1, linearView);// �����Ϸ���view
//						mShow = !mShow;
//					}
//				} 
//				else if (direction == 0) 
//				{
//					if (!mShow) 
//					{
//						LinearViewAnim(0, linearView);// չʾ�Ϸ���view
//						mShow = !mShow;
//					}
//				}
//			}
//		});
//	}
	
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
					"translationY", linearView.getTranslationY(),-(linearView.getHeight()+searchBarHeight));
			mAnimator2=ObjectAnimator.ofFloat(linearView, "alpha", 1f,0f);
			
		}
		set=new AnimatorSet();
		set.playTogether(mAnimator1,mAnimator2);
		set.start();
		
}

}
