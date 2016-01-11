package popUp

import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import main.MyJMonitoringListTable

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * This class is the PopUp whose appears to change the port use by this software to create a server
 */
class ChangeDefaultPortPopUp implements ActionListener {

	/**
	 * @param e Useless
	 * 
	 * Create and display the PopUp
	 */
	override actionPerformed(ActionEvent e) {
		/* Creation of the panel */
		var field1 = new JTextField("")
		var panel = new JPanel(new GridLayout(0, 1))
		panel.add(new JLabel("Enter the new port's value (0=automatic choice)"))
		panel.add(field1);
		var result = JOptionPane::showConfirmDialog(null, panel, "Default Server Port Value",
			JOptionPane::OK_CANCEL_OPTION, JOptionPane::PLAIN_MESSAGE)
		/* if user agrees */
		if (result == JOptionPane::OK_OPTION) {
			try {
				/* Test user entry is a number */
				MyJMonitoringListTable::SERVER_DEFAULT_PORT = Integer::parseInt(field1.text)
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Number Format Error", "Error", JOptionPane.ERROR_MESSAGE)
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE)
			}
		}
	}

}