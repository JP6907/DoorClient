package neo.company.data;

import java.util.ArrayList;
import java.util.List;

public class CompanyDataManager {

	public static List<CompanyData> createHousekeepingCompanyData(){
		
		List<CompanyData> list = new ArrayList<CompanyData>();
		
		CompanyData data1 = new CompanyData("���ռ���", "������ѹ㳡100��", 5.5, "15365987565");
		CompanyData data2 = new CompanyData("�������", "Ա��ٺϹ㳡163��", 9.4, "18863524958");
		CompanyData data3 = new CompanyData("�������", "���������100��", 6.5, "135654897563");
		CompanyData data4 = new CompanyData("��������", "��خ��ѧ�Ǳ��������3¥305��", 8.3, "18826245496");
		
		list.add(data1);
		list.add(data2);
		list.add(data3);
		list.add(data4);
		
		return list;
	}
	
	
	public static List<CompanyData> createFitCompanyData(){
		
		List<CompanyData> list = new ArrayList<CompanyData>();
		
		CompanyData data1 = new CompanyData("�������յ�ά������", "����·830��", 6.7, "15334987565");
		CompanyData data2 = new CompanyData("�����ҵ�ά��", "����·163��", 9.0, "18863524958");
		CompanyData data3 = new CompanyData("���¿յ�ά���ۺ����", "���������760��", 6.5, "135654897563");
		CompanyData data4 = new CompanyData("��˳��ԡά�޷���", "����·305��", 8.3, "18826245496");
		CompanyData data5 = new CompanyData("�¿��ҵ�������", "�߿���·645��", 8.3, "18346245496");
		
		list.add(data1);
		list.add(data2);
		list.add(data3);
		list.add(data4);
		list.add(data5);
		
		return list;
	}
	
	
	
}
