package org.godot.launcher.controls;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Special button to set font icons
 */
public class IcomoonButton extends Button {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Icon character property
	 */
	private StringProperty iconProperty;

	/**
	 * Icon container
	 */
	private final Label iconContainer = new Label();

	/* ---------------------------------------------------------
	 *
	 * Constructors
	 *
	 * --------------------------------------------------------- */

	/**
	 * Empty button
	 */
	public IcomoonButton() {
		super();
		initialize();
	}

	/**
	 * String button
	 *
	 * @param text Target button text
	 */
	public IcomoonButton(String text) {
		super(text);
		initialize();
	}

	/**
	 * Button with icon
	 *
	 * @param text Target button text
	 * @param icon Target button icon
	 */
	public IcomoonButton(String text, String icon) {
		super(text);
		setIcon(icon);
		initialize();
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Initialize button
	 */
	private void initialize() {
		// Set class
		getStyleClass().add("icomoon-button");
		// configure icon container
		iconContainer.getStyleClass().add("icon-label");
		iconContainer.textProperty().bind(getIconProperty());
		// insert icon in label
		setGraphic(iconContainer);
	}

	/* ---------------------------------------------------------
	 *
	 * JavaFX Methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get icon property
	 *
	 * @return Icon property
	 */
	public StringProperty getIconProperty() {
		if (iconProperty == null)
			iconProperty = new SimpleStringProperty(this, "iconProperty", "");

		return iconProperty;
	}

	/**
	 * Get current button icon
	 *
	 * @return character icon
	 */
	public String getIcon() {
		return getIconProperty().get();
	}

	/**
	 * Set button icon
	 *
	 * @param icon Target button icon
	 */
	public void setIcon(String icon) {
		getIconProperty().set(icon);
	}

}
