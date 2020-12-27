package org.godot.launcher.utils.db

import org.godot.launcher.utils.EnvironmentUtils
import org.godot.launcher.utils.UtilsHelper
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

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
		get() = UtilsHelper.getResourceStream(
			databaseProperties.getProperty("db.config.sqlTablesLocation")
		)!!

	/**
	 * Database properties
	 */
	val databaseProperties: Properties = Properties()

	/**
	 * Get database connection
	 */
	val databaseLocation: Path
		get() = Paths.get(
			EnvironmentUtils.environmentPath.toString(),
			databaseProperties.getProperty("db.config.fileName")
		)

	/**
	 * Get database connection url
	 */
	val databaseURL: String
		get() = databaseProperties
			.getProperty("db.config.url")
			.format(
				databaseLocation.toString().replace(Regex("(//|\\\\)"), "/")
			)

	/* ---------------------------------------------------------
	 *
	 * Initializer
	 *
	 * --------------------------------------------------------- */

	/**
	 * Initialize method
	 */
	init {
		// load database properties
		databaseProperties.load(UtilsHelper.getResourceStream("db/database.properties")!!)
	}

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
