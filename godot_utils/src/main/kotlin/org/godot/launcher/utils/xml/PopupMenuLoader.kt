package org.godot.launcher.utils.xml

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.awt.Menu
import java.awt.MenuItem
import java.awt.PopupMenu
import java.io.InputStream

/**
 * Custom object to manage popup menu
 */
object PopupMenuLoader {


	/**
	 * Load popup menu from xml document
	 *
	 * @param document Target file stream
	 * @return [PopupMenu] Custom popup menu
	 */
	@JvmStatic
	fun loadMenuFromXML(document: Document): PopupMenu {
		val documentName = document.documentElement.getAttribute("name")
		val popup = PopupMenu(documentName)
		val elements = document.documentElement.childNodes

		for (element in 0 until elements.length) {
			val current = elements.item(element)

			if (current.nodeName == "menu") configureMenu(current, popup)
			if (current.nodeName == "item") popup.add(configureItem(current))
			if (current.nodeName == "separator") popup.addSeparator()
		}

		return popup
	}

	/**
	 * Load popup menu from xml stream
	 *
	 * @param document Target document stream
	 * @return [PopupMenu] Custom popup menu
	 */
	@JvmStatic
	fun loadMenuFromXMLStream(document: InputStream): PopupMenu =
		loadMenuFromXML(XMLUtils.loadXML(document))


	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Create menu item from configuration
	 *
	 * @param node Target node to configure
	 * @return [MenuItem] Menu item result
	 */
	private fun configureItem(node: Node): MenuItem {
		val nodeEl = node as Element
		val name = nodeEl.getAttribute("name")
		val mItem = MenuItem(name)

		if (nodeEl.hasAttribute("command"))
			mItem.actionCommand = nodeEl.getAttribute("command")
		if (nodeEl.hasAttribute("enabled"))
			mItem.isEnabled = nodeEl.getAttribute("enabled").toBoolean()

		return mItem
	}

	/**
	 * Configure sub menu popup menu
	 *
	 * @param node Target node to configure
	 * @param parent Parent menu item
	 *
	 * @return [Menu] Menu result
	 */
	private fun configureMenu(node: Node, parent: Menu): Menu {
		val iElem = node as Element
		val iMenuName = iElem.getAttribute("name")
		val iMenu = Menu(iMenuName)

		for (elem in 0 until iElem.childNodes.length) {
			val current = iElem.childNodes.item(elem)

			if (current.nodeName == "menu") configureMenu(current, iMenu)
			if (current.nodeName == "item") iMenu.add(configureItem(current))
			if (current.nodeName == "separator") iMenu.addSeparator()
		}

		parent.add(iMenu)
		return iMenu
	}

}
