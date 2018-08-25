package com.example.communityfunction.tool;

import java.util.ArrayList;
import java.util.List;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.Food;
import com.example.communityfunction.informationclass.FoodEvaluate;
import com.example.communityfunction.informationclass.FoodRestaurant;


public class GetRestaurantInformations {

	  private List<FoodRestaurant> restaurantList = new ArrayList<FoodRestaurant>();
	  private List<Food> foodList = new ArrayList<Food>();
	  private List<FoodEvaluate> evaluateList = new ArrayList<FoodEvaluate>();
	
	public List<FoodEvaluate> initEvaluates() {
		
   	 FoodEvaluate a = new FoodEvaluate();
   	 a.setPersonName("xiaoming");
   	 a.setEvaluateDate("2014-11-11");
   	 a.setEvaluateGrade((float) 3.1);
   	 a.setEvaluateContent("�óԣ���̫�󣬲���");
   	 evaluateList.add(a);
   	 
   	 FoodEvaluate b = new FoodEvaluate();
   	 b.setPersonName("xiaomi");
   	 b.setEvaluateDate("2014-11-12");
   	 b.setEvaluateGrade((float) 1);
   	 b.setEvaluateContent("�óԣ���̫�󣬲���,�óԣ���̫�󣬲���,�óԣ���̫�󣬲���,�óԣ���̫�󣬲���,�óԣ���̫�󣬲���,�óԣ���̫�󣬲���");
   	 evaluateList.add(b);
	return evaluateList;
	
}
	
	
	public List<Food> initFoods() {
		Food a = new Food();
		a.setCount(1);
		a.setImageId(R.drawable.food_one);
		a.setInformation("����Գԣ�������Ѽ");
		a.setPrice(0);
		a.setPrePrice(10000);
		foodList.add(a);
		
		Food  b= new Food();
		b.setCount(100);
		b.setImageId(R.drawable.food_three);
		b.setInformation("����ൺϺ");
		b.setPrice(100);
		b.setPrePrice((float) 0.1);
		foodList.add(b);
		
		Food  c= new Food();
		c.setCount(57);
		c.setImageId(R.drawable.food_two);
		c.setInformation("С��");
		c.setPrice(12);
		c.setPrePrice((float) 15.5);
		foodList.add(c);
		
		Food  d= new Food();
		d.setCount(6);
		d.setImageId(R.drawable.food_four);
		d.setInformation("��Ʒ����");
		d.setPrice(5.5);
		d.setPrePrice(8);
		foodList.add(d);
		return foodList;
	
	}
	
