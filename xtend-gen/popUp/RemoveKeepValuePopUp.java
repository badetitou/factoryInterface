package popUp;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.MyJMonitoringListTable;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;

/**
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * Pop-Up to ask if user wants to remove an item from the monitoring list.
 */
@SuppressWarnings("all")
public class RemoveKeepValuePopUp implements ListSelectionListener {
  /**
   * The table contains the keep value
   */
  @Accessors(AccessorType.NONE)
  private MyJMonitoringListTable myJKeepValueTable;
  
  /**
   * @param table The Monitoring list we want to remove a value
   */
  public RemoveKeepValuePopUp(final MyJMonitoringListTable table) {
    this.myJKeepValueTable = table;
  }
  
  /**
   * Create and display the pop-up
   * @param listSelectionEvent Get information for the row selected
   */
  @Override
  public void valueChanged(final ListSelectionEvent listSelectionEvent) {
    boolean _valueIsAdjusting = listSelectionEvent.getValueIsAdjusting();
    if (_valueIsAdjusting) {
      return;
    }
    Object _source = listSelectionEvent.getSource();
    ListSelectionModel lsm = ((ListSelectionModel) _source);
    boolean _isSelectionEmpty = lsm.isSelectionEmpty();
    boolean _not = (!_isSelectionEmpty);
    if (_not) {
      final int selectedRow = lsm.getMinSelectionIndex();
      GridLayout _gridLayout = new GridLayout(0, 1);
      final JPanel panel = new JPanel(_gridLayout);
      JTable _jTable = this.myJKeepValueTable.getJTable();
      Object _valueAt = _jTable.getValueAt(selectedRow, 0);
      String _plus = (_valueAt + " : ");
      JLabel _jLabel = new JLabel(_plus);
      panel.add(_jLabel);
      final String[] options = new String[2];
      options[0] = "Delete";
      options[1] = "Keep";
      Object _get = options[0];
      final int result = JOptionPane.showOptionDialog(null, panel, "Do you want to remove this item", JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE, null, options, _get);
      if ((result == 0)) {
        this.myJKeepValueTable.removeSubscription(selectedRow);
      }
    }
  }
}
