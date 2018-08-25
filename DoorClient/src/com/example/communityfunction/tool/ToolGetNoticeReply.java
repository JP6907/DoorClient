package com.example.communityfunction.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.communityfunction.informationclass.NoticeComment;
import com.example.communityfunction.informationclass.NoticeReply;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

/**
	 * ��ȡ�µ�������Ϣ
	 * 
	 * @author Administrator
	 *
	 */
public class ToolGetNoticeReply extends AsyncTask {


		private Activity mContext;
		private TextView mShowMessage;
		private ProgressDialog progressDialog;
		private int temid;
		private List<NoticeReply> NoticeReplyList = new ArrayList<NoticeReply>();
		
		private getNoticeReplySucceedListener noticeReplyListener;

		/**
		 * 
		 * @param context
		 *            Activity
		 * @param showMessage
		 *            TextView
		 */
		public ToolGetNoticeReply(Activity context, TextView showMessage,int bbsid) {
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
			List<NoticeReply> temlist = new ArrayList<NoticeReply>(); 
			try {
				
				JSONObject jo = new JSONObject();
				jo.put("id", temid);
				JSONArray joa = new JSONArray();
				joa.put(jo);
				/*
				 * GetNoticeReply
				 */
				String requestMethod = Config.GetNoticeReplyMethod;
		
				respMessage = WebUtil.getJsonByWeb(requestMethod,joa);
				
				for(int i = 0 ;i < respMessage.length() ; i++){
					NoticeReply tem = new NoticeReply();
					tem.setReplyDateTime(respMessage.getJSONObject(i).getString("datetime"));
					tem.setReplyFloor(respMessage.getJSONObject(i).getInt("floor"));
					tem.setReplyId(respMessage.getJSONObject(i).getLong("id"));
					tem.setReplyJudge(respMessage.getJSONObject(i).getInt("judge"));
					tem.setReplyNoticeId(respMessage.getJSONObject(i).getLong("noticeid"));
					tem.setReplyPublisher(respMessage.getJSONObject(i).getString("publisher"));
					tem.setReplyResponder(respMessage.getJSONObject(i).getString("responder"));
					tem.setReplyText(respMessage.getJSONObject(i).getString("text"));
					temlist.add(tem);
				}
				/**
				 * 排序
				 */
				for(int i = temlist.size()-1 ; i >=0 ; i--){
					NoticeReply tem = temlist.get(i);
					if(tem.getReplyJudge() == 1){
						NoticeReplyList.add((int)tem.getReplyId(),tem);
					}
					else{
						NoticeReply ltem = NoticeReplyList.get(tem.getReplyFloor()-1);
						NoticeComment comment = new NoticeComment();
						comment.setId(tem.getReplyId());
						comment.setDatetime(tem.getReplyDateTime());
						comment.setComentName(tem.getReplyResponder());
						comment.setReplyName(tem.getReplyPublisher());
						comment.setCommentContent(tem.getReplyText());
						ltem.addcommentlist(comment);
						NoticeReplyList.add(tem.getReplyFloor()-1,ltem);
					}
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

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result.equals("Success")) {
				Toast.makeText(mContext, "    " + NoticeReplyList.size() + "    ", Toast.LENGTH_LONG).show();
				StringBuilder sb = new StringBuilder();
				sb.append(NoticeReplyList.toString());
				//mShowMessage.setText(sb.toString() + "\n");	
				//mShowMessage.append(respMessage + "\n");
				Log.e("ccccc", ""+NoticeReplyList.get(0).getReplyNoticeId());
				if(NoticeReplyList!=null)
				{
					Log.e("ccccc", "eee");
					noticeReplyListener.showNoticeReply(NoticeReplyList);
				}
			} else if (result.equals("Error!")) {
				Toast.makeText(mContext, "!", Toast.LENGTH_SHORT).show();
				//mShowMessage.setText(respMessage);
			}else
			{
				Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
			}
			// 
			progressDialog.cancel();
		}
		
		public void setOnGetNoticeReplyListener(getNoticeReplySucceedListener noticeReplyListener)
		{
			this.noticeReplyListener=noticeReplyListener;
			Log.e("aaaaa", "ww");
		}
		
		public interface getNoticeReplySucceedListener
		{
			 void showNoticeReply(List<NoticeReply> noticeReplyList);
		}
		
		public List<NoticeReply> getListNoticeReply()
		{
			if(NoticeReplyList==null)
				return null;
			return NoticeReplyList;
		}

	}
