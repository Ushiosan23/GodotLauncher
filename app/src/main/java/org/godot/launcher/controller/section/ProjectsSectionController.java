package org.godot.launcher.controller.section;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.godot.launcher.controller.BaseController;
import org.godot.launcher.controls.IcomoonButton;

/**
 * Engine section controller
 */
public final class ProjectsSectionController extends BaseController {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Project menu button
	 */
	@FXML
	public IcomoonButton projectMenuButton;

	/**
	 * Scroll pane container
	 */
	@FXML
	public ScrollPane projectScrollPane;

	/**
	 * Project container
	 */
	@FXML
	public VBox projectContainer;

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
