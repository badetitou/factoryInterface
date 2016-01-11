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
 * This class corresponding to the PopUp when you want to select a specific address for the deployment of
 * your server
 */
@SuppressWarnings("all")
public class ChangeDefaultIpAddressPopUp implements ActionListener {
  /**
   * @param e Useless
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    JTextField field1 = new JTextField("");
    GridLayout _gridLayout = new GridLayout(0, 1);
    JPanel panel = new JPanel(_gridLayout);
    JLabel _jLabel = new JLabel("Enter the new ip address\'s value (0 : all interface)");
    panel.add(_jLabel);
    panel.add(field1);
    int result = JOptionPane.showConfirmDialog(null, panel, "Default Server Ip Address Value", JOptionPane.OK_CANCEL_OPTION, 
      JOptionPane.PLAIN_MESSAGE);
    if ((result == JOptionPane.OK_OPTION)) {
      String _text = field1.getText();
      MyJMonitoringListTable.SERVER_DEFAULT_IP_ADDRESS = _text;
    }
  }
}
