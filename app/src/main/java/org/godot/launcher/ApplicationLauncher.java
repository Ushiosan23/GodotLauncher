package org.godot.launcher;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import org.godot.launcher.core.BaseApplication;
import org.godot.launcher.core.screen.ScreenUtils;
import org.godot.launcher.utils.UtilsHelper;
import org.godot.launcher.utils.xml.PopupMenuLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class manage application behaviour.
 */
public final class ApplicationLauncher extends BaseApplication {

	/**
	 * Called after environment was initialized
	 *
	 * @throws Exception Error to launch application
	 */
	@Override
	protected void onReady() throws Exception {
		configureLauncher();
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Configure launcher properties
	 */
	private void configureLauncher() throws Exception {
		// Configure tray icon
		if (trayIconFX != null)
			configureTray();
		// Configure window
		primaryStage.setTitle(AppHelper.appProperties.getProperty("app.name"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) primaryStage.hide();
		});
		// Display window
		configureScene();
		primaryStage.show();
	}

	/**
	 * Configure primary scene
	 */
	private void configureScene() throws Exception {
		URL sceneURL = UtilsHelper.getResourceSafe("scenes/MainScene.fxml");
		Parent parent = FXMLLoader.load(sceneURL);
		// Other configurations
		Rectangle2D screenSize = ScreenUtils.getScreenSize(null);
		Rectangle2D winSize = ScreenUtils.getScreenSizePercent(
			null,
			new Dimension2D(35, 95)
		);
		// Create scene
		Scene mainScene = new Scene(parent, winSize.getWidth(), winSize.getHeight());
		primaryStage.setScene(mainScene);
		// Configure position
		primaryStage.setX(screenSize.getWidth() - (winSize.getWidth() + 10));
		primaryStage.setY(screenSize.getHeight() - (winSize.getHeight() + 10));
	}

	/**
	 * Configure tray icon behaviour
	 */
	private void configureTray() {
		trayIconFX.setToolTip(AppHelper.appProperties.getProperty("app.name"));
		trayIconFX.addActionListener(this::onTrayAction);
		// Create popup menu
		PopupMenu trayPopup = PopupMenuLoader.loadMenuFromXMLStream(
			UtilsHelper.getResourceStreamSafe("menus/tray_menu.xml")
		);

		trayIconFX.setPopupMenu(trayPopup);
		trayIconFX.addPopupItemSelectionListener(event -> Platform.runLater(() ->
			onTrayPopupItemSelected(event)
		));
	}

	/* ---------------------------------------------------------
	 *
	 * Events
	 *
	 * --------------------------------------------------------- */

	/**
	 * Called when tray icon was clicked
	 *
	 * @param event Target mouse event
	 */
	private void onTrayAction(ActionEvent event) {
		// Run in javaFX Thread
		Platform.runLater(() -> {
			primaryStage.show();
			primaryStage.requestFocus();
		});
	}

	/**
	 * Called when tray item is selected
	 *
	 * @param event Target action event
	 */
	private void onTrayPopupItemSelected(ActionEvent event) {
		switch (event.getActionCommand().toLowerCase()) {
			case "open":
				primaryStage.show();
				primaryStage.requestFocus();
				break;
			case "config":
				break;
			case "quit":
				// Exit application
				Platform.exit();
				System.exit(0);
				break;
		}
	}

	/**
	 * Check if value is valid separator
	 *
	 * @param value Target value to check
	 * @return Match result
	 */
	private static boolean isSeparator(String value) {
		Pattern pattern = Pattern.compile("^(-){3,}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);

		return matcher.matches();
	}

}
