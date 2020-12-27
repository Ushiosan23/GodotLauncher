package org.godot.launcher.core;

import com.github.ushiosan23.javafx.dialogs.ExceptionAlert;
import com.github.ushiosan23.javafx.system.TrayIconFX;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.godot.launcher.utils.EnvironmentUtils;
import org.godot.launcher.utils.UtilsHelper;
import org.godot.launcher.utils.system.Platform;

/**
 * Base application class
 */
public abstract class BaseApplication extends Application {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Primary stage
	 */
	protected Stage primaryStage;

	/**
	 * System tray icon
	 */
	protected TrayIconFX trayIconFX = null;

	/**
	 * Application icon
	 */
	protected Image appImage;

	/* ---------------------------------------------------------
	 *
	 * Implemented methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * JavaFX Entry method
	 *
	 * @param appStage application stage
	 * @throws Exception application error
	 */
	@Override
	public void start(Stage appStage) throws Exception {
		try {
			// Initialize resources
			checkPlatform();
			EnvironmentUtils.initializeEnvironment();
			// Initialize properties
			appImage = new Image(UtilsHelper.getResourceStreamSafe("icons/app_icon.png"));
			primaryStage = appStage;
			if (TrayIconFX.isPlatformSupport()) {
				trayIconFX = new TrayIconFX(appImage);
				trayIconFX.attachToSystem();
				// Set events
				primaryStage.setOnCloseRequest(this::onCloseRequest);
			}
			// Call methods
			onReady();
		} catch (Exception err) {
			ExceptionAlert exceptionAlert = new ExceptionAlert(err);
			exceptionAlert.showAndWait();

			// Exit application
			javafx.application.Platform.exit();
			System.exit(0);
		}
	}

	/**
	 * Called when environment was initialized
	 *
	 * @throws Exception Application error
	 */
	protected abstract void onReady() throws Exception;

	/**
	 * This method is called when the application should stop, and provides a
	 * convenient place to prepare for application exit and destroy resources.
	 *
	 * <p>
	 * The implementation of this method provided by the Application class does nothing.
	 * </p>
	 *
	 * <p>
	 * NOTE: This method is called on the JavaFX Application Thread.
	 * </p>
	 *
	 * @throws java.lang.Exception if something goes wrong
	 */
	@SuppressWarnings("FinalizeCalledExplicitly")
	@Override
	public void stop() throws Exception {
		super.stop();
		EnvironmentUtils.finalize();
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Check if current platform is valid to use
	 *
	 * @throws UnsupportedOperationException Platform error
	 */
	private void checkPlatform() throws UnsupportedOperationException {
		Platform platform = Platform.getFromSystem();

		if (platform == Platform.MACOS || platform == Platform.UNKNOWN)
			throw new UnsupportedOperationException(String.format(
				"\"%s\" platform is not supported yet.",
				Platform.getCurrentPlatform()
			));
	}


	/* ---------------------------------------------------------
	 *
	 * Events
	 *
	 * --------------------------------------------------------- */

	/**
	 * Primary window close request
	 */
	private void onCloseRequest(WindowEvent event) {
		trayIconFX.detachToSystem();
		primaryStage.close();
		event.consume();
	}

}
