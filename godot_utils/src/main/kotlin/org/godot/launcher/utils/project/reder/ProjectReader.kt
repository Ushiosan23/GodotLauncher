package org.godot.launcher.utils.project.reder

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.godot.launcher.utils.engine.EngineVersion
import org.godot.launcher.utils.serialize.ini.Ini
import java.io.File
import java.io.IOException
import java.nio.file.Paths

/**
 * Project reader class.
 * This class read directory project and detect next things:
 * 	- Read project file (*.cfg or *.godot)
 * 	- Project name
 * 	- Project icon
 * 	- Main scene
 * 	- List of all scenes (only used scenes)
 */
@Serializable
class ProjectReader(
	@Contextual
	internal val location: File
) {

	/**
	 * Properties of project.
	 * This object can read godot project file.
	 */
	internal val projectProperties: Ini

	/**
	 * Internal engine version
	 */
	@Suppress("MemberVisibilityCanBePrivate")
	var engineVersion: EngineVersion
		internal set

	/**
	 * Initialize object
	 */
	init {
		// Initialize properties
		projectProperties = Ini(this.location)
		// Check engine version
		val localVer = EngineVersion.getFromProjectFile(this.location)
			?: throw IOException("File \"${this.location}\" is not valid project")
		// Check if version is valid
		// Set version
		engineVersion = localVer
	}

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get project file name depends of engine version
	 */
	val projectName: String = when (engineVersion) {
		EngineVersion.V1, EngineVersion.V2 -> projectProperties["application"]["name"]
		EngineVersion.V3, EngineVersion.V4 -> projectProperties["application"]["config/name"]
	}

	/**
	 * Get project icon depends of engine version
	 */
	val projectIcon: String = when (engineVersion) {
		EngineVersion.V1, EngineVersion.V2 -> resolveProjectFile(projectProperties["application"]["icon"])
		EngineVersion.V3, EngineVersion.V4 -> resolveProjectFile(projectProperties["application"]["config/icon"])
	}

	/**
	 * Get project main scene, depends of engine version
	 */
	val projectMainScene: String = when (engineVersion) {
		EngineVersion.V1, EngineVersion.V2 -> resolveProjectFile(projectProperties["application"]["main_scene"])
		EngineVersion.V3, EngineVersion.V4 -> resolveProjectFile(projectProperties["application"]["run/main_scene"])
	}

	/**
	 * Resolve file project
	 *
	 * @param relativeLocation Target file to resolve
	 * @return [String] resolved file
	 */
	fun resolveProjectFile(relativeLocation: String): String {
		// Check if relative location is empty
		if (relativeLocation.isEmpty()) return ""
		// check if location has res:// prefix
		val check = relativeLocation.startsWith("res://")
		// Return location
		if (!check) return Paths.get(location.parentFile.path, relativeLocation).toString()
		// Replace res://
		val newLocation = relativeLocation.replace("res://", "")
		return resolveProjectFile(newLocation)
	}

}
