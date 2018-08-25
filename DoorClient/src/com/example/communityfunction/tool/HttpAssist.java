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
	private static final int TIME_OUT = 10 * 10000000; // ��ʱʱ��
	private static final String CHARSET = "utf-8"; // �����ʽ
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";
	public static final String TimeOut = "-2";

	public static String uploadFile(String Url, String userName, String userPhone,
										String title, String content,List<File> files) {
		String BOUNDARY = UUID.randomUUID().toString(); // �߽�ָ���
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������
		String RequestURL = Url;
		//分割
		byte[] end_data = (PREFIX + BOUNDARY + LINE_END).getBytes();
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ��������
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("charset", CHARSET); // ���ñ����ʽ
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
		
			if (files!=null) {
				/**
				 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
				 */
				OutputStream outputSteam = conn.getOutputStream();

				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				

				/*
				 * �ı���� ��ʽ: ------------------------7ds8a9sdsjiuza
				 * Content-Disposition: form-data; name="word" ��!!!�˴�Ҫ��һ��!!!��
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
				 * �ı�2���
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"userPhone\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(userPhone);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				dos.write(end_data);
				/*
				 * �ı�3���
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"title\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(title);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				dos.write(end_data);
				/*
				 * �ı�4���
				 */
				sb = new StringBuffer();
				sb.append("Content-Disposition: form-data; name=\"content\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(content);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				//��ͼƬ����Ϊ0
				//��˴����ü�  end_data
				//�������ʱ���м�
				//��ֹ�ظ�
				if(files.size()>0){
					dos.write(end_data);
					
					//�ϴ��ļ�
					//���һ��ͼƬҪ���ϲ�һ���Ľ�����
					for (int i = 0; i < files.size()-1; i++) {
						System.out.println("i = " + i + "  " + files.get(i).getName());
						sb = new StringBuffer();
						/**
						 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
						 * filename���ļ������֣�������׺���� ����:abc.png
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
					//���һ��ͼƬ
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
				//����
				end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
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
	//��������ת����String
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