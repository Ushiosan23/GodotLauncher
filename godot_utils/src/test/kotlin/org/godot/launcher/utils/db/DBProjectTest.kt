package org.godot.launcher.utils.db

import org.junit.Test

class DBProjectTest {

	private fun projectTest() {
		val projectLocation = "D:\\Games\\Gd1Example\\engine.cfg"
		val project = DBProject.getProject(projectLocation)

		println(project?.projectReader?.engineVersion)

		println(DBProject.getProjectEngine(projectLocation))
	}

	@Test
	fun runTest() {
		DBConnector.initializeConnector()
		projectTest()
		DBConnector.finalize()
	}

}
