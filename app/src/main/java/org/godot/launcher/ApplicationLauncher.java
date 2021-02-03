package org.godot.launcher;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.godot.launcher.core.BaseApplication;
import org.godot.launcher.core.screen.ScreenUtils;
import org.godot.launcher.utils.UtilsHelper;
import org.godot.launcher.utils.xml.PopupMenuLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Optional;

/**
 * This class manage application behaviour.
 */
public final class ApplicationLauncher extends BaseApplication {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Current application
	 */
	private static ApplicationLauncher currentLauncher;

	/* ---------------------------------------------------------
	 *
	 * Implemented methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Called after environment was initialized
	 *
	 * @throws Exception Error to launch application
	 */
	@Override
	protected void onReady() throws Exception {
		currentLauncher = this;
		configureLauncher();

//		EngineServerAccessor.getAvailableEngines(null);
	}

	/* ---------------------------------------------------------
	 *
	 * Methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get current application instance
	 *
	 * @return {@link ApplicationLauncher} current application object
	 */
	public static ApplicationLauncher getCurrentLauncher() {
		return currentLauncher;
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
		primaryStage.getIcons().add(
			new Image(UtilsHelper.getResourceStreamSafe(
				AppHelper.appProperties.getProperty("app.icon")
			))
		);
		if (trayIconFX != null)
			primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (trayIconFX != null) {
				if (!newValue) primaryStage.hide();
			}
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
			new Dimension2D(32, 95)
		);
		// Create scene
		Scene mainScene = new Scene(parent, winSize.getWidth(), winSize.getHeight());
		primaryStage.setScene(mainScene);
		// Configure scene
		Platform.runLater(() ->
			mainScene.getStylesheets().add(UtilsHelper.getResourceSafe(
				AppHelper.appProperties.getProperty("app.theme.dark")
			).toExternalForm())
		);
		// Configure position
		primaryStage.setX(screenSize.getWidth() - (winSize.getWidth() + 60));
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
	 * Implemented methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Called when select one option in exception alert
	 *
	 * @param buttonType Optional button type
	 */
	@Override
	protected void onExceptionAlertSelection(Optional<ButtonType> buttonType) {
		// Exit application
		Platform.exit();
		System.exit(0);
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

}
