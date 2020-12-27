package org.godot.launcher.utils.serialize.ini

import kotlinx.serialization.Serializable
import java.util.*
import kotlin.collections.HashSet

@Serializable
class Section : ICaseSensitive {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Default section instance
	 */
	lateinit var defaultSection: Section

	/**
	 * Check if section is case sensitive.
	 * You can change this property
	 */
	override var isCaseSensitive: Boolean = false

	/**
	 * Chek if section is empty
	 */
	val isEmpty: Boolean
		get() = properties.isEmpty()

	/**
	 * Get immutable property map
	 */
	val immutableProperties: Map<String, String>
		get() = properties.toMap()

	/* ---------------------------------------------------------
	 *
	 * Internal properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * This property contains all file properties
	 */
	private val properties: MutableMap<String, String> = mutableMapOf()

	/* ---------------------------------------------------------
	 *
	 * Public methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get property keys from section
	 *
	 * @param includeDefault Include default section in list
	 * @return [Set] Immutable map with all keys
	 */
	fun getKeys(includeDefault: Boolean = true): Set<String> = if (!includeDefault) {
		Collections.unmodifiableSet(properties.keys)
	} else {
		HashSet<String>(properties.keys).also {
			if (this::defaultSection.isInitialized)
				it.addAll(defaultSection.getKeys(false))
		}
	}

	/**
	 * Check if section contains specific key
	 *
	 * @param key Target key to search
	 * @param includeDefault Include default section
	 */
	fun containsKey(key: String, includeDefault: Boolean = true): Boolean =
		getKeys(includeDefault).contains(transformKey(key))


	/**
	 * Put a key in current section
	 *
	 * @param key Property name
	 * @param value Property value
	 * @return [String] Previous value associated with the key, or `null` if key was not present in the section
	 */
	fun put(key: String, value: String): String? =
		properties.put(key, cleanString(value))

	/**
	 * Get property string
	 *
	 * @param key Target key to get
	 * @param default A default value if key does not exists
	 * @return [String] Property value or [default]
	 */
	fun getKey(key: String, default: String = ""): String {
		val transform = transformKey(key)

		if (!containsKey(transform, false)) {
			if (this::defaultSection.isInitialized) {
				if (!defaultSection.containsKey(transform, false))
					return default
				return defaultSection.getKey(transform)
			}
		}

		return properties[transform] ?: default
	}

	/**
	 * Get property list.
	 *
	 * This method gets a simple value like "[getKey]" but splits the value into a list.
	 * You can replace the separator with a custom string (it depends on the value in your file)
	 *
	 * ## Example:
	 * ``` kotlin
	 * val list = section.getList("example")
	 * println(list)
	 * ```
	 *
	 * ## Output:
	 * ```cmd
	 * [val1, val2, val3]
	 * ```
	 *
	 * @param key Target key to get
	 * @param separator Value list separator
	 * @return [List] Result list
	 */
	fun getList(key: String, separator: CharSequence = DEFAULT_SEPARATOR): List<String> {
		val data = getKey(key)

		return if (data.isEmpty()) {
			emptyList()
		} else {
			data.split(separator.toString())
				.map { it.trim() }
				.toList()
		}
	}

	/**
	 * Get integer property
	 *
	 * @param key Target property to get
	 * @param default Default number if not exists or is not valid number
	 * @return [Int] result or [default] if not exists or is not valid number
	 */
	fun getInt(key: String, default: Int? = null): Int? =
		getKey(key).toIntOrNull() ?: default

	/**
	 * Get float property
	 *
	 * @param key Target property to get
	 * @param default Default number if not exists or is not valid number
	 * @return [Float] result or [default] if not exists or is not valid number
	 */
	fun getFloat(key: String, default: Float? = null): Float? =
		getKey(key).toFloatOrNull() ?: default


	/**
	 * Get double property
	 *
	 * @param key Target property to get
	 * @param default Default number if not exists or is not valid number
	 * @return [Double] result or [default] if not exists or is not valid number
	 */
	fun getDouble(key: String, default: Double? = null): Double? =
		getKey(key).toDoubleOrNull() ?: default

	/**
	 * Get long property
	 *
	 * @param key Target property to get
	 * @param default Default number if not exists or is not valid number
	 * @return [Long] result or [default] if not exists or is not valid number
	 */
	fun getLong(key: String, default: Long? = null): Long? =
		getKey(key).toLongOrNull() ?: default

	/**
	 * Get boolean property
	 *
	 * @param key Target property to get
	 * @return [Boolean] result
	 */
	fun getDouble(key: String): Boolean =
		getKey(key).toBoolean()

	/* ---------------------------------------------------------
	 *
	 * Operators
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get key by operator
	 *
	 * ## Example
	 * ```kotlin
	 * val value = section["key", "default_value"]
	 * println(value)
	 * ```
	 *
	 * @param key Target property to get
	 * @param default Default value if key does not exists
	 * @return [String] key value result
	 */
	operator fun get(key: String, default: String = ""): String =
		getKey(key, default)

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Transform key to valid key (depends of configuration)
	 *
	 * @param key Target key to transform
	 * @return [String] with correct format
	 */
	private fun transformKey(key: String): String =
		Companion.transformKey(this, key)

	/* ---------------------------------------------------------
	 *
	 * Implemented methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Object string representation
	 *
	 * @return Object string representation
	 */
	override fun toString(): String {
		return "$properties"
	}

	/* ---------------------------------------------------------
	 *
	 * Companions
	 *
	 * --------------------------------------------------------- */

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * Default list separator
		 */
		@JvmStatic
		val DEFAULT_SEPARATOR: String = ","

		/**
		 * Transform key to valid key (depends of configuration)
		 *
		 * @param key Target key to transform
		 * @return [String] with correct format
		 */
		internal fun transformKey(instance: ICaseSensitive, key: String): String =
			if (instance.isCaseSensitive) key.trim() else key.trim().toLowerCase()

		/**
		 * Clean string value and replace quotes
		 *
		 * @param value Target value to check
		 * @return [String] cleaned value
		 */
		internal fun cleanString(value: String): String {
			var result = value

			if (result.startsWith("\""))
				result = result.substring(1, result.length)
			if (result.endsWith("\""))
				result = result.substring(0, result.length - 1)

			return result
		}

	}
}
