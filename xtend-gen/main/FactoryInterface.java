package main;

import com.prosysopc.ua.client.AddressSpace;
import com.prosysopc.ua.client.UaClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import main.Browser;
import main.MyJMonitoringListTable;
import main.MyJTable;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.Identifiers;
import popUp.ChangeDefaultClientAddressPopUp;
import popUp.ChangeDefaultIpAddressPopUp;
import popUp.ChangeDefaultPortPopUp;
import popUp.ChangeUpdateTimePopUp;
import popUp.SelectServerPopUp;
import process.Node;

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * The main frame
 */
@SuppressWarnings("all")
public class FactoryInterface extends JFrame {
  /**
   * The tree with all node
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private JTree jtree;
  
  /**
   * The menu bar
   */
  @Accessors(AccessorType.NONE)
  private JMenuBar menuBar;
  
  /**
   * The first table
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private MyJTable jTableContainer;
  
  /**
   * The second table
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private MyJMonitoringListTable jKeepValueTable;
  
  /**
   * Using it to display server's address
   */
  public static JLabel SERVER_ADRESS = new JLabel(" - ");
  
  /**
   * Using it to display server's connection status
   */
  public static JLabel SERVER_CONNECTION = new JLabel("WAIT ... ");
  
  /**
   * Using it to display all information about update or connection
   */
  public static JLabel INFO = new JLabel("");
  
  /**
   * Utilities for the client and update tree
   */
  @Accessors({ AccessorType.PUBLIC_GETTER, AccessorType.PUBLIC_SETTER })
  private Browser browser;
  
  /**
   * A map between the node of the tree and the process
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Map<DefaultMutableTreeNode, Node> nodes = new HashMap<DefaultMutableTreeNode, Node>();
  
  /**
   * The first node
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private DefaultMutableTreeNode processNode;
  
  /**
   * @param args useless
   * 
   * Just start the main frame
   */
  public static void main(final String[] args) {
    InputOutput.<String>println("START");
    new FactoryInterface();
  }
  
  /**
   * Init all element and try to browse the tree
   */
  public FactoryInterface() {
    this.setTitle("FactoryInterface - University of Emden - Benoît Verhaeghe");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension _dimension = new Dimension(900, 600);
    this.setSize(_dimension);
    this.setLocationRelativeTo(null);
    Container _contentPane = this.getContentPane();
    BorderLayout _borderLayout = new BorderLayout();
    _contentPane.setLayout(_borderLayout);
    LocalizedText _localizedText = new LocalizedText("Root");
    DefaultMutableTreeNode _defaultMutableTreeNode = new DefaultMutableTreeNode(_localizedText);
    this.processNode = _defaultMutableTreeNode;
    JTree _jTree = new JTree(this.processNode);
    this.jtree = _jTree;
    TreeSelectionModel _selectionModel = this.jtree.getSelectionModel();
    _selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    final TreeSelectionListener _function = new TreeSelectionListener() {
      @Override
      public void valueChanged(final TreeSelectionEvent tse) {
        Object _lastSelectedPathComponent = FactoryInterface.this.jtree.getLastSelectedPathComponent();
        Node nodess = FactoryInterface.this.nodes.get(((DefaultMutableTreeNode) _lastSelectedPathComponent));
        UaClient _client = FactoryInterface.this.browser.getClient();
        FactoryInterface.this.jTableContainer.updateJTable(_client, nodess);
      }
    };
    this.jtree.addTreeSelectionListener(_function);
    MyJMonitoringListTable _myJMonitoringListTable = new MyJMonitoringListTable();
    this.jKeepValueTable = _myJMonitoringListTable;
    MyJTable _myJTable = new MyJTable(this);
    this.jTableContainer = _myJTable;
    JMenuBar _jMenuBar = new JMenuBar();
    this.menuBar = _jMenuBar;
    this.initMenu();
    final JScrollPane scrollPan = new JScrollPane(this.jtree);
    Dimension _size = this.getSize();
    Dimension _dimension_1 = new Dimension((this.getSize().width / 3), _size.height);
    scrollPan.setPreferredSize(_dimension_1);
    JPanel centerPan = new JPanel();
    GridLayout _gridLayout = new GridLayout(2, 1);
    centerPan.setLayout(_gridLayout);
    JPanel _scrollPan = this.jTableContainer.getScrollPan();
    centerPan.add(_scrollPan);
    JPanel _scrollPan_1 = this.jKeepValueTable.getScrollPan();
    centerPan.add(_scrollPan_1);
    Container _contentPane_1 = this.getContentPane();
    _contentPane_1.add(scrollPan, BorderLayout.LINE_START);
    Container _contentPane_2 = this.getContentPane();
    _contentPane_2.add(centerPan, BorderLayout.CENTER);
    Container _contentPane_3 = this.getContentPane();
    _contentPane_3.add(this.menuBar, BorderLayout.NORTH);
    this.setVisible(true);
    Browser _browser = new Browser();
    this.browser = _browser;
    FactoryInterface.INFO.setForeground(Color.BLUE);
    FactoryInterface.INFO.setText("Update the tree wait");
    UaClient _client = this.browser.getClient();
    AddressSpace _addressSpace = _client.getAddressSpace();
    this.browser.browse(_addressSpace, Identifiers.RootFolder, this.processNode, this.nodes);
    FactoryInterface.INFO.setText("");
  }
  
