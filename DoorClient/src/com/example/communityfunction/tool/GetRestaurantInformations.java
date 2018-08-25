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
   	 a.setEvaluateContent("好吃，但太贵，差评");
   	 evaluateList.add(a);
   	 
   	 FoodEvaluate b = new FoodEvaluate();
   	 b.setPersonName("xiaomi");
   	 b.setEvaluateDate("2014-11-12");
   	 b.setEvaluateGrade((float) 1);
   	 b.setEvaluateContent("好吃，但太贵，差评,好吃，但太贵，差评,好吃，但太贵，差评,好吃，但太贵，差评,好吃，但太贵，差评,好吃，但太贵，差评");
   	 evaluateList.add(b);
	return evaluateList;
	
}
	
	
	public List<Food> initFoods() {
		Food a = new Food();
		a.setCount(1);
		a.setImageId(R.drawable.food_one);
		a.setInformation("免费试吃，北京烤鸭");
		a.setPrice(0);
		a.setPrePrice(10000);
		foodList.add(a);
		
		Food  b= new Food();
		b.setCount(100);
		b.setImageId(R.drawable.food_three);
		b.setInformation("天价青岛虾");
		b.setPrice(100);
		b.setPrePrice((float) 0.1);
		foodList.add(b);
		
		Food  c= new Food();
		c.setCount(57);
		c.setImageId(R.drawable.food_two);
		c.setInformation("小吃");
		c.setPrice(12);
		c.setPrePrice((float) 15.5);
		foodList.add(c);
		
		Food  d= new Food();
		d.setCount(6);
		d.setImageId(R.drawable.food_four);
		d.setInformation("甜品饮料");
		d.setPrice(5.5);
		d.setPrePrice(8);
		foodList.add(d);
		return foodList;
	
	}
	
	public List<FoodRestaurant> initRestaurants() {
		initFoods();
		 initEvaluates();

	FoodRestaurant a = new FoodRestaurant();
		
		a.setAddress("广工二饭");
		a.setDistance("12km");
		a.setPhoneNumber("12314514541");
		a.setRestaurantName("明月餐厅");
		a.setEvaluateGrade((float) 2.3);
		a.setEvaluateStar((float) 2.3);
		a.foodList =foodList;
		a.evaluateList=evaluateList;
		a.setPurchaseDetail("       歌曲部分用简谱、五线谱或双谱对照的样式记谱。五线谱读谱技能不熟练的网友可将简谱作为媒介，解决视读五线谱的某些障碍；完全不懂五线谱的网友读双谱对照的歌曲时，小节线、反复记号、跳越记号、强弱记号（它们的形态皆与简谱相同）要看五线谱部分，其它看简谱部分则可。");
		restaurantList.add(a);
		
		
	FoodRestaurant b = new FoodRestaurant();
		
		b.setAddress("广工1饭");
		b.setDistance("14km");
		b.setPhoneNumber("12314514542");
		b.setRestaurantName("太阳餐厅");
		b.setEvaluateGrade((float) 2.5);
		b.setEvaluateStar((float) 2.5);
		b.foodList =foodList;
		b.evaluateList=evaluateList;
		b.setPurchaseDetail("       这里的所有作品版权属著作权人所有。网友们可以将它们用于自娱、欣赏、学习、研究，如果在使用过程中损害了权利人的权益，请自负责任。未经本站同意，不得转载本站制作的乐谱等文件，反对抄袭、盗链或将本站镜像等非法行为。如果有人认为本站侵权，敬请示知。");
		restaurantList.add(b);
		
	FoodRestaurant c = new FoodRestaurant();
		
		c.setAddress("广工7饭");
		c.setDistance("15km");
		c.setPhoneNumber("12314514543");
		c.setRestaurantName("免费餐厅");
		c.setEvaluateGrade((float) 2.4);
		c.setEvaluateStar((float) 2.4);
		c.foodList =foodList;
		c.evaluateList=evaluateList;
		c.setPurchaseDetail("　　        然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(c);
		
	FoodRestaurant d = new FoodRestaurant();
		
		d.setAddress("大学城北");
		d.setDistance("16km");
		d.setPhoneNumber("12314514544");
		d.setRestaurantName("光明饭店");
		d.setEvaluateGrade((float)5);
		d.setEvaluateStar((float) 5);
		d.foodList =foodList;
		d.evaluateList=evaluateList;
		d.setPurchaseDetail("　　    然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(d);
		
	FoodRestaurant e = new FoodRestaurant();
		
		e.setAddress("中兴湖");
		e.setDistance("100m");
		e.setPhoneNumber("12314514545");
		e.setRestaurantName("野味烧烤店");
		e.setEvaluateGrade((float) 4);
		e.setEvaluateStar((float) 4);
		e.foodList =foodList;
		e.evaluateList=evaluateList;
		e.setPurchaseDetail("     　 　然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(e);
		
	FoodRestaurant f = new FoodRestaurant();
		
		f.setAddress("广工二饭");
		f.setDistance("17km");
		f.setPhoneNumber("12314514546");
		f.setRestaurantName("明月餐厅");
		f.setEvaluateGrade((float) 2.3);
		f.setEvaluateStar((float) 2.3);
		f.foodList =foodList;
		f.evaluateList=evaluateList;
		f.setPurchaseDetail("     　 　然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(f);
		
		
	FoodRestaurant g = new FoodRestaurant();
		
		g.setAddress("广工二饭");
		g.setDistance("17km");
		g.setPhoneNumber("12314514547");
		g.setRestaurantName("明月餐厅");
		g.setEvaluateGrade((float) 2.3);
		g.setEvaluateStar((float) 2.3);
		g.foodList =foodList;
		g.evaluateList=evaluateList;
		g.setPurchaseDetail("　  　然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(g);
		
	FoodRestaurant h = new FoodRestaurant();
		
		h.setAddress("广工二饭");
		h.setDistance("17km");
		h.setPhoneNumber("12314514548");
		h.setRestaurantName("明月餐厅");
		h.setEvaluateGrade((float) 2.3);
		h.setEvaluateStar((float) 2.3);
		h.foodList =foodList;
		h.evaluateList=evaluateList;
		h.setPurchaseDetail("  　　然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(h);
		
	FoodRestaurant i = new FoodRestaurant();
		
		i.setAddress("广工二饭");
		i.setDistance("17km");
		i.setPhoneNumber("12314514549");
		i.setRestaurantName("明月餐厅");
		i.setEvaluateGrade((float) 2.3);
		i.setEvaluateStar((float) 2.3);
		i.foodList =foodList;
		i.evaluateList=evaluateList;
		i.setPurchaseDetail("　    　然而,这一滴水也许可以在一定程度上反映出大海的斑斓色彩。这里所提供的歌曲，内容丰富，题材广泛，形式多样，经典性较强。从汉朝才女蔡文姬的《胡笳十八拍》到当代著名女作曲家谷建芬二十世纪末年的获奖作品《再过二十年，我们来相会》，时间纵越将近两千年；从古典大师亨德尔不朽的合唱《哈利路亚》到当代美国著名影片《铁达尼号》的主题歌《我心永恒》，地域横跨五大洲。如果你是饱经沧桑的长者，如果你二十世纪在中国大陆受过中等以上的教育，只要有耐心，相信能够在这里找到你难忘的旋律的");
		restaurantList.add(i);
		return restaurantList;
	
}
	
	
}
