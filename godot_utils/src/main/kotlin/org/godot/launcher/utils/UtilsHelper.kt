package org.godot.launcher.utils

import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * Module helper object
 */
object UtilsHelper {

	/**
	 * Class loader object
	 */
	private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

	/**
	 * Get resource from this module
	 *
	 * @param location Target location to get
	 * @return [URL] Resource url
	 */
	@JvmStatic
	fun getResource(location: String): URL? = classLoader.getResource(location)

	/**
	 * Get resource from this module
	 *
	 * @param location Target location to get
	 * @return [URL] Resource url
	 */
	@JvmStatic
	fun getResourceSafe(location: String): URL =
		Objects.requireNonNull(classLoader.getResource(location))

	/**
	 * Get resource from this module
	 *
	 * @param location Target location to get
	 * @return [InputStream] Stream resource
	 */
	@JvmStatic
	fun getResourceStream(location: String): InputStream? = classLoader.getResourceAsStream(location)

	/**
	 * Get resource from this module
	 *
	 * @param location Target location to get
	 * @return [InputStream] Stream resource
	 */
	@JvmStatic
	fun getResourceStreamSafe(location: String): InputStream =
		Objects.requireNonNull(classLoader.getResourceAsStream(location))

	/**
	 * Get properties file from custom location
	 *
	 * @param location Target file location
	 * @return [Properties] property object
	 */
	@JvmStatic
	fun getPropertiesFrom(location: String): Properties {
		val properties = Properties()

		try {
			properties.load(getResourceStream(location))
		} catch (err: Exception) {
			err.printStackTrace()
		}

		return properties
	}

}
