package org.godot.launcher.utils.db

/**
 * Object to manage database configuration
 */
object DBConfig {

	/**
	 * Check if configuration exists
	 *
	 * @param configName Target config to check
	 * @return [Boolean] configuration result
	 */
	@JvmStatic
	fun configExists(configName: String) = DBConnector.requireConnection<Boolean> { conn ->
		val query = "SELECT EXISTS(SELECT * FROM `${DBResources.Tables.TABLE_CONFIGURATION}` WHERE name=?) as found"
		val statement = conn.prepareStatement(query)

		// Set statement config
		statement.setString(1, configName)
		// Execute statement
		val resultSet = statement.executeQuery()
		val found = resultSet.getInt("found")
		// Close resources
		statement.close()
		// Return value
		return@requireConnection found != 0
	}

	/**
	 * Get db configuration from name
	 *
	 * @param configName Target config from name
	 * @return [String] configuration value or `null` if not exists
	 */
	@JvmStatic
	fun getConfig(configName: String) = DBConnector.requireConnection<String?> { conn ->
		// Check if config exists
		if (!configExists(configName)) return@requireConnection null
		// Create query
		val query = "SELECT * FROM `${DBResources.Tables.TABLE_CONFIGURATION}` WHERE name=?"
		val statement = conn.prepareStatement(query)

		// Statement config
		statement.setString(1, configName)
		// Execute statement
		val resultSet = statement.executeQuery()
		val result = resultSet.getString("value")
		// Close resources
		statement.close()
		// Return found
		return@requireConnection result
	}

	/**
	 * Insert configuration to table
	 *
	 * @param configName Target configuration
	 * @param configValue Target configuration value
	 *
	 * @return [Boolean] action result
	 */
	@JvmStatic
	fun updateConfig(configName: String, configValue: String) = DBConnector.requireConnection<Boolean> { conn ->
		// Check if configuration exists
		val exists = configExists(configName)
		val query = if (!exists)
			"INSERT INTO `${DBResources.Tables.TABLE_CONFIGURATION}` (name, value) VALUES (?, ?)"
		else
			"UPDATE `${DBResources.Tables.TABLE_CONFIGURATION}` SET value=? WHERE name=?"

		// Create statement
		val statement = conn.prepareStatement(query)
		// Configure statement
		if (!exists) {
			statement.setString(1, configName)
			statement.setString(2, configValue)
		} else {
			statement.setString(1, configValue)
			statement.setString(2, configName)
		}
		// Execute statement
		val result: Boolean = statement.executeUpdate() != 0
		// Close resources
		statement.close()
		return@requireConnection result
	}

	/**
	 * Delete configuration from database
	 *
	 * @param configName Target configuration
	 * @return [Boolean] transaction result
	 */
	@JvmStatic
	fun deleteConfig(configName: String) = DBConnector.requireConnection<Boolean> { conn ->
		// Check if configuration exists
		if (!configExists(configName)) return@requireConnection false
		// Create query
		val query = "DELETE FROM `${DBResources.Tables.TABLE_CONFIGURATION}` WHERE name=?"
		// Create transaction
		val statement = conn.prepareStatement(query)

		// Set configuration statement
		statement.setString(1, configName)
		// Execute query
		val result = statement.executeUpdate() != 0
		// Release resources
		statement.close()

		return@requireConnection result
	}

}
