package org.godot.launcher;

import org.godot.launcher.utils.UtilsHelper;

import java.util.Properties;

/**
 * Application element helper
 */
public final class AppHelper {

	/**
	 * This class cannot be instanced
	 */
	private AppHelper() {
	}

	/**
	 * Application properties
	 */
	public static final Properties appProperties =
		UtilsHelper.getPropertiesFrom("strings/information.properties");

	/**
	 * Build properties file
	 */
	public static final Properties buildProperties =
		UtilsHelper.getPropertiesFrom("buildProperties.properties");

}
