package com.example.communityfunction.myView;

import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImgViewPagerAdapter extends PagerAdapter
{
	private List<Bitmap> imgBm;
	private Context mContext;
	private ViewPager pager;
	
	public ImgViewPagerAdapter(Context context,List<Bitmap>imgBm,ViewPager pager)
	{
		this.imgBm=imgBm;
		this.mContext=context;
		this.pager=pager;
	}
	@Override
	public int getCount() 
	{
		return imgBm.size();
	}
	@Override
	public Object instantiateItem(final ViewGroup container, int position) 
	{
		ImageView imageView=new ImageView(mContext);
		imageView.setImageBitmap(imgBm.get(position));
		container.addView(imageView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		imageView.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{

				startAnim();
			}
		});
		return imageView;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		container.removeView((View)object);
	}; 

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) 
	{
		return arg0==arg1;
	}
	public void startAnim()
	{
		AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(pager, "alpha", 1.0f,0f))
        .with(ObjectAnimator.ofFloat(pager, "scaleX",1.0f,0f))
        .with(ObjectAnimator.ofFloat(pager, "scaleY",1.0f,0f));
        animSet.setDuration(400);
        animSet.start();
        animSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}
			@Override
			public void onAnimationRepeat(Animator animation) {}
			@Override
			public void onAnimationEnd(Animator animation) 
			{
				pager.setVisibility(View.GONE);
			}
			@Override
			public void onAnimationCancel(Animator animation){}
		});
	}

}
