package popUp

import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import main.MyJTable
import org.eclipse.xtend.lib.annotations.Accessors
import tools.TranslateAttributeId

/**
 * Pop-Up to change value of a process's attribute or keep it on the second table.
 * 
 * @autor Benoit Verhaeghe
 */
class SelectAttributePopUp implements ListSelectionListener {

	/**
	 * The table where we come from. 
	 */
	@Accessors(NONE) MyJTable myJTable

	/**
	 * @param table The table where we want select an attribute
	 */
	new(MyJTable table) {
		myJTable = table
	}

	/**
	 * @param listSelectionEvent Information about the selection to known which rows are selected
	 */
	override valueChanged(ListSelectionEvent listSelectionEvent) {
		/* Some test */
		if (listSelectionEvent.getValueIsAdjusting())
			return
		var lsm = (listSelectionEvent.source as ListSelectionModel)
		/* Avoid empty error */
		if (!lsm.isSelectionEmpty) {
			/*Just one row can be selected, so the number of the line selected */
			var selectedRow = lsm.minSelectionIndex
			
			/* Creation of the panel for the PopUp */
			var field1 = new JTextField("")
			var panel = new JPanel(new GridLayout(0, 1))
			panel.add(new JLabel(myJTable.getJTable.getValueAt(selectedRow, 0) + " : "))
			panel.add(field1)
			
			/* Number and name of options for the PopUp */
			var options = newArrayOfSize(3)
			options.set(0, "OK")
			options.set(1, "Cancel")
			options.set(2, "To Monitoring List")
			/* Create and display the PopUp */
			var result = JOptionPane::showOptionDialog(null, panel, "Write the new value",
				JOptionPane::OK_CANCEL_OPTION, JOptionPane::PLAIN_MESSAGE, null, options, options.get(0))
			/* If user agree */
			if (result == 0) {
				var type = ""
				/* Find type of data before sending this to a better conversion */
				for (var i = 0; i < myJTable.getJTable.rowCount; i++) {
					if (myJTable.getJTable.getValueAt(i, 0).toString == "DataType") {
						type = myJTable.getJTable.getValueAt(i, 1).toString
					}
				}

				/* String corresponding to a Double format */
				if (type == "i=11") {
					myJTable.client.writeAttribute(myJTable.node.nodeId,
						TranslateAttributeId::getAttributeNumber(myJTable.getJTable.getValueAt(selectedRow, 0).toString),
						Double::parseDouble(field1.text))
				} 
				/* String corresponding to an Integer format */
				else if (type == "i=6") {
					myJTable.client.writeAttribute(myJTable.node.nodeId,
						TranslateAttributeId::getAttributeNumber(myJTable.getJTable.getValueAt(selectedRow, 0).toString),
						Integer::parseInt(field1.text))
				} 
				/* Else we used a simple String format (maybe not all type are supported) */
				else {
					myJTable.client.writeAttribute(myJTable.node.nodeId,
						TranslateAttributeId::getAttributeNumber(myJTable.getJTable.getValueAt(selectedRow, 0).toString), field1.text)
				}
			}
			/* 
			 * If the user selects to put the value to the monitoring list
			 * We add a new subscription
			 */
			else if (result == 2) {
				myJTable.factoryInterface.JKeepValueTable.addSubscription(myJTable.client, myJTable.node.nodeId,
						TranslateAttributeId::getAttributeNumber(myJTable.getJTable.getValueAt(selectedRow, 0).toString))
			} 
		}
	}
}