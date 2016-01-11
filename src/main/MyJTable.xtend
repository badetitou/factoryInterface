package main

import com.prosysopc.ua.client.UaClient
import java.awt.BorderLayout
import java.util.Vector
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel
import org.eclipse.xtend.lib.annotations.Accessors
import popUp.SelectAttributePopUp
import process.Node
import tools.TranslateAttributeId

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * The details table. 
 */
class MyJTable {

	/**
	 * Time for the auto-update
	 */
	public static Integer UPDATE_TIME = 500

	/**
	 * The client corresponding to the actual process
	 */
	@Accessors(PUBLIC_GETTER) UaClient client
	/**
	 * The process actually display on the table
	 */
	@Accessors(PUBLIC_GETTER) Node node
	/**
	 * An access to the main part of the program to ask somestaff
	 */
	@Accessors(PUBLIC_GETTER) FactoryInterface factoryInterface
	/**
	 * The real JTable
	 */
	@Accessors(PUBLIC_GETTER) JTable jTable
	/**
	 * The panel for display
	 */
	@Accessors(PUBLIC_GETTER) JPanel scrollPan

	/**
	 * Update values of the table every UPDATE_TIME
	 */
	def autoUpdate() {
		new Thread(
			|
				while (true) {
					try {
						Thread::sleep(UPDATE_TIME)
						if (client != null && node != null) {
							updateTable(node, client)
						}
					} catch (Exception e) {
					}
				}
			).start
		}

		/**
		 * @param client The client connects to the server
		 * @param process The node concerning for the Table
		 * 
		 * Do one update of the table
		 */
		def updateJTable(UaClient client, Node node) {
			this.client = client
			this.node = node
			updateTable(node, client)

		}

		/**
		 * @param fi The main component to an easier interaction between this and other component
		 * 
		 * Create the JTable and all other features. And start the auto-update
		 */
		new(FactoryInterface fi) {
			this.factoryInterface = fi
			scrollPan = new JPanel
			var model = new DefaultTableModel
			model.addColumn("Attribute")
			model.addColumn("Value")
			jTable = new JTable(model)
			scrollPan.layout = new BorderLayout
			scrollPan.add(new JLabel("Details List"), BorderLayout::NORTH)
			scrollPan.add(new JScrollPane(jTable), BorderLayout::CENTER)

			var listSelectionModel = jTable.selectionModel
			jTable.selectionMode = ListSelectionModel::SINGLE_SELECTION
			listSelectionModel.addListSelectionListener(new SelectAttributePopUp(this))
			autoUpdate
		}

		/**
		 * @param node The node displayed by this table
		 * @param client The client to ask information from the server
		 * 
		 * Do the update 
		 */
		def private updateTable(Node node, UaClient client) {
			var model = new DefaultTableModel
			model.addColumn("Attributes")
			model.addColumn("Values")
			if (client.connected && node != null) {
				/* Update information of the Node */
				node.updateAttributes(client)
				/* Creation of the new model */
				for (attribute : node.attributes) {
					var vector = new Vector
					vector.add(TranslateAttributeId::getName(attribute.typeOPCAttributes))
					vector.add(attribute.value.toString)
					model.addRow(vector)
				}
			}
			/* Update the table with the new model */
			jTable.model = model
		}
	}