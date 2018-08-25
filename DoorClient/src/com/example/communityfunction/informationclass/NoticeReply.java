package com.example.communityfunction.informationclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoticeReply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -852482044494637469L;
	private long replyId; 
	private long replyNoticeId; 
	private int replyJudge; 
	private String replyDateTime; 
	private String replyResponder; 
	private String replyPublisher; 
	private int replyFloor; 
	private String replyText;
	private List<NoticeComment> commentlist;
	public List<NoticeComment> getcommentlist(){
		return commentlist;
	}
	public void setcommentlist(List<NoticeComment> tem){
		commentlist = tem;
	}
	public void addcommentlist(NoticeComment tem){
		commentlist.add(tem);
	}
	public long getReplyId() {
		return replyId;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	public long getReplyNoticeId() {
		return replyNoticeId;
	}
	public void setReplyNoticeId(long tem) {
		this.replyNoticeId = tem;
	}
	public int getReplyJudge() {
		return replyJudge;
	}
	public void setReplyJudge(int replyJudge) {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
