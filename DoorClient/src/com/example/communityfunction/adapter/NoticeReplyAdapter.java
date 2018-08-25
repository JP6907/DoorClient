package com.example.communityfunction.adapter;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.NoticeComment;
import com.example.communityfunction.informationclass.NoticeReply;
import com.example.communityfunction.myView.CircleImageView;

public class NoticeReplyAdapter extends ArrayAdapter<NoticeReply> {
	   
	private int resourceId;
	Context context;
	
     public  NoticeReplyAdapter(Context context, int resource, List<NoticeReply> objects) {
		
    	 super(context, resource, objects);
		
		resourceId = resource;
		
		this.context = context;
		 
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		NoticeReply noticeReply= (NoticeReply) getItem(position);
					
		View view;
		
		if(convertView == null)
		
		{
			view = LayoutInflater.from(getContext()).inflate(resourceId,null);
		}else
		{
			view =convertView;
		}
		
		CircleImageView commentImg = (CircleImageView) view.findViewById(R.id.commentItemImg);
		
		TextView nickName = (TextView) view.findViewById(R.id.commentNickname);
		
		TextView replyTime = (TextView) view.findViewById(R.id.commentItemTime);
		
		TextView replyContent =(TextView) view.findViewById(R.id.commentItemContent);
		
//		commentImg.setImageResource(R.drawable.);
//		
//		nickName.setText(noticeReply.getNickname());
//		
//		replyTime.setText(noticeReply.getReplyTime());
//		
//		replyContent.setText(noticeReply.getReplyContent()+"\n");
//		
//		List<NoticeComment> commentList = noticeReply.getCommentList();
		

//	if(commentList != null){
//		
//		Iterator<NoticeComment> it = commentList.iterator();
	
	    String commentContent;
        String commentName;
        String replyName;
		SpannableString commentReplyNameContent = null;
		
//		while(it.hasNext())
		{

////				NoticeComment comment = it.next();
//				commentContent = comment.getCommentContent();
//				commentName = comment.getComentName();
//				replyName = comment.getReplyName();
			
			
//			  commentReplyNameContent = new SpannableString(commentName+"回复"+replyName
//						+"："+commentContent);
//			  commentReplyNameContent.setSpan(new ForegroundColorSpan(Color.BLUE),0, 
//					  commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			  commentReplyNameContent.setSpan(new ForegroundColorSpan(Color.BLUE),commentName.length()+2, 
//					  commentName.length()+replyName.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			
//			  commentReplyNameContent.setSpan(new TextSpanClick(commentName), 0, commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			
//			  commentReplyNameContent.setSpan(new TextSpanClick(replyName),commentName.length()+2,commentName.length()+replyName.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//  
			replyContent.append(commentReplyNameContent);
		    replyContent.append("\n");
			
		}
//	}
		replyContent.setMovementMethod(LinkMovementMethod.getInstance());
		
	   /**
	    * 修改中
	    */
		
		final TextView replyText =  (TextView) view.findViewById(R.id.replyText);
	    replyText.setTag(position);
	      replyText.setOnClickListener(new OnClickListener() {					
			
				public void onClick(View view) {				      
																
					((Activity)getContext()).findViewById(R.id.reply_linear).setVisibility(View.VISIBLE);
					((Activity)getContext()).findViewById(R.id.bottom_linear).setVisibility(View.GONE);
					EditText commentEdit = (EditText) ((Activity)getContext()).findViewById(R.id.replyEdit);
					commentEdit.requestFocus();
					
					//奇特
					
					TextView useless = (TextView) ((Activity)getContext()).findViewById(R.id.useless);
					useless.setText(""+replyText.getTag());
				}
			});	

		
		return view;

	}
	
	
	public final class TextSpanClick extends ClickableSpan{
		private String name;
		public TextSpanClick(String name){
			this.name = name;
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
		}
		@Override
		public void onClick(View v) {
							
			Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
		}
	}
		
	
}