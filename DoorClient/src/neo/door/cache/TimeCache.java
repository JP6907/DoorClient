package neo.door.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.os.Environment;
import neo.door.usermanager.UserConfig;

public class TimeCache {
	
	private static File  file;
	
	@SuppressLint("NewApi")
	public static boolean writeNotice(String tem){
		file = new File(UserConfig.getTimeCachePath()+"/TimeCache.txt");
		try{
			JSONArray fjo;
			FileInputStream is = new FileInputStream(file);
			String line = null;
			StringBuffer sb = new StringBuffer("");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((line = reader.readLine())!=null){
				sb.append(line);
			}
			reader.close();
			is.close();
			fjo = new JSONArray(sb.toString());
			JSONObject jo = fjo.getJSONObject(0);
			jo.put("notice", tem);
			fjo.remove(0);
			fjo.put(jo);
			
			FileWriter fw = new FileWriter(file);
			fw.write(fjo.toString());
			fw.close();
		}	catch (Exception e){
			return false;
		}
		return true;
	}
	
	@SuppressLint("NewApi")
	public static String readNotice() throws FileNotFoundException, JSONException{
		
		JSONArray fjo;
		JSONObject jo = null;
		file = new File(UserConfig.getTimeCachePath()+"/TimeCache.txt");
		
		if(!file.exists()){
			try{
				file.createNewFile();
				fjo = new JSONArray();
				jo = new JSONObject();
				jo.put("notice", "0000-00-00 00:00:00");
				jo.put("bbs", "0000-00-00 00:00:00");
				fjo.put(jo);
				FileWriter fw = new FileWriter(file);
				fw.write(fjo.toString());
				fw.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			try{
				StringBuffer sb = new StringBuffer("");
			    InputStream is = new FileInputStream(file);
			    String line; 
			    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			    while ((line = reader.readLine())!= null) { // 如果 line 为空说明读完了
			            sb.append(line);
			     }
			    reader.close();
			    is.close();
				fjo = new JSONArray(sb.toString());
				jo = fjo.getJSONObject(0);			
			}	catch(Exception e){
				e.printStackTrace();
			}
		}
		return jo.getString("notice");
	}
	
	@SuppressLint("NewApi")
	public static boolean writeBBS(String tem){
		file = new File(UserConfig.getTimeCachePath()+"/TimeCache.txt");
		try{
			FileInputStream is = new FileInputStream(file);
			String line = null;
			StringBuffer sb = new StringBuffer("");
			BufferedReader br= new BufferedReader(new InputStreamReader(is));
			while((line = br.readLine())!= null){
				sb.append(line);
			}
			br.close();
			is.close();
			JSONArray fjo = new JSONArray(sb.toString());
			JSONObject jo = fjo.getJSONObject(0);
			jo.put("bbs", tem);
			fjo.remove(0);
			fjo.put(jo);
			
			FileWriter fw = new FileWriter(file);
			fw.write(fjo.toString());
			fw.close();
		}	catch (Exception e){
			return false;
		}
		return true;
	}
	
	@SuppressLint("NewApi")
	public static String readBBS() throws JSONException{
		file = new File(UserConfig.getTimeCachePath()+"/TimeCache.txt");
		JSONArray fjo;
		JSONObject jo = null;
		
		if(!file.exists()){
			try{
				file.createNewFile();
				fjo = new JSONArray();
				jo = new JSONObject();
				jo.put("notice", "0000-00-00 00:00:00");
				jo.put("bbs", "0000-00-00 00:00:00");
				fjo.put(jo);
				FileWriter fw = new FileWriter(file);
				fw.write(fjo.toString());
				fw.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			try{
				StringBuffer sb = new StringBuffer("");
			    InputStream is = new FileInputStream(file);
			    String line; 
			    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			    while ((line = reader.readLine())!= null) { // 如果 line 为空说明读完了
			            sb.append(line);
			     }
			    reader.close();
			    is.close();
				fjo = new JSONArray(sb.toString());
				jo = fjo.getJSONObject(0);			
			}	catch(Exception e){
				e.printStackTrace();
			}
		}
		return jo.getString("bbs");
	}
	
}
