plugins {
	java
	application
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
}

group = "org.godot.launcher"
version = "0.0.1"

application {
	mainClassName = "${group}.Program"
}

configure<JavaPluginConvention> {
	sourceCompatibility = JavaVersion.VERSION_11
}

tasks {

	// Configure kotlin compile
	withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			jvmTarget = "11"
		}
	}

}

javafx {
	version = "15.0.1"
	modules = arrayListOf(
		"javafx.base",
		"javafx.controls",
		"javafx.fxml",
		"javafx.graphics",
		"javafx.media",
		"javafx.swing",
		"javafx.web"
	)
}

dependencies {
	/* basic dependencies */
	implementation("org.jetbrains:annotations:19.0.0")
	/* custom dependencies */
	implementation("com.github.ushiosan23:javafxcontrols:0.0.2")
	implementation(project(":godot_utils"))
	/* test */
	implementation("junit", "junit", "4.12")
}


// include scripts
apply("tasks.gradle.kts")


tasks.getByName("classes") {
	dependsOn(tasks["buildProperties"])
}
