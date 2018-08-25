package neo.adapter;

import android.support.v4.app.Fragment;

/**
 * ��ʱ����Fragment
 */
public abstract class BaseFragment extends Fragment{
	
	protected boolean isVisible;
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(getUserVisibleHint()) {            // ����ɼ�
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
	 * ��ʱ����
	 */
	protected abstract void lazyLoad();
}
