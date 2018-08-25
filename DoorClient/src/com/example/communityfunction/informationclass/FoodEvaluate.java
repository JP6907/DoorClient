package com.example.communityfunction.informationclass;

import java.io.Serializable;

public class FoodEvaluate implements Serializable{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String personName;
    private String evaluateDate;
    private float  evaluateGrade;
    private String evaluateContent;

    public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getEvaluateDate() {
		return evaluateDate;
	}
	public void setEvaluateDate(String evaluateDate) {
		this.evaluateDate = evaluateDate;
	}
	public float getEvaluateGrade() {
		return evaluateGrade;
	}
	public void setEvaluateGrade(float evaluateGrade) {
		this.evaluateGrade = evaluateGrade;
	}
	public String getEvaluateContent() {
		return evaluateContent;
	}
	public void setEvaluateContent(String evaluateContent) {
		this.evaluateContent = evaluateContent;
	}
    
}
