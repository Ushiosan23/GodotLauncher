package org.godot.launcher.utils

import org.junit.Test

class UtilsHelperTest {

	@Test
	fun runTest() {
		println(UtilsHelper.getResource("db/database.properties"))
		EnvironmentUtils.initializeEnvironment()
	}

}
