package popUp

import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import main.MyJMonitoringListTable
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * Pop-Up to ask if user wants to remove an item from the monitoring list.
 */
class RemoveKeepValuePopUp implements ListSelectionListener {

	/**
	 * The table contains the keep value
	 */
	@Accessors(NONE)MyJMonitoringListTable myJKeepValueTable

	/**
	 * @param table The Monitoring list we want to remove a value
	 */
	new(MyJMonitoringListTable table) {
		myJKeepValueTable = table
	}

	/**
	 * Create and display the pop-up
	 * @param listSelectionEvent Get information for the row selected
	 */
	override valueChanged(ListSelectionEvent listSelectionEvent) {
		/* Some test */
		if (listSelectionEvent.getValueIsAdjusting())
			return;
		var lsm = (listSelectionEvent.source as ListSelectionModel)
		/* Check no error with empty selection  */
		if (!lsm.isSelectionEmpty) {
			/* One element only can be selected, so the number of the row selected  */
			val selectedRow = lsm.minSelectionIndex
			
			/* Creation of the panel will displayed */
			val panel = new JPanel(new GridLayout(0, 1))
			panel.add(new JLabel(myJKeepValueTable.JTable.getValueAt(selectedRow, 0) + " : "))
			
			/* The different user option */
			val options = newArrayOfSize(2)
			options.set(0, "Delete")
			options.set(1, "Keep")
			
			/* Display the PopUp */
			val result = JOptionPane::showOptionDialog(null, panel, "Do you want to remove this item", JOptionPane::OK_CANCEL_OPTION,
				JOptionPane::PLAIN_MESSAGE, null, options, options.get(0))
			/* If user agrees */
			if (result == 0) {
				myJKeepValueTable.removeSubscription(selectedRow)
			}
		}
	}
	
}