buildscript {
	val ktVersion = "1.4.20"

	repositories {
		mavenCentral()
		jcenter()
		maven {
			url = uri("https://plugins.gradle.org/m2/")
		}
	}

	dependencies {
		classpath(kotlin("gradle-plugin", ktVersion))
		classpath(kotlin("serialization", ktVersion))
		classpath("org.jetbrains.dokka:dokka-gradle-plugin:$ktVersion")
		classpath("org.openjfx:javafx-plugin:0.0.9")
	}
}

allprojects {
	repositories {
		mavenCentral()
		jcenter()
		maven {
			url = uri("https://plugins.gradle.org/m2/")
		}
	}
}
