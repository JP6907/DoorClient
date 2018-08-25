package com.example.communityfunction.informationclass;

public class NoticeComment {
        private  long id;
		private  String commentContent;
        private  String comentName;
        private  String replyName;
        private  String datetime;
        public long getId(){
        	return id;
        }
        public void setId(long tem){
        	id = tem;
        }
        public String getDatetime(){
        	return datetime;
        }
        public void setDatetime(String tem){
        	datetime = tem;
        }
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

}
