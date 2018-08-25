package com.example.communityfunction.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communityfunction.pulltorefreshlistview.view.OnRefreshListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import neo.door.webutils.Config;

/*
 * 
 */
public class ToolPostBBS extends AsyncTask {

	private Activity mContext;
	private String responMessage;
	private OnPostBbsListener listener;

	public ToolPostBBS(Activity context) {
		this.mContext = context;
	}


	@Override
	protected Object doInBackground(Object... params) {
		String result = null;
		String userName = (String) params[0];
		String userPhone = (String) params[1];
		String title = (String) params[2];
		String content = (String) params[3];
		List<String> filePathList = (List<String>) params[4];
		List<File> files = new ArrayList<File>();
		try {
			for (int i = 0; i < filePathList.size(); i++) {
				File file = new File(filePathList.get(i));
				files.add(file);
			}
		} catch (Exception e) {
			result = "GetFileFailed";
		}
		try {
			responMessage = HttpAssist.uploadFile(Config.URL + Config.METHOD_POSTBBS, 
						userName, userPhone, title,content, files);
			if(responMessage.equals(HttpAssist.FAILURE)){
				result = HttpAssist.FAILURE;
			} else{
				JSONArray jsonArray = new JSONArray(responMessage);
				String returnResult = jsonArray.getJSONObject(0).getString("RESULT");
				String returnTime = jsonArray.getJSONObject(0).getString("TIME");
				String returnID = jsonArray.getJSONObject(0).getString("ID");
	
				responMessage = "Reuslt:" + returnResult + "\n" + "Time:" + returnTime + "\n" + "ID:" + returnID;
				result = HttpAssist.SUCCESS;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("e", e.toString());
			result = HttpAssist.FAILURE;
		}
		return result;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result.equals(HttpAssist.SUCCESS)) {
			Toast.makeText(mContext, "发贴成功!\n" + responMessage, Toast.LENGTH_LONG).show();
		} else if (result.equals(HttpAssist.TimeOut)) {
			Toast.makeText(mContext, "连接超时!", Toast.LENGTH_LONG).show();
		} else if (result.equals("GetFileFailed")) {
			Toast.makeText(mContext, "文件获取失败!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext, "发贴失败!", Toast.LENGTH_LONG).show();
		} 
		listener.getPostBbs();
	}
	
	public void setOnPostBbsListener(OnPostBbsListener listener)
	{
		this.listener=listener;
	}
	public interface OnPostBbsListener
	{
		void getPostBbs();
	}
}
