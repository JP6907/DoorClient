package com.example.communityfunction.notice;


import java.text.SimpleDateFormat;
import java.util.List;

import com.example.communityfunction.adapter.NoticeReplyAdapter;
import com.example.communityfunction.informationclass.Notice;
import com.example.communityfunction.informationclass.NoticeComment;
import com.example.communityfunction.informationclass.NoticeReply;
import com.example.communityfunction.informationclass.NoticeSelfReply;
import com.example.communityfunction.myView.NoScrollListView;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class NoticeItemActivity extends Activity{
	
	Boolean isComment = false;	
	NoScrollListView noScrollListView ;
    List<NoticeReply>  replyList ;
	NoticeReplyAdapter adapter;
	EditText commentEdit;
	TextView useless;
	NoticeSelfReply self;
	SpannableString replyCommentContent;
	private View  view ;
	NoticeReply noticeCommentMan;
	Integer clickPosition = 0;
	List<NoticeComment> commentList;
	static Notice notice;
	InputMethodManager imm;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_notice_item_content);
		notice = (Notice) getIntent().getSerializableExtra("noticeData");
		initView();
		initReplyListView();
	    initClick();
	}
	private void initView() 
	{
		TextView title = (TextView)findViewById(R.id.title);
		TextView titleContent = (TextView)findViewById(R.id.notice_content_title1);
		TextView date = (TextView)findViewById(R.id.notice_content_date1);
		TextView content = (TextView)findViewById(R.id.notice_content);
		
		title.setText("小区公告");
		title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.bbs_item_title));
		Log.e("NoticeItemFrahment", "ccc");
		titleContent.setText(notice.getTitle());
		date.setText(notice.getDate());
		content.setText(Html.fromHtml("\u3000\u3000"+notice.getContent()));
//		content.setMovementMethod(LinkMovementMethod.getInstance());
		
		// 回复输入框
		commentEdit = (EditText)findViewById(R.id.replyEdit);
		useless = (TextView)findViewById(R.id.useless);

		replyList = notice.getReplyList();
	}
		
	private void initReplyListView() 
	{

		if (!replyList.isEmpty() && replyList != null) 
		{
			noScrollListView = (NoScrollListView) findViewById(R.id.notice_reply_listview);
			adapter = new NoticeReplyAdapter(this, R.layout.notice_reply_item, replyList);

			noScrollListView.setAdapter(adapter);
			noScrollListView.setOnItemClickListener(new OnItemClickListener() 
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
				{
					noticeCommentMan = replyList.get(position);

					findViewById(R.id.reply_linear).setVisibility(View.VISIBLE);
					findViewById(R.id.bottom_linear).setVisibility(View.GONE);
					commentEdit.requestFocus();
				}

			});

		}

	}
	


	// 注册按键响应
	void initClick() 
	{
		Button replyButton = (Button) findViewById(R.id.replyButton);

		TextView share = (TextView) findViewById(R.id.notice_content_share);
		TextView collection = (TextView) findViewById(R.id.notice_content_collection);

//		comment.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				isComment = true;
//
//				findViewById(R.id.reply_linear).setVisibility(View.VISIBLE);
//				findViewById(R.id.bottom_linear).setVisibility(View.GONE);
//				commentEdit.requestFocus();
//				// 这一步之后commentEdit.setOnFocusChangeListener就会起作用
//
//			}
//		});
		
		share.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				showShare();
			}
		});

		collection.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(NoticeItemActivity.this, "收藏", Toast.LENGTH_SHORT).show();
			}
		});

		replyButton.requestFocus();

		commentEdit.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			public void onFocusChange(View view, boolean hasFocus) {

				final boolean isFocus = hasFocus;

				(new Handler()).postDelayed(new Runnable() 
				{
					public void run() 
					{
						imm = (InputMethodManager) commentEdit.getContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						if (isFocus) 
						{
							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
						} 
						else 
						{
							imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
						}
					}
				}, 100);
			}
		});
		commentEdit.setFocusable(true);// 键盘能够获得焦点
		commentEdit.setFocusableInTouchMode(true);// 触摸能够获得焦点

		commentEdit.setOnKeyListener(new OnKeyListener() 
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
				if (keyCode == KeyEvent.KEYCODE_BACK) 
				{
					findViewById(R.id.reply_linear).setVisibility(View.GONE);
					findViewById(R.id.bottom_linear).setVisibility(View.VISIBLE);
					return true;
				}
				return false;
			}
		});

		replyButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				if (!replyList.isEmpty() && replyList != null) 
				{
					noticeCommentMan = replyList.get(Integer.parseInt(useless.getText().toString()));
					// commentList = noticeCommentMan.getCommentList();
				}

				findViewById(R.id.reply_linear).setVisibility(View.GONE);
				findViewById(R.id.bottom_linear).setVisibility(View.VISIBLE);

				final String editText = commentEdit.getText().toString();
				commentEdit.setText("");

				if (!TextUtils.isEmpty(editText.trim()))
					if (isComment) 
					{
						new AsyncTask<Void, Void, Void>() 
						{
							@SuppressLint("SimpleDateFormat")
							@Override
							protected Void doInBackground(Void... params) 
							{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								// self.setReplyTime(sdf.format(System.currentTimeMillis()));
								// self.setReplyContent(editText);
								/**
								 * self必须克隆，因为只有一个self，添加到list会有影响
								 */
								NoticeSelfReply selfClone = new NoticeSelfReply();
								// selfClone.setImgId(self.getImgId()) ;
								// selfClone.setNickname(self.getNickname());
								// selfClone.setReplyTime(self.getReplyTime());
								// selfClone.setReplyContent(self.getReplyContent());

								replyList.add(selfClone);

								return null;
							}

							@Override
							protected void onPostExecute(Void result) 
							{
								initReplyListView();
								adapter.notifyDataSetChanged();
							}
						}.execute(new Void[] {});
					} 
					else 
					{
						new AsyncTask<Void, Void, Void>() 
						{
							@Override
							protected Void doInBackground(Void... params) 
							{
								SystemClock.sleep(500);

								NoticeComment newComment = new NoticeComment();
								// newComment.setComentName(self.getNickname());
								newComment.setCommentContent(editText);
								// newComment.setReplyName(noticeCommentMan.getNickname());
								commentList.add(newComment);

								return null;
							}

							@Override
							protected void onPostExecute(Void result) 
							{
								adapter.notifyDataSetChanged();
							}
						}.execute(new Void[] {});
					}

				isComment = false;
			}
		});
	}
	
	private void showShare() 
	{
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("分享");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");

		// oks.setImageUrl("http://p1.qqyou.com/touxiang/uploadpic/2011-7/201172222431866818.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(this.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}



