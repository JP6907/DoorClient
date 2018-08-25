package com.example.communityfunction.tool;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hyphenate.chat.EMClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import neo.door.cache.FriendCache;
import neo.door.usermanager.User;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

public class ToolQueryUser extends AsyncTask {


	private Activity mContext;
	private ProgressDialog progressDialog;
	private getUserSucceedListener userSucceedListener;
	private User queriedUser = new User("");
	private String TAG="ToolQueryUser";
	/**
	 * 
	 * @param context
	 *            Activity
	 * @param showMessage
	 *            TextView
	 */
	public ToolQueryUser(Activity context) {
		mContext = context;
	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.show();
	}

	@Override
	protected Object doInBackground(Object... params) {
		String result = null;
		JSONArray respMessage = null;
		String username = (String) params[0];
		try {
			JSONObject jo = new JSONObject();
			jo.put("username", username);
			JSONArray joa = new JSONArray();
			joa.put(jo);
			/*
			 * QueryUserId
			 */
			String requestMethod = Config.QueryUser;
			respMessage = WebUtil.getJsonByWeb(requestMethod,joa);
			queriedUser.setImgPath(respMessage.getJSONObject(0).getString("imgpath"));
			queriedUser.setUserName(respMessage.getJSONObject(0).getString("username"));
			queriedUser.setJudge(judgeFriend(username));
			
			result = "Success";
			} catch (Exception e){
				result = "error";
				e.printStackTrace();
			}
		return result; 
		}
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result.equals("Success")) {
			Toast.makeText(mContext, "获取到名片信息" , Toast.LENGTH_LONG).show();
		} else if (result.equals("Error!")) {
			Toast.makeText(mContext, "获取名片信息失败!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "获取名片信息失败", Toast.LENGTH_SHORT).show();
		}
		progressDialog.dismiss();
		userSucceedListener.showUser(queriedUser);
	}

	public void setOnUserSucceedListener(getUserSucceedListener userSucceedListener) {
		this.userSucceedListener = userSucceedListener;
	}

	public interface getUserSucceedListener {
		void showUser(User queriedUser);
	}

	public User getUser() {
		if (queriedUser == null)
			return null;
		return queriedUser;
	}
	
	private int judgeFriend(String aim)
	{
		if(EMClient.getInstance().getCurrentUser().equals(aim))
			return -1;
		List<User> list = null;
		try {
			list = FriendCache.readCache();
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for(int i = 0 ; i < list.size() ; i++)
		{
			if(list.get(i).getUsername().equals(queriedUser.getUsername())) 
			{
				return 1;
			}
		}
		return 0;
	}
}
