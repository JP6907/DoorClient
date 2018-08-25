package neo.company.data;

import java.util.ArrayList;
import java.util.List;

public class CompanyDataManager {

	public static List<CompanyData> createHousekeepingCompanyData(){
		
		List<CompanyData> list = new ArrayList<CompanyData>();
		
		CompanyData data1 = new CompanyData("明日家政", "天河正佳广场100号", 5.5, "15365987565");
		CompanyData data2 = new CompanyData("保洁家政", "员村百合广场163号", 9.4, "18863524958");
		CompanyData data3 = new CompanyData("明宇家政", "增城新天地100号", 6.5, "135654897563");
		CompanyData data4 = new CompanyData("哈啦家政", "番禺大学城贝冈新天地3楼305号", 8.3, "18826245496");
		
		list.add(data1);
		list.add(data2);
		list.add(data3);
		list.add(data4);
		
		return list;
	}
	
	
	public static List<CompanyData> createFitCompanyData(){
		
		List<CompanyData> list = new ArrayList<CompanyData>();
		
		CompanyData data1 = new CompanyData("大金中央空调维修中心", "民生路830号", 6.7, "15334987565");
		CompanyData data2 = new CompanyData("永进家电维修", "长柳路163号", 9.0, "18863524958");
		CompanyData data3 = new CompanyData("松下空调维修售后服务", "增城新天地760号", 6.5, "135654897563");
		CompanyData data4 = new CompanyData("民顺卫浴维修服务", "锦绣路305号", 8.3, "18826245496");
		CompanyData data5 = new CompanyData("月康家电清洁服务", "高科西路645号", 8.3, "18346245496");
		
		list.add(data1);
		list.add(data2);
		list.add(data3);
		list.add(data4);
		list.add(data5);
		
		return list;
	}
	
	
	
}
