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

import com.example.communityfunction.informationclass.Notice;

import neo.door.usermanager.UserConfig;

public class NoticeCache {
	private  static File filename;
	public final static int num = 50;
	public static boolean writeCache(List<Notice> l){
		filename= new File(UserConfig.getNoticeCachePath()+"/NoticeCache.txt");
		try{
			JSONArray fjo = new JSONArray();
			for(int i = 0 ; i < l.size() ; i++){
				JSONObject jo = new JSONObject();
				jo.put("id", l.get(i).getId());
				jo.put("date",l.get(i).getDate());
				jo.put("content", l.get(i).getContent());
				jo.put("title",l.get(i).getTitle());
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
	
	public static List<Notice> readCache() throws JSONException, IOException{
		filename= new File(UserConfig.getNoticeCachePath()+"/NoticeCache.txt");
		List<Notice> l =new ArrayList<Notice>();
		
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
				Notice tem = new Notice();
				tem.setContent(fjo.getJSONObject(i).getString("content"));
				tem.setDate(fjo.getJSONObject(i).getString("date"));
				tem.setId(fjo.getJSONObject(i).getLong("id"));
				tem.setTitle(fjo.getJSONObject(i).getString("title"));
				l.add(tem);
			}	
		}
		return l;
		}
}
