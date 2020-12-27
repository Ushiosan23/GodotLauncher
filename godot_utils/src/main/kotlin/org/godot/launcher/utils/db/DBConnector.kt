package org.godot.launcher.utils.db

import org.sqlite.JDBC
import org.w3c.dom.Element
import java.sql.Connection
import java.sql.DriverManager
import javax.xml.parsers.DocumentBuilderFactory

object DBConnector {

	/**
	 * Database connection
	 */
	private var connection: Connection? = null

	/**
	 * Initialize connection
	 */
	fun initializeConnector() {
		try {
			DriverManager.registerDriver(JDBC())
			// Initialize connection
			connection = DriverManager.getConnection(DBResources.databaseURL)
			// Check database content
			createTablesIfNotExists()
		} catch (err: Exception) {
			err.printStackTrace()
		}
	}

	/**
	 * Finish connection
	 */
	fun finalize() = requireConnectionNotThrow {
		it.close()
		connection = null
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Create database structure if not exists
	 */
	private fun createTablesIfNotExists() = requireConnection { conn ->
		if (verifyTables()) return@requireConnection
		// XML Document
		val documentFactory = DocumentBuilderFactory.newInstance()
		val builder = documentFactory.newDocumentBuilder()
		val document = builder.parse(DBResources.sqlCreationFile)
		// Node sections
		val tableNodes = document.getElementsByTagName("table")

		for (i in 0 until tableNodes.length) {
			val node = tableNodes.item(i) as Element
			val comment = node.getAttribute("comment")
			val content = node.textContent

			// Create statement
			val statement = conn.prepareStatement(content)
			// Info
			println("Creating \"$comment\"...")
			// Launch statement
			statement.execute()
		}
	}

	/**
	 * Verify if database tables exists
	 *
	 * @return [Boolean] transaction result
	 */
	private fun verifyTables() = requireConnection<Boolean> { conn ->
		// Query
		val query = "SELECT COUNT(name) FROM `sqlite_master` WHERE name NOT LIKE ?"
		val statement = conn.prepareStatement(query)

		// Configure statement
		statement.setString(1, "sql_%")
		// Execute statement
		val resultSet = statement.executeQuery()
		val result = resultSet.getInt(1)

		// Close resources
		statement.close()
		// Return result
		return@requireConnection result != 0
	}

	/**
	 * Check if connection is valid and launch block
	 *
	 * @param block Target block to launch
	 */
	internal fun requireConnection(block: (connection: Connection) -> Unit) {
		if (connection == null) throw UninitializedPropertyAccessException("Connection is not initialized")
		block.invoke(connection!!)
	}

	/**
	 * Check if connection is valid and launch block.
	 *
	 * This method don't launch exception
	 *
	 * @param block Target block to launch
	 */
	internal fun requireConnectionNotThrow(block: (connection: Connection) -> Unit) {
		if (connection != null) block.invoke(connection!!)
	}

	/**
	 * Check if connection is valid and launch block
	 *
	 * @param block Target block to launch
	 * @param T Target return value
	 * @return [T] block result
	 */
	internal inline fun <reified T> requireConnection(block: (connection: Connection) -> T): T {
		if (connection == null) throw UninitializedPropertyAccessException("Connection is not initialized")
		return block.invoke(connection!!)
	}

}
