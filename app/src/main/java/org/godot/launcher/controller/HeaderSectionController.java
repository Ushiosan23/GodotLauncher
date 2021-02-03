package org.godot.launcher.controller;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.godot.launcher.AppHelper;
import org.godot.launcher.utils.UtilsHelper;

/**
 * Header section controller
 */
public final class HeaderSectionController extends BaseController {

	/**
	 * Application logo
	 */
	@FXML
	public ImageView appLogo;

	/**
	 * Called when controller is ready
	 */
	@Override
	public void onReady() {
		// Listen stylesheet event
		appLogo.getScene().getStylesheets().addListener(this::onSceneStylesheetsChange);

	}

	/* ---------------------------------------------------------
	 *
	 * Events
	 *
	 * --------------------------------------------------------- */

	/**
	 * Called when scene stylesheets changed
	 *
	 * @param change Change event
	 */
	private void onSceneStylesheetsChange(ListChangeListener.Change<? extends String> change) {
		String dark_theme_file = UtilsHelper.getResourceSafe(
			appBundle.getProperty("app.theme.dark")
		).toExternalForm();
		String dark_theme = appBundle.getProperty("app.theme.icon.dark");
		String light_theme = appBundle.getProperty("app.theme.icon.light");

		// Check changes
		while (change.next()) {
			// Check added elements
			if (change.wasAdded()) {
				int index = change.getFrom();
				String current = change.getList().get(index);

				if (current.equals(dark_theme_file)) {
					appLogo.setImage(new Image(UtilsHelper.getResourceStreamSafe(dark_theme)));
				} else {
					appLogo.setImage(new Image(UtilsHelper.getResourceStreamSafe(light_theme)));
				}
			}
		}
	}

}
