package org.godot.launcher.utils.db

import org.godot.launcher.utils.EnvironmentUtils
import org.godot.launcher.utils.engine.Engine
import org.junit.Test

class EngineDBTest {

	private fun configurationTable() {
		val engineToAdd = Engine("C:\\tools\\Godot\\2.1.6\\Standard\\Godot.exe")

		println(DBEngine.getAllEngines())
		println(DBEngine.addEngine(engineToAdd))
		println(DBEngine.getAllEngines())
		println(DBEngine.removeEngine(engineToAdd))
		println(DBEngine.getAllEngines())
		println(DBEngine.updateEngine(engineToAdd))
	}

	@Test
	fun runTest() {
		EnvironmentUtils.initializeEnvironment()
		configurationTable()
		EnvironmentUtils.finalize()
	}

}
