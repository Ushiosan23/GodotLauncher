plugins {
	kotlin("jvm")
	id("kotlinx-serialization")
	id("org.jetbrains.dokka")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		jvmTarget = "11"
	}
}

tasks.dokkaHtml.configure {
	dokkaSourceSets {
		named("main") {
			suppress.set(false)
			includeNonPublic.set(true)
			skipDeprecated.set(false)
			skipEmptyPackages.set(false)
			sourceRoots.from(file("src/main"))
			jdkVersion.set(11)
			noStdlibLink.set(false)
			noJdkLink.set(false)
			noAndroidSdkLink.set(false)
		}
	}
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
	/* sqlite dependencies */
	implementation("org.xerial:sqlite-jdbc:3.34.0")
	/* custom dependencies */
	implementation("com.github.ushiosan23:networkutils:0.0.4")
	/* test */
	implementation("junit", "junit", "4.12")
}
