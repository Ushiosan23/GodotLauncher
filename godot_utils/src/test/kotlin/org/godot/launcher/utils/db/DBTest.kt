package org.godot.launcher.utils.db

import org.godot.launcher.utils.EnvironmentUtils
import org.junit.Test

class DBTest {

	private fun configurationTable() {
		println(DBConfig.configExists("app_theme"))
		println(DBConfig.getConfig("app_theme"))
		if (!DBConfig.configExists("app_theme")) {
			println(DBConfig.updateConfig("app_theme", "dark"))
			println(DBConfig.getConfig("app_theme"))
		}

		println(DBConfig.deleteConfig("app_theme"))
	}

	@Test
	fun runTest() {
		EnvironmentUtils.initializeEnvironment()
		configurationTable()
		EnvironmentUtils.finalize()
	}

}
