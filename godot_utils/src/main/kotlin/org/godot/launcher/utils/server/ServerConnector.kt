package org.godot.launcher.utils.server

import java.net.URI

/**
 * Http server connection
 */
object ServerConnector {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Server url
	 */
	@JvmStatic
	lateinit var serverURI: URI
		private set

	/**
	 * Initialize server connection
	 *
	 * @param location Url server connection
	 */
	@JvmStatic
	fun initializeServer(location: URI) {
		serverURI = location
	}

	/**
	 * Initialize server connection
	 *
	 * @param location Url server connection
	 */
	@JvmStatic
	fun initializeServer(location: String) {
		initializeServer(URI.create(location))
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Resolve url from relative server path
	 *
	 * @param path Relative url path
	 */
	@JvmStatic
	internal fun resolveUrl(path: String): URI = requireInit<URI> {
		return@requireInit serverURI.resolve(path)
	}

	/**
	 * Check if server connection is valid
	 *
	 * @param block Target block to execute
	 */
	internal fun requireInit(block: () -> Unit) {
		if (!this::serverURI.isInitialized)
			throw UninitializedPropertyAccessException("Server connection is not initialized")

		block.invoke()
	}

	/**
	 * Check if server connection is valid
	 *
	 * @param T Return value type
	 * @param block Target block to execute
	 * @return [T] Type value
	 */
	internal fun <T> requireInit(block: () -> T): T {
		if (!this::serverURI.isInitialized)
			throw UninitializedPropertyAccessException("Server connection is not initialized")

		return block.invoke()
	}

}
