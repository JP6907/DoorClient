package neo.tools.pulltorefresh;

public interface Pullable
{
	/**
	 * �ж��Ƿ��������ˢ��
	 * @return ����true, ��������ˢ��, ����, ����
	 */
	boolean canPullDown();
	
	
	boolean canPullUp();
}
