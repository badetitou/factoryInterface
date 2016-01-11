package popUp;

import com.google.common.base.Objects;
import com.prosysopc.ua.client.UaClient;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.FactoryInterface;
import main.MyJMonitoringListTable;
import main.MyJTable;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import process.Node;
import tools.TranslateAttributeId;

/**
 * Pop-Up to change value of a process's attribute or keep it on the second table.
 * 
 * @autor Benoit Verhaeghe
 */
@SuppressWarnings("all")
public class SelectAttributePopUp implements ListSelectionListener {
  /**
   * The table where we come from.
   */
  @Accessors(AccessorType.NONE)
  private MyJTable myJTable;
  
  /**
   * @param table The table where we want select an attribute
   */
  public SelectAttributePopUp(final MyJTable table) {
    this.myJTable = table;
  }
  
  /**
   * @param listSelectionEvent Information about the selection to known which rows are selected
   */
  @Override
  public void valueChanged(final ListSelectionEvent listSelectionEvent) {
    try {
      boolean _valueIsAdjusting = listSelectionEvent.getValueIsAdjusting();
      if (_valueIsAdjusting) {
        return;
      }
      Object _source = listSelectionEvent.getSource();
      ListSelectionModel lsm = ((ListSelectionModel) _source);
      boolean _isSelectionEmpty = lsm.isSelectionEmpty();
      boolean _not = (!_isSelectionEmpty);
      if (_not) {
        int selectedRow = lsm.getMinSelectionIndex();
        JTextField field1 = new JTextField("");
        GridLayout _gridLayout = new GridLayout(0, 1);
        JPanel panel = new JPanel(_gridLayout);
        JTable _jTable = this.myJTable.getJTable();
        Object _valueAt = _jTable.getValueAt(selectedRow, 0);
        String _plus = (_valueAt + " : ");
        JLabel _jLabel = new JLabel(_plus);
        panel.add(_jLabel);
        panel.add(field1);
        String[] options = new String[3];
        options[0] = "OK";
        options[1] = "Cancel";
        options[2] = "To Monitoring List";
        Object _get = options[0];
        int result = JOptionPane.showOptionDialog(null, panel, "Write the new value", 
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, _get);
        if ((result == 0)) {
          String type = "";
          for (int i = 0; (i < this.myJTable.getJTable().getRowCount()); i++) {
            JTable _jTable_1 = this.myJTable.getJTable();
            Object _valueAt_1 = _jTable_1.getValueAt(i, 0);
            String _string = _valueAt_1.toString();
            boolean _equals = Objects.equal(_string, "DataType");
            if (_equals) {
              JTable _jTable_2 = this.myJTable.getJTable();
              Object _valueAt_2 = _jTable_2.getValueAt(i, 1);
              String _string_1 = _valueAt_2.toString();
              type = _string_1;
            }
          }
          boolean _equals = Objects.equal(type, "i=11");
          if (_equals) {
            UaClient _client = this.myJTable.getClient();
            Node _node = this.myJTable.getNode();
            NodeId _nodeId = _node.getNodeId();
            JTable _jTable_1 = this.myJTable.getJTable();
            Object _valueAt_1 = _jTable_1.getValueAt(selectedRow, 0);
            String _string = _valueAt_1.toString();
            UnsignedInteger _attributeNumber = TranslateAttributeId.getAttributeNumber(_string);
            String _text = field1.getText();
            double _parseDouble = Double.parseDouble(_text);
            _client.writeAttribute(_nodeId, _attributeNumber, Double.valueOf(_parseDouble));
          } else {
            boolean _equals_1 = Objects.equal(type, "i=6");
            if (_equals_1) {
              UaClient _client_1 = this.myJTable.getClient();
              Node _node_1 = this.myJTable.getNode();
              NodeId _nodeId_1 = _node_1.getNodeId();
              JTable _jTable_2 = this.myJTable.getJTable();
              Object _valueAt_2 = _jTable_2.getValueAt(selectedRow, 0);
              String _string_1 = _valueAt_2.toString();
              UnsignedInteger _attributeNumber_1 = TranslateAttributeId.getAttributeNumber(_string_1);
              String _text_1 = field1.getText();
              int _parseInt = Integer.parseInt(_text_1);
              _client_1.writeAttribute(_nodeId_1, _attributeNumber_1, Integer.valueOf(_parseInt));
            } else {
              UaClient _client_2 = this.myJTable.getClient();
              Node _node_2 = this.myJTable.getNode();
              NodeId _nodeId_2 = _node_2.getNodeId();
              JTable _jTable_3 = this.myJTable.getJTable();
              Object _valueAt_3 = _jTable_3.getValueAt(selectedRow, 0);
              String _string_2 = _valueAt_3.toString();
              UnsignedInteger _attributeNumber_2 = TranslateAttributeId.getAttributeNumber(_string_2);
              String _text_2 = field1.getText();
              _client_2.writeAttribute(_nodeId_2, _attributeNumber_2, _text_2);
            }
          }
        } else {
          if ((result == 2)) {
            FactoryInterface _factoryInterface = this.myJTable.getFactoryInterface();
            MyJMonitoringListTable _jKeepValueTable = _factoryInterface.getJKeepValueTable();
            UaClient _client_3 = this.myJTable.getClient();
            Node _node_3 = this.myJTable.getNode();
            NodeId _nodeId_3 = _node_3.getNodeId();
            JTable _jTable_4 = this.myJTable.getJTable();
            Object _valueAt_4 = _jTable_4.getValueAt(selectedRow, 0);
            String _string_3 = _valueAt_4.toString();
            UnsignedInteger _attributeNumber_3 = TranslateAttributeId.getAttributeNumber(_string_3);
            _jKeepValueTable.addSubscription(_client_3, _nodeId_3, _attributeNumber_3);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