	public List<FoodRestaurant> initRestaurants() {
		initFoods();
		 initEvaluates();

	FoodRestaurant a = new FoodRestaurant();
		
		a.setAddress("�㹤����");
		a.setDistance("12km");
		a.setPhoneNumber("12314514541");
		a.setRestaurantName("���²���");
		a.setEvaluateGrade((float) 2.3);
		a.setEvaluateStar((float) 2.3);
		a.foodList =foodList;
		a.evaluateList=evaluateList;
		a.setPurchaseDetail("       ���������ü��ס������׻�˫�׶��յ���ʽ���ס������׶��׼��ܲ����������ѿɽ�������Ϊý�飬����Ӷ������׵�ĳЩ�ϰ�����ȫ���������׵����Ѷ�˫�׶��յĸ���ʱ��С���ߡ������Ǻš���Խ�Ǻš�ǿ���Ǻţ����ǵ���̬���������ͬ��Ҫ�������ײ��֣����������ײ�����ɡ�");
		restaurantList.add(a);
		
		
	FoodRestaurant b = new FoodRestaurant();
		
		b.setAddress("�㹤1��");
		b.setDistance("14km");
		b.setPhoneNumber("12314514542");
		b.setRestaurantName("̫������");
		b.setEvaluateGrade((float) 2.5);
		b.setEvaluateStar((float) 2.5);
		b.foodList =foodList;
		b.evaluateList=evaluateList;
		b.setPurchaseDetail("       �����������Ʒ��Ȩ������Ȩ�����С������ǿ��Խ������������顢���͡�ѧϰ���о��������ʹ�ù���������Ȩ���˵�Ȩ�棬���Ը����Ρ�δ����վͬ�⣬����ת�ر�վ���������׵��ļ������Գ�Ϯ�������򽫱�վ����ȷǷ���Ϊ�����������Ϊ��վ��Ȩ������ʾ֪��");
		restaurantList.add(b);
		
	FoodRestaurant c = new FoodRestaurant();
		
		c.setAddress("�㹤7��");
		c.setDistance("15km");
		c.setPhoneNumber("12314514543");
		c.setRestaurantName("��Ѳ���");
		c.setEvaluateGrade((float) 2.4);
		c.setEvaluateStar((float) 2.4);
		c.foodList =foodList;
		c.evaluateList=evaluateList;
		c.setPurchaseDetail("����        Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(c);
		
	FoodRestaurant d = new FoodRestaurant();
		
		d.setAddress("��ѧ�Ǳ�");
		d.setDistance("16km");
		d.setPhoneNumber("12314514544");
		d.setRestaurantName("��������");
		d.setEvaluateGrade((float)5);
		d.setEvaluateStar((float) 5);
		d.foodList =foodList;
		d.evaluateList=evaluateList;
		d.setPurchaseDetail("����    Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(d);
		
	FoodRestaurant e = new FoodRestaurant();
		
		e.setAddress("���˺�");
		e.setDistance("100m");
		e.setPhoneNumber("12314514545");
		e.setRestaurantName("Ұζ�տ���");
		e.setEvaluateGrade((float) 4);
		e.setEvaluateStar((float) 4);
		e.foodList =foodList;
		e.evaluateList=evaluateList;
		e.setPurchaseDetail("     �� ��Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(e);
		
	FoodRestaurant f = new FoodRestaurant();
		
		f.setAddress("�㹤����");
		f.setDistance("17km");
		f.setPhoneNumber("12314514546");
		f.setRestaurantName("���²���");
		f.setEvaluateGrade((float) 2.3);
		f.setEvaluateStar((float) 2.3);
		f.foodList =foodList;
		f.evaluateList=evaluateList;
		f.setPurchaseDetail("     �� ��Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(f);
		
		
	FoodRestaurant g = new FoodRestaurant();
		
		g.setAddress("�㹤����");
		g.setDistance("17km");
		g.setPhoneNumber("12314514547");
		g.setRestaurantName("���²���");
		g.setEvaluateGrade((float) 2.3);
		g.setEvaluateStar((float) 2.3);
		g.foodList =foodList;
		g.evaluateList=evaluateList;
		g.setPurchaseDetail("��  ��Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(g);
		
	FoodRestaurant h = new FoodRestaurant();
		
		h.setAddress("�㹤����");
		h.setDistance("17km");
		h.setPhoneNumber("12314514548");
		h.setRestaurantName("���²���");
		h.setEvaluateGrade((float) 2.3);
		h.setEvaluateStar((float) 2.3);
		h.foodList =foodList;
		h.evaluateList=evaluateList;
		h.setPurchaseDetail("  ����Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(h);
		
	FoodRestaurant i = new FoodRestaurant();
		
		i.setAddress("�㹤����");
		i.setDistance("17km");
		i.setPhoneNumber("12314514549");
		i.setRestaurantName("���²���");
		i.setEvaluateGrade((float) 2.3);
		i.setEvaluateStar((float) 2.3);
		i.foodList =foodList;
		i.evaluateList=evaluateList;
		i.setPurchaseDetail("��    ��Ȼ��,��һ��ˮҲ�������һ���̶��Ϸ�ӳ���󺣵İ��ɫ�ʡ��������ṩ�ĸ��������ݷḻ����Ĺ㷺����ʽ�����������Խ�ǿ���Ӻ�����Ů���ļ��ġ�����ʮ���ġ�����������Ů�����ҹȽ��Ҷ�ʮ����ĩ��Ļ���Ʒ���ٹ���ʮ�꣬��������ᡷ��ʱ����Խ������ǧ�ꣻ�ӹŵ��ʦ��¶�����ĺϳ�������·�ǡ���������������ӰƬ��������š�������衶�������㡷������������ޡ�������Ǳ�����ɣ�ĳ��ߣ�������ʮ�������й���½�ܹ��е����ϵĽ�����ֻҪ�����ģ������ܹ��������ҵ������������ɵ�");
		restaurantList.add(i);
		return restaurantList;
	
}
	
	
}
