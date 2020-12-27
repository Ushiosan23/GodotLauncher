import java.nio.file.*
import java.util.*

tasks.create("buildProperties") {
	dependsOn(tasks["processResources"])
	doLast {
		val fProp = file("$buildDir/resources/main/buildProperties.properties")
		if (!fProp.exists()) {
//			Files.createDirectories(fProp.parentFile.toPath())
			fProp.createNewFile()
		} else {
			val tempOut = fProp.outputStream()
			val channel = tempOut.channel

			channel.truncate(0)

			channel.close()
			tempOut.close()
		}

		val properties = Properties()

		properties.setProperty("build.version", version.toString())
		properties.setProperty("build.time", "${Calendar.getInstance().getTimeInMillis()}")
		properties.setProperty("build.java.version", "${System.getProperty("java.version")}")
		properties.setProperty("build.java.vendor", "${System.getProperty("java.vendor")}")

		val outPut = fProp.outputStream()
		properties.store(outPut, null)
		outPut.close()
	}
}
