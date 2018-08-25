package neo.sms;


public class Check {

	/**
	 * 判断字符是否为中文
	 * @param c 判断的字符串
	 * @return 若为中文, 返回true, 否则, 返回false
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
	 * 判断字符串是否为中文
	 * @param input 输入的字符串
	 * @return 若含中文, 返回true, 否则, 返回false
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
	 * 判断输入是否为6位数字或字母
	 * @param content 输入
	 * @return 若为6, 返回true
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
	 * 判断是否为合法的手机号码
	 * @param phoneNumber 手机号码
	 * @return 若是手机号码, 返回true
	 */
	public static boolean isPhoneNumber(String phoneNumber){
		
		boolean result = false;
		
		if(phoneNumber.length() == 11){
			result = true;
		}
		
		return result;
	}
}
