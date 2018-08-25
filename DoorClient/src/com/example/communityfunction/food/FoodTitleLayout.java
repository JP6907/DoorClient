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
				Toast.makeText(getContext(), "���ڴ�", Toast.LENGTH_SHORT).show();
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
        //��Ϊ��ͬ�������Ҫ��ı��������ֲ�ͬ
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
		    	  title.setText("��ʳ��");   	     	  
		    	    	  
		      } break;
		    
		      case 2:
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("���ڲ˵�");
		
		      } break;
		     
		      case 3:
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.VISIBLE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("��������");
		
		      } break;
		    
		      case 4:
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("�ύ����");
		
		      } break;
		   
		      case 5:	      
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("����λ��");
		
		      } break;
		      
		      case 6:	      
		      {
		    	  titleLocate.setVisibility(View.GONE); 
		    	  titleShare.setVisibility(View.GONE);
		    	  titleSearch.setVisibility(View.GONE);
		    	  title.setText("��ѯ·��");
		
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
//			 //�ر�sso��Ȩ
//			 oks.disableSSOWhenAuthorize(); 
//			 
//			// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
//			 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//			 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
//			 oks.setTitle("����");
//			 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
//			 oks.setTitleUrl("http://sharesdk.cn");
//			 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
//			 oks.setText("���Ƿ����ı�");
//			 
//			 oks.setImageUrl("http://p1.qqyou.com/touxiang/uploadpic/2011-7/201172222431866818.jpg");//ȷ��SDcard������ڴ���ͼƬ
//			 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//			 oks.setUrl("http://sharesdk.cn");
//			 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//			 oks.setComment("���ǲ��������ı�");
//			 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
//			 oks.setSite(context.getString(R.string.app_name));
//			 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
//			 oks.setSiteUrl("http://sharesdk.cn");
//			 
//			// ��������GUI
//			 oks.show(context);
//			 }
}
