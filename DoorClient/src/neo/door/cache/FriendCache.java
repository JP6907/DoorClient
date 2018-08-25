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

import neo.door.usermanager.User;
import neo.door.usermanager.UserConfig;

public class FriendCache {
	private  static File filename;
	public static boolean writeCache(List<User> l){
		filename= new File(UserConfig.getFriendCachePath()+"/FriendCache.txt");
		try{
			JSONArray fjo = new JSONArray();
			for(int i = 0 ; i < l.size() ; i++){
				JSONObject jo = new JSONObject();
			
				jo.put("imgpath",l.get(i).getImgPath());
				jo.put("nickname", l.get(i).getNickname());
				jo.put("username",l.get(i).getUsername());
				
				fjo.put(jo);
			}
			FileWriter fw = new FileWriter(filename);
			fw.write(fjo.toString());
			fw.close();
		}	catch (Exception e){
			return false;
		}
		return true;
	}
	
	public static List<User> readCache() throws JSONException, IOException{
		List<User> l =new ArrayList<User>();
		filename= new File(UserConfig.getFriendCachePath()+"/FriendCache.txt");
		
		if(filename.exists()){
			FileInputStream is = new FileInputStream(filename);
			String line = null;
			StringBuffer sb = new StringBuffer("");
			BufferedReader br= new BufferedReader(new InputStreamReader(is));
			while((line = br.readLine())!= null)	sb.append(line);
			br.close();
			is.close();
			JSONArray fjo = new JSONArray(sb.toString());
			for(int i = 0 ; i < fjo.length() ; i++){
				User tem = new User(fjo.getJSONObject(i).getString("username"));
				tem.setImgPath(fjo.getJSONObject(i).getString("imgpath"));
				tem.setNickname(fjo.getJSONObject(i).getString("nickname"));
				l.add(tem);
			}	
		}
		return l;
		}
}
