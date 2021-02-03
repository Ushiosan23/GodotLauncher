package org.godot.launcher.controller.section;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.godot.launcher.controller.BaseController;
import org.godot.launcher.controls.IcomoonButton;

/**
 * Engine section controller
 */
public final class EnginesSectionController extends BaseController {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Engine menu button
	 */
	@FXML
	public IcomoonButton engineMenuButton;

	/**
	 * Engine scroll pane
	 */
	@FXML
	public ScrollPane engineScrollPane;

	/**
	 * Engine container box
	 */
	@FXML
	public VBox engineContainer;

	/* ---------------------------------------------------------
	 *
	 * Implemented methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Called when controller is ready
	 */
	@Override
	public void onReady() {

	}

}
