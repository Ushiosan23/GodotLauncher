package org.godot.launcher.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import org.godot.launcher.AppHelper;
import org.godot.launcher.ApplicationLauncher;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Base controller class
 */
public abstract class BaseController implements Initializable {

	/**
	 * Scene location url
	 */
	protected URL sceneLocation;

	/**
	 * Application properties
	 */
	protected Properties appBundle;

	/**
	 * Scene bundle object
	 */
	@Nullable
	protected ResourceBundle sceneBundle;

	/**
	 * Current application
	 */
	ApplicationLauncher applicationLauncher;

	/**
	 * Called when controller is initialized
	 *
	 * @param location  Target scene location
	 * @param resources Resource controller
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sceneLocation = location;
		sceneBundle = resources;
		appBundle = AppHelper.appProperties;
		applicationLauncher = ApplicationLauncher.getCurrentLauncher();
		// Call ready
		Platform.runLater(this::onReady);
	}

	/**
	 * Called when controller is ready
	 */
	public abstract void onReady();

}
