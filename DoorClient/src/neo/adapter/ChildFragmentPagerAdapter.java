package neo.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

public class ChildFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> fragmentsList;

	public ChildFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public ChildFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragmentsList = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentsList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentsList.size();
	}
//	@Override  
//
//	  public int getItemPosition(Object object) { 
//
//	  return PagerAdapter.POSITION_NONE;  
//
//	 }

}
