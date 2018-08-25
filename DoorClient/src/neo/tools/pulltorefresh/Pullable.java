package neo.tools.pulltorefresh;

public interface Pullable
{
	/**
	 * 判断是否可以下拉刷新
	 * @return 返回true, 可以下拉刷新, 否则, 不能
	 */
	boolean canPullDown();
	
	
	boolean canPullUp();
}
