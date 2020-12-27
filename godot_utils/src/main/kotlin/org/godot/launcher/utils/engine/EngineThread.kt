package org.godot.launcher.utils.engine

import org.godot.launcher.utils.engine.events.EngineEvents
import org.godot.launcher.utils.engine.events.EngineListeners
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.swing.event.EventListenerList
import kotlin.random.Random

/**
 * Launch process and launch program
 *
 * @param engine Target engine location to launch
 */
class EngineThread(
	private val engine: Engine
) {

	/* ---------------------------------------------------------
	 *
	 * Internal properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Random number string
	 */
	private val threadRandom: String = Integer.toHexString((Random.Default.nextFloat() * Int.MAX_VALUE).toInt())

	/**
	 * base engine thread name
	 */
	private val baseName: String = engine.location.replace(Regex("(//|:|\\\\|\\.)"), "_")

	/**
	 * Create thread name
	 */
	private val threadName: String = "$baseName@$threadRandom"

	/**
	 * Engine thread
	 */
	private val thread: Thread = createCustomThread(
		runnableProcess(),
		threadName
	)

	/**
	 * Arguments to launch in process
	 */
	private var processArgs: Array<out String>? = null

	/**
	 * Process builder model
	 */
	private var processB: ProcessBuilder? = null

	/**
	 * Process to launch
	 */
	private var process: Process? = null

	/**
	 * Manage all events
	 */
	private val eventListenerList = EventListenerList()

	/**
	 * Check if process is live
	 */
	@Volatile
	private var processStatus: Boolean = false

	/* ---------------------------------------------------------
	 *
	 * Methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Launch thread
	 */
	fun launch(vararg args: String): EngineThread {
		try {
			processArgs = args
			processStatus = true
			thread.start()
			fireStartListener(this)
		} catch (err: Exception) {
			err.printStackTrace()
		}

		return this
	}

	/**
	 * Kill current process
	 */
	fun kill() {
		processStatus = false
		process?.destroy()
		// Check if godot launch other process
		try {
			ProcessHandle.allProcesses().forEach { pHandle ->
				// Check if command is empty
				if (pHandle.info().command().isEmpty) return@forEach
				// Check if command line is valid
				val cmd = pHandle.info().command().get()
				// Compare with current process
				if (cmd.contains(engine.location)) pHandle.destroy()
			}
		} catch (err: Exception) {
			err.printStackTrace()
		}
	}

	/**
	 * Waits for this thread to die.
	 *
	 * @throws InterruptedException if any thread has interrupted the current thread.
	 * The interrupted status of the current thread is cleared when this exception is thrown.
	 */
	fun join() {
		if (!thread.isAlive) return
		thread.join()
	}

	/* ---------------------------------------------------------
	 *
	 * Event methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Add listener to current thread
	 *
	 * @param listener Target listener to add
	 */
	fun addStartListener(listener: EngineListeners.StartListener) {
		eventListenerList.add(EngineListeners.StartListener::class.java, listener)
	}

	/**
	 * Remove listener to current thread
	 *
	 * @param listener Target listener to remove
	 */
	fun removeStartListener(listener: EngineListeners.StartListener) {
		eventListenerList.remove(EngineListeners.StartListener::class.java, listener)
	}

	/**
	 * Add listener to current thread
	 *
	 * @param listener Target listener to add
	 */
	fun addEndListener(listener: EngineListeners.EndListener) {
		eventListenerList.add(EngineListeners.EndListener::class.java, listener)
	}

	/**
	 * Remove listener to current thread
	 *
	 * @param listener Target listener to remove
	 */
	fun removeEndListener(listener: EngineListeners.EndListener) {
		eventListenerList.remove(EngineListeners.EndListener::class.java, listener)
	}

	/**
	 * Add listener to current thread
	 *
	 * @param listener Target listener to add
	 */
	fun addProcessListener(listener: EngineListeners.ProcessListener) {
		eventListenerList.add(EngineListeners.ProcessListener::class.java, listener)
	}

	/**
	 * Remove listener to current thread
	 *
	 * @param listener Target listener to remove
	 */
	fun removeProcessListener(listener: EngineListeners.ProcessListener) {
		eventListenerList.remove(EngineListeners.ProcessListener::class.java, listener)
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Called when process start
	 *
	 * @param data Target event data to send
	 */
	private fun fireStartListener(data: EngineThread) {
		val selected = eventListenerList.getListeners(EngineListeners.StartListener::class.java)
		selected.forEach {
			it.engineStart(EngineEvents.StartEvent(it, data))
		}
	}

	/**
	 * Called when process end
	 *
	 * @param data Target event data to send
	 */
	private fun fireEndListener(data: EngineThread) {
		val selected = eventListenerList.getListeners(EngineListeners.EndListener::class.java)
		selected.forEach {
			it.engineEnd(EngineEvents.EndEvent(it, data))
		}
	}

	/**
	 * Called when process is running.
	 * Is only called when receive info data or error data.
	 *
	 * @param data Target event data to send
	 * @param isError Target type data to send
	 */
	private fun fireProcessListener(data: ByteArray, isError: Boolean) {
		val selected = eventListenerList.getListeners(EngineListeners.ProcessListener::class.java)
		selected.forEach {
			it.engineProcess(EngineEvents.ProcessEvent(it, data, isError))
		}
	}

	/**
	 * Create runnable process
	 *
	 * @return [Runnable] action
	 */
	private fun runnableProcess() = Runnable {
		// Initialize process
		processB = ProcessBuilder(engine.location, *processArgs ?: emptyArray())
		// Start process
		try {
			process = processB?.start()
			// Check if process is not null
			if (process == null) return@Runnable

			// manage process
			val stream = process!!.inputStream
			val reader = InputStreamReader(stream)
			val buffReader = BufferedReader(reader)

			do {
				val line = buffReader.readLine()
				line?.let {
					fireProcessListener(it.toByteArray(), false)
				}
			} while (line != null && processStatus)

		} catch (err: Exception) {
			fireProcessListener(err.stackTrace.toString().toByteArray(Charsets.UTF_8), true)
		}
	}

	/* ---------------------------------------------------------
	 *
	 * Implemented method
	 *
	 * --------------------------------------------------------- */

	/**
	 * String object representation
	 *
	 * @return Object representation
	 */
	override fun toString(): String {
		return if (thread.threadGroup != null)
			"${thread.threadGroup.name}: ${thread.name}"
		else
			thread.name
	}

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * Threads group
		 */
		private const val threadGroup: String = "GDThread"

		/**
		 * Create custom thread
		 *
		 * @param runnable Target action to launch in thread
		 * @param name Target thread name
		 */
		private fun createCustomThread(runnable: Runnable, name: String): Thread = Thread(
			ThreadGroup(threadGroup),
			runnable,
			name
		)

	}

}
