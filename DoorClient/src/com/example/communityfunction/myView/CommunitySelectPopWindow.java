package com.example.communityfunction.myView;

import java.lang.reflect.Method;

import com.neo.huikaimen.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import neo.door.utils.MyToast;
import neo.door.utils.ScreenUtil;

public class CommunitySelectPopWindow extends PopupWindow implements OnClickListener
{
	private Context mContext;
	private int mWidth;
	private int mHeight;
	private View mConvertView;
	private MyToast mToast;
	
	private LinearLayout btnForum;
	private LinearLayout btnAnnouncement;
	private LinearLayout btnHousekeeping;
	private LinearLayout btnFit;
	private LinearLayout btnFood;
	private LinearLayout btnOther;
	
	private OnstartActivityListener mActivityListener;
	
	public CommunitySelectPopWindow(Context context) 
	{
		this.mContext=context;
		
		mConvertView=LayoutInflater.from(mContext).inflate(R.layout.community_pop_view, null);
		
		setContentView(mConvertView);
		
		calWidthAndHeight();
		setWidth(mWidth);
		setHeight(mHeight);
		
		setFocusable(true);
		setTouchable(true);
		
		setPopupWindowTouchModal(this, false);
		
		initView();
	}

	private void initView() 
	{
		mToast = new MyToast(mContext.getApplicationContext());
		
		btnForum = (LinearLayout) mConvertView.findViewById(R.id.btn_community_forum);
		btnAnnouncement = (LinearLayout) mConvertView.findViewById(R.id.btn_community_announcement);
		btnHousekeeping = (LinearLayout) mConvertView.findViewById(R.id.btn_community_housekeeping);
		btnFit = (LinearLayout) mConvertView.findViewById(R.id.btn_community_fit);
		btnFood = (LinearLayout) mConvertView.findViewById(R.id.btn_community_food);
		btnOther = (LinearLayout) mConvertView.findViewById(R.id.btn_community_other);
		
		btnForum.setOnClickListener((android.view.View.OnClickListener) this);
		btnAnnouncement.setOnClickListener(this);
		btnHousekeeping.setOnClickListener(this);
		btnFit.setOnClickListener(this);
		btnFood.setOnClickListener(this);
		btnOther.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (mConvertView.getId()) {

		case R.id.btn_community_forum:
			mActivityListener.startAnotherActivity(R.id.btn_community_forum);
			break;
		case R.id.btn_community_announcement:
			mActivityListener.startAnotherActivity(R.id.btn_community_announcement);
			break;
		case R.id.btn_community_housekeeping:
			mActivityListener.startAnotherActivity(R.id.btn_community_housekeeping);
			break;
		case R.id.btn_community_fit:
			mActivityListener.startAnotherActivity( R.id.btn_community_fit);
			break;
		case R.id.btn_community_food:
			mActivityListener.startAnotherActivity(R.id.btn_community_food);
			break;
		case R.id.btn_community_other:
			mActivityListener.startAnotherActivity(R.id.btn_community_other);
			break;
		default:
			break;
		}
		
	}


	/**
	 * ����popWindow�Ŀ�͸�
	 */
	private void calWidthAndHeight() 
	{
		mWidth=ScreenUtil.getScreenSize(mContext).widthPixels;
		mHeight=160;
	}
	
	/**
	 * ����ⲿ����,��������ʧ,���ǵ���¼����������activity����,Ҫ��Window����һ��Flag��
����     * WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
����     * ����Դ�룬���Flag�������������һ����mNotTouchModal���ֶο��ƣ��������ø��ֶε�set���������Ϊ@hide��
����     * ����Ҫͨ������ķ������ã�
     * Set whether this window is touch modal or if outside touches will be sent
     * to
     * other windows behind it.
     *
     */
    public static void setPopupWindowTouchModal(PopupWindow popupWindow,
            boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setOnStartActivityListener(OnstartActivityListener activityListener)
    {
    	this.mActivityListener=activityListener;
    }
    public interface OnstartActivityListener
    {
    	void startAnotherActivity(int id);
    }

}
