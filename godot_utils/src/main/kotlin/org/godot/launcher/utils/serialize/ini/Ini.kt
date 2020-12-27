package org.godot.launcher.utils.serialize.ini

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.godot.launcher.utils.io.readByLine
import java.io.File
import java.io.IOException

/**
 * This class read ini files or similar files.
 *
 * This kind of files are easy to make and have the next structure:
 *
 * ```ini
 * [header_section]
 * item_section=1
 * ```
 *
 * If the file does not have a header section but has items, they are all added in the default section.
 */
@Serializable
class Ini : ICaseSensitive {


	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Map with all ini sections
	 */
	val sections: MutableMap<String, Section> = mutableMapOf()

	/**
	 * Case sensitive properties
	 */
	override var isCaseSensitive: Boolean = false
		set(value) {
			field = value
			updateContent()
		}

	/* ---------------------------------------------------------
	 *
	 * Internal properties
	 *
	 * --------------------------------------------------------- */

	@Contextual
	private val localFile: File

	/* ---------------------------------------------------------
	 *
	 * Constructors
	 *
	 * --------------------------------------------------------- */

	/**
	 * Primary constructor with url file
	 *
	 * @param file Target file to read
	 */
	constructor(file: File) {
		// Check if file exists
		if (!file.exists()) throw IOException("File \"$file\" not found")
		// Set file
		localFile = file
		// Read file
		updateContent()
	}

	/* ---------------------------------------------------------
	 *
	 * Methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get selected section
	 *
	 * @param name Target section to get
	 * @param default Default section if selected not exists
	 * @return [Section] Result section or [default] section
	 */
	fun getSection(name: String, default: Section = Section()): Section =
		sections[name] ?: default

	/**
	 * Get selected section, but used like operator
	 *
	 * @param name Target section to get
	 * @param default Default section if selected not exists
	 * @return [Section] Result section or [default] section
	 */
	operator fun get(name: String, default: Section = Section()): Section =
		getSection(name, default)

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Update section content
	 */
	private fun updateContent() {
		// Clean storage data
		if (sections.isNotEmpty())
			sections.clear()

		// Create default section
		var sectionKey = Section.transformKey(this, DEFAULT_SECTION_KEY)
		sections[sectionKey] = Section()
		sections[sectionKey]?.isCaseSensitive = isCaseSensitive

		// Current property
		var currentProperty: String? = null

		// Read file
		localFile.readByLine {
			// Ignore empty and comment lines
			if (isCommentLine(it)) return@readByLine

			// Check if line is section
			if (isSectionLine(it)) {
				sectionKey = Section.transformKey(this, getSectionName(it))

				// Don't duplicate keys
				if (sections.containsKey(sectionKey)) return@readByLine

				val localSection = Section()
				localSection.isCaseSensitive = isCaseSensitive
				localSection.defaultSection = sections[Section.transformKey(this, DEFAULT_SECTION_KEY)] ?: localSection

				sections[sectionKey] = localSection
			}

			// Create properties
			if (it.contains("=")) {
				val localPair = it.split(Regex("="), 2)
				if (localPair.size == 2) {
					currentProperty = localPair[0].trim()
					sections[sectionKey]?.put(
						currentProperty!!,
						localPair[1].trim()
					)
				}
			} else {
				val section = sections[sectionKey]
				currentProperty?.let { property ->
					section?.get(property)?.let { sProperty ->
						section.put(property, sProperty + it)
					}
				}
			}
		}

	}

	/* ---------------------------------------------------------
	 *
	 * Implemented methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Object string representation
	 *
	 * @return [String] Object string representation
	 */
	override fun toString(): String {
		return "$sections"
	}

	/* ---------------------------------------------------------
	 *
	 * Companion objects
	 *
	 * --------------------------------------------------------- */

	companion object {

		/**
		 * Ini file comment
		 */
		private const val COMMENT_ITEM: String = ";"

		/**
		 * Default section name
		 */
		private const val DEFAULT_SECTION_KEY: String = "Default"

		/* ---------------------------------------------------------
		 *
		 * Internal static methods
		 *
		 * --------------------------------------------------------- */

		/**
		 * Check if line is valid comment.
		 * Also empty lines are recognized like comments to.
		 *
		 * @param line Target line to check
		 * @return [Boolean] comment result
		 */
		private fun isCommentLine(line: String): Boolean {
			// Trim line
			val fLine = line.trim()

			return if (fLine.isEmpty())
				true
			else
				COMMENT_ITEM.contains(fLine.substring(0, 1))
		}

		/**
		 * Check if current line is valid section
		 *
		 * ## Example
		 * ```kotlin
		 * // Valid section string
		 * println(isSectionLine("[section_name]"))
		 * // Invalid section string
		 * println(isSectionLine("hello [section] name"))
		 * ```
		 *
		 * @param line Target line to check
		 * @return [Boolean] section result
		 */
		private fun isSectionLine(line: String): Boolean {
			val fLine = line.trim()
			return fLine.startsWith("[") && fLine.endsWith("]")
		}

		/**
		 * Get line section name
		 *
		 * ## Example
		 * ```kotlin
		 * // Valid section name
		 * println(getSectionName("[section_name]"))
		 * // Invalid section name
		 * println(getSectionName("[]"))
		 * println(getSectionName("error[]section"))
		 * ```
		 *
		 * @param line Target line to check
		 * @return [String] section name result
		 * @throws IllegalArgumentException If section is not valid or section name is empty
		 */
		private fun getSectionName(line: String): String {
			// Check if is valid section
			if (!isSectionLine(line))
				throw IllegalArgumentException("Line \"$line\" is not valid section")

			// Create section name
			val fLine = line.trim()
			val sectionName = fLine.substring(1 until fLine.length - 1).trim()

			// Check section key
			if (sectionName.isEmpty())
				throw IllegalArgumentException("Section key cannot be empty")

			return sectionName
		}

	}

}
