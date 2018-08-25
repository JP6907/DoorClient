package neo.adapter;

import android.support.v4.app.Fragment;

/**
 * 延时加载Fragment
 */
public abstract class BaseFragment extends Fragment{
	
	protected boolean isVisible;
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(getUserVisibleHint()) {            // 如果可见
			isVisible = true;
			onVisible();
		}else {
			isVisible = false;
			onInvisible();
		}
	}

	private void onInvisible() {
		
	}

	private void onVisible() {
		lazyLoad();
	}
	
	/**
	 * 延时加载
	 */
	protected abstract void lazyLoad();
}
