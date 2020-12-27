package org.godot.launcher.core.screen;

import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ScreenUtils {

	/**
	 * This class cannot be instanced
	 */
	private ScreenUtils() {
	}

	/**
	 * Get current screen
	 *
	 * @return {@link Screen} Screen device
	 */
	public static Screen currentScreen() {
		return Screen.getPrimary();
	}

	/**
	 * Get screen work area size
	 *
	 * @param screen Target screen to get size
	 * @return {@link Rectangle2D} Rectangle with all sizes
	 */
	public static Rectangle2D getScreenSize(@Nullable Screen screen) {
		if (screen == null) return getScreenSize(currentScreen());
		// Get visual bounds
		return screen.getVisualBounds();
	}

	/**
	 * Get percentage of screen size
	 *
	 * @param screen     Target screen to get dimension
	 * @param percentage Target dimension percentage
	 * @return {@link Rectangle2D} result dimension
	 */
	public static Rectangle2D getScreenSizePercent(@Nullable Screen screen, @NotNull Dimension2D percentage) {
		Rectangle2D screenDimen = getScreenSize(screen);
		return new Rectangle2D(
			0,
			0,
			(percentage.getWidth() * screenDimen.getWidth() / 100),
			(percentage.getHeight() * screenDimen.getHeight() / 100)
		);
	}

}
