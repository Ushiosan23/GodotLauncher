package org.godot.launcher.utils

import org.godot.launcher.utils.db.DBConnector
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
		get() = Resources.environMenResources.getProperty("env.config.dir")

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
		val environMenResources: Properties = Properties()

		/**
		 * Initialize resources object
		 */
		init {
			environMenResources.load(UtilsHelper.getResourceStream("gd_config.properties")!!)
		}

	}
}
