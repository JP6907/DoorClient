package com.zbar.lib;

public class Utils {

	public static boolean isDoor_id(String result) {
		System.out.println("result:" + result);
		if (result.startsWith("door_")) {
			return true;

		}
		return false;

	}
}
