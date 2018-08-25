package com.example.communityfunction.bbs;

import java.io.File;
import java.util.ArrayList;

import com.example.communityfunction.adapter.PostBBSImageAdapter;
import com.example.communityfunction.adapter.PostBBSImageLoaderAdapter;
import com.example.communityfunction.informationclass.BBS;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.usermanager.UserManager;
import neo.door.utils.ChangeStatusBarColor;

public class PostBbsActivity extends Activity {
	private final int POST_OK=11;
	
	private EditText edTitle;
	private EditText edContent;
	private Button postButton;
	
	private ImageButton back;
	private TextView title;
	private PostBBSImageAdapter mImageAdapter;
	private GridView imageGridView;
	private ArrayList<String> imageFromImageLoader=new ArrayList<String>(); 
	private ArrayList<String> imageSelectedPath=new ArrayList<String>();
	
	
	private BBS bbs;

	private static String TEMP_IMAGE_PATH=Environment.getExternalStorageDirectory().getPath()+"/temp.png";
	private static final int RESULT_CAMERA=200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_postbbs);
		initView();
		
		initGridViewEvent();
		

	}

	/**
	 * ͼƬ����¼�
	 */
	private void initGridViewEvent() 
	{
		imageGridView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				if(arg2<imageSelectedPath.size())
				{
					String [] command=new String[1];
					command[0]="ɾ��";
					ShowDialog(arg2,command);
				}
					
				if(arg2==imageSelectedPath.size() && imageSelectedPath.size()<9)//�������ͼƬ��ť
				{
					String [] command=new String[2];
					command[0]="ѡͼ";
					command[1]="����";
					ShowDialog(arg2,command);
				}
			}
		});
		
	}

	/**
	 * ɾ��ͼƬ�����գ�ѡͼ�Ի���
	 * @param position
	 */
	protected void ShowDialog(final int position,final String []command) 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(PostBbsActivity.this);
		builder.setItems(command, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (command[which]) 
				{
				case "ɾ��":
					mImageAdapter.remove(position);
//					PostBBSImageLoaderAdapter.imageSelec.remove(position);
					break;
				case "����":
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Uri photoUri=Uri.fromFile(new File(TEMP_IMAGE_PATH));
					intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
					startActivityForResult(intent, RESULT_CAMERA);
					break;
				case "ѡͼ":
					toImagePick();
					break;
				}
			}
		}).create().show();
	}
	/**
	 * ͼƬѡ�����
	 */
	public void toImagePick()
	{
		Intent intent=new Intent(PostBbsActivity.this,PostBbsImageLoaderActivity.class);
		Bundle bundle=new Bundle();
		bundle.putInt("imageCount", imageSelectedPath.size());//��ͼƬ����Ŀ��֪ͼƬ������
		intent.putExtras(bundle);
		startActivityForResult(intent, 0);
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {

		// ����״̬��
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);

		edTitle = (EditText) this.findViewById(R.id.note_content_title_right);
		edContent = (EditText) findViewById(R.id.note_content_body_below);

		title = (TextView) findViewById(R.id.title);
		title.setText("����");
		

		back = (ImageButton) findViewById(R.id.back);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});

		imageGridView=(GridView)findViewById(R.id.post_image_gridView);
		mImageAdapter=new PostBBSImageAdapter(PostBbsActivity.this,imageSelectedPath);
		Log.e("PostBbsActivity",""+ imageSelectedPath.size());
		imageGridView.setAdapter(mImageAdapter);
		

		postButton = (Button) findViewById(R.id.post_bbs);

		postButton.setVisibility(View.VISIBLE);

		postButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View view) 
			{
				String userPhone = UserManager.getPhone();
				String userName = UserManager.getUsername();
				String bbsTitle = edTitle.getText().toString().trim();
				String bbsContent = edContent.getText().toString().trim();
				
				if (userName.equals("")) {
					Toast.makeText(PostBbsActivity.this, "  ���ȵ�¼�ʺ�!  ", Toast.LENGTH_LONG).show();
				} else if(bbsTitle.equals("") || bbsContent.equals("")){
					Toast.makeText(PostBbsActivity.this, "  ��������ݲ���Ϊ��!  ", Toast.LENGTH_LONG).show();
				} else if(bbsTitle.toCharArray().length > 15){   //����15��
					Toast.makeText(PostBbsActivity.this, "  ���ⲻ�ܳ���15��!  ", Toast.LENGTH_LONG).show();
				} else if(bbsContent.toCharArray().length > 100){ 
					Toast.makeText(PostBbsActivity.this, "  ���ݲ��ܳ���100��!  ", Toast.LENGTH_LONG).show();
				}else {
//					new PostBBSTask(PostBbsActivity.this).execute(userName ,userPhone, bbsTitle, bbsContent,imageSelectedPath);
					//����ص���̳չʾ
					Intent intent=new Intent();
					intent.putExtra("userPhone",userPhone);
					intent.putExtra("userName",userName);
					intent.putExtra("bbsTitle",bbsTitle);
					intent.putExtra("bbsContent", bbsContent);
					intent.putStringArrayListExtra("imageSelectedPath", imageSelectedPath);
					PostBbsActivity.this.setResult(POST_OK, intent);
					finish();
				}
				
			}
		});
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		
		if(requestCode==0 && resultCode==0)
		{
			imageFromImageLoader=intent.getStringArrayListExtra("imagePath");
			PostBBSImageLoaderAdapter.mSelectedImg.clear();//���ѡ�б��
			for(int i=0;i<imageFromImageLoader.size();i++)
			{
//				imageSelectedPath.add(imageFromImageLoader.get(i));//Ҫ�鿴һ�·��غ�activity�ǲ��ǻ��ٴ�ִ��innitView����Ϊ���������һ�䣬��ô��Ƭ�ͻ��ظ�
				mImageAdapter.add(imageFromImageLoader.get(i));//ִ�������������imageSelectedPath���������𣿲�Ȼ�����ִ�з���setOnItemListener��ôʵ��
//				imageSelectedPath.add(imageFromImageLoader.get(i));ע���������Ľ��淵��ʱ���Ϸ��ķ��ؼ���������е�ѡ�б��
			}//ȡ���Ѿ�ѡ�еı��?Ϊ�μ������setAdapterʧЧ���ѵ���պ�imageSelectedPathҲû�ˣ��ѵ��������õ���ͬ����ַ��
		}
		else if(resultCode==RESULT_OK)
		{
			if(requestCode==RESULT_CAMERA)
			{
//				imageSelectedPath.add(TEMP_IMAGE_PATH);
				mImageAdapter.add(TEMP_IMAGE_PATH);
//				PostBBSImageLoaderAdapter.imageSelec.add(TEMP_IMAGE_PATH);
//				imageSelectedPath.add(TEMP_IMAGE_PATH);
			}
				
		}
	}
	

}
