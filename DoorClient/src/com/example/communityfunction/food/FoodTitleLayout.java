package com.example.communityfunction.food;


import java.util.HashMap;
import java.util.Map;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.baidumap.MapMainActivity;
import com.neo.huikaimen.R;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FoodTitleLayout extends LinearLayout {
	Context context = null;

	public FoodTitleLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		LayoutInflater.from(context).inflate(R.layout.food_title, this);
		ImageButton titleBack = (ImageButton) findViewById(R.id.title_back);
		ImageButton titleLocate = (ImageButton) findViewById(R.id.title_locate);
		ImageButton titleShare = (ImageButton) findViewById(R.id.title_share);
		ImageButton titleSearch = (ImageButton) findViewById(R.id.title_search);
		TextView title = (TextView) findViewById(R.id.title_text);
		titleBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				((Activity) context).finish();
			}
		});
		
        titleShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {				
//				showShare();
				Toast.makeText(getContext(), "请期待", Toast.LENGTH_SHORT).show();
			}
		});
  	    
        titleLocate.setOnClickListener(new OnClickListener() {
    			
  			public void onClick(View view) {
  				
  				Intent intent = new Intent(context,MapMainActivity.class);
  				context.startActivity(intent);
  			}
  		});
  
        titleSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				Toast.makeText(context.getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
			}
		});
        //因为不同界面可能要求的标题栏布局不同
       Map<String,Integer> activity = getMap();   
       Boolean flag = activity.containsKey(((Activity) context).getClass().getSimpleName().toString());
          
       if(flag)
		     switch(activity.get(((Activity) context).getClass().getSimpleName().toString()))
		     {
		     
		      case 1:
		      {  	 
		    	  titleLocate.setVisibility(View.VISIBLE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("美食店");   	     	  
		    	    	  
		      } break;
		    
		      case 2:
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("店内菜单");
		
		      } break;
		     
		      case 3:
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.VISIBLE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("购买详情");
		
		      } break;
		    
		      case 4:
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("提交订单");
		
		      } break;
		   
		      case 5:	      
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("搜索位置");
		
		      } break;
		      
		      case 6:	      
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("查询路线");
		
		      } break;
		      
		     default: 
		    	  titleLocate.setVisibility(View.GONE);  	
		    	  titleShare.setVisibility(View.GONE); 
		    	  titleSearch.setVisibility(View.GONE);
		     
			}  
		     
		
		}
		
		Map<String, Integer> getMap()
		{
			Map<String,Integer> activity = new HashMap<String, Integer>();
			
			activity.put("FoodRestaurantActivity", 1);
			activity.put("FoodActivity", 2);	
			activity.put("FoodPurchaseActivity", 3);
			activity.put("FoodPayingActivity", 4);
			activity.put("MapMainActivity", 5);
			activity.put("RoutePlanActivity", 6);
			return activity;
		}	
			
	
//		private void showShare() {
//			 ShareSDK.initSDK(context);
//			 OnekeyShare oks = new OnekeyShare();
//			 //关闭sso授权
//			 oks.disableSSOWhenAuthorize(); 
//			 
//			// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//			 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//			 oks.setTitle("分享");
//			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//			 oks.setTitleUrl("http://sharesdk.cn");
//			 // text是分享文本，所有平台都需要这个字段
//			 oks.setText("我是分享文本");
//			 
//			 oks.setImageUrl("http://p1.qqyou.com/touxiang/uploadpic/2011-7/201172222431866818.jpg");//确保SDcard下面存在此张图片
//			 // url仅在微信（包括好友和朋友圈）中使用
//			 oks.setUrl("http://sharesdk.cn");
//			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//			 oks.setComment("我是测试评论文本");
//			 // site是分享此内容的网站名称，仅在QQ空间使用
//			 oks.setSite(context.getString(R.string.app_name));
//			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//			 oks.setSiteUrl("http://sharesdk.cn");
//			 
//			// 启动分享GUI
//			 oks.show(context);
//			 }
}
