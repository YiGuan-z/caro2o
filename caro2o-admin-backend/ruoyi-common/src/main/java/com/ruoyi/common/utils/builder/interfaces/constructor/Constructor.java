package com.ruoyi.common.utils.builder.interfaces.constructor;

/**
 * 无参构造
 * @param <T>
 */
@FunctionalInterface
public interface Constructor<T> extends Const<T> {
	T get();
}