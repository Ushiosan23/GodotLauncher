package org.godot.launcher.utils

import org.godot.launcher.utils.db.DBConnector
import org.godot.launcher.utils.server.ServerConnector
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Environment utils object
 */
object EnvironmentUtils {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Environment directory
	 */
	val environmentDir: String
		get() = Resources.environmentResources.getProperty("utils.environment.dir")

	/**
	 * Full path dir
	 */
	val environmentPath: Path
		get() = Paths.get(System.getProperty("user.home"), environmentDir)

	/* ---------------------------------------------------------
	 *
	 * Methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Initialize environment
	 */
	@JvmStatic
	fun initializeEnvironment() {
		// Check path exists
		if (!environmentPath.toFile().exists())
			Files.createDirectories(environmentPath)
		// Initialize database
		DBConnector.initializeConnector()
		ServerConnector.initializeServer(
			Resources.environmentResources.getProperty("utils.server.url")
		)
	}

	/**
	 * Finalize environment
	 */
	@JvmStatic
	fun finalize() {
		DBConnector.finalize()
	}

	/* ---------------------------------------------------------
	 *
	 * Internal objects
	 *
	 * --------------------------------------------------------- */

	object Resources {

		/**
		 * Environment properties
		 */
		@JvmStatic
		val environmentResources: Properties =
			UtilsHelper.getPropertiesFrom("utils_config.properties")

	}
}
