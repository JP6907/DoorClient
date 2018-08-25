package neo.door.inputpass;

import java.util.ArrayList;
import java.util.List;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class InputPassPopupWindow implements OnClickListener{
	
	private TextView mPayBox1, mPayBox2, mPayBox3, mPayBox4, mPayBox5, mPayBox6;
	private ImageView del, zero, one, two, three, four, five,
					  six, seven, eight, nine;
	
	private List<Integer> mList;
	private View mView;
	private OnInputListener mListener;
	private Context mContext;
	
	private void init(View view) {
		mList = new ArrayList<Integer>();
		mPayBox1 = (TextView)view.findViewById(R.id.pay_box1);
		mPayBox2 = (TextView)view.findViewById(R.id.pay_box2);
		mPayBox3 = (TextView)view.findViewById(R.id.pay_box3);
		mPayBox4 = (TextView)view.findViewById(R.id.pay_box4);
		mPayBox5 = (TextView)view.findViewById(R.id.pay_box5);
		mPayBox6 = (TextView)view.findViewById(R.id.pay_box6);
		
		del = (ImageView)view.findViewById(R.id.pay_keyboard_del);
		zero = (ImageView)view.findViewById(R.id.pay_keyboard_zero);
		one = (ImageView)view.findViewById(R.id.pay_keyboard_one);
		two = (ImageView)view.findViewById(R.id.pay_keyboard_two);
		three = (ImageView)view.findViewById(R.id.pay_keyboard_three);
		four = (ImageView)view.findViewById(R.id.pay_keyboard_four);
		five = (ImageView)view.findViewById(R.id.pay_keyboard_five);
		six = (ImageView)view.findViewById(R.id.pay_keyboard_six);
		seven = (ImageView)view.findViewById(R.id.pay_keyboard_seven);
		eight = (ImageView)view.findViewById(R.id.pay_keyboard_eight);
		nine = (ImageView)view.findViewById(R.id.pay_keyboard_nine);
		del.setOnClickListener(this);
		zero.setOnClickListener(this);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		seven.setOnClickListener(this);
		eight.setOnClickListener(this);
		nine.setOnClickListener(this);
	}
	
	@SuppressLint("InflateParams")
	private InputPassPopupWindow(Context context, OnInputListener listener) {
		this.mContext = context;
		this.mListener = listener;
		mView = LayoutInflater.from(mContext).inflate(R.layout.input_pass_window, null);
		init(mView);
	}
	
	public static InputPassPopupWindow getInstance(Context context, OnInputListener listener) {
		return new InputPassPopupWindow(context, listener);
	}
	
	
	public interface OnInputListener {
		void commit(String password);
	}


	@Override
	public void onClick(View v) {
		
		if(mList.size() < 6) {
			switch (v.getId()) {
			case R.id.pay_keyboard_zero:
				mList.add(0);
				break;
				
			case R.id.pay_keyboard_one:
				mList.add(1);
				break;
				
			case R.id.pay_keyboard_two:
				mList.add(2);
				break;
		
			case R.id.pay_keyboard_three:
				mList.add(3);
				break;
				
			case R.id.pay_keyboard_four:
				mList.add(4);
				break;
				
			case R.id.pay_keyboard_five:
				mList.add(5);
				break;
				
			case R.id.pay_keyboard_six:
				mList.add(6);
				break;
				
			case R.id.pay_keyboard_seven:
				mList.add(7);
				break;
				
			case R.id.pay_keyboard_eight:
				mList.add(8);
				break;
				
			case R.id.pay_keyboard_nine:
				mList.add(9);
				break;
				
			case R.id.pay_keyboard_del:
				if(mList.size() > 0){
					mList.remove(mList.size() - 1);
				}
				break;
	
			default:
				break;
			}
			updateUi();
			if(mList.size() == 6) {
				String pass = "";
				for(int i=0; i<mList.size(); i++) {
					pass += mList.get(i);
				}
				mListener.commit(pass);
			}
			}
		}
	
	private void updateUi() {
		if(mList.size()==0){
			mPayBox1.setText("");
			mPayBox2.setText("");
			mPayBox3.setText("");
			mPayBox4.setText("");
			mPayBox5.setText("");
			mPayBox6.setText("");
		}else if(mList.size()==1){
			mPayBox1.setText(mList.get(0)+"");
			mPayBox2.setText("");
			mPayBox3.setText("");
			mPayBox4.setText("");
			mPayBox5.setText("");
			mPayBox6.setText("");
		}else if(mList.size()==2){
			mPayBox1.setText(mList.get(0)+"");
			mPayBox2.setText(mList.get(1)+"");
			mPayBox3.setText("");
			mPayBox4.setText("");
			mPayBox5.setText("");
			mPayBox6.setText("");
		}else if(mList.size()==3){
			mPayBox1.setText(mList.get(0)+"");
			mPayBox2.setText(mList.get(1)+"");
			mPayBox3.setText(mList.get(2)+"");
			mPayBox4.setText("");
			mPayBox5.setText("");
			mPayBox6.setText("");
		}else if(mList.size()==4){
			mPayBox1.setText(mList.get(0)+"");
			mPayBox2.setText(mList.get(1)+"");
			mPayBox3.setText(mList.get(2)+"");
			mPayBox4.setText(mList.get(3)+"");
			mPayBox5.setText("");
			mPayBox6.setText("");
		}else if(mList.size()==5){
			mPayBox1.setText(mList.get(0)+"");
			mPayBox2.setText(mList.get(1)+"");
			mPayBox3.setText(mList.get(2)+"");
			mPayBox4.setText(mList.get(3)+"");
			mPayBox5.setText(mList.get(4)+"");
			mPayBox6.setText("");
		}else if(mList.size()==6){
			mPayBox1.setText(mList.get(0)+"");
			mPayBox2.setText(mList.get(1)+"");
			mPayBox3.setText(mList.get(2)+"");
			mPayBox4.setText(mList.get(3)+"");
			mPayBox5.setText(mList.get(4)+"");
			mPayBox6.setText(mList.get(5)+"");
		}
	}
	
	
	public View getView() {
		return mView;
	}

}
