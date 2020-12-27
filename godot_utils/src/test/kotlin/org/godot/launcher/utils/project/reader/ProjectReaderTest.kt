package org.godot.launcher.utils.project.reader

import org.godot.launcher.utils.project.reder.ProjectReader
import org.junit.Test
import java.io.File

class ProjectReaderTest {


	lateinit var readerOldVersions: ProjectReader
	lateinit var readerNewVersions: ProjectReader

	private fun initializeOld() {
		val uri = TestUtils.getResource("engine.cfg")!!.toURI()
		val ff = File(uri)

		readerOldVersions = ProjectReader(ff)

		println("--------------------------------- OLD VERSION -----------------------------------------")
		println(readerOldVersions.projectName)
		println(readerOldVersions.projectIcon)
		println(readerOldVersions.projectMainScene)
	}

	private fun initializeNew() {
		val uri = TestUtils.getResource("project.godot")!!.toURI()
		val ff = File(uri)

		readerNewVersions = ProjectReader(ff)

		println("--------------------------------- NEW VERSION -----------------------------------------")
		println(readerNewVersions.projectName)
		println(readerNewVersions.projectIcon)
		println(readerNewVersions.projectMainScene)
	}

	@Test
	fun runTest() {
		initializeOld()
		initializeNew()
	}

}
