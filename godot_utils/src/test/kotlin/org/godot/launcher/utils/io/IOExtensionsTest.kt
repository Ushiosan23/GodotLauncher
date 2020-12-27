package org.godot.launcher.utils.io

import TestUtils
import org.junit.Test
import java.io.File

class IOExtensionsTest {

	private fun readFile() {
		val url = TestUtils.getResource("project.godot")!!.toURI()
		val file = File(url)

		file.readByLine {
			println(it)
		}
	}


	@Test
	fun runTest() {
		readFile()
	}

}
