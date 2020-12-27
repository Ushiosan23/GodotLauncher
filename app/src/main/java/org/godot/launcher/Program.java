package org.godot.launcher;

import javafx.application.Application;

/**
 * This class only launch application.
 */
public final class Program {

	/**
	 * Entry main point application.
	 *
	 * @param args Program arguments
	 */
	public static void main(String[] args) {
		Application.launch(ApplicationLauncher.class, args);
	}

}
