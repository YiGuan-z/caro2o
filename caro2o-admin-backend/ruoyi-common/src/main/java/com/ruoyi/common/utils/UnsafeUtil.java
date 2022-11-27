package com.ruoyi.common.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author caseycheng
 * @date 2022/11/7-21:34
 **/
public class UnsafeUtil {
	private static final Unsafe unsafe;
	
	static {
		try {
			final Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException("unsafe获取异常，请检查模块权限", e);
		}
	}
	
	public static Unsafe getUnsafe() {
		return unsafe;
	}
}
