package org.godot.launcher.utils.project

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.godot.launcher.utils.engine.EngineVersion
import org.godot.launcher.utils.project.reder.ProjectReader
import org.godot.launcher.utils.serialize.ini.Ini
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * Project class
 *
 * @param location Target project location
 * @throws IOException If file not exists or if location is not a directory
 * @throws ProjectException If directory does not contains project file
 */
@Serializable
class Project(
	@Contextual
	private val location: File
) {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Project reader object
	 */
	@Suppress("MemberVisibilityCanBePrivate")
	lateinit var projectReader: ProjectReader
		private set

	/**
	 * Get all project scenes
	 */
	val projectScenes: List<String>
		get() {
			val walk = Files.walk(location.toPath())
			val result = walk
				.filter(Files::isRegularFile)
				.filter { it.toString().endsWith(projectReader.engineVersion.sceneExtension) }
				.collect(Collectors.toList())
				.map { it.toString() }

			walk.close()
			return result
		}

	/**
	 * Get project file location
	 */
	val projectFile: String
		get() = projectReader.location.path

	/**
	 * Project directory location
	 */
	val projectDirectory: String
		get() = location.path

	/**
	 * Get project properties
	 */
	val projectProperties: Ini
		get() = projectReader.projectProperties

	/* ---------------------------------------------------------
	 *
	 * Constructors
	 *
	 * --------------------------------------------------------- */

	/**
	 * Create project with custom engine
	 *
	 * @param location Target project location
	 * @param engineVersion Target engine version
	 */
	constructor(location: File, engineVersion: EngineVersion) : this(location) {
		projectReader.engineVersion = engineVersion
	}

	/**
	 * Initialize object
	 */
	init {
		// Check if location exists
		if (!location.exists()) throw IOException("File \"$location\" not found")
		// Check if file is directory
		if (!location.isDirectory) throw IOException("File \"$location\" is not directory")
		// walk directory
		walkDirectory { iFile ->
			if (EngineVersion.isValidProjectFile(iFile)) {
				projectReader = ProjectReader(iFile)
			}
		}
		// Check if project reader is null
		if (!this::projectReader.isInitialized) {
			val projectFiles = EngineVersion.getValidFiles().joinToString(" | ")
			throw ProjectException("Project directory does not contain \"$projectFiles\" file")
		}
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Wall all directories and list only files
	 *
	 * @param block Target block to execute
	 */
	private fun walkDirectory(block: (file: File) -> Unit) {
		// Search only in primary directory
		val walk = Files.walk(location.toPath(), 1)
		// Iterate stream objects
		walk.forEach {
			if (Files.isRegularFile(it))
				block.invoke(it.toFile())
		}
		// Close stream
		walk.close()
	}

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * Create project in custom location
		 *
		 * @param location Target file location
		 * @param targetVersion Target engine version to detect
		 */
		@JvmStatic
		fun createProject(location: File, targetVersion: EngineVersion = EngineVersion.V3): Project {
			// Create directory if not exists
			if (!location.exists())
				Files.createDirectories(location.toPath())
			// Check if file is not directory
			if (!location.isDirectory)
				throw IOException("Location \"$location\" is not valid directory.")
			// Check if directory is empty
			if (Files.list(location.toPath()).findFirst().isPresent)
				throw ProjectException("Directory \"$location\" is not empty.")
			// File project
			val pLocation = Paths.get(location.path, targetVersion.projectFileName)
			val fLocation = pLocation.toFile()
			// Create file
			if (!fLocation.exists()) fLocation.createNewFile()

			return Project(location, targetVersion)
		}

	}
}
