package org.godot.launcher.utils.server

import org.junit.Test
import java.net.URI

class EngineServerAccessorTest {

	var uri = URI.create("http://example.com")

	@Test
	fun runTest() {
		val data = mapOf(
			"ref" to "second",
			"location" to "tmp://file.exe"
		)

		uri = EngineServerAccessor.attachData(uri, data)
		println(uri)
	}


}
