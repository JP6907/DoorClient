package neo.door.webutils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;

public class OpenDoorByGprsTask extends AsyncTask<String, Void, Integer>{
	
	private Handler mHandler;
	
	public OpenDoorByGprsTask(Handler handler) {
		mHandler = handler;
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		Integer result = -1;
		try {
			JSONArray reqArray = new JSONArray();
			reqArray.put(new JSONObject().put("PHONE", params[0])
										 .put("OD_PASS", params[1]) );
			JSONArray respArray = WebUtil.getJsonByWeb(Config.METHOD_OPEN_BY_GPRS, reqArray);
			System.out.println(respArray);
			if(respArray != null) {
				result = respArray.getJSONObject(0).getInt("RESULT");
			}
		} catch (Exception e) {
			result = -1;
		}
		System.out.println("****result: " + result);
		return result;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		switch (result) {
		case 1:
			mHandler.sendEmptyMessage(Config.LOGIN_SUCCEED);
			break;
		case 0:
			mHandler.sendEmptyMessage(Config.LOGIN_FAILED);
			break;			
		case -1:
			mHandler.sendEmptyMessage(Config.LOGIN_NO_NETWORK);
			break;

		default:
			break;
		}
		super.onPostExecute(result);
	}

}