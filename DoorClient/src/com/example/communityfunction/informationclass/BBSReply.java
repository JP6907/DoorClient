package com.example.communityfunction.informationclass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neo.door.utils.CircleImageView;

public class BBSReply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8257429880277711117L;
	private long replyId; //回复也就是楼层的排序，楼层里面的再次排序也用这个,它的值不一定是连续的，它只是用来根据大小排序而已
	private long replyPostId; //回复哪一个帖子
	private Integer replyJudge; //1为回复楼主，0为回复楼层
	private String replyDateTime; 
	private String replyResponder; //回复人，显示用的
	private String replyPublisher; //被回复人
	private int replyFloor; //所处的楼层
	private String replyText; 
	private List<BBSComment> bbsCommentList=new ArrayList<BBSComment>();//回复评论的集合
	public long getReplyId() {
		return replyId;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	public long getReplyPostId() {
		return replyPostId;
	}
	public void setReplyPostId(long replyPostId) {
		this.replyPostId = replyPostId;
	}
	public Integer getReplyJudge() {
		return replyJudge;
	}
	public void setReplyJudge(Integer replyJudge) {
		this.replyJudge = replyJudge;
	}
	public String getReplyDateTime() {
		return replyDateTime;
	}
	public void setReplyDateTime(String replyDateTime) {
		this.replyDateTime = replyDateTime;
	}
	public String getReplyResponder() {
		return replyResponder;
	}
	public void setReplyResponder(String replyResponder) {
		this.replyResponder = replyResponder;
	}
	public String getReplyPublisher() {
		return replyPublisher;
	}
	public void setReplyPublisher(String replyPublisher) {
		this.replyPublisher = replyPublisher;
	}
	public int getReplyFloor() {
		return replyFloor;
	}
	public void setReplyFloor(int replyFloor) {
		this.replyFloor = replyFloor;
	}
	public String getReplyText() {
		return replyText;
	}
	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}
	
	public void addBbsCommentList(BBSComment bbsComment)
	{
		bbsCommentList.add(bbsComment);
	}
	public List<BBSComment> getBbsComment() {
		return bbsCommentList;
	}
	public void setBbsComment(List<BBSComment> bbsComment) {
		this.bbsCommentList = bbsComment;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
