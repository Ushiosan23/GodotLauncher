package org.godot.launcher.utils.engine.events

import java.util.*

/**
 * Interface to save all events
 */
interface EngineListeners {

	/**
	 * Used to manage all events
	 */
	interface EngineAdapter : StartListener, EndListener, ProcessListener {

		/**
		 * Default behaviour
		 *
		 * @see StartListener.engineStart
		 */
		override fun engineStart(event: EngineEvents.StartEvent) {}

		/**
		 * Default behaviour
		 *
		 * @see EndListener.engineEnd
		 */
		override fun engineEnd(event: EngineEvents.EndEvent) {}

		/**
		 * Default behaviour
		 *
		 * @see ProcessListener.engineProcess
		 */
		override fun engineProcess(event: EngineEvents.ProcessEvent) {}

	}

	/**
	 * Called when process started
	 */
	interface StartListener : EventListener {

		/**
		 * Called when process start
		 *
		 * @param event Event with information
		 */
		fun engineStart(event: EngineEvents.StartEvent)

	}

	/**
	 * Used to manage end process
	 */
	interface EndListener : EventListener {

		/**
		 * Called when process end
		 *
		 * @param event Event with information
		 */
		fun engineEnd(event: EngineEvents.EndEvent)

	}

	/**
	 * Used to manage running state process
	 */
	interface ProcessListener : EventListener {

		/**
		 * Called when process send information to process
		 *
		 * @param event Event with information
		 */
		fun engineProcess(event: EngineEvents.ProcessEvent)

	}

}
