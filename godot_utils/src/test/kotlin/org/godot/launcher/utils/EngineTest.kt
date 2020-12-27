package org.godot.launcher.utils

import org.godot.launcher.utils.engine.Engine
import org.godot.launcher.utils.engine.events.EngineEvents
import org.godot.launcher.utils.engine.events.EngineListeners
import org.junit.Test

class EngineTest {

	lateinit var engine: Engine

	private fun initializeTest() {
		engine = Engine("C:\\tools\\Godot\\3.0.6\\Mono\\Godot.exe")
	}

	private fun getEngineProperties() {
		println(engine.engineTypeVersion)
		println(engine.engineName)
		println(engine.engineVersion)
		println(engine.engineType)
	}

	private fun launchEditor() {
		// Save thread
		val thread = engine.launch()
		// Listen thread data
		thread?.addProcessListener(object : EngineListeners.ProcessListener {
			override fun engineProcess(event: EngineEvents.ProcessEvent) {
				println(event.data.toString(Charsets.UTF_8))
			}
		})
		// Wait until finish
		thread?.join()
	}

	private fun launchMultipleTimes() {
		engine.launch()?.join()
		Thread.sleep(3000)
		engine.launch()?.join()
	}

	@Test
	fun runTest() {
		// initialize engine
		initializeTest()
		getEngineProperties()
	}

}
