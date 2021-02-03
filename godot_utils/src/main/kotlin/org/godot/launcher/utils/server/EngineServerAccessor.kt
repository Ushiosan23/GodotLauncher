package org.godot.launcher.utils.server

import com.github.ushiosan23.networkutils.http.HttpRequestAction
import org.godot.launcher.utils.engine.EngineType
import org.godot.launcher.utils.system.Platform
import java.net.URI
import java.net.URLEncoder

/**
 * This object connect to server and request specific engine to download
 */
object EngineServerAccessor {


	@JvmStatic
	fun getAvailableEngines(engineType: EngineType? = null) =
		ServerConnector.requireInit<List<ServerEngine>> {
			// Initialize properties
			val requestMap = getEngineDefaultInfo()
			// Check engine type
			if (engineType != null)
				requestMap["type"] = engineType.typeName.toLowerCase()
			// Create action
			val requestAction = createRequestAction("/engines", requestMap)
			val response = requestAction.get().body()

			return@requireInit emptyList()
		}

	/**
	 * Attach data to uri object
	 *
	 * @param url Target url
	 * @param data Data to attach
	 * @return [URI] url result
	 */
	@JvmStatic
	fun attachData(url: URI, data: Map<String, String>): URI {
		// Check data size
		if (data.isEmpty()) return url
		// Check data
		var query = if (url.query == null) "" else "${url.query}&"

		// Iterate data
		data.forEach { entry ->
			query += URLEncoder.encode(entry.key, Charsets.UTF_8)
			query += "="
			query += URLEncoder.encode(entry.value, Charsets.UTF_8)
			query += "&"
		}
		val lastIndex = query.lastIndexOf("&")
		query = query.substring(0, lastIndex)


		val cleanUrl = "$url".split("?").first()
		return URI.create("${cleanUrl}?$query")
	}

	/**
	 * Create request action
	 *
	 * @param location Target server location
	 * @param data Target request data
	 */
	private fun createRequestAction(
		location: String,
		data: Map<String, String>
	): HttpRequestAction {
		// Initialize properties
		var requestURL = ServerConnector.resolveUrl(location)
		// Resolve uri
		requestURL = attachData(requestURL, data)
		// Create request action
		return HttpRequestAction(requestURL)
	}

	/**
	 * Get Engine default request info
	 *
	 * @return [Map] with default request data
	 */
	private fun getEngineDefaultInfo(): MutableMap<String, String> =
		mutableMapOf(
			"platform" to Platform.getFromSystem().osName,
			"x64" to if (Platform.isArchX64) "1" else "0",
			"type" to "all"
		)

}
