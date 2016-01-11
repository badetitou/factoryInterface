package popUp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import main.MyJMonitoringListTable;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * This class is the PopUp whose appears to change the port use by this software to create a server
 */
@SuppressWarnings("all")
public class ChangeDefaultPortPopUp implements ActionListener {
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
    JLabel _jLabel = new JLabel("Enter the new port\'s value (0=automatic choice)");
    panel.add(_jLabel);
    panel.add(field1);
    int result = JOptionPane.showConfirmDialog(null, panel, "Default Server Port Value", 
      JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if ((result == JOptionPane.OK_OPTION)) {
      try {
        String _text = field1.getText();
        int _parseInt = Integer.parseInt(_text);
        MyJMonitoringListTable.SERVER_DEFAULT_PORT = Integer.valueOf(_parseInt);
      } catch (final Throwable _t) {
        if (_t instanceof NumberFormatException) {
          final NumberFormatException ex = (NumberFormatException)_t;
          JOptionPane.showMessageDialog(null, "Number Format Error", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (_t instanceof Exception) {
          final Exception ex_1 = (Exception)_t;
          JOptionPane.showMessageDialog(null, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
}
