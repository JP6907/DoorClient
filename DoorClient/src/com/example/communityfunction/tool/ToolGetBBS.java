package com.example.communityfunction.tool;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.communityfunction.informationclass.BBS;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.cache.BBSCache;
import neo.door.cache.TimeCache;
import neo.door.utils.XmlHandle;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;

/**
 * ��ȡ�µ�������Ϣ
 * 
 * @author Administrator
 *
 */
public class ToolGetBBS extends AsyncTask {


	private Activity mContext;
	private TextView mShowMessage;
	private List<BBS> bbslist = null;
	private String exception;
	private getBbsSucceedListener bbsSucceedListener;
	private int mflag;
	/**
	 * 
	 * @param context
	 *            Activity
	 * @param showMessage
	 *            ��ʾ��Ϣ��TextView
	 */
	public ToolGetBBS(Activity context,int flag,List<BBS> l) {
		mContext = context;
		mflag = flag;
		bbslist = l;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(Object... params) {
		String result = null;
		String reqMessage1;
		String respMessage1 = null;
		try {
			/***
			 * 刷新
			 */
			if(mflag == 1){
				/***
				 * 发送请求数据
				 */
				String requestMethod1 = null;
				String time = TimeCache.readBBS();
				List<BBS> oldlist = BBSCache.read();
				String deletedId = "";
				if(oldlist.size() > 0){
					deletedId += oldlist.get(0).getBbsId();
				}
				else if(oldlist.size() == 0){
					deletedId += "null";
				}
				for(int i = 1 ; i < oldlist.size() ; i++){
					deletedId += ","+oldlist.get(i).getBbsId();
				}
				reqMessage1 = XmlHandle.packXmls("flag", "new", "time", time , "deletedId", deletedId);
				requestMethod1 = Config.GetBBSPostDateMethod;
				/***
				 * 得到数据开始检查
				 */
				respMessage1 = WebUtil.getXmlByWeb(reqMessage1, requestMethod1);
				Log.i("reque", respMessage1);
				{
					Set s = XmlHandle.getDeletedSet(respMessage1);
					List<BBS> newlist = XmlHandle.getBBSPostDate(respMessage1);
					
					/**
					 * 如果服务器没有数据，那么该线程停止两秒，让刷新的圆圈可以转动两秒，让用户有更良好的体验
					 */
					if(newlist.size()==0)
						SystemClock.sleep(2000);
					
					/***
					 * 刷新超过10条清楚全部缓存
					 */
					if(s.contains(-1)){
						bbslist = newlist;
						BBSCache.addwrite(bbslist);
					}
					/***
					 * 刷新少于10条更新list并且保持listSize的一致
					 */
					else{
						int size = bbslist.size();
						if(size < 10) size = 10;
						for(int i = 0 ; i < bbslist.size() ; i++ ){
							newlist.add(bbslist.get(i));
						}
						Iterator<BBS> it = newlist.iterator();
						while(it.hasNext()){
							long id = it.next().getBbsId();
							if(s.contains(Long.valueOf(id))){
								it.remove();
							}
						}
						if(newlist.size()>BBSCache.num){
							for(int i = newlist.size()-1 ; i >= BBSCache.num ;i--){
								newlist.remove(i);
							}
						}
						BBSCache.addwrite(newlist);
						bbslist.clear();
						for(int i = 0 ; i < size && i < newlist.size() ; i++){
						bbslist.add(newlist.get(i));
						}
					}	
				}
				TimeCache.writeBBS(XmlHandle.getTime(respMessage1));
				result = "Success";
			}
			/***
			 * 加载
			 */
			else if(mflag == 0){
				String requestMethod1 = null;
				String time = bbslist.get(bbslist.size()-1).getBbsPublishdt();
				List<BBS> oldlist = BBSCache.read();
				/***
				 * 缓存够用取缓存
				 */
				if(oldlist.size() >= bbslist.size() + 10){
					
					for(int i = bbslist.size() ; i < bbslist.size()+10 ; i++){
						bbslist.add(oldlist.get(i));
					}
					
				}
				/***
				 * 缓存不够请求服务器
				 */
				else{
					reqMessage1 = XmlHandle.packXmls("flag", "old", "time", time ,"null","null");
					requestMethod1 = Config.GetBBSPostDateMethod;
					respMessage1 = WebUtil.getXmlByWeb(reqMessage1, requestMethod1);
					Log.i("reque", respMessage1);
					bbslist = XmlHandle.getBBSPostDate(respMessage1);
					
					for(int i = 0 ; i < bbslist.size() ; i++ ){
						oldlist.add(bbslist.get(i));
					}
					bbslist = oldlist;

					

					if(bbslist.size() <= BBSCache.num)		BBSCache.addwrite(bbslist);

				}
				result = "Success";
			}
		} catch (IOException e) {
			result = "IO";
			e.printStackTrace();
			exception = e.toString();
			Log.w("reque", exception);
		} catch (Exception e) {
			result = "Error";
			// TODO Auto-generated catch block
			e.printStackTrace();
			exception = e.toString();
			Log.w("reque", exception);
		}

		return result;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result.equals("Success")) {
			Toast.makeText(mContext, "获取到" + bbslist.size() + "条信息", Toast.LENGTH_LONG).show();
			StringBuilder sb = new StringBuilder();
			sb.append(bbslist.toString());
//			if(bbslist!=null)
//			{
				bbsSucceedListener.refreshBbs(bbslist);
//			}

		} else if(result.equals("IO")){
			Toast.makeText(mContext, "无法连接服务器!", Toast.LENGTH_SHORT).show();
			bbsSucceedListener.refreshBbs(null);
		}else if (result.equals("Error")) {
			Toast.makeText(mContext, "无法连接服务器!", Toast.LENGTH_SHORT).show();
			bbsSucceedListener.refreshBbs(null);
		}else
		{
			Toast.makeText(mContext, "无法连接服务器", Toast.LENGTH_SHORT).show();
			bbsSucceedListener.refreshBbs(null);
		}
		// 取消进度�?
	}
	
	public void setOnBbsSucceedListener(getBbsSucceedListener bbsSucceedListener)
	{
		this.bbsSucceedListener=bbsSucceedListener;
	}
	
	public interface getBbsSucceedListener
	{
		 void refreshBbs(List<BBS> bbslist);
	}
	
	public List<BBS> getListBBS()
	{
		if(bbslist==null)
			return null;
		return bbslist;
	}

}