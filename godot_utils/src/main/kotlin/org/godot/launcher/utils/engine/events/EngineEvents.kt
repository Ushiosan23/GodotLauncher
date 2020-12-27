package org.godot.launcher.utils.engine.events

import org.godot.launcher.utils.engine.EngineThread
import java.util.*

/**
 * This object manage all event classes
 */
object EngineEvents {

	/**
	 * Class to manage engine thread start event
	 *
	 * @param src Source object
	 * @param thread Current engine thread
	 */
	data class StartEvent(
		private val src: Any,
		val thread: EngineThread
	) : EventObject(src)

	/**
	 * Class to manage engine thread end event
	 *
	 * @param src Source object
	 * @param thread Current thread engine
	 */
	data class EndEvent(
		private val src: Any,
		val thread: EngineThread
	) : EventObject(src)

	/**
	 * Class to manage engine thread process event
	 *
	 * @param src Source object
	 * @param data Engine data received
	 * @param isError Check if data is error information
	 */
	@Suppress("ArrayInDataClass")
	data class ProcessEvent(
		private val src: Any,
		val data: ByteArray,
		val isError: Boolean
	) : EventObject(src)

}
