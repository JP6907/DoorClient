package neo.adapter;

import java.util.List;

import com.example.communityfunction.fix.FixFragment;
import com.neo.huikaimen.R;

import neo.company.data.CompanyData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCompanyBaseAdapter extends BaseAdapter{

	
	List<CompanyData> list;
	private Context mContext;
	
	public MyCompanyBaseAdapter(Context context, List<CompanyData> l) {
		mContext = context;
		list = l;
	}
	
	/**
	 * 加入数据
	 * @param date
	 */
	public void addItem(CompanyData date){
		list.add(date);
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public CompanyData getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null){
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_company_item, null);
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.company_name);
			holder.address = (TextView)convertView.findViewById(R.id.company_address);
			holder.score = (TextView)convertView.findViewById(R.id.company_score);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		// 填充数据
		holder.name.setText(list.get(position).getName());
		holder.address.setText(list.get(position).getAddress());
		holder.score.setText(Double.toString(list.get(position).getScore()));
		
		return convertView;
	}
	
	private class ViewHolder{
		
		TextView name;
		TextView address;
		TextView score;
	}

}
