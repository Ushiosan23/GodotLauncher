package org.godot.launcher.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.godot.launcher.utils.UtilsHelper;
import org.godot.launcher.utils.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;

/**
 * Main scene controller
 */
public final class MainSceneController extends BaseController {

	/**
	 * Tab section pane
	 */
	@FXML
	TabPane tabSectionPane;

	/**
	 * Called when scene is ready
	 */
	@Override
	public void onReady() {
		// Load tab menus
		loadTabs();
		// request focus
		tabSectionPane.requestFocus();
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Load tab sections
	 */
	private void loadTabs() {
		// Load xml file
		Document document = XMLUtils.loadXML(
			UtilsHelper.getResourceSafe("menus/tab_menu.xml")
		);
		NodeList nodes = document.getDocumentElement().getElementsByTagName("item");

		// Iterate all nodes
		for (int i = 0; i < nodes.getLength(); i++) {
			// Save current node
			Element current = (Element) nodes.item(i);
			// Check if node is valid
			if (!current.hasAttribute("name")) continue;
			if (!current.hasAttribute("section")) continue;

			// Initialize properties
			String name = current.getAttribute("name");
			String location = current.getAttribute("section");
			URL sectionURL = UtilsHelper.getResource(location);

			// Check if location is valid
			if (sectionURL == null) continue;

			// Create tab
			try {
				Parent parent = FXMLLoader.load(sectionURL);
				Tab tab = new Tab(name, parent);

				// Add tab to panel
				tabSectionPane.getTabs().add(tab);
			} catch (Exception ignored) {
			}
		}
	}

}
