package neo.adapter;

import java.util.ArrayList;

import neo.bluetooth.DeviceState;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBluetoothBaseAdapter extends BaseAdapter {
    private ArrayList<DeviceState> list;
    private LayoutInflater mInflater;
  
    public MyBluetoothBaseAdapter(Context context, ArrayList<DeviceState> list) {
    	this.list = list;
		mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder = null;
    	DeviceState item = list.get(position);
        if(convertView == null){
        	convertView = mInflater.inflate(R.layout.bluetooth_list_item, null);          
        	viewHolder=new ViewHolder(
        			(View) convertView.findViewById(R.id.list_child),
        			(TextView) convertView.findViewById(R.id.device)
        	       );
        	convertView.setTag(viewHolder);
        }
        else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }       
        
        if(item.getParied())
        {
        	viewHolder.child.setBackgroundResource(R.color.light_blue);
        }
        else 
        {
        	viewHolder.child.setBackgroundResource(R.color.white);
        }
        viewHolder.msg.setText(item.getMsg());    
        
        return convertView;
    }
    
    class ViewHolder {
    	  protected View child;
          protected TextView msg;
  
          public ViewHolder(View child, TextView msg){
              this.child = child;
              this.msg = msg;        
          }
    }
}
