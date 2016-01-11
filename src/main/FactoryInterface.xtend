package main

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.util.HashMap
import java.util.Map
import javax.swing.Box
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.KeyStroke
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeSelectionModel
import org.eclipse.xtend.lib.annotations.Accessors
import org.opcfoundation.ua.builtintypes.LocalizedText
import org.opcfoundation.ua.core.Identifiers
import popUp.ChangeDefaultClientAddressPopUp
import popUp.ChangeDefaultIpAddressPopUp
import popUp.ChangeDefaultPortPopUp
import popUp.ChangeUpdateTimePopUp
import popUp.SelectServerPopUp
import process.Node

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * The main frame
 */
class FactoryInterface extends JFrame {

	/**
	 * The tree with all node
	 */
	@Accessors(PUBLIC_GETTER)JTree jtree
	/**
	 * The menu bar
	 */
	@Accessors(NONE)JMenuBar menuBar
	/**
	 * The first table
	 */
	@Accessors(PUBLIC_GETTER)MyJTable jTableContainer
	/**
	 * The second table
	 */
	@Accessors(PUBLIC_GETTER)MyJMonitoringListTable jKeepValueTable
	
	/**
	 * Using it to display server's address
	 */
	public static JLabel SERVER_ADRESS = new JLabel(" - ")
	/**
	 * Using it to display server's connection status
	 */
	public static JLabel SERVER_CONNECTION = new JLabel("WAIT ... ")
	/**
	 * Using it to display all information about update or connection
	 */
	public static JLabel INFO = new JLabel("")

	/**
	 * Utilities for the client and update tree
	 */
	@Accessors(PUBLIC_GETTER,PUBLIC_SETTER)Browser browser
	/**
	 * A map between the node of the tree and the process
	 */
	@Accessors(PUBLIC_GETTER)Map<DefaultMutableTreeNode, Node> nodes = new HashMap<DefaultMutableTreeNode, Node>
	/**
	 * The first node
	 */
	@Accessors(PUBLIC_GETTER)DefaultMutableTreeNode processNode
	
	/**
	 * @param args useless
	 * 
	 * Just start the main frame
	 */
	def static void main(String[] args) {
		println("START")
		new FactoryInterface
	}
	
	/**
	 * Init all element and try to browse the tree
	 */
	new() {

		/* Init the Frame */
		this.title = "FactoryInterface - University of Emden - Benoît Verhaeghe"
		this.defaultCloseOperation = JFrame::EXIT_ON_CLOSE
		this.size = new Dimension(900, 600)
		this.locationRelativeTo = null
		this.contentPane.layout = new BorderLayout

		/* initialize the main Component */ 
		processNode = new DefaultMutableTreeNode(new LocalizedText("Root"))
		jtree = new JTree(processNode)
		jtree.selectionModel.selectionMode = TreeSelectionModel::SINGLE_TREE_SELECTION
		jtree.addTreeSelectionListener([ tse |
			var nodess = nodes.get(jtree.lastSelectedPathComponent as DefaultMutableTreeNode)
			jTableContainer.updateJTable(browser.client, nodess)
		])

		jKeepValueTable = new MyJMonitoringListTable
		jTableContainer = new MyJTable(this)
		menuBar = new JMenuBar
		initMenu

		/* Add scroll to the tree */
		val scrollPan = new JScrollPane(jtree)
		scrollPan.preferredSize = new Dimension(this.getSize.width / 3, this.getSize.height)

		/* Center Panel */
		var centerPan = new JPanel
		centerPan.layout = new GridLayout(2, 1)
		centerPan.add(jTableContainer.scrollPan)
		centerPan.add(jKeepValueTable.scrollPan)

		/* Put the component */
		this.contentPane.add(scrollPan, BorderLayout::LINE_START)
		this.contentPane.add(centerPan, BorderLayout::CENTER)
		this.contentPane.add(menuBar, BorderLayout::NORTH)

		this.visible = true

		browser = new Browser

		FactoryInterface::INFO.foreground = Color.BLUE
		FactoryInterface::INFO.text = "Update the tree wait"
		/* Creation of the Jtree */
		browser.browse(browser.client.addressSpace, Identifiers::RootFolder, processNode, nodes)
		FactoryInterface::INFO.text = ""
	}
	
	/**
	 * Initialization of the menu to separate the code
	 */
	def private initMenu() {
		/* Init Menu */
		var menu = new JMenu("A Menu")
		menu.setMnemonic(KeyEvent::VK_A)
		menuBar.add(menu)

		/* A group of JMenuItems */
		val menuItemServerSelection = new JMenuItem("Connection to server", KeyEvent::VK_T)
		menuItemServerSelection.accelerator = KeyStroke::getKeyStroke(KeyEvent::VK_A, ActionEvent::ALT_MASK)
		menuItemServerSelection.addActionListener(new SelectServerPopUp(this))

		val menuItemUpdateTime = new JMenuItem("Change refresh time", KeyEvent::VK_T)
		menuItemUpdateTime.accelerator = KeyStroke::getKeyStroke(KeyEvent::VK_T, ActionEvent::ALT_MASK)
		menuItemUpdateTime.addActionListener(new ChangeUpdateTimePopUp)

		val menuItemUpdateTree = new JMenuItem("Update the tree", KeyEvent::VK_T)
		menuItemUpdateTree.accelerator = KeyStroke::getKeyStroke(KeyEvent::VK_R, ActionEvent::ALT_MASK)
		menuItemUpdateTree.addActionListener([e |
			new Thread([|
			processNode.removeAllChildren	
			INFO.foreground = Color::BLUE
			INFO.text = "UPDATE TREE ... "		
			browser.browse(browser.client.addressSpace, Identifiers::RootFolder, processNode, nodes)
			jtree.updateUI
			INFO.text = ""]).start])
			
		val menuItemChangeDefaultServerPort = new JMenuItem("Change default server's port", KeyEvent::VK_T)
		menuItemChangeDefaultServerPort.accelerator = KeyStroke::getKeyStroke(KeyEvent::VK_P, ActionEvent::ALT_MASK)
		menuItemChangeDefaultServerPort.addActionListener(new ChangeDefaultPortPopUp)
		
		val menuItemChangeDefaultServerIpAddress = new JMenuItem("Change default server's ip's address", KeyEvent::VK_T)
		menuItemChangeDefaultServerIpAddress.accelerator = KeyStroke::getKeyStroke(KeyEvent::VK_S, ActionEvent::ALT_MASK)
		menuItemChangeDefaultServerIpAddress.addActionListener(new ChangeDefaultIpAddressPopUp)

		val menuItemChangeDefaultClientIpAddress = new JMenuItem("Change default client's ip's address", KeyEvent::VK_T)
		menuItemChangeDefaultClientIpAddress.accelerator = KeyStroke::getKeyStroke(KeyEvent::VK_C, ActionEvent::ALT_MASK)
		menuItemChangeDefaultClientIpAddress.addActionListener(new ChangeDefaultClientAddressPopUp)
		
		/* Add the menu to the left side */
		menu.add(menuItemServerSelection)
		menu.add(menuItemUpdateTime)
		menu.add(menuItemUpdateTree)
		menu.add(menuItemChangeDefaultServerPort)
		menu.add(menuItemChangeDefaultServerIpAddress)
		menu.add(menuItemChangeDefaultClientIpAddress)

		/* Add information Label to right side */
		menuBar.add(Box::createGlue)
		menuBar.add(SERVER_ADRESS)
		menuBar.add(INFO)
		menuBar.add(SERVER_CONNECTION)
		SERVER_CONNECTION.foreground = Color::RED
	}

}