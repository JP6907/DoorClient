package com.example.communityfunction.tool;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communityfunction.adapter.BBSAdapter;
import com.example.communityfunction.adapter.BBSReplyAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

public class ToolPostBBSReply extends AsyncTask{

	private Activity mContext;
	private ProgressDialog progressDialog;
	JSONArray respArray = null;
	private OnUpLoadBbsReplyListener replyListener;
	
	public ToolPostBBSReply(Activity mContext){
		this.mContext = mContext;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.show();
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		
		Integer result = null;
		JSONArray reqArray;
		try {
			reqArray = new JSONArray();
			reqArray.put(new JSONObject().put("reply_postid", params[0])
										.put("reply_judge", params[1])
										.put("reply_responder", params[2])
										.put("reply_publisher", params[3])
										.put("reply_floor", params[4])
										.put("reply_text", params[5]));
			// 发送请求, 并接收服务器响应
			respArray = WebUtil.getJsonByWeb(Config.METHOD_BBSREPLY, reqArray);
			if(respArray != null){                            
				result = respArray.getJSONObject(0).getInt("ID");
			}
		} catch (JSONException e) {
			result = -2;
			e.printStackTrace();
		}catch (IOException e) {
			result = -1;
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		
		if(result.equals(-1)){
			Toast.makeText(mContext, "网络连接超时!", Toast.LENGTH_LONG).show();
		} else if(result.equals(-2)){
			Toast.makeText(mContext, "数据解析异常！", Toast.LENGTH_LONG).show();
		} else{
			Toast.makeText(mContext, "评论成功!\n Id为：" + result, Toast.LENGTH_LONG).show();
			replyListener.upLoadSuccess("Success");
		}
		progressDialog.cancel();
	}
	
	public void setOnUpLoadBbsReplyListener(OnUpLoadBbsReplyListener replyListener)
	{
		this.replyListener=replyListener;
	}
	
	public interface OnUpLoadBbsReplyListener
	{
		void upLoadSuccess(String result);
	}
}