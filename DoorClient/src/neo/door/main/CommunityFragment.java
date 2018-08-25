package neo.door.main;

import java.util.ArrayList;

import com.example.communityfunction.bbs.BbsFragment;
import com.example.communityfunction.fix.FixFragment;
import com.example.communityfunction.food.FoodFragment;
import com.example.communityfunction.housekeeping.HouseFragment;
import com.example.communityfunction.notice.NoticeFragment;
import com.neo.huikaimen.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import neo.adapter.ChildFragmentPagerAdapter;
import neo.door.utils.MyToast;

public class CommunityFragment extends Fragment implements OnClickListener{

	private ArrayList<Fragment> childFragmentsList;
	private View view;
	private View line0,line1,line2,line3,line4,line5;
	private ImageView bbsImage,announcementImage,housekeepingImage,fitImage,foodImage,otherImage;
	private int currentIndex=0;
	private MyToast mToast;
	private ViewPager pager;
	LinearLayout linearView;//包含按钮的布局
	LinearLayout btnForum;
	LinearLayout btnAnnouncement;
	LinearLayout btnHousekeeping;
	LinearLayout btnFit;
	LinearLayout btnFood;
	LinearLayout btnOther;
	
	private Resources res;
	
	private Button btnSearch;
	private ImageView ivDeleteText;
	private EditText etSearch;
	private InputMethodManager imm;
	
