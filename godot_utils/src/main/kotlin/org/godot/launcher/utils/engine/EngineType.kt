package org.godot.launcher.utils.engine

/**
 * Engine type
 */
enum class EngineType(val typeName: String) {
	Standard("Standard"), // Default type engine
	Mono("Mono"); // Mono version type engine

	/**
	 * Object string representation
	 */
	override fun toString(): String = typeName

}
