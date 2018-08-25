package neo.door.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.communityfunction.informationclass.BBS;

import android.os.Environment;
import android.util.Log;
import neo.door.usermanager.UserConfig;

public class BBSCache {
	public final static int num = 50;
	private static File file;
	public static boolean addwrite(List<BBS> BBSlist) throws Exception{
		file = new File(UserConfig.getBBSCachePath()+"/bbsCache.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//得到 DOM 解析器的工厂实例
		DocumentBuilder builder = factory.newDocumentBuilder();//调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象  
		Document doc = builder.newDocument(); //builder创建新文档
		Element root = doc.createElement("Information");
		doc.appendChild(root);
		for(BBS bbs : BBSlist)
		{
			Element ee = doc.createElement("BBS");
			
			Element Element1 = doc.createElement("bbs_id");
			Element1.setTextContent(String.valueOf(bbs.getBbsId()));
			ee.appendChild(Element1);
			
			Element Element2 = doc.createElement("bbs_title");
			Element2.setTextContent(bbs.getBbsTitle());
			ee.appendChild(Element2);

			Element Element3 = doc.createElement("bbs_text");
			Element3.setTextContent(bbs.getBbsText());
			ee.appendChild(Element3);

			Element Element4 = doc.createElement("bbs_publisher");
			Element4.setTextContent(bbs.getBbsPublisher());
			ee.appendChild(Element4);
			
			Element Element5 = doc.createElement("bbs_replynum");
			Element5.setTextContent(String.valueOf(bbs.getBbsReplyNum()));
			ee.appendChild(Element5);
			
			Element Element6 = doc.createElement("bbs_publishdt");
			Element6.setTextContent(bbs.getBbsPublishdt());
			ee.appendChild(Element6);

			Element Element7 = doc.createElement("bbs_newdt");
			Element7.setTextContent(bbs.getBbsNewDt());
			ee.appendChild(Element7);
			
			Element Element8 = doc.createElement("bbs_picture1");
			
			Element8.setTextContent(bbs.getBbsPictureList().get(0));
			ee.appendChild(Element8);

			Element Element9 = doc.createElement("bbs_picture2");
			Element9.setTextContent(bbs.getBbsPictureList().get(1));
			ee.appendChild(Element9);
			
			Element Element10 = doc.createElement("bbs_picture3");
			Element10.setTextContent(bbs.getBbsPictureList().get(2));
			ee.appendChild(Element10);
//			Log.e("hhhhjj",  BBSlist.get(3).getBbsText());//只输出两次，说明第三次有问题
			Element Element11 = doc.createElement("bbs_picture4");
			Element11.setTextContent(bbs.getBbsPictureList().get(3));
			ee.appendChild(Element11);
			
			Element Element12 = doc.createElement("bbs_picture5");
			Element12.setTextContent(bbs.getBbsPictureList().get(4));
			ee.appendChild(Element12);
			
			Element Element13 = doc.createElement("bbs_picture6");
			Element13.setTextContent(bbs.getBbsPictureList().get(5));
			ee.appendChild(Element13);
		
			Element Element14 = doc.createElement("bbs_picture7");
			Element14.setTextContent(bbs.getBbsPictureList().get(6));
			ee.appendChild(Element14);
			
			Element Element15 = doc.createElement("bbs_picture8");
			Element15.setTextContent(bbs.getBbsPictureList().get(7));
			ee.appendChild(Element15);
			
			Element Element16 = doc.createElement("bbs_picture9");
			Element16.setTextContent(bbs.getBbsPictureList().get(8));
			ee.appendChild(Element16);
			
			Element Element17 = doc.createElement("bbs_phone");
			Element17.setTextContent(bbs.getBbsPhone());
			ee.appendChild(Element17);
//			Log.e("hhhhjj",  BBSlist.get(4).getBbsText());
			root.appendChild(ee);
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		
		try{
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
			FileOutputStream fos = new FileOutputStream(file,false);
			fos.write(sw.toString().getBytes());
			fos.close();Log.e("ok3", "ok3");
		} catch (TransformerConfigurationException e) {  
            System.out.println(e.getMessage());  
        } catch (IllegalArgumentException e) {  
            System.out.println(e.getMessage());  
        } catch (FileNotFoundException e) {  
            System.out.println(e.getMessage());  
        } catch (TransformerException e) {  
            System.out.println(e.getMessage());  
        }
		return true;  
		
		}
	
	public static List<BBS> read(){
		List l = new ArrayList<BBS>();
		file = new File(UserConfig.getBBSCachePath()+"/bbsCache.xml");
		if(file.exists()){
			try{
				
				DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
				DocumentBuilder DB = DBF.newDocumentBuilder();
				Document doc = DB.parse(file);
				Element root = doc.getDocumentElement();
				NodeList nl = root.getElementsByTagName("BBS");
				for(int k = 0 ;k<nl.getLength();k++){
					Node node = nl.item(k);
				//获得BBS节点
					Log.e("x", "fghjkl");
					NodeList bbsChildNodeList = node.getChildNodes();
					BBS bbs = new BBS(); 
					for (int j = 0; j < bbsChildNodeList.getLength(); j++) {
						Node node1 = bbsChildNodeList.item(j);
						if ("bbs_id".equals(node1.getNodeName())) {
							String bbs_id = node1.getTextContent();
							bbs.setBbsId(Integer.valueOf(bbs_id));
						} else if ("bbs_title".equals(node1.getNodeName())) {
							String bbs_title = node1.getTextContent();
							bbs.setBbsTitle(bbs_title);
						} else if ("bbs_text".equals(node1.getNodeName())) {
							String bbs_text = node1.getTextContent();
							bbs.setBbsText(bbs_text);
							Log.e("hhhhhhhhh", bbs_text);
						} else if ("bbs_publisher".equals(node1.getNodeName())) {
							String bbs_publisher = node1.getTextContent();
							bbs.setBbsPublisher(bbs_publisher);
						} else if ("bbs_replynum".equals(node1.getNodeName())) {
							String bbs_replynum = node1.getTextContent();
							bbs.setBbsReplyNum(Integer.valueOf(bbs_replynum));
						} else if ("bbs_publishdt".equals(node1.getNodeName())) {
							String bbs_publishdt = node1.getTextContent();
							bbs.setBbsPublishdt(bbs_publishdt);
						} else if ("bbs_newdt".equals(node1.getNodeName())) {
							String bbs_newdt = node1.getTextContent();
							bbs.setBbsNewDt(bbs_newdt);
						} else if ("bbs_picture1".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture2".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture3".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture4".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture5".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture6".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture7".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture8".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_picture9".equals(node1.getNodeName())) {
							String bbs_picture = node1.getTextContent();
							if(bbs_picture!="null")	bbs.addBbsPictureList(bbs_picture);
						} else if ("bbs_phone".equals(node1.getNodeName())) {
							String bbs_phone = node1.getTextContent();
							bbs.setBbsPhone(bbs_phone); 
						}
					}
					l.add(bbs);
				}
				}catch (Exception e){
					e.printStackTrace();
				}
		}
		
		return l;
	}
	private static Document createDocument(){
		file = new File(UserConfig.getBBSCachePath()+"/bbsCache.xml");
		Document doc =null;
		try{
			DocumentBuilderFactory dbfc = DocumentBuilderFactory.newInstance();
			DocumentBuilder docb = dbfc.newDocumentBuilder();
			 doc = docb.parse(file);
			doc.setXmlStandalone(true);
		}catch (IOException e){
			e.printStackTrace();
		}catch (SAXException e){
			e.printStackTrace();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}
		return doc;
	}
}
