package org.godot.launcher.utils.xml

import org.w3c.dom.Document
import java.io.InputStream
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

/**
 * XML File utilities
 */
object XMLUtils {

	/**
	 * Load xml from input stream
	 *
	 * @param stream Target file stream
	 * @return [Document] with xml data
	 */
	@JvmStatic
	fun loadXML(stream: InputStream): Document {
		val docFactory = DocumentBuilderFactory.newInstance()
		val docBuilder = docFactory.newDocumentBuilder()

		return try {
			docBuilder.parse(stream)
		} catch (err: Exception) {
			err.printStackTrace()
			docBuilder.newDocument()
		}
	}

	/**
	 * Load xml from url
	 *
	 * @param document Target file url
	 * @return [Document] with xml data
	 */
	fun loadXML(document: URL): Document = loadXML(document.openStream())

}
