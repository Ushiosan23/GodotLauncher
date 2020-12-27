package org.godot.launcher.utils.system

/**
 * OS Platform
 */
enum class Platform(regex: String?) {
	WINDOWS("(win|windows|win32)"), // Windows platform
	LINUX("(linux|nux)"), // Linux platform
	MACOS("(darwin|nix)"),
	UNKNOWN(null); // Mac OS platform

	/**
	 * Regular expression object
	 */
	private val regexObj: Regex? = if (regex == null)
		null
	else
		Regex(regex, setOf(RegexOption.IGNORE_CASE))

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * Get current platform name
		 */
		@JvmStatic
		val currentPlatform: String
			get() = System.getProperty("os.name")

		/**
		 * Get platform from system
		 *
		 * @return [Platform] platform element
		 */
		@JvmStatic
		fun getFromSystem(): Platform {
			// Get system platform name
			val system = currentPlatform.toLowerCase()
			// Iterate all values
			values().forEach { platform ->
				platform.regexObj?.let { reg ->
					if (reg.containsMatchIn(system))
						return platform
				}
			}

			// Return unknown if not exists in current system
			return UNKNOWN
		}

	}

}