  /**
   * Initialization of the menu to separate the code
   */
  private void initMenu() {
    JMenu menu = new JMenu("A Menu");
    menu.setMnemonic(KeyEvent.VK_A);
    this.menuBar.add(menu);
    final JMenuItem menuItemServerSelection = new JMenuItem("Connection to server", KeyEvent.VK_T);
    KeyStroke _keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK);
    menuItemServerSelection.setAccelerator(_keyStroke);
    SelectServerPopUp _selectServerPopUp = new SelectServerPopUp(this);
    menuItemServerSelection.addActionListener(_selectServerPopUp);
    final JMenuItem menuItemUpdateTime = new JMenuItem("Change refresh time", KeyEvent.VK_T);
    KeyStroke _keyStroke_1 = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK);
    menuItemUpdateTime.setAccelerator(_keyStroke_1);
    ChangeUpdateTimePopUp _changeUpdateTimePopUp = new ChangeUpdateTimePopUp();
    menuItemUpdateTime.addActionListener(_changeUpdateTimePopUp);
    final JMenuItem menuItemUpdateTree = new JMenuItem("Update the tree", KeyEvent.VK_T);
    KeyStroke _keyStroke_2 = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK);
    menuItemUpdateTree.setAccelerator(_keyStroke_2);
    final ActionListener _function = new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final Runnable _function = new Runnable() {
          @Override
          public void run() {
            FactoryInterface.this.processNode.removeAllChildren();
            FactoryInterface.INFO.setForeground(Color.BLUE);
            FactoryInterface.INFO.setText("UPDATE TREE ... ");
            UaClient _client = FactoryInterface.this.browser.getClient();
            AddressSpace _addressSpace = _client.getAddressSpace();
            FactoryInterface.this.browser.browse(_addressSpace, Identifiers.RootFolder, FactoryInterface.this.processNode, FactoryInterface.this.nodes);
            FactoryInterface.this.jtree.updateUI();
            FactoryInterface.INFO.setText("");
          }
        };
        Thread _thread = new Thread(_function);
        _thread.start();
      }
    };
    menuItemUpdateTree.addActionListener(_function);
    final JMenuItem menuItemChangeDefaultServerPort = new JMenuItem("Change default server\'s port", KeyEvent.VK_T);
    KeyStroke _keyStroke_3 = KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK);
    menuItemChangeDefaultServerPort.setAccelerator(_keyStroke_3);
    ChangeDefaultPortPopUp _changeDefaultPortPopUp = new ChangeDefaultPortPopUp();
    menuItemChangeDefaultServerPort.addActionListener(_changeDefaultPortPopUp);
    final JMenuItem menuItemChangeDefaultServerIpAddress = new JMenuItem("Change default server\'s ip\'s address", KeyEvent.VK_T);
    KeyStroke _keyStroke_4 = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK);
    menuItemChangeDefaultServerIpAddress.setAccelerator(_keyStroke_4);
    ChangeDefaultIpAddressPopUp _changeDefaultIpAddressPopUp = new ChangeDefaultIpAddressPopUp();
    menuItemChangeDefaultServerIpAddress.addActionListener(_changeDefaultIpAddressPopUp);
    final JMenuItem menuItemChangeDefaultClientIpAddress = new JMenuItem("Change default client\'s ip\'s address", KeyEvent.VK_T);
    KeyStroke _keyStroke_5 = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK);
    menuItemChangeDefaultClientIpAddress.setAccelerator(_keyStroke_5);
    ChangeDefaultClientAddressPopUp _changeDefaultClientAddressPopUp = new ChangeDefaultClientAddressPopUp();
    menuItemChangeDefaultClientIpAddress.addActionListener(_changeDefaultClientAddressPopUp);
    menu.add(menuItemServerSelection);
    menu.add(menuItemUpdateTime);
    menu.add(menuItemUpdateTree);
    menu.add(menuItemChangeDefaultServerPort);
    menu.add(menuItemChangeDefaultServerIpAddress);
    menu.add(menuItemChangeDefaultClientIpAddress);
    Component _createGlue = Box.createGlue();
    this.menuBar.add(_createGlue);
    this.menuBar.add(FactoryInterface.SERVER_ADRESS);
    this.menuBar.add(FactoryInterface.INFO);
    this.menuBar.add(FactoryInterface.SERVER_CONNECTION);
    FactoryInterface.SERVER_CONNECTION.setForeground(Color.RED);
  }
  
  @Pure
  public JTree getJtree() {
    return this.jtree;
  }
  
  @Pure
  public MyJTable getJTableContainer() {
    return this.jTableContainer;
  }
  
  @Pure
  public MyJMonitoringListTable getJKeepValueTable() {
    return this.jKeepValueTable;
  }
  
  @Pure
  public Browser getBrowser() {
    return this.browser;
  }
  
  public void setBrowser(final Browser browser) {
    this.browser = browser;
  }
  
  @Pure
  public Map<DefaultMutableTreeNode, Node> getNodes() {
    return this.nodes;
  }
  
  @Pure
  public DefaultMutableTreeNode getProcessNode() {
    return this.processNode;
  }
}
