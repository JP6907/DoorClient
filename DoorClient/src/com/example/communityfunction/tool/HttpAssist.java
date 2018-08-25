package com.example.communityfunction.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import android.util.Log;

public class HttpAssist {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 10000000; // 超时时间
	private static final String CHARSET = "utf-8"; // 编码格式
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";
	public static final String TimeOut = "-2";

	public static String uploadFile(String Url, String userName, String userPhone,
										String title, String content,List<File> files) {
		String BOUNDARY = UUID.randomUUID().toString(); // 边界分隔符
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String RequestURL = Url;
		//鍒嗗壊
		byte[] end_data = (PREFIX + BOUNDARY + LINE_END).getBytes();
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("charset", CHARSET); // 设置编码格式
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
		
			if (files!=null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				OutputStream outputSteam = conn.getOutputStream();

				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				

				/*
				 * 文本输出 格式: ------------------------7ds8a9sdsjiuza
				 * Content-Disposition: form-data; name="word" （!!!此处要空一行!!!）
				 * Hello Word! ------------------------7ds8a9sdsjiuza
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"userName\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(userName);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				dos.write(end_data);
				// dos.flush();
				/*
				 * 文本2输出
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"userPhone\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(userPhone);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				dos.write(end_data);
				/*
				 * 文本3输出
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"title\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(title);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				dos.write(end_data);
				/*
				 * 文本4输出
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"content\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(content);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				//若图片数量为0
				//则此处不用加  end_data
				//后面结束时已有加
				//防止重复
				if(files.size()>0){
					dos.write(end_data);
					
					//上传文件
					//最后一张图片要加上不一样的结束符
					for (int i = 0; i < files.size()-1; i++) {
						System.out.println("i = " + i + "  " + files.get(i).getName());
						sb = new StringBuffer();
						/**
						 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
						 * filename是文件的名字，包含后缀名的 比如:abc.png
						 */
	
						sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + files.get(i).getName() + "\""
								+ LINE_END);
						sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
						sb.append(LINE_END);
						dos.write(sb.toString().getBytes());
						InputStream is = new FileInputStream(files.get(i));
						byte[] bytes = new byte[1024];
						int len = 0;
						while ((len = is.read(bytes)) != -1) {
							dos.write(bytes, 0, len);
						}
						is.close();
						dos.write(LINE_END.getBytes());
						dos.write(end_data);
					}
					//最后一张图片
					sb = new StringBuffer();
					sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + files.get(files.size()-1).getName() + "\""
							+ LINE_END);
					sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
					InputStream is = new FileInputStream(files.get(files.size()-1));
					byte[] bytes = new byte[1024];
					int len = 0;
					while ((len = is.read(bytes)) != -1) {
						dos.write(bytes, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());
				}
				//结束
				end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if (res == 200) {
					String responMes = conn.getResponseMessage();
					responMes = convertStreamToString(conn.getInputStream());
					return responMes;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e("e", e.toString());
			return FAILURE;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("e", e.toString());
			return FAILURE;
		}
		return FAILURE;
	}
	//将输入流转化成String
	public static String convertStreamToString(InputStream is){
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try{
			while((line = reader.readLine())!=null){
				sb.append(line + "\n");
			}
		}catch(Exception e){
			return null;
		}
		return sb.toString();
	}
}