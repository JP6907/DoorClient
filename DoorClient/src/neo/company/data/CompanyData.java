package neo.company.data;

import java.io.Serializable;

public class CompanyData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String address;
	private double score;
	private String phone;
	
	public CompanyData(String name, String address, double score, String phone){
		
		this.name = name;
		this.address = address;
		this.score = score;
		this.phone = phone;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return address;
	}
	
	public void setScore(float score){
		this.score = score;
	}
	
	public double getScore(){
		return score;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public String getPhone(){
		return phone;
	}

}
