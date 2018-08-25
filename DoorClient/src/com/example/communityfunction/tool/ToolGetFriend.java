package com.example.communityfunction.tool;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hyphenate.chat.EMClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import neo.door.cache.FriendCache;
import neo.door.usermanager.User;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

public class ToolGetFriend extends AsyncTask{

	private Context mContext;
	private List<User> friendList = new ArrayList<User>();

	/**
	 * 
	 * @param context
	 *            Activity
	 * @param showMessage
	 *            锟斤拷示锟斤拷息锟斤拷TextView
	 */
	public ToolGetFriend(Context context) {
		this.mContext=context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		String result = null;
		String reqMessage1 = null;
		JSONArray respMessage1 = null;
		try {
				/***
				 * 发送请求数据
				 */
				String requestMethod1 = null;
				String userId = "";
				requestMethod1 = Config.GetMyFriendMethod;
				JSONArray joa = new JSONArray();
				
				List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
				for(int i = 0 ; i < usernames.size() ; i++){
					JSONObject jo = new JSONObject();
					jo.put("username", usernames.get(i));
					joa.put(jo);
				}
				
				respMessage1 = WebUtil.getJsonByWeb(requestMethod1, joa);
				for(int i = 0 ; i < respMessage1.length() ; i++ ){
					User tem = new User(respMessage1.getJSONObject(i).getString("username"));
					tem.setImgPath(respMessage1.getJSONObject(i).getString("imgpath"));
					tem.setNickname(respMessage1.getJSONObject(i).getString("nickname"));
					friendList.add(tem);
				}
				FriendCache.writeCache(friendList);
				result = "Success";
		} catch(Exception e){
			result = "Error";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result.equals("Success")) {
			Toast.makeText(mContext, "获取到" + friendList.size() + "位好友", Toast.LENGTH_LONG).show();
			
		} else if (result.equals("Error!")) {
			Toast.makeText(mContext, "获取好友失败!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "获取好友失败", Toast.LENGTH_SHORT).show();
		}
	}
}
