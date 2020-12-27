package org.godot.launcher.utils.db

import org.godot.launcher.utils.engine.Engine

/**
 * Object to manage database engines
 */
object DBEngine {

	/**
	 * Check if engine exists
	 *
	 * @param engineName Target engine name
	 * @return [Boolean] transaction result
	 */
	@JvmStatic
	fun engineExists(engineName: String) = DBConnector.requireConnection<Boolean> { conn ->
		// Create query
		val query = "SELECT EXISTS(SELECT * FROM '${DBResources.Tables.TABLE_ENGINE}' WHERE name=?) as found"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setString(1, engineName)
		// Execute statement
		val resultSet = statement.executeQuery()
		val found = resultSet.getInt("found")
		// Close resource
		statement.close()
		// Get result
		return@requireConnection found != 0
	}

	/**
	 * Get engine from name
	 *
	 * @param engineName Target engine name
	 * @return [Engine] engine instance
	 */
	@JvmStatic
	fun getEngine(engineName: String) = DBConnector.requireConnection<Engine?> { conn ->
		// Check if engine exist
		if (!engineExists(engineName)) return@requireConnection null
		// Create query
		val query = "SELECT * FROM `${DBResources.Tables.TABLE_ENGINE}` WHERE name=?"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setString(1, engineName)
		// Execute statement
		val resultSet = statement.executeQuery()
		val defaultVal = resultSet.getInt("is_default")
		val result = Engine(
			resultSet.getString("location"),
			defaultVal != 0
		)
		// Close resources
		statement.close()
		// Return value
		return@requireConnection result
	}

	/**
	 * Get all engine objects
	 *
	 * @return [List] with all engine instances
	 */
	@JvmStatic
	fun getAllEngines() = DBConnector.requireConnection<List<Engine>> { conn ->
		// Create query
		val resultList: MutableList<Engine> = mutableListOf()
		// Create query
		val query = "SELECT name FROM ${DBResources.Tables.TABLE_ENGINE} ORDER BY name DESC"
		val statement = conn.prepareStatement(query)

		// Execute query
		val resultSet = statement.executeQuery()
		// Iterate results
		while (resultSet.next()) {
			getEngine(resultSet.getString("name"))?.let {
				resultList.add(it)
			}
		}

		// Close resources
		statement.close()
		// Return list of engines
		return@requireConnection resultList
	}

	/**
	 * Add engine to database
	 *
	 * @param engine Target engine to add
	 * @return [Boolean] action result
	 */
	@JvmStatic
	fun addEngine(engine: Engine) = DBConnector.requireConnection<Boolean> { conn ->
		// Check if engine exists
		if (engineExists(engine.engineName)) return@requireConnection false
		// Create query
		val query = "INSERT INTO `${DBResources.Tables.TABLE_ENGINE}` (name, location, is_default) VALUES (?, ?, ?)"
		val statement = conn.prepareStatement(query)

		// Confirm statement
		statement.setString(1, engine.engineName)
		statement.setString(2, engine.location)
		statement.setInt(3, if (engine.isDefault) 1 else 0)

		// Execute statement
		val result = statement.executeUpdate()
		// Close resources
		statement.close()
		// Return value
		return@requireConnection result != 0
	}

	/**
	 * Update engine
	 *
	 * @param engine Target engine to update
	 * @return [Boolean] transaction result
	 */
	@JvmStatic
	fun updateEngine(
		engine: Engine
	) = DBConnector.requireConnection<Boolean> { conn ->
		// Check if engine exists
		if (!engineExists(engine.engineName)) return@requireConnection false
		// Create query
		val query = "UPDATE `${DBResources.Tables.TABLE_ENGINE}` SET location=?, is_default=? WHERE name=?"
		// Create statement
		val statement = conn.prepareStatement(query)

		// configure statement
		statement.setString(1, engine.location)
		statement.setInt(2, if (engine.isDefault) 1 else 0)
		statement.setString(3, engine.engineName)

		// Execute query
		val result = statement.executeUpdate()
		statement.close()

		return@requireConnection result != 0
	}

	/**
	 * Delete engine
	 *
	 * @param engine Target engine to delete
	 * @return [Boolean] transaction result
	 */
	@JvmStatic
	fun removeEngine(
		engine: Engine
	) = DBConnector.requireConnection<Boolean> { conn ->
		// Check if engine exists
		if (!engineExists(engine.engineName)) return@requireConnection false
		// Create query
		val query = "DELETE FROM `${DBResources.Tables.TABLE_ENGINE}` WHERE name=? AND location=?"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setString(1, engine.engineName)
		statement.setString(2, engine.location)

		// Execute update
		val result = statement.executeUpdate()
		// Close resources
		statement.close()
		// Return result
		return@requireConnection result != 0
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Check if engine exists in db by id
	 *
	 * @param id Target engine id
	 * @return [Boolean] transaction result
	 */
	@JvmStatic
	internal fun engineExists(id: Int) = DBConnector.requireConnection<Boolean> { conn ->
		// Create query
		val query = "SELECT EXISTS(SELECT * FROM '${DBResources.Tables.TABLE_ENGINE}' WHERE id=?) as found"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setInt(1, id)
		// Execute statement
		val resultSet = statement.executeQuery()
		val found = resultSet.getInt("found")
		// Close resource
		statement.close()
		// Get result
		return@requireConnection found != 0
	}

	/**
	 * Get engine from name
	 *
	 * @param id Target engine id
	 * @return [Engine] engine instance
	 */
	@JvmStatic
	internal fun getEngine(id: Int) = DBConnector.requireConnection<Engine?> { conn ->
		// Check if engine exist
		if (!engineExists(id)) return@requireConnection null
		// Create query
		val query = "SELECT * FROM `${DBResources.Tables.TABLE_ENGINE}` WHERE id=?"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setInt(1, id)
		// Execute statement
		val resultSet = statement.executeQuery()
		val defaultVal = resultSet.getInt("is_default")
		val result = Engine(
			resultSet.getString("location"),
			defaultVal != 0
		)
		// Close resources
		statement.close()
		// Return value
		return@requireConnection result
	}

}
