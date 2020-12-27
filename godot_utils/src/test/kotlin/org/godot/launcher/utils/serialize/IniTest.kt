package org.godot.launcher.utils.serialize

import org.godot.launcher.utils.serialize.ini.Ini
import org.junit.Test
import java.io.File

class IniTest {

	lateinit var iniFile: Ini

	fun initialize() {
		val uri = TestUtils.getResource("project.godot")!!.toURI()
		val file = File(uri)

		iniFile = Ini(file)
	}

	@Test
	fun runTest() {
		initialize()

		val application = iniFile["application"]

		println(application)
		println("-----------------------------------------------------------------")
		println(application["run/main_scene"])
		println(application["config/name"])
		println(application["config/icon"])
	}
}
