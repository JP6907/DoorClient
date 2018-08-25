package neo.door.main;

import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class MyBaseFragmentActivity extends FragmentActivity
{
	private String TAG="MyBaseFragmentActivity";
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Log.e("ss",""+resultCode);
		Log.e("ss",""+requestCode);
		if(resultCode==10)
			return;
		super.onActivityResult(requestCode, resultCode, data);
		FragmentManager fragmentManager=getSupportFragmentManager();
		for(int indext=0;indext<fragmentManager.getFragments().size();indext++)
		{
			Fragment fragment=fragmentManager.getFragments().get(indext);
			if(fragment==null)
				 Log.w(TAG, "Activity result no fragment exists for index: 0x"  
	                     + Integer.toHexString(requestCode));  
			else 
				handleResult(fragment,requestCode,resultCode,data);
		}
	}
	/**
	 * 递归调用，对所有的子Fragment生效
	 * @param fragment
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) 
	{
		fragment.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "MyBaseFragmentActivity");
		List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); 
		if(childFragment!=null)
			for(Fragment f:childFragment)
				if(f!=null)
				{
					handleResult(f, requestCode, resultCode, data);
				}
		if(childFragment==null)
			Log.e(TAG, "MyBaseFragmentActivity1111");
	}

}
