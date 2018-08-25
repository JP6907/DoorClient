package com.example.communityfunction.informationclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Notice implements Serializable{
     
	  /**
	 * 
	 */
	  private static final long serialVersionUID = -440508860144471063L;
	  private long id;
	  private String  title;
      private String  date;
      private List<NoticeReply>  replyList = new ArrayList<NoticeReply>(); 
      private String  content;
	
       public String getTitle() {	
    	  return title;
       }
       public long getId(){
    	   return id;
       }
       public void setId(long tem){
    	   this.id = tem;
       }
       public void setTitle(String title) {
		this.title = title;
	   }
	 	public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public Integer getReplyCount() {
			return replyList.size();
		}

		public List<NoticeReply> getReplyList() {
			return replyList;
		}
		public void setReplyList(List<NoticeReply> replyList) {
			this.replyList = replyList;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	       
}
