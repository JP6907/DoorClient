package neo.door.webutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import neo.door.usermanager.UserConfig;

/**
 * 访问服务器类
 * @author leo
 *
 */
public class WebUtil {
	
	private static final int REQUEST_TIMEOUT = 10*1000;
	private static final int SO_TIMEOUT = 8*1000;
	private static final String BOUNDARY = "---------------------------7db1c523809b2";     //数据分割�?
	
	/**
	 * 请求服务器并得到响应
	 * @param methodName 
	 * @param params 
	 * @return 返回json对象
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray getJsonByWeb(String methodName, JSONArray params) 
			throws IOException, JSONException{
		JSONArray json = null;
		HttpPost httpPost = new HttpPost(Config.WEB + "/DoorServer/servlet/" + methodName);
		httpPost.setEntity(new StringEntity(params.toString(),"utf-8"));
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpPost);
		System.out.println("getStatuCode: "+response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 200){
			String resMsg = EntityUtils.toString(response.getEntity(),"utf-8");
			json = new JSONArray(resMsg);
		}
		if(httpClient != null){
			httpClient.getConnectionManager().shutdown();
		}	
		return json;
	}
	
	/**
	 * 请求服务器并得到登录状�??
	 * @param methodName
	 * @param params
	 * @return 返回1代表成功,0代表失败,-1无网
	 * @throws IOException
	 */
	public static int getLoginStatuByWeb(String methodName, JSONArray params)
			throws IOException{
		int result = -1;
		HttpPost httpPost = new HttpPost(Config.WEB + "/DoorServer/servlet/" + methodName);
		httpPost.setEntity(new StringEntity(params.toString(),"utf-8"));
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode() == 200){
			String resMsg = EntityUtils.toString(response.getEntity(),"utf-8");
			System.out.println(resMsg);
			result = Integer.parseInt(resMsg);
		}
		if(httpClient != null){
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	private static HttpClient getHttpClient(){
		HttpParams params = new BasicHttpParams();
		params.setParameter("charset", "utf-8");
		HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		return new DefaultHttpClient(params);
	}
	/**
	 * 
	 * @param xml 论坛XML格式发�到服务�
	 * @return 
	 * @throws IOException
	 */
	public static String getXmlByWeb(String xml ,String methodName) throws IOException {
		String resMsg = "......";
		HttpPost httpPost = new HttpPost(Config.WEB + "/DoorServer/servlet/" + methodName);
		httpPost.setEntity(new StringEntity(xml, "utf-8"));
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpPost);
		System.out.println("getStatuCode: " + response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			Log.e("WebUtil",getEncoding(response.toString()));
			resMsg = EntityUtils.toString(response.getEntity(),"utf-8");
			
		}
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
		}
		return resMsg;
	}
	
	/**
	 * 上传图片至服务器
	 * @param username 用户�?
	 * @param fileName 图片文件�?
	 * @return 成功返回true, 否则返回false
	 * @throws Exception
	 */
	public static boolean uploadBySocket(Context context, String username, String fileName) {  
	    // 根据path找到SDCard中的文件  
		String filePath = UserConfig.getMyHeadPath();
		System.out.println("****file: " + filePath);
	    File file = new File(filePath, fileName);  
	    // 组装表单字段和文件之前的数据  
	    StringBuilder sb = new StringBuilder();
	    // 传输用户�
	    sb.append("\r\n--" + BOUNDARY + "\r\n");   // �头格式一�
	    sb.append("Content-Disposition: form-data; name=\"username\"" + "\r\n");  
	    sb.append("\r\n");  
	    sb.append(username + "\r\n"); 
	    // 传输文件
	    sb.append("--" + BOUNDARY + "\r\n");  
	    sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + "\r\n");  
	    sb.append("Content-Type: image/*" + "\r\n");  
	    sb.append("\r\n");    
	    try {
	    	// 文件之前的数�?  
		    byte[] before = sb.toString().getBytes("UTF-8");  
		    // 文件之后的数�?  
		    byte[] after = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");     // 结束格式也一�
		  
		    URL url = new URL(Config.WEB + "/DoorServer/servlet/" + Config.METHOD_UPLOAD);  
		  
		    // 由于HttpURLConnection中会缓存数据, 上传较大文件时会导致内存溢出, �?以我们使用Socket传输  
		    Socket socket = new Socket(url.getHost(), url.getPort());  
		    OutputStream out = socket.getOutputStream();  
		    PrintStream ps = new PrintStream(out, true, "UTF-8");  
		  
		    // 写出请求�?  
		    ps.println("POST /DoorServer/servlet/UploadPicServlet HTTP/1.1");  
		    ps.println("Content-Type: multipart/form-data; boundary=" + BOUNDARY);  
		    ps.println("Content-Length: " + String.valueOf(before.length + file.length() + after.length));  
		    ps.println("Host:" + Config.IP + ":8080");  
		      
		    InputStream in = new FileInputStream(file);  	  
		    // 写出数据  
		    out.write(before);  	  
		    byte[] buf = new byte[1024];  
		    int len;  
		    while ((len = in.read(buf)) != -1) { 
		        out.write(buf, 0, len);  
		    }
		    out.write(after);  
		  
		    in.close();  
		    ps.close();
		    socket.close();
		} catch (IOException e) {
			return false;
		}
	    return true;  
	}  
	
	/**
	 * 获取服务器端的图�?
	 * @param url
	 * @return bitmap
	 */
	public static Bitmap getPic(String url) {
		Bitmap bitmap = null;
		try {
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
			bitmap = BitmapFactory.decodeStream(conn.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	public static String getEncoding(String str) {      
        String encode = "ISO-8859-1";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s = encode;      
               return s;      
            }      
        } catch (Exception exception) {      
        }      
        encode = "GB2312";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s1 = encode;      
               return s1;      
            }      
        } catch (Exception exception1) {      
        }      
        encode = "UTF-8";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s2 = encode;      
               return s2;      
            }      
        } catch (Exception exception2) {      
        }      
        encode = "GBK";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s3 = encode;      
               return s3;      
            }      
        } catch (Exception exception3) {      
        }      
       return "";      
    }      

}



