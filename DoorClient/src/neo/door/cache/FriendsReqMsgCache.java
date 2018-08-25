package neo.door.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chat.Activity.FriendsReqMsg;
import com.example.chat.Activity.FriendsReqMsg.MsgState;
import com.hyphenate.chat.EMClient;

import android.util.Log;
import android.widget.Toast;
import neo.door.usermanager.UserConfig;

public class FriendsReqMsgCache 
{
	private static File filename;
	
	public static boolean writeCache(List<FriendsReqMsg> reqMsgList)
	{
		filename=new File(UserConfig.getMessageCachePath()+"/FriendReqMassage.txt");
		try
		{
			JSONArray array=new JSONArray();
			for(int i=0;i<reqMsgList.size();i++)
			{
				FriendsReqMsg reqMsg=reqMsgList.get(i);
				JSONObject ob=new JSONObject();
				ob.put("name", reqMsg.getName());
				ob.put("reason", reqMsg.getReason());
				ob.put("msgStateOrder", reqMsg.getState().ordinal());
				
				array.put(ob);
			}
			
			FileWriter fw=new FileWriter(filename);
			fw.write(array.toString());
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static List<FriendsReqMsg> readCache() throws JSONException,IOException
	{
		filename=new File(UserConfig.getMessageCachePath()+"/FriendReqMassage.txt");
		List<FriendsReqMsg> reqMsgsList=new ArrayList<FriendsReqMsg>();
		
		if(filename.exists())
		{
			FileInputStream in=new FileInputStream(filename);
			String line=null;
			StringBuffer sb=new StringBuffer("");
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			
			while((line=br.readLine())!=null)
				sb.append(line);
			br.close();
			in.close();
			
			JSONArray array=new JSONArray(sb.toString());
			for(int i=0;i<array.length();i++)
			{
				JSONObject ob=new JSONObject();
				ob=array.getJSONObject(i);
				FriendsReqMsg reqMsg=new FriendsReqMsg();
				reqMsg.setName(ob.getString("name"));
//				reqMsg.setReason(ob.getString("reason"));
				if(ob.getInt("msgStateOrder")==MsgState.BEAGREED.ordinal())
					reqMsg.setState(MsgState.BEAGREED);
				else if(ob.getInt("msgStateOrder")==MsgState.BEINVITEED.ordinal())
					reqMsg.setState(MsgState.BEINVITEED);
				else if(ob.getInt("msgStateOrder")==MsgState. BEREFUSED.ordinal())
					reqMsg.setState(MsgState.BEREFUSED);
				else if(ob.getInt("msgStateOrder")==MsgState. AGREED.ordinal())
					reqMsg.setState(MsgState.AGREED);
				else if(ob.getInt("msgStateOrder")==MsgState. REFUSED.ordinal())
					reqMsg.setState(MsgState.REFUSED);
				reqMsgsList.add(reqMsg);
			}
		}
		return reqMsgsList;
		
	}
}
