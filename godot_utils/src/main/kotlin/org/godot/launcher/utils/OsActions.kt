package org.godot.launcher.utils

import java.awt.Desktop
import java.io.File
import java.net.URI
import java.nio.file.Files


/**
 * This class contains actions to launch os actions like:
 *
 * - Open web pages in browser
 * - Open resource in file explorer
 */
object OsActions {

	/**
	 * Native desktop instance
	 */
	private val desktopToolkit: Desktop = Desktop.getDesktop()

	/**
	 * Open url in default browser
	 *
	 * @param url Target url to launch
	 */
	@JvmStatic
	fun openURL(url: URI): Boolean {
		try {
			desktopToolkit.browse(url)
		} catch (ignored: Exception) {
			return false
		}

		return true
	}

	/**
	 * Open url in default browser
	 *
	 * @param url Target url to launch
	 */
	@JvmStatic
	fun openURL(url: String): Boolean = try {
		openURL(URI.create(url))
	} catch (ignored: Exception) {
		false
	}

	/**
	 * Open file explorer in specific path.
	 * If file is not directory, this opens folder container
	 *
	 * @param resource Target file to open
	 */
	@JvmStatic
	fun openInFileExplorer(resource: File): Boolean {
		// Check if file exists and check if resource is directory
		if (!resource.exists()) return false
		// Set resource file
		var resourceF = resource
		if (!resourceF.isDirectory)
			resourceF = resource.parentFile
		// Open file
		try {
			desktopToolkit.open(resourceF)
		} catch (ignored: Exception) {
			ignored.printStackTrace()
			return false
		}

		return true
	}

	/**
	 * Open file explorer in specific path.
	 * If file is not directory, this opens folder container
	 *
	 * @param resource Target file to open
	 */
	@JvmStatic
	fun openInFileExplorer(resource: String): Boolean = openInFileExplorer(File(resource))

	/**
	 * Get file size
	 */
	@JvmStatic
	fun fileSize(path: File): Long? {
		// Check if file exists
		if (!path.exists()) return null
		// Check if file is not directory
		if (!path.isDirectory) return Files.size(path.toPath())
		// Create size var
		val totalSize: Long
		// Walk directory
		try {
			val walk = Files.walk(path.toPath())
			totalSize = walk
				.filter(Files::isRegularFile)
				.mapToLong { localPath ->
					return@mapToLong try {
						Files.size(localPath)
					} catch (err: Exception) {
						0L
					}
				}
				.sum()
			// Close walk
			walk.close()
		} catch (err: Exception) {
			return null
		}

		// Get file size
		return totalSize
	}

}
