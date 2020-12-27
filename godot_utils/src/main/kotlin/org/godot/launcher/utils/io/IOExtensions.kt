package org.godot.launcher.utils.io

import java.io.*

/**
 * Read file line by line
 *
 * @param block Action to launch
 * @throws IOException Error if file not exists
 */
fun File.readByLine(block: (line: String) -> Unit) {
	// Check if file not exists
	if (!exists()) throw IOException("File \"$this\" not found")

	// Stream actions
	val stream = inputStream()
	val fReader = BufferedReader(InputStreamReader(stream))

	fReader.forEachLine {
		block.invoke(it)
	}

	fReader.close()
	stream.close()
}
