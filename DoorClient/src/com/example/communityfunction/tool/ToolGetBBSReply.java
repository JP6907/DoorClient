package com.example.communityfunction.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.communityfunction.informationclass.BBSReply;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

/**
	 * 锟斤拷取锟铰碉拷锟斤拷锟斤拷锟斤拷息
	 * 
	 * @author Administrator
	 *
	 */
	public class ToolGetBBSReply extends AsyncTask {


		private Activity mContext;
		private TextView mShowMessage;
		private ProgressDialog progressDialog;
		private int temid;
		List<BBSReply> BBSReplyList = new ArrayList<BBSReply>(); 
		private getBbsReplySucceedListener bbsSucceedListener;

		/**
		 * 
		 * @param context
		 *            Activity
		 * @param showMessage
		 *            锟斤拷示锟斤拷息锟斤拷TextView
		 */
		public ToolGetBBSReply(Activity context, TextView showMessage,int bbsid) {
			mContext = context;
			mShowMessage = showMessage;
			temid = bbsid;
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
			
			try {
				
				JSONObject jo = new JSONObject();
				jo.put("id", temid);
				JSONArray joa = new JSONArray();
				joa.put(jo);
				/*
				 * GetBBSReply
				 */
				String requestMethod = Config.GetBBSReplyDateMethod;
		
				respMessage = WebUtil.getJsonByWeb(requestMethod,joa);
				if(respMessage!=null)
				{
					for(int i = 0 ;i < respMessage.length() ; i++){
						BBSReply tem = new BBSReply();
						tem.setReplyDateTime(respMessage.getJSONObject(i).getString("datetime"));
						tem.setReplyFloor(respMessage.getJSONObject(i).getInt("floor"));
						tem.setReplyId(respMessage.getJSONObject(i).getLong("id"));
						tem.setReplyJudge(respMessage.getJSONObject(i).getInt("judge"));
						tem.setReplyPostId(respMessage.getJSONObject(i).getLong("postid"));
						tem.setReplyPublisher(respMessage.getJSONObject(i).getString("publisher"));
						tem.setReplyResponder(respMessage.getJSONObject(i).getString("responder"));
						tem.setReplyText(respMessage.getJSONObject(i).getString("text"));
						BBSReplyList.add(tem);
					}
					result="Success";
				}
				
				Log.e("aaaaaaaaaaaa", "dddddddddd");
			} catch (IOException e) {
				result = "Error!";
				e.printStackTrace();
			} catch (Exception e) {
				result = "Error!";
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result.equals("Success")) {
				Toast.makeText(mContext, "成功获取" + BBSReplyList.size() + "条信息", Toast.LENGTH_LONG).show();
				StringBuilder sb = new StringBuilder();
				sb.append(BBSReplyList.toString());
				//mShowMessage.setText(sb.toString() + "\n");	
				//mShowMessage.append(respMessage + "\n");
//				Log.e("ccccc", BBSReplyList.get(0).getReplyDateTime());
				if(BBSReplyList!=null)
				{
					Log.e("ccccc", "abcd");
					bbsSucceedListener.showBbsReply(BBSReplyList);
				}
			} else if (result.equals("Error!")) {
				Toast.makeText(mContext, "获取失败!", Toast.LENGTH_SHORT).show();
				//mShowMessage.setText(respMessage);
			}else
			{
				Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
			}
			// 鍙栨秷杩涘害锟�?
			progressDialog.cancel();
		}
		
		public void setOnGetBbsReplySucceedListener(getBbsReplySucceedListener bbsSucceedListener)
		{
			this.bbsSucceedListener=bbsSucceedListener;
			Log.e("aaaaa", "ww");
		}
		
		public interface getBbsReplySucceedListener
		{
			 void showBbsReply(List<BBSReply> BBSReplyList);
		}
		
		public List<BBSReply> getListBBSReply()
		{
			if(BBSReplyList==null)
				return null;
			return BBSReplyList;
		}

	}
