package com.example.communityfunction.informationclass;

public class BBSComment {
	
	private  String commentContent;
    
	private  String comentName;
  
	private  String replyName;
	
	private String replyTime;
	
	private int replyFloor;
	
	private long replyId; //�ظ�Ҳ����¥�������¥��������ٴ�����Ҳ�������
	
	public String getCommentContent() {
		return commentContent;	
	}	
	public void setCommentContent(String commentContent) {

		this.commentContent = commentContent;
	}
	public String getComentName() {
		return comentName;
	}
	public void setComentName(String comentName) {
		this.comentName = comentName;
	}
	public String getReplyName() {
		return replyName;
	}
	public void setReplyName(String replyName) {
			this.replyName = replyName;
		}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public int getReplyFloor() {
		return replyFloor;
	}
	public void setReplyFloor(int replyFloor) {
		this.replyFloor = replyFloor;
	}
	public long getReplyId() {
		return replyId;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	
	

}
