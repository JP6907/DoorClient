package neo.door.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.communityfunction.informationclass.BBS;

public class XmlHandle {
	public static String packXml(String userName ,String title, String content, String time) {
		if(title.equals("")){
			title = " ";
		}
		if(content.equals("")){
			content = " ";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<BbsInformation>");
		sb.append("    <userName>" + userName + "</userName>");
		sb.append("    <title>" + title + "</title>");
		sb.append("    <content>" + content + "</content>");
		sb.append("    <time>" + time + "</time>");
		sb.append("</BbsInformation>");
		return sb.toString();
	}

	/*
	 * ��������Ϣ�����xml��ʽ
	 */
	public static String packXmls(String node1, String node1Value, String node2, String node2Value, String node3 , String node3Value) {
		if (node1Value.equals("")) {
			node1Value = " ";
		}
		if (node2Value.equals("")) {
			node2Value = " ";
		}
		if (node3Value.equals("")) {
			node3Value = " ";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Information>");
		sb.append("    <" + node1 + ">" + node1Value + "</" + node1 + ">");
		sb.append("    <" + node2 + ">" + node2Value + "</" + node2 + ">");
		sb.append("    <" + node3 + ">" + node3Value + "</" + node3 + ">");
		sb.append("</Information>");
		return sb.toString();
	}
	public static String getTime(String xml) throws Exception{
		String time = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(xml)));
		NodeList list = document.getElementsByTagName("BBSInformation");
		
			Element element = (Element) list.item(0);
			NodeList childNodes = element.getChildNodes();
			for(int k = 0 ;k<childNodes.getLength();k++){
				Node node = childNodes.item(k);
			//���PostProfile�ڵ�
			if(node.getNodeName().equals("Time")){
				NodeList BBSChildNodeList = node.getChildNodes();
				for (int j = 0; j < BBSChildNodeList.getLength(); j++) {
					Node node1 = BBSChildNodeList.item(j);
					if ("time".equals(node1.getNodeName())) {
						time = node1.getFirstChild().getNodeValue();
					} 
				}
			}
		}
		return time;
	}
	
	public static Set<Long> getDeletedSet(String xml) throws Exception{
		Set<Long> s = new HashSet<Long>();
		String deletedId = null;
		s.clear();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(xml)));
		NodeList list = document.getElementsByTagName("BBSInformation");
		
			Element element = (Element) list.item(0);
			NodeList childNodes = element.getChildNodes();
			for(int k = 0 ;k<childNodes.getLength();k++){
				Node node = childNodes.item(k);
			
			if(node.getNodeName().equals("DeletedId")){
				NodeList BBSChildNodeList = node.getChildNodes();
				for (int j = 0; j < BBSChildNodeList.getLength(); j++) {
					Node node1 = BBSChildNodeList.item(j);
					if ("deletedId".equals(node1.getNodeName())) {
						 deletedId = node1.getFirstChild().getTextContent();
					} 
				}
			}
		}
			String[] tem = deletedId.split(",");
			if(deletedId.charAt(0) == '-'){
				s.add(Long.valueOf(-1));
			}
			else if(deletedId.charAt(0) != 'n'){
				for(int i = 0 ; i < tem.length ; i++){
					s.add(Long.valueOf(tem[i]));
				}
			}
		return s;
	}
	
	
	/**
	 * ��xml�ļ���ȡ��������
	 * @param xml
	 * @return List<PostProfile>
	 * @throws Exception
	 */
	
	public static List<BBS> getBBSPostDate(String xml) throws Exception {
		List<BBS> bbslist = new ArrayList<BBS>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(xml)));
		NodeList list = document.getElementsByTagName("BBSInformation");
		
			Element element = (Element) list.item(0);
			NodeList childNodes = element.getChildNodes();
			for(int k = 0 ;k<childNodes.getLength();k++){
				Node node = childNodes.item(k);
			//���PostProfile�ڵ�
			if(node.getNodeName().equals("PostProfile")){
				NodeList BBSChildNodeList = node.getChildNodes();
				BBS bbs = new BBS(); 
				for (int j = 0; j < BBSChildNodeList.getLength(); j++) {
					Node node1 = BBSChildNodeList.item(j);
					if ("post_id".equals(node1.getNodeName())) {
						String post_id = node1.getFirstChild().getNodeValue();
						bbs.setBbsId(Integer.valueOf(post_id));
					} else if ("post_title".equals(node1.getNodeName())) {
						String post_title = node1.getFirstChild().getNodeValue();
						bbs.setBbsTitle(post_title);
					} else if ("post_text".equals(node1.getNodeName())) {
						String post_text = node1.getFirstChild().getNodeValue();
						bbs.setBbsText(post_text);
					} else if ("post_publisher".equals(node1.getNodeName())) {
						String post_publisher = node1.getFirstChild().getNodeValue();
						bbs.setBbsPublisher(post_publisher);
					} else if ("post_replynum".equals(node1.getNodeName())) {
						String post_replynum = node1.getFirstChild().getNodeValue();
						bbs.setBbsReplyNum(Integer.valueOf(post_replynum));
					} else if ("post_publishdt".equals(node1.getNodeName())) {
						String post_publishdt = node1.getFirstChild().getNodeValue();
						bbs.setBbsPublishdt(post_publishdt);
					} else if ("post_newdt".equals(node1.getNodeName())) {
						String post_newdt = node1.getFirstChild().getNodeValue();
						bbs.setBbsNewDt(post_newdt);
					} else if ("post_picture1".equals(node1.getNodeName())) {
						String post_picture1 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture1);
					} else if ("post_picture2".equals(node1.getNodeName())) {
						String post_picture2 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture2);;
					} else if ("post_picture3".equals(node1.getNodeName())) {
						String post_picture3 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture3);
					} else if ("post_picture4".equals(node1.getNodeName())) {
						String post_picture4 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture4);
					} else if ("post_picture5".equals(node1.getNodeName())) {
						String post_picture5 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture5);
					} else if ("post_picture6".equals(node1.getNodeName())) {
						String post_picture6 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture6);
					} else if ("post_picture7".equals(node1.getNodeName())) {
						String post_picture7 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture7);
					} else if ("post_picture8".equals(node1.getNodeName())) {
						String post_picture8 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture8);
					} else if ("post_picture9".equals(node1.getNodeName())) {
						String post_picture9 = node1.getFirstChild().getNodeValue();
						bbs.addBbsPictureList(post_picture9);
					} else if ("post_phone".equals(node1.getNodeName())) {
						String post_phone = node1.getFirstChild().getNodeValue();
						bbs.setBbsPhone(post_phone);
					}
				}
				bbslist.add(bbs);
			}
		}
		return bbslist;
		
	}
	
	public static String praseXml(String xml) throws Exception {
		String result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(xml)));
		NodeList list = document.getElementsByTagName("BbsInformation");
		for (int i = 0; i < list.getLength(); i++) {
			Element element = (Element) list.item(i);
			result = element.getElementsByTagName("PostResult").item(0).getFirstChild().getNodeValue();
		}
		return result;
	}
}
