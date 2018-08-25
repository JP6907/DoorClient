package com.example.communityfunction.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.communityfunction.informationclass.BBS;
import com.example.communityfunction.informationclass.Notice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.cache.BBSCache;
import neo.door.cache.NoticeCache;
import neo.door.cache.TimeCache;
import neo.door.utils.XmlHandle;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

/**
 * 锟斤拷取锟铰碉拷锟斤拷锟斤拷锟斤拷息
 * 
 * @author Administrator
 *
 */
public class ToolGetNotice extends AsyncTask {

	private Activity mContext;
	private TextView mShowMessage;
	private List<Notice> NoticeList = null;
	private int mflag;

	private getNoticeSucceedListener noticeSucceedListener;

	/**
	 * 
	 * @param context
	 *            Activity
	 * @param showMessage
	 *            锟斤拷示锟斤拷息锟斤拷TextView
	 */
	public ToolGetNotice(Activity context , int flag , List<Notice> l) {
		mContext = context;
		mflag = flag;
		NoticeList = l;
	}


	@Override
	protected Object doInBackground(Object... params) {
		String result = null;
		String reqMessage1;
		JSONArray respMessage1 = null;
		try {
			if(mflag == 1){
				/***
				 * 发送请求数据
				 */
				String requestMethod1 = null;
				requestMethod1 = Config.GetNoticeMethod;
				JSONObject jo = new JSONObject();
				jo.put("time", TimeCache.readNotice());
				jo.put("flag", "new");
				
				String deletedId = "";
				List<Notice> oldlist = NoticeCache.readCache();
				if(oldlist.size() > 0){
					deletedId += oldlist.get(0).getId();
				}
				else if(oldlist.size() == 0){
					deletedId += "null";
				}
				for(int i = 1 ; i < oldlist.size() ; i++ ){
					deletedId += ","+oldlist.get(i).getId();
				}
				jo.put("deletedId",deletedId);
				JSONArray joa = new JSONArray();
				joa.put(jo);
				/***
				 * 得到数据开始检查
				 */
				respMessage1 = WebUtil.getJsonByWeb(requestMethod1, joa);
				{
					List<Notice> newlist = new ArrayList<Notice>();
					for (int i = 1; i < respMessage1.length() ; i++) {
						Notice tem = new Notice();
						tem.setId(respMessage1.getJSONObject(i).getLong("id"));
						tem.setDate(respMessage1.getJSONObject(i).getString("date"));
						tem.setTitle(respMessage1.getJSONObject(i).getString("title"));
						tem.setContent(respMessage1.getJSONObject(i).getString("content"));
						newlist.add(tem);
					}
					deletedId = respMessage1.getJSONObject(0).getString("deletedId");
					String[] tem = deletedId.split(",");
					Set<Long> s = new HashSet<Long>();
					s.clear();
					if(deletedId.charAt(0) == '-'){
						s.add(Long.valueOf(-1));
					}
					else if(deletedId.charAt(0) != 'n'){
						for(int i = 0 ; i < tem.length ; i++){
							s.add(Long.valueOf(tem[i]));
						}
					}
					/***
					 * 刷新超过10条缓存全删除
					 */
					if(s.contains(-1)){
						NoticeList = newlist;
						NoticeCache.writeCache(NoticeList);
					}
					/***
					 * 刷新不够10条更新list并且保证listSize的一致
					 */
					else{
						
						int size = NoticeList.size();
						if(size < 10) size = 10 ;
						for(int i = 0 ; i < NoticeList.size() ; i++ ){
							newlist.add(NoticeList.get(i));
						}
						Iterator<Notice> it = newlist.iterator();
						while(it.hasNext()){
							long id = it.next().getId();
							if(s.contains(Long.valueOf(id))){
								it.remove();
							}
						}
						if(newlist.size()>NoticeCache.num){
							for(int i = newlist.size()-1 ; i >= NoticeCache.num ;i--){
								newlist.remove(i);
							}
						}
						NoticeCache.writeCache(newlist);
						NoticeList.clear();
						for(int i = 0 ; i < size && i < newlist.size() ; i++){
						NoticeList.add(newlist.get(i));
						}
						
					}
				
					TimeCache.writeNotice(respMessage1.getJSONObject(0).getString("time"));
					result = "Success";
				}
			}
			else if(mflag == 0){
				List<Notice> oldlist = NoticeCache.readCache();
				/***
				 * 缓存够用取缓存
				 */
				if(oldlist.size() >= NoticeList.size() + 10){
					
					for(int i = NoticeList.size() ; i < NoticeList.size() + 10; i++){
						NoticeList.add(oldlist.get(i));
					}
					
				}
				/***
				 * 缓存不够请求服务器
				 */
				else{
					String requestMethod1 = null;
					String time = NoticeList.get(NoticeList.size()-1).getDate();
					requestMethod1 = Config.GetNoticeMethod;
					JSONObject jo = new JSONObject();
					jo.put("time", time);
					jo.put("flag", "old");
					JSONArray joa = new JSONArray();
					joa.put(jo);
					respMessage1 = WebUtil.getJsonByWeb(requestMethod1, joa);
					{
						for (int i = 1; i < respMessage1.length() ; i++) {
							Notice tem = new Notice();
							tem.setId(respMessage1.getJSONObject(i).getLong("id"));
							tem.setDate(respMessage1.getJSONObject(i).getString("date"));
							tem.setTitle(respMessage1.getJSONObject(i).getString("title"));
							tem.setContent(respMessage1.getJSONObject(i).getString("content"));
							oldlist.add(tem);
						}	
						NoticeList = oldlist;
					}
					if(NoticeList.size() <= NoticeCache.num)	NoticeCache.writeCache(NoticeList);
				}				
				result = "Success";
			}

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
			Toast.makeText(mContext, "获取到" + NoticeList.size() + "条公告", Toast.LENGTH_LONG).show();
			StringBuilder sb = new StringBuilder();
			sb.append(NoticeList.toString());
//			if (NoticeList != null) {
//				Log.e("ccccc", "eee");
//				noticeSucceedListener.showNotice(NoticeList);
//			}
		} else if (result.equals("Error!")) {
			Toast.makeText(mContext, "获取公告失败!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "获取公告失败", Toast.LENGTH_SHORT).show();
		}
		noticeSucceedListener.showNotice(NoticeList);
	}

	public void setOnNoticeSucceedListener(getNoticeSucceedListener noticeSucceedListener) {
		this.noticeSucceedListener = noticeSucceedListener;
	}

	public interface getNoticeSucceedListener {
		void showNotice(List<Notice> NoticeList);
	}

	public List<Notice> getListNotice() {
		if (NoticeList == null)
			return null;
		return NoticeList;
	}

}