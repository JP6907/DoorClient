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
	private long replyId; //�ظ�Ҳ����¥�������¥��������ٴ�����Ҳ�����,����ֵ��һ���������ģ���ֻ���������ݴ�С�������
	private long replyPostId; //�ظ���һ������
	private Integer replyJudge; //1Ϊ�ظ�¥����0Ϊ�ظ�¥��
	private String replyDateTime; 
	private String replyResponder; //�ظ��ˣ���ʾ�õ�
	private String replyPublisher; //���ظ���
	private int replyFloor; //������¥��
	private String replyText; 
	private List<BBSComment> bbsCommentList=new ArrayList<BBSComment>();//�ظ����۵ļ���
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
