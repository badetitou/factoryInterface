package popUp;

import com.prosysopc.ua.client.AddressSpace;
import com.prosysopc.ua.client.UaClient;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import main.Browser;
import main.FactoryInterface;
import main.MyJMonitoringListTable;
import main.MyJTable;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.SecurityMode;
import process.Node;
import tools.TextPrompt;

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * The pop-up to select the server we want to connect
 */
@SuppressWarnings("all")
public class SelectServerPopUp implements ActionListener {
  /**
   * Access to the main part of the program.
   */
  @Accessors(AccessorType.NONE)
  private FactoryInterface factoryInterface;
  
  /**
   * @param fi The factory interface to interact with the program
   */
  public SelectServerPopUp(final FactoryInterface fi) {
    this.factoryInterface = fi;
  }
  
  /**
   * Display a pop up with just one text field.
   * If user press OK. Update all the tree with the new Address
   * @param e Totally useless
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    GridLayout _gridLayout = new GridLayout(10, 2);
    final JPanel panel = new JPanel(_gridLayout);
    final JTextField field1 = new JTextField("");
    final JCheckBox check = new JCheckBox("Force use this address");
    final JTextField accountUsername = new JTextField();
    final TextPrompt promptUsername = new TextPrompt("username", accountUsername);
    promptUsername.changeAlpha(0.5f);
    accountUsername.setEditable(false);
    final JTextField accountPassword = new JTextField();
    final TextPrompt promptPassword = new TextPrompt("password", accountPassword);
    promptPassword.changeAlpha(0.5f);
    accountPassword.setEditable(false);
    final JRadioButton anonymous = new JRadioButton("Anonymous");
    anonymous.setSelected(true);
    String _text = anonymous.getText();
    anonymous.setActionCommand(_text);
    final ActionListener _function = new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent useless) {
        accountUsername.setEditable(false);
        accountPassword.setEditable(false);
      }
    };
    anonymous.addActionListener(_function);
    final JRadioButton account = new JRadioButton("Username & Password");
    String _text_1 = account.getText();
    account.setActionCommand(_text_1);
    final ActionListener _function_1 = new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent useless) {
        accountUsername.setEditable(true);
        accountPassword.setEditable(true);
      }
    };
    account.addActionListener(_function_1);
    final JRadioButton certificate = new JRadioButton("My Specific Certificate In progress");
    String _text_2 = certificate.getText();
    certificate.setActionCommand(_text_2);
    final ActionListener _function_2 = new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent useless) {
        accountUsername.setEditable(false);
        accountPassword.setEditable(false);
      }
    };
    certificate.addActionListener(_function_2);
    final ButtonGroup connectionTypeGroup = new ButtonGroup();
    connectionTypeGroup.add(anonymous);
    connectionTypeGroup.add(account);
    connectionTypeGroup.add(certificate);
    final JRadioButton none = new JRadioButton("None");
    String _text_3 = none.getText();
    none.setActionCommand(_text_3);
    none.setSelected(true);
    final JRadioButton basic128 = new JRadioButton("Basic 128 Sign");
    String _text_4 = none.getText();
    none.setActionCommand(_text_4);
    final JRadioButton basic128Encrypt = new JRadioButton("Basic 128 Sign and Encrypt");
    String _text_5 = none.getText();
    none.setActionCommand(_text_5);
    final JRadioButton basic256 = new JRadioButton("Basic 256 Sign");
    String _text_6 = none.getText();
    none.setActionCommand(_text_6);
    final JRadioButton basic256Encrypt = new JRadioButton("Basic 256 Sign and Encrypt");
    String _text_7 = none.getText();
    none.setActionCommand(_text_7);
    final ButtonGroup securityGroup = new ButtonGroup();
    securityGroup.add(none);
    securityGroup.add(basic128);
    securityGroup.add(basic128Encrypt);
    securityGroup.add(basic256);
    securityGroup.add(basic256Encrypt);
    JLabel _jLabel = new JLabel("Enter the address");
    panel.add(_jLabel);
    panel.add(field1);
    panel.add(check);
    JLabel _jLabel_1 = new JLabel();
    panel.add(_jLabel_1);
    panel.add(anonymous);
    JLabel _jLabel_2 = new JLabel();
    panel.add(_jLabel_2);
    panel.add(account);
    panel.add(accountUsername);
    JLabel _jLabel_3 = new JLabel();
    panel.add(_jLabel_3);
    panel.add(accountPassword);
    panel.add(certificate);
    JLabel _jLabel_4 = new JLabel();
    panel.add(_jLabel_4);
    JLabel _jLabel_5 = new JLabel("Security Mode");
    panel.add(_jLabel_5);
    panel.add(none);
    panel.add(basic128);
    panel.add(basic256);
    panel.add(basic128Encrypt);
    panel.add(basic256Encrypt);
    int result = JOptionPane.showConfirmDialog(null, panel, "Write the address", JOptionPane.OK_CANCEL_OPTION, 
      JOptionPane.PLAIN_MESSAGE);
    if ((result == JOptionPane.OK_OPTION)) {
      MyJMonitoringListTable _jKeepValueTable = this.factoryInterface.getJKeepValueTable();
      _jKeepValueTable.resetSubscription();
      MyJTable _jTableContainer = this.factoryInterface.getJTableContainer();
      JTable _jTable = _jTableContainer.getJTable();
      _jTable.removeAll();
      DefaultMutableTreeNode _processNode = this.factoryInterface.getProcessNode();
      _processNode.removeAllChildren();
      JTree _jtree = this.factoryInterface.getJtree();
      int _rowCount = _jtree.getRowCount();
      ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_rowCount, 0, false);
      for (final Integer i : _greaterThanDoubleDot) {
        JTree _jtree_1 = this.factoryInterface.getJtree();
        _jtree_1.collapseRow((i).intValue());
      }
      Browser _browser = this.factoryInterface.getBrowser();
      _browser.disconnection();
      boolean _isSelected = check.isSelected();
      if (_isSelected) {
        Browser.FORCE_URL = true;
      } else {
        Browser.FORCE_URL = false;
      }
      boolean _isSelected_1 = account.isSelected();
      if (_isSelected_1) {
        String _text_8 = accountUsername.getText();
        Browser.USERNAME = _text_8;
        String _text_9 = accountPassword.getText();
        Browser.PASSWORD = _text_9;
      } else {
        Browser.USERNAME = "";
        Browser.PASSWORD = "";
      }
      boolean _isSelected_2 = none.isSelected();
      if (_isSelected_2) {
        Browser.SECURITY_MODE = SecurityMode.NONE;
      } else {
        boolean _isSelected_3 = basic128.isSelected();
        if (_isSelected_3) {
          Browser.SECURITY_MODE = SecurityMode.BASIC128RSA15_SIGN;
        } else {
          boolean _isSelected_4 = basic128Encrypt.isSelected();
          if (_isSelected_4) {
            Browser.SECURITY_MODE = SecurityMode.BASIC128RSA15_SIGN_ENCRYPT;
          } else {
            boolean _isSelected_5 = basic256.isSelected();
            if (_isSelected_5) {
              Browser.SECURITY_MODE = SecurityMode.BASIC256_SIGN;
            } else {
              boolean _isSelected_6 = basic256Encrypt.isSelected();
              if (_isSelected_6) {
                Browser.SECURITY_MODE = SecurityMode.BASIC256_SIGN_ENCRYPT;
              }
            }
          }
        }
      }
      String _text_10 = field1.getText();
      Browser _browser_1 = new Browser(_text_10);
      this.factoryInterface.setBrowser(_browser_1);
      Browser _browser_2 = this.factoryInterface.getBrowser();
      Browser _browser_3 = this.factoryInterface.getBrowser();
      UaClient _client = _browser_3.getClient();
      AddressSpace _addressSpace = _client.getAddressSpace();
      DefaultMutableTreeNode _processNode_1 = this.factoryInterface.getProcessNode();
      Map<DefaultMutableTreeNode, Node> _nodes = this.factoryInterface.getNodes();
      _browser_2.browse(_addressSpace, Identifiers.RootFolder, _processNode_1, _nodes);
    }
  }
}
