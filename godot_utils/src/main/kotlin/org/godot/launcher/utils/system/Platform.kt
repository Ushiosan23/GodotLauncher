package org.godot.launcher.utils.system

/**
 * OS Platform
 */
enum class Platform(regex: String?, val osName: String) {
	WINDOWS("(win|windows|win32)", "windows"), // Windows platform
	LINUX("(linux|nux)", "linux"), // Linux platform
	MACOS("(darwin|nix)", "darwin"),
	UNKNOWN(null, ""); // Mac OS platform

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
		 * Check if platform is x64 or x86
		 */
		@JvmStatic
		val isArchX64: Boolean
			get() = System.getProperty("os.arch").contains("64")

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
