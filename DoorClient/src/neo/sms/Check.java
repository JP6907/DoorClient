package neo.sms;


public class Check {

	/**
	 * �ж��ַ��Ƿ�Ϊ����
	 * @param c �жϵ��ַ���
	 * @return ��Ϊ����, ����true, ����, ����false
	 */
	private static final boolean isChinese(char c)
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
			return true;
		return false;
	}
	
	/**
	 * �ж��ַ����Ƿ�Ϊ����
	 * @param input ������ַ���
	 * @return ��������, ����true, ����, ����false
	 */
	public static boolean isChinese(String input){
		
		char[] c = input.toCharArray();
		for(int i=0; i<c.length; i++){
			if(isChinese(c[i]))
				return true;
		}
		
		return false;
	}
	
	/**
	 * �ж������Ƿ�Ϊ6λ���ֻ���ĸ
	 * @param content ����
	 * @return ��Ϊ6, ����true
	 */
	public static boolean isLegal(String input){
		
		boolean result = true;
		
		if(input.length() < 4 || input.length() > 6)
			result = false;
		
		if(isChinese(input))
			result = false;
			
		return result;
	}
	
	/**
	 * �ж��Ƿ�Ϊ�Ϸ����ֻ�����
	 * @param phoneNumber �ֻ�����
	 * @return �����ֻ�����, ����true
	 */
	public static boolean isPhoneNumber(String phoneNumber){
		
		boolean result = false;
		
		if(phoneNumber.length() == 11){
			result = true;
		}
		
		return result;
	}
}
