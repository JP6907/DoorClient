package com.example.communityfunction.bbs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.communityfunction.adapter.BBSReplyAdapter;
import com.example.communityfunction.informationclass.BBS;
import com.example.communityfunction.informationclass.BBSComment;
import com.example.communityfunction.informationclass.BBSReply;
import com.example.communityfunction.myView.ActionItem;
import com.example.communityfunction.myView.ImgViewPagerAdapter;
import com.example.communityfunction.myView.NoScrollListView;
import com.example.communityfunction.myView.TitlePopup;
import com.example.communityfunction.tool.ImagesTool;
import com.example.communityfunction.tool.ToolGetBBSReply;
import com.example.communityfunction.tool.ToolPostBBSReply;
import com.example.communityfunction.tool.ToolGetBBSReply.getBbsReplySucceedListener;
import com.example.communityfunction.tool.ToolPostBBSReply.OnUpLoadBbsReplyListener;
import com.neo.huikaimen.R;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.usermanager.UserManager;
import neo.door.utils.ScreenUtil;

public class BBSItemActivity extends Activity 
{
	private final static String DefaultSavePath = "/sdcard/Huikaimeng";

	Boolean isComment = false;
	NoScrollListView noScrollListView;
	List<BBSReply> replyListAll;
	List<BBSReply> replyList;
	BBSReplyAdapter adapter;
	EditText commentEdit;
	TextView useless;
	Button replyButton,backButton;
	SpannableString replyCommentContent;
	BBSReply bbsReplyMan;
	Integer clickPosition = 0;
	List<BBSComment> commentList;
	BBS bbs;
	InputMethodManager imm;
	List<String> bbsPictureList;
	private TitlePopup titlePopup;
	private ImageView comment;
	private LinearLayout imageGroup1,imageGroup2,imageGroup3;
	private TextView replyCount;
	private ImageView imageView;
	private ImageView bigImage;
	private ViewPager pager;//点击图片放大后图片的容器。
	private List<Bitmap> imageBm=new ArrayList<Bitmap>();//传进去ViewPager的bitmap集合
	private int position=-1;//标示imagView的位置
	/**
	 * 展示图片
	 */
	final Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			Bitmap bm=(Bitmap)msg.obj;
			
			final ImageView imageView = new ImageView(BBSItemActivity.this);
			imageView.setLayoutParams(new LayoutParams(ScreenUtil.getScreenSize(BBSItemActivity.this).widthPixels / 3,
					ScreenUtil.getScreenSize(BBSItemActivity.this).widthPixels / 3));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setPadding(5, 5, 5, 5);
			
