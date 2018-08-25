package com.neo.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.example.communityfunction.tool.HttpAssist;

import neo.door.webutils.Config;

public class BBSTest {
	@Test
	public void test(){
		String s = "http://192.168.1.113:8092/DoorServer/servlet/PostBbsServlet";
		List<File> files = new ArrayList<File>();
		File file = new File("E:/pic/test1.png");
		files.add(file);
		file = new File("E:/pic/test2.jpg");
		files.add(file);
		System.out.println(files.size());
		String result = 
				HttpAssist.uploadFile(s, "peng", "15521374237", "Hello", "HelloWorld", files);
		System.out.print(result);
	}
}
