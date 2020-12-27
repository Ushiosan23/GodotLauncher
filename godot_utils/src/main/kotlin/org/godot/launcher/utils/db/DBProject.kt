package org.godot.launcher.utils.db

import org.godot.launcher.utils.engine.Engine
import org.godot.launcher.utils.project.Project
import java.io.File

/**
 * Database project object manager
 */
object DBProject {

	/**
	 * Check if project exists
	 *
	 * @param location Target file location
	 */
	@JvmStatic
	fun projectExists(location: String) = DBConnector.requireConnection<Boolean> { conn ->
		// Create query
		val query = "SELECT EXISTS(SELECT * FROM `${DBResources.Tables.TABLE_PROJECT}` WHERE location=?) as found"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setString(1, location)
		// Execute query
		val resultSet = statement.executeQuery()
		val result = resultSet.getInt("found")
		// Close resources
		statement.close()
		// Return result
		return@requireConnection result != 0
	}

	/**
	 * Check if project exists
	 *
	 * @param project Target project object
	 */
	@JvmStatic
	fun projectExists(project: Project): Boolean =
		projectExists(project.projectFile)

	/**
	 * Get project from location
	 *
	 * @param location Target project location
	 * @return [Project] Project instance or `null` if not exists
	 *
	 * @throws java.io.IOException if project location not exists
	 * @throws org.godot.launcher.utils.project.ProjectException if project is not valid
	 */
	@JvmStatic
	fun getProject(location: String) = DBConnector.requireConnection<Project?> { conn ->
		// Check if project exists
		if (!projectExists(location)) return@requireConnection null
		// Create query
		val query = "SELECT * FROM `${DBResources.Tables.TABLE_PROJECT}` WHERE location=? ORDER BY name DESC"
		val statement = conn.prepareStatement(query)
		// Configure statement
		statement.setString(1, location)
		// Execute query
		val resultSet = statement.executeQuery()
		// Create project object
		val projectLocation = resultSet.getString("location")
		val fileLocation = File(projectLocation)
		// Create project object
		val projectObj = Project(File(fileLocation.parent))
		// Close resources
		statement.close()
		// Get project
		return@requireConnection projectObj
	}

	/**
	 * Get list with all projects
	 *
	 * @return [List] With all valid projects
	 */
	@JvmStatic
	fun getAllProjects() = DBConnector.requireConnection<List<Project>> { conn ->
		// Create query
		val resultList: MutableList<Project> = mutableListOf()
		// Create query
		val query = "SELECT name FROM ${DBResources.Tables.TABLE_PROJECT} ORDER BY name DESC"
		val statement = conn.prepareStatement(query)

		// Execute query
		val resultSet = statement.executeQuery()
		// Iterate results
		while (resultSet.next()) {
			getProject(resultSet.getString("location"))?.let {
				resultList.add(it)
			}
		}
		// Close resources
		statement.close()
		// Return list of engines
		return@requireConnection resultList
	}

	/**
	 * Get engine from project location
	 *
	 * @param location Target project location
	 * @return [Engine] Engine instance or `null` if project doesn't have linked engine or if engine doesn't exists
	 */
	@JvmStatic
	fun getProjectEngine(location: String) = DBConnector.requireConnection<Engine?> { conn ->
		// Check if project exists
		if (!projectExists(location)) return@requireConnection null
		// Create query
		val query = "SELECT * FROM `${DBResources.Tables.TABLE_PROJECT}` WHERE location=?"
		val statement = conn.prepareStatement(query)
		// Configure statement
		statement.setString(1, location)
		// Execute query
		val resultSet = statement.executeQuery()
		// Get engine id
		val engineId = resultSet.getInt("engine_ref")
		// Close resources
		statement.close()
		// Get project
		return@requireConnection DBEngine.getEngine(engineId)
	}

}
