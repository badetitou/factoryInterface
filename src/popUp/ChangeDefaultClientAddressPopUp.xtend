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
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * This class corresponding to the PopUp to select the client's IP Address to send JSON data from
 * the monitoring list
 */
class ChangeDefaultClientAddressPopUp implements ActionListener{
	
	/**
	 * @param e Useless
	 * 
	 * Create and display the PopUp
	 */
	override actionPerformed(ActionEvent e) {
		var field1 = new JTextField("")
		var panel = new JPanel(new GridLayout(0, 1))
		panel.add(new JLabel("Enter the new ip address's value"))
		panel.add(field1);
		var result = JOptionPane::showConfirmDialog(null, panel, "Default Client Ip Address Value", JOptionPane::OK_CANCEL_OPTION,
			JOptionPane::PLAIN_MESSAGE)
		/* If user agrees */
		if (result == JOptionPane::OK_OPTION) {
			MyJMonitoringListTable::CLIENT_DEFAULT_ADDRESS = field1.text
		}
	}
}