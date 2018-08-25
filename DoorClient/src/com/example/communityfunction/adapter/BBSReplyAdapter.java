package com.example.communityfunction.adapter;

import java.util.Iterator;
import java.util.List;

import com.example.communityfunction.informationclass.BBSComment;
import com.example.communityfunction.informationclass.BBSReply;
import com.example.communityfunction.myView.CircleImageView;
import com.neo.huikaimen.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AnalogClock;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class BBSReplyAdapter extends BaseAdapter {

	private Context mContext;
	private List<BBSReply> replyList;

	public BBSReplyAdapter(Context context, List<BBSReply> replyList) {
		this.mContext = context;
		this.replyList = replyList;
	}

	@Override
	public int getCount() {
		return replyList.size();
	}

	@Override
	public Object getItem(int position) {
		if (replyList == null)
			return null;
		return replyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final Holder holder;
		if(convertView==null)
		{
			holder=new Holder();
			convertView=LayoutInflater.from(mContext).inflate( R.layout.bbs_reply_item, null);
			holder.commentImg=(CircleImageView) convertView.findViewById(R.id.commentItemImg);
			holder.nickName=(TextView) convertView.findViewById(R.id.commentNickname);
			holder.replyContent=(TextView) convertView.findViewById(R.id.commentItemContent);
			holder.replyTime=(TextView) convertView.findViewById(R.id.commentItemTime);
			holder.replyText = (TextView) convertView.findViewById(R.id.replyText);
			holder.floorId=(TextView)convertView.findViewById(R.id.floorid);
			convertView.setTag(holder);
		}
		else
			holder=(Holder)convertView.getTag();
		
		BBSReply bbsReply=replyList.get(position);
		
		holder.commentImg.setImageResource(R.drawable.replyimg1);
		holder.nickName.setText(bbsReply.getReplyResponder());
		holder.replyContent.setText(bbsReply.getReplyText());
		holder.replyTime.setText("发表于"+bbsReply.getReplyDateTime());
		holder.floorId.setText(bbsReply.getReplyFloor()+"");
		holder.replyText.setTag(position);//为了判断是回复哪一个的
		/**
		 * 回复评论监听
		 */
		holder.replyText.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				showKeyBoard(holder.nickName.getText().toString());
				TextView useless = (TextView) ((Activity)mContext).findViewById(R.id.useless);
				/**
				 * 做个标记，方便判断是回复哪一层楼的，只要判断出回复哪一层楼就行了，至于
				 * 是回复该楼层的哪一个就无需知道了，
				 */
				useless.setText(holder.replyText.getTag() + "");
			}
		});
		
		List<BBSComment> commentList = bbsReply.getBbsComment();//取出回复评论的集合
		
		if (commentList != null) {
			
			 Iterator<BBSComment> it = commentList.iterator();
			 String commentContent;
			 String commentName;
			 String replyName;
			 SpannableString commentReplyNameContent = null;
			
			 while (it.hasNext()) 
			 {
				final BBSComment comment = it.next();
				commentContent = comment.getCommentContent();
				commentName = comment.getComentName();
				replyName = comment.getReplyName();

				commentReplyNameContent = new SpannableString(commentName + "回复" + replyName + "：" + commentContent);
				/**
				 * 回复评论的点击事件
				 */
				commentReplyNameContent.setSpan(new ClickableSpan()
				{
					@Override
					public void updateDrawState(TextPaint ds) 
					{
						super.updateDrawState(ds);
						ds.setUnderlineText(false);
					}

					@Override
					public void onClick(View widget) 
					{
						showKeyBoard(comment.getComentName());
						TextView useless = (TextView) ((Activity) mContext).findViewById(R.id.useless);
						useless.setText(holder.replyText.getTag()+" "+comment.getComentName());
					}
				}, 0, commentName.length() + replyName.length() + commentContent.length() + 3,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				commentReplyNameContent.setSpan(new ForegroundColorSpan(Color.BLUE), 0, commentName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				commentReplyNameContent.setSpan(new ForegroundColorSpan(Color.BLACK), commentName.length(),
						commentName.length() + replyName.length() + commentContent.length() + 3,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				// commentReplyNameContent.setSpan(new
				// ForegroundColorSpan(Color.BLUE),commentName.length()+2,
				// commentName.length()+replyName.length()+2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//
				// commentReplyNameContent.setSpan(new
				// TextSpanClick(commentName), 0, commentName.length(),
				// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//
				// commentReplyNameContent.setSpan(new
				//
				// TextSpanClick(replyName),commentName.length()+2,commentName.length()+replyName.length()+2,
				// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.replyContent.append("\n");
				holder.replyContent.append(commentReplyNameContent);// 评论回复在原先的内容后面添加，即在评论的内容后面追加
			}
		}
		
		holder.replyContent.setMovementMethod(new MyLinkMovementMethod());//这句话仅仅是为了响应点击效果。
		return convertView;
	}
	/**
	 * 弹出软键盘
	 */
	private void showKeyBoard(String name)
	{
		final EditText commentEdit = (EditText)((Activity) mContext).findViewById(R.id.replyEdit);
		commentEdit.setHint("回复"+name);
		commentEdit.requestFocus();
		(new Handler()).postDelayed(new Runnable() 
		{
			 public void run() 
			 {
			 ((InputMethodManager) (commentEdit.getContext()
			                                  .getSystemService(Context.INPUT_METHOD_SERVICE)))
			                                  .toggleSoftInput(0,
			 InputMethodManager.HIDE_NOT_ALWAYS);
			 }
			 }, 100);
	}
	
	private class Holder
	{
		CircleImageView commentImg;
		TextView nickName;
		TextView replyTime;
		TextView replyContent;
		TextView replyText;
		TextView floorId;
	}

	private class MyLinkMovementMethod extends LinkMovementMethod 
	{
		@Override
		public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) 
		{
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_DOWN
					|| action == MotionEvent.ACTION_MOVE) 
			{
				int x = (int) event.getX();
				int y = (int) event.getY();

				x -= widget.getTotalPaddingLeft();// 返回textView的偏移量，如textView设置了padding＝3px，则返回3
				y -= widget.getTotalPaddingTop();

				/**
				 * 左侧超出屏幕的偏移量，如一个view可左右滑动，由于此view比较大，左侧的view被挡住了，
				 * 被挡住的偏移量就是getScrollX()，同理getScrollY()
				 */
				x += widget.getScrollX();
				y += widget.getScrollY();

				// 上述的目的是定位出点击的位置 在整个view组件的绝对坐标（不是屏幕的相对坐标）
				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);// 获取点击位置的 text的行数
				int off = layout.getOffsetForHorizontal(line, x);// 获取点击位置的偏移量

				// 通过偏移量来获取span （具体是通过偏移量对应的span来确定）
				ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
				if (link.length != 0) 
				{
					if (action == MotionEvent.ACTION_UP) 
					{
						link[0].onClick(widget);

						buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), buffer.getSpanStart(link[0]),
								buffer.getSpanEnd(link[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						Selection.removeSelection(buffer);

					} 
					else if (action == MotionEvent.ACTION_DOWN) 
					{

						buffer.setSpan(new BackgroundColorSpan(Color.GRAY), buffer.getSpanStart(link[0]),
								buffer.getSpanEnd(link[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
					} 
					else if (action == MotionEvent.ACTION_MOVE) 
					{

						buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), buffer.getSpanStart(link[0]),
								buffer.getSpanEnd(link[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						Selection.removeSelection(buffer);
					}

					return true;
				} 
				else 
				{
					Selection.removeSelection(buffer);
				}
			}
			//必须returnsuper.onTouchEvent(widget, buffer, event);
			//否则因为重写了onTouchEvent，导致在action_up的时候无法调用ClickableSpan
			return super.onTouchEvent(widget, buffer, event);
		}
	}
}
// public class BBSReplyAdapter extends ArrayAdapter<BBSReply> {
//
// private int resourceId;
// Context context;
//
// public BBSReplyAdapter(Context context, int resource, List<BBSReply> objects)
// {
//
// super(context, resource, objects);
//
// resourceId = resource;
//
// this.context = context;
//
// }
//
// @Override
// public View getView(int position, View convertView, ViewGroup parent) {
//
// BBSReply bbsReply = (BBSReply) getItem(position);
//
// View view;
//
// if (convertView == null)
//
// {
// view = LayoutInflater.from(getContext()).inflate(resourceId, null);
// } else {
// view = convertView;
// }
//
// CircleImageView commentImg = (CircleImageView) view
// .findViewById(R.id.commentItemImg);
//
// TextView nickName = (TextView) view.findViewById(R.id.commentNickname);
//
// TextView replyTime = (TextView) view.findViewById(R.id.commentItemTime);
//
// TextView replyContent = (TextView) view
// .findViewById(R.id.commentItemContent);
//
// commentImg.setImageResource(bbsReply.getImgId());
//
// nickName.setText(bbsReply.getNickname());
//
// replyTime.setText(bbsReply.getReplyTime());
//
// //回复评论的地方
//
// replyContent.setText(bbsReply.getReplyContent() + "\n");
//
// List<BBSComment> commentList = bbsReply.getCommentList();//取出回复评论的集合
//
// if (commentList != null) {
//
// Iterator<BBSComment> it = commentList.iterator();
//
// String commentContent;
// String commentName;
// String replyName;
// SpannableString commentReplyNameContent = null;
//
// while (it.hasNext()) {
//
// BBSComment comment = it.next();
// commentContent = comment.getCommentContent();
// commentName = comment.getComentName();
// replyName = comment.getReplyName();
//
// commentReplyNameContent = new SpannableString(commentName
// + "回复"// +replyName
// + "：" + commentContent);
// commentReplyNameContent.setSpan(new ForegroundColorSpan(
// Color.BLUE), 0, commentName.length(),
// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// // commentReplyNameContent.setSpan(new
// // ForegroundColorSpan(Color.BLUE),commentName.length()+2,
// // commentName.length()+replyName.length()+2,
// // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
// // commentReplyNameContent.setSpan(new
// // TextSpanClick(commentName), 0, commentName.length(),
// // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
// // commentReplyNameContent.setSpan(new
// //
// TextSpanClick(replyName),commentName.length()+2,commentName.length()+replyName.length()+2,
// // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
// replyContent.append(commentReplyNameContent);//评论回复在原先的内容后面添加，即在评论的内容后面追加
// replyContent.append("\n");
//
// }
// }
// replyContent.setMovementMethod(LinkMovementMethod.getInstance());
//
// TextView floorId = (TextView) view.findViewById(R.id.floorid);
// floorId.setText(bbsReply.getFloorId() + "楼");
//
// final TextView replyText = (TextView) view.findViewById(R.id.replyText);
//
// replyText.setTag(position);
//
// /**
// * 回复评论监听
// */
// replyText.setOnClickListener(new OnClickListener() {
//
// public void onClick(View view) {
//
// final EditText commentEdit = (EditText) ((Activity) getContext())
// .findViewById(R.id.replyEdit);
//
// commentEdit.requestFocus();
//
// (new Handler()).postDelayed(new Runnable() {
// public void run() {
//
// ((InputMethodManager) (commentEdit.getContext()
// .getSystemService(Context.INPUT_METHOD_SERVICE)))
// .toggleSoftInput(0,
// InputMethodManager.HIDE_NOT_ALWAYS);
// }
// }, 100);
// // 奇特
//
// TextView useless = (TextView) ((Activity) getContext())
// .findViewById(R.id.useless);
// useless.setText(replyText.getTag() + "");
// }
// });
//
// return view;
//
// }
//
// // 还不需要
// private final class TextSpanClick extends ClickableSpan 
// {
// private String name;
//
// public TextSpanClick(String name) 
// {
// this.name = name;
// }
//
// @Override
// public void updateDrawState(TextPaint ds) 
// {
// super.updateDrawState(ds);
// ds.setUnderlineText(false);
// }
//
// @Override
// public void onClick(View v) {
//
//
//
// makeText(getContext(), name, Toast.LENGTH_SHORT).show();
// 
//
//  ((Activity)getContext()).findViewById(R.id.reply_linear).
//  setVisibility(View.VISIBLE);
//  ((Activity)getContext()).findViewById
//  (R.id.bottom_linear).setVisibility(View.GONE); EditText
//  commentEdit = (EditText)
//  ((Activity)getContext()).findViewById(R.id.replyEdit);
//  commentEdit.requestFocus();
// 
//  //奇特
// 
//  TextView useless = (TextView)
//  ((Activity)getContext()).findViewById(R.id.useless);
//  useless.setText(replyText.getTag()+" "+"false"+" "+name);
// 
// }
// }
//
// }
