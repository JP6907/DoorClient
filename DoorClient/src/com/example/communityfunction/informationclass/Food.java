package com.example.communityfunction.informationclass;

import java.io.Serializable;

public class Food implements Serializable{
    
	private static final long serialVersionUID = 1L;
	private int imageId;       //ʳ��ͼƬ
    private String information; // ������Ϣ
    private int count;   //�۳���������
    private double price;   //�۸�
    private float prePrice; //ԭ��
	
    public int getImageId() {
		return imageId;
	}
	
    public void setImageId(int imageId) {
		this.imageId = imageId;
	}

    public String getInformation() {
		return information;
	}
	
    public void setInformation(String information) {
		this.information = information;
	}
	
    public int getCount() {
		return count;
	}
	
    public void setCount(int count) {
		this.count = count;
	}
	
    public double getPrice() {
		return price;
	}
	
    public void setPrice(double d) {
		this.price = d;
	}
	
    public float getPrePrice() {
		return prePrice;
	}
	
    public void setPrePrice(float prePrice) {
		this.prePrice = prePrice;
	}
      
}
