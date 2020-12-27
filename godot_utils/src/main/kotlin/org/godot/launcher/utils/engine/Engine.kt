package org.godot.launcher.utils.engine

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

/**
 * Engine object manager
 */
@Serializable
class Engine {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get engine location string
	 */
	val location: String
		get() = engineLocation.path

	/**
	 * Check if engine location exists
	 */
	val exists: Boolean
		get() = engineLocation.exists()

	/**
	 * Check if engine is valid
	 */
	var isValid: Boolean = false
		private set

	/**
	 * Check if engine is default
	 */
	var isDefault: Boolean = false
		private set

	/**
	 * Get engine name
	 */
	val engineName: String
		get() = "Godot Engine ($engineType) $engineVersion"

	/**
	 * Current engine version
	 */
	val engineVersion: String
		get() = if (rawEngineVersion != null) {
			rawEngineVersion!!
		} else {
			updateRawVersion()
			rawEngineVersion!!
		}

	/**
	 * Get engine type
	 */
	val engineTypeVersion: EngineVersion
		get() = EngineVersion.getFromEngine(this)

	/**
	 * Get engine type
	 */
	val engineType: EngineType
		get() = rawEngineType

	/**
	 * Current engine thread
	 */
	@Contextual
	var engineThread: EngineThread? = null
		private set

	/* ---------------------------------------------------------
	 *
	 * Internal Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Raw engine version
	 */
	private var rawEngineVersion: String? = null

	/**
	 * Raw engine type
	 */
	private var rawEngineType: EngineType = EngineType.Standard

	/**
	 * Engine location
	 */
	@Contextual
	@Suppress("JoinDeclarationAndAssignment")
	internal val engineLocation: File

	/* ---------------------------------------------------------
	 *
	 * Constructors
	 *
	 * --------------------------------------------------------- */

	/**
	 * Primary constructor
	 *
	 * @param fLocation File location
	 * @param default Set engine by default
	 */
	constructor(fLocation: File, default: Boolean) {
		engineLocation = fLocation
		isDefault = default

		checkMimeType()
		checkValidEngine()
	}

	/**
	 * Create basic engine instance
	 * This constructor set engine by not default
	 *
	 * @param fLocation File location
	 */
	constructor(fLocation: File) : this(fLocation, false)

	/**
	 * Create engine from string location
	 *
	 * @param fLocation Target file location
	 * @param default Set engine by default
	 */
	constructor(fLocation: String, default: Boolean) : this(File(fLocation), default)

	/**
	 * Create engine from string location
	 *
	 * @param fLocation Target file location
	 */
	constructor(fLocation: String) : this(File(fLocation), false)

	/* ---------------------------------------------------------
	 *
	 * Methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Launch engine with specific arguments
	 *
	 * @param args Target arguments to launch
	 */
	fun launch(vararg args: String): EngineThread? {
		return try {
			engineThread = EngineThread(this)
			engineThread!!.launch(*args)
		} catch (err: Exception) {
			err.printStackTrace()
			null
		}
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Check if mime type is valid
	 */
	private fun checkMimeType() = requireExists {
		val url = engineLocation.toURI().toURL()
		val connection = url.openConnection()

		if (!acceptMimeTypes.contains(connection.contentType))
			throw IOException("File \"$location\" is not valid executable file (${connection.contentType}).")
	}

	/**
	 * Check if engine is valid
	 */
	private fun checkValidEngine() = requireExists {
		// Create file stream
		val stream = engineLocation.inputStream()
		val reader = InputStreamReader(stream)
		val bReader = BufferedReader(reader)

		// Iterate lines
		for (line in bReader.lines()) {
			if (line.contains(regexContent)) {
				isValid = true
				return@requireExists
			}
		}

		// check if stream is open
		stream.close()
		throw IOException("File \"$location\" is not valid Godot Engine executable.")
	}

	/**
	 * Update engine version
	 */
	private fun updateRawVersion() {
		// Help process
		val lBuilder = ProcessBuilder(engineLocation.path, "--help")
		val lProcess = lBuilder.start()
		val lReader = BufferedReader(InputStreamReader(lProcess.inputStream))
		val data = StringBuilder()

		// Iterate data
		lReader.forEachLine {
			data.append(it)
		}

		// Check if version exists
		rawEngineVersion = if (regexVersion.containsMatchIn(data)) {
			regexVersion.find(data)!!.groupValues.first()
		} else {
			""
		}

		// Check engine type
		rawEngineType = if (regexMonoVersion.containsMatchIn(data)) {
			EngineType.Mono
		} else {
			EngineType.Standard
		}

		// Replace "v" if exists
		if (rawEngineVersion!!.toLowerCase().startsWith("v")) {
			rawEngineVersion = rawEngineVersion!!.replace(Regex("v", RegexOption.IGNORE_CASE), "")
		}

		// Wait until exit
		lProcess.waitFor()
	}

	/**
	 * Execute block if engine exists.
	 *
	 * @param block Action to launch
	 * @throws IOException Launch error if file not exists
	 */
	private fun requireExists(block: () -> Unit) = if (exists)
		block.invoke()
	else
		throw IOException("File location \"$location\" not exists.")

	/**
	 * Execute block if engine exists. Return a value.
	 *
	 * @param block Action to launch
	 * @throws IOException Launch error if file not exists
	 */
	private inline fun <reified T> requireExists(block: () -> T): T = if (exists)
		block.invoke()
	else
		throw IOException("File location \"$location\" not exists.")

	/**
	 * Object string representation
	 *
	 * @return object string representation
	 */
	override fun toString(): String {
		return "$engineName: $location"
	}

	/* ---------------------------------------------------------
	 *
	 * Companion object
	 *
	 * --------------------------------------------------------- */

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * List with all accepted mime types
		 */
		private val acceptMimeTypes: List<String> = listOf(
			"application/octet-stream",
			"application/x-executable",
			"application/x-match-binary", // generic type (Macos)
			"application/x-dosexec", // generic type (windows)
		)

		/**
		 * Regular expression to detect engine content
		 */
		private val regexContent: Regex = Regex("Godot Engine", RegexOption.IGNORE_CASE)

		/**
		 * Regular expression to detect version information like (v|V)*.*.*
		 */
		private val regexVersion: Regex = Regex(
			"v(\\d+\\.){1,5}(\\w+\\.?){1,4}",
			setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
		)

		/**
		 * Regular expression to detect engine type (Standard | Mono)
		 */
		private val regexMonoVersion: Regex = Regex(
			"(\\.mono\\.)",
			setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
		)

	}

}
