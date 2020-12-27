package org.godot.launcher.utils.project

import org.godot.launcher.utils.EnvironmentUtils
import org.godot.launcher.utils.UtilsHelper
import org.godot.launcher.utils.engine.EngineVersion
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class ProjectTest {

	lateinit var project: Project

	private fun initialize() {
		val localFile = File("D:\\Games\\Runner")
		project = Project(localFile)

		println(project.projectDirectory)
		println(project.projectFile)
		println(project.projectReader)
	}

	private fun createNew() {
		val location = Paths.get(
			System.getProperty("user.home"),
			"Desktop",
			"newProject"
		).toFile()

		val project = Project.createProject(location, EngineVersion.V3)

		println(project.projectDirectory)
		println(project.projectFile)
		println(project.projectScenes)
	}

	@Test
	fun runTest() {
		createNew()
	}

}
