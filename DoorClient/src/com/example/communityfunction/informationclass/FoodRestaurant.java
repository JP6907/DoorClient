package com.example.communityfunction.informationclass;

import java.io.Serializable;
import java.util.List;

public class FoodRestaurant implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String restaurantName ;
	private String address ;
	private String distance ;
	private String phoneNumber;
	private float evaluateGrade ;
	private float evaluateStar ;
	public List<Food> foodList; //²Ëµ¥
	public List<FoodEvaluate> evaluateList;
	private String purchaseDetail;

	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public float getEvaluateGrade() {
		return evaluateGrade;
	}
	public void setEvaluateGrade(float evaluateGrade) {
		this.evaluateGrade = evaluateGrade;
	}
	public float getEvaluateStar() {
		return evaluateStar;
	}
	public void setEvaluateStar(float evaluateStar) {
		this.evaluateStar = evaluateStar;
	}
	public List<Food> getFoodList() {
		return foodList;
	}
	public void setfoodList(List<Food> food) {
		this.foodList = food;
	}
	public String getPurchaseDetail() {
		return purchaseDetail;
	}
	public void setPurchaseDetail(String purchaseDetail) {
		this.purchaseDetail = purchaseDetail;
	}
	public List<FoodEvaluate> getEvaluateList() {
		return evaluateList;
	}
	public void setEvaluateList(List<FoodEvaluate> evaluateList) {
		this.evaluateList = evaluateList;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
			
}
