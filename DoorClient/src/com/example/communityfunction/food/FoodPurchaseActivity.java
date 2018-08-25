package com.example.communityfunction.food;

import java.util.List;

import neo.door.utils.ChangeStatusBarColor;
import com.neo.huikaimen.R;
import com.example.communityfunction.adapter.FoodEvaluateAdapter;
import com.example.communityfunction.informationclass.Food;
import com.example.communityfunction.informationclass.FoodEvaluate;
import com.example.communityfunction.informationclass.FoodRestaurant;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FoodPurchaseActivity extends Activity{
  
	private List<FoodEvaluate> evaluateList ;
	
	private Food food ;
	
	@SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//����
//		 ShareSDK.initSDK(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.food_purchase);
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		setRestaurantInformation();
		initListView();
		
		initButtonResponse();
		
  }
	
	 private void initButtonResponse() {
	      //����ť��Ӧ
	      Button purchaseButton = (Button) findViewById(R.id.purchase_button);
	      
	      purchaseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				 Intent intent = new Intent(FoodPurchaseActivity.this,FoodPayingActivity.class);
				 intent.putExtra("food",food);
				 startActivity(intent);		
			}
		});
		
	      
	}

	private void initListView() {

		//����ʳ���������� 
      FoodEvaluateAdapter adapter = new FoodEvaluateAdapter(FoodPurchaseActivity.this, R.layout.food_purchase_evaluate, evaluateList);
		
      ListView evaluateListView = (ListView) findViewById(R.id.evaluate_listview);
      
      evaluateListView.setAdapter(adapter);
      
      //setListViewHeightBasedOnChildren( evaluateListView);
      
	}
	 /*** 
	     * ��̬����listview�ĸ߶� 
	     *  
	     * @param listView 
	     */  
	    public void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();  
	        if (listAdapter == null) {  
	            return;  
	        }  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  //�ڻ�û�й���View ֮ǰ�޷�ȡ��View�Ķȿ� �ڴ�֮ǰ���Ǳ���ѡ measure һ��. 

	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight  
	                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        // params.height += 5;// if without this statement,the listview will be  
	        // a  
	        // little short  
	        // listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�  
	        // params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�  
	        listView.setLayoutParams(params);  
	    }
	private void setRestaurantInformation() {
		
		 Intent intent = getIntent();
			
	     food = (Food) intent.getSerializableExtra("food");
	     
	     final FoodRestaurant restaurant = (FoodRestaurant) intent.getSerializableExtra("restaurant");
	     
	     evaluateList = restaurant.evaluateList;
	     
	     //�������鲼���ϲ���
	     
	     ImageView foodImage = (ImageView) findViewById(R.id.food_image);
	     
	     TextView restaurantName = (TextView) findViewById(R.id.brief_information_above);
	    
	     TextView foodText = (TextView) findViewById(R.id.brief_information_below);
	     
	     foodImage.setImageResource(food.getImageId());
	     
	     foodText.setText(food.getInformation());
	     
	     restaurantName.setText(restaurant.getRestaurantName());
	     
	     //ʳ��۸��������
	     
	     TextView foodPrice = (TextView) findViewById(R.id.food_price);
		    
	     TextView foodPrePrice = (TextView) findViewById(R.id.food_pre_price);
	     
	     TextView purchasedNumber = (TextView) findViewById(R.id.purchased_number);
	     
	     foodPrice.setText(food.getPrice()+"Ԫ");
	     
	     //��ɾ����
	     foodPrePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);	     
	     foodPrePrice.setText(food.getPrePrice()+"Ԫ");
	     
		  purchasedNumber.setText("����"+food.getCount());      
	     
	     //�̼���Ϣ
	     TextView phoneNumber = (TextView) findViewById(R.id.phonenumber);
	     
	     TextView detailAddress = (TextView) findViewById(R.id.detail_address);
	     
	     ImageButton dailButton = (ImageButton) findViewById(R.id.dial_button);
	     
	     dailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent(Intent.ACTION_DIAL);
				 intent.setData(Uri.parse("tel:"+restaurant.getPhoneNumber()));
				 startActivity(intent);
				
			}
		});
	     
	     phoneNumber.setText("��ϵ�绰�� "+restaurant.getPhoneNumber());
	     
	     detailAddress.setText("��ַ�� "+restaurant.getAddress());
	     
	   
	     //������֪
	     TextView  purchaseDetail = (TextView) findViewById(R.id.description);
	           
	     purchaseDetail.setText(restaurant.getPurchaseDetail());
	     

	}
      

}
