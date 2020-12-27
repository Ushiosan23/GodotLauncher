# Godot Launcher

## Multi godot installation and project manager

This application is used to install multiple versions of "[Godot](https://godotengine.org)" (can be downloaded from the same
application)
and manage your projects by engine version. It is very useful if you have a few, or many projects on different engine
versions. Or if you just want to make small changes to your project or just want to launch a project scene without starting
the full program.

## Features

- Engines
	- [ ] You can download multiple engine
	  versions ([Standard or Mono](https://godotengine.org/qa/54711/whats-the-difference-between-mono-and-standard-version#:~:text=2%3A%20as%20you%20said%2C%20the,do%20in%20the%20mono%20version.))
	- [ ] You can add your onw installations
	- [x] You can start different engine instances
	- [ ] You can play games without start program
	- [ ] You can start scene without start program
	- [ ] You can deploy your game without start program
- Projects
	- [ ] You can add local projects
	- [x] You can create new projects
	- [x] The application Scan your project and (This information is only used for the convenience of the user and at no time
	  is it sent to any remote server or any tracking system.):
		- [x] List all scenes in the app
		- [x] Display real project name (defined in an engine.cfg | project.godot file)
		- [x] Display real project icon (defined in an engine.cfg | project.godot file)
		- [x] Detect a main scene (defined in an engine.cfg | project.godot file)
	- [ ] You can change default engine version
		- [ ] Display a warning if project and engine versions are different
- TODO
	- [ ] Add engine documentation section
	- [ ] Implement error tracking (The application by default hide godot system console)

## Support platforms

- [x] Windows
- [x] Linux
- [ ] MacOS (I don't have a Mac device to check it)
	- MacOS manage applications a little different
	- It is easy to start an application in macOS, but all applications are packaged in a special folder, and it is difficult
	  to know where the program is actually stored.

## Build

- Requirements:
	- Windows 8.1 or newest
		- Java [JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
		  or [OpenJDK](https://developers.redhat.com/products/openjdk/download) 11
		- Gradle 6.0 or newest
		- Internet connection

Download app from [releases](https://github.com/Ushiosan23/GodotLauncher/releases) section or source code

```cmd
git clone https://github.com/Ushiosan23/GodotLauncher.git ./YourDirectory
```

Run application from project

```cmd
gradlew :run
```

Build application

```cmd
gradlew :build
```
