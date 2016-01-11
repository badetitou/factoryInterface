package popUp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import main.MyJTable;

/**
 * @autor Benoît Verhaeghe - benoît.verhaeghe59@gmail.com
 * 
 * Pop Up to ask to the user if we want an other refresh time for the keep value and the actual value
 */
@SuppressWarnings("all")
public class ChangeUpdateTimePopUp implements ActionListener {
  /**
   * @param e Useless
   * Create and display the pop up
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    JTextField field1 = new JTextField("");
    GridLayout _gridLayout = new GridLayout(0, 1);
    JPanel panel = new JPanel(_gridLayout);
    JLabel _jLabel = new JLabel("Enter the new Value in millisecond (must be Integer)");
    panel.add(_jLabel);
    panel.add(field1);
    int result = JOptionPane.showConfirmDialog(null, panel, "Write the new value", JOptionPane.OK_CANCEL_OPTION, 
      JOptionPane.PLAIN_MESSAGE);
    if ((result == JOptionPane.OK_OPTION)) {
      String _text = field1.getText();
      int _parseInt = Integer.parseInt(_text);
      MyJTable.UPDATE_TIME = Integer.valueOf(_parseInt);
    }
  }
}
