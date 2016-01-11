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
 * This class corresponding to the PopUp when you want to select a specific address for the deployment of 
 * your server
 */
class ChangeDefaultIpAddressPopUp implements ActionListener{
	
	/**
	 * @param e Useless
	 */
	override actionPerformed(ActionEvent e) {
		/* Creation of the PopUp */
		var field1 = new JTextField("")
		var panel = new JPanel(new GridLayout(0, 1))
		panel.add(new JLabel("Enter the new ip address's value (0 : all interface)"))
		panel.add(field1);
		var result = JOptionPane::showConfirmDialog(null, panel, "Default Server Ip Address Value", JOptionPane::OK_CANCEL_OPTION,
			JOptionPane::PLAIN_MESSAGE)
		/* If user agrees */
		if (result == JOptionPane::OK_OPTION) {
			MyJMonitoringListTable::SERVER_DEFAULT_IP_ADDRESS = field1.text
		}
	}
}