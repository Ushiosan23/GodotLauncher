package org.godot.launcher.utils

import org.godot.launcher.utils.system.Platform
import org.junit.Test

class PlatformTest {

	@Test
	fun runTest() {
		println(Platform.getFromSystem())
	}

}
