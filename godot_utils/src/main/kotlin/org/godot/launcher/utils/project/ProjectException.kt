package org.godot.launcher.utils.project

import java.lang.Exception

/**
 * Project exception class
 */
class ProjectException : Exception {

	/**
	 * Empty constructor
	 */
	constructor() : super()

	/**
	 * Message constructor
	 *
	 * @param message Target exception message
	 */
	constructor(message: String?) : super(message)

	/**
	 * Message and cause constructor
	 *
	 * @param message Target exception message
	 * @param cause Cause exception
	 */
	constructor(message: String?, cause: Throwable?) : super(message, cause)

	/**
	 * Cause constructor
	 *
	 * @param cause Cause exception
	 */
	constructor(cause: Throwable?) : super(cause)

	/**
	 * Message, cause, enableSuppression and writableStackTrace constructor
	 *
	 * @param message Target exception message
	 * @param cause Cause exception
	 * @param enableSuppression Whether or not suppression is enabled or disabled
	 * @param writableStackTrace Whether or not the stack trace should be writable
	 */
	constructor(
		message: String?,
		cause: Throwable?,
		enableSuppression: Boolean,
		writableStackTrace: Boolean
	) : super(message, cause, enableSuppression, writableStackTrace)
}
