package org.godot.launcher.utils

import org.junit.Test
import java.io.File
import javax.swing.filechooser.FileSystemView


class OsActionsTest {

	private val urlToOpen = "https://github.com/Ushiosan23"
	private val fileToOpen = "${System.getProperty("user.home")}\\Pictures"

	private fun openActions() {
		println(OsActions.openInFileExplorer(fileToOpen))
		Thread.sleep(5000)
		println(OsActions.openURL(urlToOpen))
	}

	private fun fileDrivers() {
		val listRoots = File.listRoots().toMutableList()
		val fsv = FileSystemView.getFileSystemView()

		listRoots.add(File("D:\\Games\\Runner"))

		listRoots.forEach {
			println("Driver $it")
			println("\tType: ${fsv.getSystemTypeDescription(it)}")
			println("\tTotal Space: ${it.totalSpace} bytes")
			println("\tFree Space: ${it.freeSpace} bytes")
			OsActions.fileSize(it)?.let { size ->
				println("\tFile Size: $size bytes")
			}
			println("\tIs system drive: ${fsv.isDrive(it)}")
		}
	}

	@Test
	fun runTest() {
		fileDrivers()
	}

}
