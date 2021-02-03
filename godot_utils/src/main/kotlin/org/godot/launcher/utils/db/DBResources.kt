package org.godot.launcher.utils.db

import org.godot.launcher.utils.EnvironmentUtils
import org.godot.launcher.utils.UtilsHelper
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Object with all database tables
 */
internal object DBResources {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get sql file location
	 */
	val sqlCreationFile: InputStream
		get() = UtilsHelper.getResourceStreamSafe(
			EnvironmentUtils.Resources.environmentResources.getProperty("utils.database.sqlLocation")
		)

	/**
	 * Get database connection
	 */
	val databaseLocation: Path
		get() = Paths.get(
			EnvironmentUtils.environmentPath.toString(),
			EnvironmentUtils.Resources.environmentResources.getProperty("utils.database.filename")
		)

	/**
	 * Get database connection url
	 */
	val databaseURL: String
		get() = EnvironmentUtils.Resources.environmentResources
			.getProperty("utils.database.url")
			.format(
				databaseLocation.toString().replace(Regex("//|\\\\"), "/")
			)

	/* ---------------------------------------------------------
	 *
	 * Internal objects
	 *
	 * --------------------------------------------------------- */

	/**
	 * Database Tables
	 */
	object Tables {

		/**
		 * Table configuration
		 */
		const val TABLE_CONFIGURATION: String = "gd_configuration"

		/**
		 * Table engine
		 */
		const val TABLE_ENGINE: String = "gd_engine"

		/**
		 * Table project
		 */
		const val TABLE_PROJECT: String = "gd_project"

	}

}
