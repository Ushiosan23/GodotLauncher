package org.godot.launcher.utils

import org.godot.launcher.utils.engine.Engine
import org.godot.launcher.utils.engine.EngineThread
import org.godot.launcher.utils.engine.events.EngineEvents
import org.godot.launcher.utils.engine.events.EngineListeners
import org.junit.Test

class EngineThreadTest : EngineListeners.EngineAdapter {

	@Test
	fun runTest() {
		val standardEngine = Engine("C:\\tools\\Godot\\3.2.3\\Standard\\Godot.exe")
		val monoEngine = Engine("C:\\tools\\Godot\\3.2.3\\Mono\\Godot.exe")

		val engineThread = EngineThread(standardEngine)
		val engineThreadM = EngineThread(monoEngine)

		engineThread.addStartListener(this)
		engineThreadM.addStartListener(this)
		engineThread.addProcessListener(this)
		engineThreadM.addProcessListener(this)

		Thread {
			Thread.sleep(10000)
			engineThread.kill()
			Thread.sleep(3000)
			engineThreadM.kill()
		}.start()

		engineThread.launch()
		engineThreadM.launch()

		engineThread.join()
		engineThreadM.join()
	}


	override fun engineStart(event: EngineEvents.StartEvent) {
		println(event.thread)
	}


	override fun engineProcess(event: EngineEvents.ProcessEvent) {
		println(event.data.toString(Charsets.UTF_8))
	}

}
