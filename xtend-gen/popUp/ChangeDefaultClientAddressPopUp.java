package popUp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import main.MyJMonitoringListTable;

/**
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * This class corresponding to the PopUp to select the client's IP Address to send JSON data from
 * the monitoring list
 */
@SuppressWarnings("all")
public class ChangeDefaultClientAddressPopUp implements ActionListener {
  /**
   * @param e Useless
   * 
   * Create and display the PopUp
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    JTextField field1 = new JTextField("");
    GridLayout _gridLayout = new GridLayout(0, 1);
    JPanel panel = new JPanel(_gridLayout);
    JLabel _jLabel = new JLabel("Enter the new ip address\'s value");
    panel.add(_jLabel);
    panel.add(field1);
    int result = JOptionPane.showConfirmDialog(null, panel, "Default Client Ip Address Value", JOptionPane.OK_CANCEL_OPTION, 
      JOptionPane.PLAIN_MESSAGE);
    if ((result == JOptionPane.OK_OPTION)) {
      String _text = field1.getText();
      MyJMonitoringListTable.CLIENT_DEFAULT_ADDRESS = _text;
    }
  }
}
