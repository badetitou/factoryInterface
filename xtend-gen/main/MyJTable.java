package main;

import com.google.common.base.Objects;
import com.prosysopc.ua.client.UaClient;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import main.FactoryInterface;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import popUp.SelectAttributePopUp;
import process.Attribute;
import process.Node;
import tools.TranslateAttributeId;

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * The details table.
 */
@SuppressWarnings("all")
public class MyJTable {
  /**
   * Time for the auto-update
   */
  public static Integer UPDATE_TIME = Integer.valueOf(500);
  
  /**
   * The client corresponding to the actual process
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private UaClient client;
  
  /**
   * The process actually display on the table
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Node node;
  
  /**
   * An access to the main part of the program to ask somestaff
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private FactoryInterface factoryInterface;
  
  /**
   * The real JTable
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private JTable jTable;
  
  /**
   * The panel for display
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private JPanel scrollPan;
  
  /**
   * Update values of the table every UPDATE_TIME
   */
  public void autoUpdate() {
    final Runnable _function = new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep((MyJTable.UPDATE_TIME).intValue());
            boolean _and = false;
            boolean _notEquals = (!Objects.equal(MyJTable.this.client, null));
            if (!_notEquals) {
              _and = false;
            } else {
              boolean _notEquals_1 = (!Objects.equal(MyJTable.this.node, null));
              _and = _notEquals_1;
            }
            if (_and) {
              MyJTable.this.updateTable(MyJTable.this.node, MyJTable.this.client);
            }
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception e = (Exception)_t;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
    };
    Thread _thread = new Thread(_function);
    _thread.start();
  }
  
  /**
   * @param client The client connects to the server
   * @param process The node concerning for the Table
   * 
   * Do one update of the table
   */
  public void updateJTable(final UaClient client, final Node node) {
    this.client = client;
    this.node = node;
    this.updateTable(node, client);
  }
  
  /**
   * @param fi The main component to an easier interaction between this and other component
   * 
   * Create the JTable and all other features. And start the auto-update
   */
  public MyJTable(final FactoryInterface fi) {
    this.factoryInterface = fi;
    JPanel _jPanel = new JPanel();
    this.scrollPan = _jPanel;
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Attribute");
    model.addColumn("Value");
    JTable _jTable = new JTable(model);
    this.jTable = _jTable;
    BorderLayout _borderLayout = new BorderLayout();
    this.scrollPan.setLayout(_borderLayout);
    JLabel _jLabel = new JLabel("Details List");
    this.scrollPan.add(_jLabel, BorderLayout.NORTH);
    JScrollPane _jScrollPane = new JScrollPane(this.jTable);
    this.scrollPan.add(_jScrollPane, BorderLayout.CENTER);
    ListSelectionModel listSelectionModel = this.jTable.getSelectionModel();
    this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    SelectAttributePopUp _selectAttributePopUp = new SelectAttributePopUp(this);
    listSelectionModel.addListSelectionListener(_selectAttributePopUp);
    this.autoUpdate();
  }
  
  /**
   * @param node The node displayed by this table
   * @param client The client to ask information from the server
   * 
   * Do the update
   */
  private void updateTable(final Node node, final UaClient client) {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Attributes");
    model.addColumn("Values");
    boolean _and = false;
    boolean _isConnected = client.isConnected();
    if (!_isConnected) {
      _and = false;
    } else {
      boolean _notEquals = (!Objects.equal(node, null));
      _and = _notEquals;
    }
    if (_and) {
      node.updateAttributes(client);
      ArrayList<Attribute> _attributes = node.getAttributes();
      for (final Attribute attribute : _attributes) {
        {
          Vector<String> vector = new Vector<String>();
          UnsignedInteger _typeOPCAttributes = attribute.getTypeOPCAttributes();
          String _name = TranslateAttributeId.getName(_typeOPCAttributes);
          vector.add(_name);
          Object _value = attribute.getValue();
          String _string = _value.toString();
          vector.add(_string);
          model.addRow(vector);
        }
      }
    }
    this.jTable.setModel(model);
  }
  
  @Pure
  public UaClient getClient() {
    return this.client;
  }
  
  @Pure
  public Node getNode() {
    return this.node;
  }
  
  @Pure
  public FactoryInterface getFactoryInterface() {
    return this.factoryInterface;
  }
  
  @Pure
  public JTable getJTable() {
    return this.jTable;
  }
  
  @Pure
  public JPanel getScrollPan() {
    return this.scrollPan;
  }
}
