package org.godot.launcher.utils;

/**
 * Interface used to manage nullable elements
 *
 * @param <T> Generic type elements
 */
public interface IRequire<T> {

	/**
	 * Called if result is valid
	 *
	 * @param result Result object
	 */
	void onRequire(T result);

}
