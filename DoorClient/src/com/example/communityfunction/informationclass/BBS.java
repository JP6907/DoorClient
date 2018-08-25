package com.example.communityfunction.informationclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BBS implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7643758351558227816L;
	private long bbsId; 
	private String bbsTitle; 
	private String bbsText; 
	private String bbsPublisher; 
	private int bbsReplyNum; 
	private String bbsPublishDt;
	private String bbsNewDt;//���»؏͕r�g 
	private List<String> bbsPictureList=new ArrayList<String>();
	private String bbsPhone;
	private List<BBSReply> bbsCommentList;
	//�жϸ������Ƿ�ո������������Ǵӷ�������ȡ��Ҳ���Ǵӻ����ȡ��ֻ�Ǹոշ���֮����ʾ�������ѣ�����������ж�����ΪͼƬ·����ͬ
	private boolean isPostJustNow=false;
	public long getBbsId() {
		return bbsId;
	}
	public void setBbsId(long bbsId) {
		this.bbsId = bbsId;
	}
	public String getBbsTitle() {
		return bbsTitle;
	}
	public void setBbsTitle(String bbsTitle) {
		this.bbsTitle = bbsTitle;
	}
	public String getBbsText() {
		return bbsText;
	}
	public void setBbsText(String bbsText) {
		this.bbsText = bbsText;
	}
	public String getBbsPublisher() {
		return bbsPublisher;
	}
	public void setBbsPublisher(String bbsPublisher) {
		this.bbsPublisher = bbsPublisher;
	}
	public int getBbsReplyNum() {
		return bbsReplyNum;
	}
	public void setBbsReplyNum(int bbsReplyNum) {
		this.bbsReplyNum = bbsReplyNum;
	}
	public String getBbsPublishdt() {
		return bbsPublishDt;
	}
	public void setBbsPublishdt(String bbsPublishdt) {
		this.bbsPublishDt = bbsPublishdt;
	}
	public String getBbsNewDt() {
		return bbsNewDt;
	}
	public void setBbsNewDt(String bbsNewDt) {
		this.bbsNewDt = bbsNewDt;
	}
	public List<String> getBbsPictureList() {
		return bbsPictureList;
	}
	public void setBbsPictureList(List<String> bbsPictureList) {
		this.bbsPictureList = bbsPictureList;
	}
	public String getBbsPhone() {
		return bbsPhone;
	}
	public void setBbsPhone(String bbsPhone) {
		this.bbsPhone = bbsPhone;
	}
	public List<BBSReply> getBbsCommentlist() {
		return bbsCommentList;
	}
	public void setBbsCommentlist(List<BBSReply> bbsCommentlist) {
		this.bbsCommentList = bbsCommentlist;
	}
	
	public void addBbsPictureList(String tem){
		bbsPictureList.add(tem);
	}
	public void addBbsReplyList(BBSReply tem){
		bbsCommentList.add(tem);
	}
	public boolean isPostJustNow() {
		return isPostJustNow;
	}
	public void setPostJustNow(boolean isPostJustNow) {
		this.isPostJustNow = isPostJustNow;
	}

	
	
}
