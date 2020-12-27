package org.godot.launcher.utils.engine

import java.io.File

/**
 * Engine version types
 *
 * @param number Version target
 * @param projectFileName Target engine project file name
 */
enum class EngineVersion(
	val number: Int,
	val projectFileName: String,
	val sceneExtension: String
) {

	/**
	 * First version of Godot Engine.
	 *
	 * This version is deprecated, but this app adds support for managing basic behavior.
	 */
	@Deprecated("This version is listed only for support")
	V1(1, "engine.cfg", "scn"),

	/**
	 * Second version of game engine.
	 * In this version "engine.cfg" is still used to handle projects.
	 */
	V2(2, V1.projectFileName, V1.sceneExtension),

	/**
	 * Third version of game engine.
	 * - In this version, the project file was changed to "project.godot"
	 * - In this version introduced 2 version of engine "Standard" and "Mono" version.
	 * - Mono version introduce C# language instead GDScript
	 * - VR support added
	 */
	V3(3, "project.godot", "tscn"),

	/**
	 * Fourth version of game engine.
	 * This version is not released yet, but you can check the progress in their official page.
	 *
	 * The main features for this engine are:
	 * 	- Rendering backend changed, now Vulkan is used instead OpenGL
	 * 	- GDScript was rewritten
	 * 	- Added better FBX support
	 */
	V4(4, V3.projectFileName, V3.sceneExtension);

	/**
	 * Object string representation
	 *
	 * @return [String] object representation
	 */
	override fun toString(): String =
		"""
		Version $number:
			Project File: $projectFileName
			Scene Extension: .$sceneExtension
		""".trimIndent()

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * Check if file is valid project file
		 */
		@JvmStatic
		fun isValidProjectFile(file: File): Boolean =
			values().any { it.projectFileName == file.name }

		/**
		 * Get engine version from project file
		 *
		 * @param file Target file to check
		 * @return [EngineVersion] Enum result or `null` if file is not valid
		 */
		@JvmStatic
		fun getFromProjectFile(file: File): EngineVersion? =
			values().firstOrNull { it.projectFileName == file.name }

		/**
		 * Get valid project files
		 *
		 * @return [Set] with all project files
		 */
		@JvmStatic
		fun getValidFiles(): Set<String> =
			values().map { it.projectFileName }.toSet()

		/**
		 * Get engine type version from engine object
		 *
		 * @param engine Target engine to get type
		 * @return [EngineVersion] Engine version type
		 */
		internal fun getFromEngine(engine: Engine): EngineVersion {
			val firstCode = engine.engineVersion.split(".").first()
			val numCode = firstCode.toInt()

			return values().first { it.number == numCode }
		}

	}
}
