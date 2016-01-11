package popUp

import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import main.MyJTable

import static main.MyJTable.*

/**
 * @autor Benoît Verhaeghe - benoît.verhaeghe59@gmail.com
 * 
 * Pop Up to ask to the user if we want an other refresh time for the keep value and the actual value
 */
class ChangeUpdateTimePopUp implements ActionListener {

	/**
	 * @param e Useless
	 * Create and display the pop up
	 */
	override actionPerformed(ActionEvent e) {
		var field1 = new JTextField("")
		var panel = new JPanel(new GridLayout(0, 1))
		panel.add(new JLabel("Enter the new Value in millisecond (must be Integer)"))
		panel.add(field1)
		var result = JOptionPane::showConfirmDialog(null, panel, "Write the new value", JOptionPane::OK_CANCEL_OPTION,
			JOptionPane::PLAIN_MESSAGE)
		/* If user agrees */
		if (result == JOptionPane.OK_OPTION) {
			MyJTable::UPDATE_TIME = Integer::parseInt(field1.text)
		}
	}
}