	private ObjectAnimator mAnimator1, mAnimator2;
	private AnimatorSet set;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_community, container, false);
		pager=(ViewPager)view.findViewById(R.id.vPagerChildFragment);
		initView();
		initSearchBar();
		

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init();

	}


	private void initView()
	{
		
		btnSearch=(Button)view.findViewById(R.id.btnSearch);
		ivDeleteText = (ImageView) view.findViewById(R.id.ivDeleteText);
		etSearch = (EditText) view.findViewById(R.id.etSearch);
		
		pager=(ViewPager)view.findViewById(R.id.vPagerChildFragment);
        childFragmentsList=new ArrayList<Fragment>();
		
		Fragment bbsFragment=new BbsFragment();
		Fragment noticeFragment=new NoticeFragment();
		Fragment houseFragment = new HouseFragment();
		Fragment fixFragment = new FixFragment();
		Fragment foodFragment = new FoodFragment();
		
		childFragmentsList.add(bbsFragment);
		childFragmentsList.add(noticeFragment);
		childFragmentsList.add(houseFragment);
		childFragmentsList.add(fixFragment);
		childFragmentsList.add(foodFragment);
		pager.setAdapter(new ChildFragmentPagerAdapter(getChildFragmentManager(),childFragmentsList));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private class MyOnPageChangeListener implements OnPageChangeListener 
	{

		@Override
		public void onPageScrollStateChanged(int arg0) 
		{
			if(arg0==2)
				resetlinearView();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageSelected(int arg0) {
			resetlinearView();
			switch (arg0) 
			{
			case 0:
				clearBtn(currentIndex);
				line0.setBackgroundColor(res.getColor(R.color.green));
				bbsImage.setImageResource(R.drawable.icon_bbs_white);
				btnForum.setBackgroundColor(res.getColor(R.color.little_blue));
				break;
			case 1:
				clearBtn(currentIndex);//根据currentIndex来判断上次处于哪一个界面以此来进行清除按钮颜色。
				line1.setBackgroundColor(res.getColor(R.color.green));
				announcementImage.setImageResource(R.drawable.icon_announcement_white);
				btnAnnouncement.setBackgroundColor(res.getColor(R.color.little_blue));
				break;
			case 2:
				clearBtn(currentIndex);
				line2.setBackgroundColor(res.getColor(R.color.green));
				housekeepingImage.setImageResource(R.drawable.icon_housekeeping_white);
				btnHousekeeping.setBackgroundColor(res.getColor(R.color.little_blue));
				break;
			case 3:
				clearBtn(currentIndex);
				line3.setBackgroundColor(res.getColor(R.color.green));
				fitImage.setImageResource(R.drawable.icon_fit_white);
				btnFit.setBackgroundColor(res.getColor(R.color.little_blue));
				break;
			case 4:
				clearBtn(currentIndex);
				line4.setBackgroundColor(res.getColor(R.color.green));
				foodImage.setImageResource(R.drawable.icon_food_white);
				btnFood.setBackgroundColor(res.getColor(R.color.little_blue));
				break;
			case 5:
				clearBtn(currentIndex);
				line5.setBackgroundColor(res.getColor(R.color.green));
				otherImage.setImageResource(R.drawable.icon_food_white);
				btnOther.setBackgroundColor(res.getColor(R.color.little_blue));
				mToast.show("请期待", Toast.LENGTH_SHORT);
				break;
			default:
				break;
			}
			currentIndex=arg0;
		}
		
	}
	/**
	 * 搜索栏控件
	 */
	private void initSearchBar() {
		//删除
		ivDeleteText.setOnClickListener(new OnClickListener() 
		{

			public void onClick(View v) 
			{
				etSearch.setText("");
			}
		});

		imm = (InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Toast.makeText(getActivity().getApplicationContext(), "还未实现搜索功能", Toast.LENGTH_SHORT).show();
				(new Handler()).postDelayed(new Runnable() {
					public void run() {

						imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
					}
				}, 100);
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {

					ivDeleteText.setVisibility(View.GONE);

				} else {

					ivDeleteText.setVisibility(View.VISIBLE);

				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}
		});

	}



	private void init() {
		mToast = new MyToast(getActivity().getApplicationContext());
		
		res=getResources();

		linearView=(LinearLayout)getActivity().findViewById(R.id.liner);
		btnForum = (LinearLayout) getActivity().findViewById(R.id.btn_community_forum);
		btnAnnouncement = (LinearLayout) getActivity().findViewById(R.id.btn_community_announcement);
		btnHousekeeping = (LinearLayout) getActivity().findViewById(R.id.btn_community_housekeeping);
		btnFit = (LinearLayout) getActivity().findViewById(R.id.btn_community_fit);
		btnFood = (LinearLayout) getActivity().findViewById(R.id.btn_community_food);
		btnOther = (LinearLayout) getActivity().findViewById(R.id.btn_community_other);

		btnForum.setOnClickListener(this);
		btnAnnouncement.setOnClickListener(this);
		btnHousekeeping.setOnClickListener(this);
		btnFit.setOnClickListener(this);
		btnFood.setOnClickListener(this);
		btnOther.setOnClickListener(this);
		
		line0=view.findViewById(R.id.bbs_line0);
		line1=view.findViewById(R.id.announcement_line1);
		line2=view.findViewById(R.id.housekeeping_line2);
		line3=view.findViewById(R.id.fit_line3);
		line4=view.findViewById(R.id.food_line4);
		line5=view.findViewById(R.id.other_line5);
		
		bbsImage=(ImageView) view.findViewById(R.id.bbs_image);
		announcementImage=(ImageView) view.findViewById(R.id.announcement_image);
		housekeepingImage=(ImageView) view.findViewById(R.id.housekeeping_image);
		fitImage=(ImageView) view.findViewById(R.id.fit_image);
		foodImage=(ImageView) view.findViewById(R.id.food_image);
		otherImage=(ImageView) view.findViewById(R.id.other_image);
		
		line0.setBackgroundColor(res.getColor(R.color.green));
		bbsImage.setImageResource(R.drawable.icon_bbs_white);
		btnForum.setBackgroundColor(res.getColor(R.color.little_blue));
	}


	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.btn_community_forum:
				pager.setCurrentItem(0);
			break;

		case R.id.btn_community_announcement:
				pager.setCurrentItem(1);
			break;

		case R.id.btn_community_housekeeping:
				pager.setCurrentItem(2);
			break;

		case R.id.btn_community_fit:
				pager.setCurrentItem(3);
			break;

		case R.id.btn_community_food:
				pager.setCurrentItem(4);
			break;

		case R.id.btn_community_other:
			pager.setCurrentItem(5);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 清除按钮颜色
	 * @param currentIndex
	 */
	@SuppressLint("ResourceAsColor")
	private void clearBtn(int currentIndex)
	{
		switch (currentIndex) 
		{
		case 0:
			line0.setBackgroundColor(res.getColor(R.color.divider_line));
			bbsImage.setImageResource(R.drawable.icon_bbs_blue);
			btnForum.setBackgroundColor(res.getColor(R.color.theme_color));
			break;
		case 1:
			line1.setBackgroundColor(res.getColor(R.color.divider_line));
			announcementImage.setImageResource(R.drawable.icon_sound_blue);
			btnAnnouncement.setBackgroundColor(res.getColor(R.color.theme_color));
			break;
		case 2:
			line2.setBackgroundColor(res.getColor(R.color.divider_line));
			housekeepingImage.setImageResource(R.drawable.icon_service_blue);
			btnHousekeeping.setBackgroundColor(res.getColor(R.color.theme_color));
			break;
		case 3:
			line3.setBackgroundColor(res.getColor(R.color.divider_line));
			fitImage.setImageResource(R.drawable.icon_fit_blue);
			btnFit.setBackgroundColor(res.getColor(R.color.theme_color));
			break;
		case 4:
			line4.setBackgroundColor(res.getColor(R.color.divider_line));
			foodImage.setImageResource(R.drawable.icon_food_blue);
			btnFood.setBackgroundColor(res.getColor(R.color.theme_color));
			break;
		case 5:
			line5.setBackgroundColor(R.color.divider_line);
			otherImage.setImageResource(R.drawable.icon_bbs_white);
			btnOther.setBackgroundColor(res.getColor(R.color.theme_color));
			break;
		default:
			break;
		}
	}
	/**
	 * 滑到另一个界面的时候恢复被隐藏的布局
	 */
	public void resetlinearView()
    {
		if(linearView.getTranslationY()==0)//判断布局是否滑离原来的位置
			return;
		if(set!=null && set.isRunning())
		{
			set.cancel();
		}
		mAnimator1 = ObjectAnimator.ofFloat(linearView, "translationY", linearView.getTranslationY(), 0);
		mAnimator2 = ObjectAnimator.ofFloat(linearView, "alpha", 0f, 1f);
		set = new AnimatorSet();
		set.playTogether(mAnimator1, mAnimator2);
		set.start();
	}

	}



	

