package neo.door.utils;

import android.text.TextUtils;

public class JudgeInput {
	

	
	/**
	 * �ж��ֻ������Ƿ����
	 * 
	 * @param phoneNums
	 */
	public static boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11)
				&& isMobileNO(phoneNums)) {
			return true;
		}
		return false;
	}

	/**
	 * �ж�һ���ַ�����λ��
	 * @param str
	 * @param length
	 * @return
	 */
	private static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			return false;
		} else {
			return str.length() == length ? true : false;
		}
	}
	
	/**
	 * ��֤�ֻ���ʽ
	 */
	private static boolean isMobileNO(String mobileNums) {
		/*
		 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186
		 * ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}
	
	
	public static boolean judgePassword(String password){
		if(password.length() >= 6){
			return true;
		}
		return false;
	}
	
	public static boolean judgeNewDoorPassword(String newDoorPassword){
		if(newDoorPassword.length() == 6){
			return true;
		}
		return false;
	}
}