			imageView.setImageBitmap(bm);
			imageView.setTag(++position);//对于imagView的位置（即第几个imagView）
			imageView.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if(imm.isActive())
						(new Handler()).postDelayed(new Runnable() {
							public void run() 
							{
								imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
							}
						}, 50);
					pager.setVisibility(View.VISIBLE);
					pagerAnimator();
					pager.setAdapter(new ImgViewPagerAdapter(BBSItemActivity.this,imageBm,
							pager));
					pager.setCurrentItem((int) imageView.getTag());
				}
			});
			
			if(imageGroup1.getChildCount()<3)
				imageGroup1.addView(imageView);
			else if(imageGroup2.getChildCount()<3)
				imageGroup2.addView(imageView);
			else if(imageGroup3.getChildCount()<3)
				imageGroup3.addView(imageView);
		}
	};

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_bbs_item_content);
		bbs=(BBS) getIntent().getSerializableExtra("bbsData");
		initTitle();
		initData();
		initView();

		/**
		 * 评论区内容
		 */
		initClick();
	}

	/**
	 * 初始化title
	 */

	private void initTitle() {

		/**
		 * 改变标题内容和布局
		 */
		TextView title = (TextView)findViewById(R.id.title);
		title.setText("帖子");

		// 改过的
		title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.bbs_item_title));
		ImageButton menuButton = (ImageButton)findViewById(R.id.menu_button);

		menuButton.setVisibility(View.VISIBLE);
		menuButton.setImageResource(R.drawable.menu_button_line);

		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				titlePopup.show(view);
			}
		});

		// 实例化标题栏弹窗

		titlePopup = new TitlePopup(this.getApplicationContext(), LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

	}

	/**
	 * ViewPager弹出的动画
	 */
	public void pagerAnimator()
	{
		AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(pager, "alpha", 0f,1.0f))
        .with(ObjectAnimator.ofFloat(pager, "scaleX", 0f,1.0f))
        .with(ObjectAnimator.ofFloat(pager, "scaleY", 0f,1.0f));
        animSet.setDuration(400);
        animSet.start();
	}
	@Override
	public void onBackPressed()
	{
		if(pager.getVisibility()==View.VISIBLE)
		{
			AnimatorSet animSet = new AnimatorSet();
	        animSet.play(ObjectAnimator.ofFloat(pager, "alpha", 1.0f,0f))
	        .with(ObjectAnimator.ofFloat(pager, "scaleX",1.0f,0f))
	        .with(ObjectAnimator.ofFloat(pager, "scaleY",1.0f,0f));
	        animSet.setDuration(400);
	        animSet.start();
	        animSet.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {}
				@Override
				public void onAnimationRepeat(Animator animation) {}
				@Override
				public void onAnimationEnd(Animator animation) 
				{
					pager.setVisibility(View.GONE);
				}
				@Override
				public void onAnimationCancel(Animator animation){}
			});
		}
		else
			super.onBackPressed();
	}
	private void initData() {
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, "转发", R.drawable.title_menulistbtu_compose));
		titlePopup.addAction(new ActionItem(this, "收藏", R.drawable.title_menulistbtn_receiver_normal));
		titlePopup.addAction(new ActionItem(this, "只看楼主", R.drawable.title_menulistbtn_receiver_normal));

	}

	/**
	 * 帖子的内容
	 * 
	 * @param bbs
	 */
	@SuppressWarnings("unchecked")
	private void initView() {
		pager=(ViewPager)findViewById(R.id.viewpager);
		
		comment=(ImageView)findViewById(R.id.comment);

		/**
		 * 评论
		 */
		replyListAll=new ArrayList<BBSReply>();
		replyList=new ArrayList<BBSReply>();
		commentList=new ArrayList<BBSComment>();//全部，还没分
		
		noScrollListView = (NoScrollListView)findViewById(R.id.bbs_reply_listview);
		adapter = new BBSReplyAdapter(this, replyList);
		noScrollListView.setAdapter(adapter);
		/**
		 * 得到评论数据的接口
		 */
		ToolGetBBSReply bbsReplyDataTask=new ToolGetBBSReply(this, null, (int) bbs.getBbsId());
		bbsReplyDataTask.execute();
		bbsReplyDataTask.setOnGetBbsReplySucceedListener(new getBbsReplySucceedListener() {
			
			@Override
			public void showBbsReply(List<BBSReply> BBSReplyList) {
				replyListAll=BBSReplyList;
				if(!replyListAll.isEmpty())
					initReplyListView();
					
			}
		});
	
		// 回复输入框
		/**
		 * 帖子的内容
		 */
		commentEdit = (EditText)findViewById(R.id.replyEdit);
		useless = (TextView)findViewById(R.id.useless);
		TextView titleContent = (TextView)findViewById(R.id.bbs_content_title1);
		titleContent.setText(bbs.getBbsTitle());

		TextView date = (TextView)findViewById(R.id.bbs_content_date1);
		date.setText("发表于"+bbs.getBbsPublishdt());

		TextView content = (TextView)findViewById(R.id.bbs_content);

		content.setText("\u3000\u3000"+Html.fromHtml(bbs.getBbsText()));
		content.setMovementMethod(LinkMovementMethod.getInstance());
		
		replyCount = (TextView)findViewById(R.id.bbs_content_replycount1);

		/**
		 * 帖子的图片展示区域
		 */
		imageGroup1 = (LinearLayout)findViewById(R.id.imagegroup1);
		imageGroup2 = (LinearLayout)findViewById(R.id.imagegroup2);
		imageGroup3 = (LinearLayout)findViewById(R.id.imagegroup3);
		final List<String> bbsPictureList=bbs.getBbsPictureList();
		/**
		 * 从本地加载帖子的图片
		 */
		new Thread() 
		{
			public void run() 
			{
				ImageView imageView = new ImageView(BBSItemActivity.this);
				imageView.setLayoutParams(new LayoutParams(ScreenUtil.getScreenSize(BBSItemActivity.this).widthPixels / 3,
						ScreenUtil.getScreenSize(BBSItemActivity.this).widthPixels / 3));
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(5, 5, 5, 5);
				
				if (bbsPictureList != null)
					for (int i = 0; i < bbsPictureList.size(); i++) 
					{
						if (bbsPictureList.get(i).equals("null"))
							continue;
						String path = null;
						
						if (!bbs.isPostJustNow())
							path = BBSItemActivity.this.DefaultSavePath + "/" + bbs.getBbsPhone() + "/ "
									+ bbs.getBbsPublishdt().replaceAll("[:,\\s+,-]", "") + "/" + bbsPictureList.get(i);
						
						else if(bbs.isPostJustNow())
							path=bbsPictureList.get(i);
						Bitmap bm = null;
						try
						{
							bm = ImagesTool.decodeSampledBitmapFromPath(path, imageView);
						}
						catch(Exception e)
						{
							runOnUiThread(new Runnable() 
							{
								@Override
								public void run() 
								{
									Toast.makeText(BBSItemActivity.this, "找不到图片", Toast.LENGTH_SHORT).show();
								}
							});
						}
						if (bm != null) {
							Message msg = Message.obtain();
							msg.obj = bm;
							imageBm.add(bm);
							mHandler.sendMessage(msg);
						}
					}
			};
		}.start();
		
		TextView authorName = (TextView) findViewById(R.id.authorname);
		authorName.setText(bbs.getBbsPublisher());

		findViewById(R.id.reply_linear).setVisibility(View.VISIBLE);
		findViewById(R.id.bottom_linear).setVisibility(View.GONE);
		
	}



	/**
	 * 评论区内容
	 */
	private void initReplyListView() {

		for(int i=0;i<replyListAll.size();i++)
		{
			if(replyListAll.get(i).getReplyJudge()==1)
				replyList.add(replyListAll.get(i));
			else
			{
				BBSComment bbsComment=new BBSComment();
				bbsComment.setComentName(replyListAll.get(i).getReplyResponder());
				bbsComment.setReplyName(replyListAll.get(i).getReplyPublisher());
				bbsComment.setCommentContent(replyListAll.get(i).getReplyText());
				bbsComment.setReplyTime(replyListAll.get(i).getReplyDateTime());
				bbsComment.setReplyFloor(replyListAll.get(i).getReplyFloor());
				bbsComment.setReplyId(replyListAll.get(i).getReplyId());
				commentList.add(bbsComment);
			}
		}
		
		/**
		 * 回复评论的进行排序
		 */
		for(int i=0;i<commentList.size()-1;i++)
		{
			for(int k=0;k<commentList.size()-1-i;k++)
			{
				if(commentList.get(k).getReplyId()>commentList.get(k+1).getReplyId())
				{
					BBSComment bbsComment=new BBSComment();
					bbsComment=commentList.get(k);
					commentList.set(k, commentList.get(k+1));
					commentList.set(k+1,bbsComment);
				}
			}
		}
		
		/**
		 * 根据replyId自动排序，以此来决定楼层（冒泡法）
		 */
		if (replyList.size() != 0)
			for (int i = 0; i < replyList.size() - 1; i++)
				for (int k = 0; k < replyList.size() - 1 - i; k++) {
					if (replyList.get(k).getReplyId() > replyList.get(k + 1).getReplyId()) {
						BBSReply bbsReply = new BBSReply();
						bbsReply = replyList.get(k);
						replyList.set(k, replyList.get(k + 1));
						replyList.set(k + 1, bbsReply);
					}
				}
		
		replyCount.setText(replyList.size() + ""); // 该帖子被回复的条数
		
		for(int i=0;i<commentList.size();i++)
		{
			replyList.get(commentList.get(i).getReplyFloor()-1).addBbsCommentList(commentList.get(i));
		}
//		if (!replyList.isEmpty() && replyList != null) {
//			noScrollListView = (NoScrollListView)findViewById(R.id.bbs_reply_listview);
//			adapter = new BBSReplyAdapter(this, replyList);
//			noScrollListView.setAdapter(adapter);
//		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 注册按键响应
	 */
	private void initClick() 
	{
		imm = (InputMethodManager) commentEdit.getContext().getSystemService(this.INPUT_METHOD_SERVICE);

		comment.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				useless.setText(1000+"");
				commentEdit.setHint("发表");
				commentEdit.requestFocus();
				(new Handler()).postDelayed(new Runnable() 
				{
					public void run() 
					{
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}, 100);
			}
		});

		replyButton = (Button) findViewById(R.id.replyButton);

		/**
		 * 评论帖子
		 */
		replyButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(View view) {
				
				if(UserManager.getLoginStatu()==false)
				{
					Toast.makeText(BBSItemActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
					return;
				}
				/**
				 * 不包含空格说明不是回复楼层中的评论
				 * 为1000说明评论的是楼主
				 */
				if ( !useless.getText().toString().contains(" ") && 
						Integer.parseInt(useless.getText().toString()) == 1000) 
				{
					isComment = true;//说明了是评论楼主
				}

				(new Handler()).postDelayed(new Runnable() {
					public void run() 
					{
						imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
//						commentEdit.setHint("发表");//键盘消失后，下次直接发的话就是评论楼主了
					}
				}, 100);

				final String editText = commentEdit.getText().toString();

				commentEdit.setText("");// 发送之后剩下的就是空的
				if (!TextUtils.isEmpty(editText.trim())) // ??????????
					if (isComment) 
					{
						final BBSReply bbsReply=new BBSReply();
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						
						bbsReply.setReplyDateTime(""+sdf.format(System.currentTimeMillis()));
						bbsReply.setReplyText(editText);
						bbsReply.setReplyResponder(UserManager.getUsername());//用户姓名。
						
						if (!replyList.isEmpty() && replyList != null)
							bbsReply.setReplyFloor(replyList.size() + 1);//评论所处的楼层
						else
							bbsReply.setReplyFloor(1);

						ToolPostBBSReply replyTask=new ToolPostBBSReply(BBSItemActivity.this);
						replyTask.setOnUpLoadBbsReplyListener(new OnUpLoadBbsReplyListener() 
						{
							@Override
							public void upLoadSuccess(String result) 
							{
								if(result.equals("Success"))
								{
									replyList.add(bbsReply);
									adapter.notifyDataSetChanged();
									replyCount.setText(replyList.size()+"");
								}
							}
						});
						replyTask.execute(bbs.getBbsId(),1,bbsReply.getReplyResponder(),
								bbs.getBbsPublisher(),bbsReply.getReplyFloor(),bbsReply.getReplyText());
					}
				//说明回复的是楼层中的评论
					else if(useless.getText().toString().contains(" "))
					{
						String str[]=useless.getText().toString().split(" ");
						if (!replyList.isEmpty() && replyList != null) 
						{
							bbsReplyMan = replyList.get(Integer.parseInt(str[0]));//str[0]为楼层序号
							useless.setText("1000");
							commentList = bbsReplyMan.getBbsComment();//所有回复楼层的评论
						}
						
						commentEdit.setHint("发表");
						
						BBSComment newComment = new BBSComment();
						newComment.setComentName(UserManager.getUsername());//用户姓名。
						newComment.setCommentContent(editText);
						newComment.setReplyName(str[1]);//str[1]为被回复的人
						newComment.setReplyFloor((int) bbsReplyMan.getReplyFloor());//不能用bbsReplyMan.getReplyId()，这只是排序用
						
						
						commentList.add(newComment);
						
						ToolPostBBSReply replyTask=new ToolPostBBSReply(BBSItemActivity.this);
						replyTask.setOnUpLoadBbsReplyListener(new OnUpLoadBbsReplyListener() 
						{
							@Override
							public void upLoadSuccess(String result) 
							{
								if(result.equals("Success"))
								{
									adapter.notifyDataSetChanged();
								}
							}
						});
						replyTask.execute(bbs.getBbsId(),0,newComment.getComentName(),
								newComment.getReplyName(),newComment.getReplyFloor(),newComment.getCommentContent());
					}
					else 
					{
						if (!replyList.isEmpty() && replyList != null) 
						{
							bbsReplyMan = replyList.get(Integer.parseInt(useless.getText().toString()));
							useless.setText("1000");
							commentList = bbsReplyMan.getBbsComment();//所有回复楼层的评论
						}
						
						commentEdit.setHint("发表");
						
						BBSComment newComment = new BBSComment();
						newComment.setComentName(UserManager.getUsername());//用户姓名。
						newComment.setCommentContent(editText);
						newComment.setReplyName(bbsReplyMan.getReplyResponder());
						newComment.setReplyFloor((int) bbsReplyMan.getReplyFloor());//不能用bbsReplyMan.getReplyId()，这只是排序用
						
						commentList.add(newComment);
						
						ToolPostBBSReply replyTask=new ToolPostBBSReply(BBSItemActivity.this);
						replyTask.setOnUpLoadBbsReplyListener(new OnUpLoadBbsReplyListener() 
						{
							@Override
							public void upLoadSuccess(String result) 
							{
								if(result.equals("Success"))
								{
									adapter.notifyDataSetChanged();
								}
							}
						});
						replyTask.execute(bbs.getBbsId(),0,newComment.getComentName(),
								newComment.getReplyName(),newComment.getReplyFloor(),newComment.getCommentContent());
					}

				isComment = false;
				replyButton.requestFocus();
			}

		});

	}
	
}
