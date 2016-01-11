package main;

import com.google.common.base.Objects;
import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.AddressSpace;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.ServerList;
import com.prosysopc.ua.client.UaClient;
import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import main.FactoryInterface;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;
import process.Node;
import tools.MyCertificateValidationListener;
import tools.ServerStatusListener;

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * This class is the connection and the exploration of all node of the
 * server
 */
@SuppressWarnings("all")
public class Browser {
  /**
   * The UaClient
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private UaClient client;
  
  public static boolean FORCE_URL = false;
  
  public static SecurityMode SECURITY_MODE = SecurityMode.BASIC128RSA15_SIGN;
  
  public static String USERNAME;
  
  public static String PASSWORD;
  
  /**
   * @param URL The URL Of the OPC UA Application
   */
  public Browser(final String URL) {
    if ((!Browser.FORCE_URL)) {
      String _chooseServer = this.chooseServer(URL);
      this.initConnection(_chooseServer);
    } else {
      this.initConnection(URL);
    }
    this.connection();
  }
  
  /**
   * @param URI The URI of the server we want to explore
   * 
   * Explore a server to find an OPC UA application
   */
  private String chooseServer(final String URI) {
    try {
      String result = "";
      ServerList serverList = new ServerList(URI);
      ApplicationDescription[] _servers = serverList.getServers();
      int _length = _servers.length;
      Object[] serverAddress = new Object[_length];
      int _length_1 = serverAddress.length;
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length_1, true);
      for (final Integer i : _doubleDotLessThan) {
        ApplicationDescription[] _servers_1 = serverList.getServers();
        ApplicationDescription _get = _servers_1[(i).intValue()];
        String[] _discoveryUrls = _get.getDiscoveryUrls();
        int _length_2 = _discoveryUrls.length;
        ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _length_2, true);
        for (final Integer j : _doubleDotLessThan_1) {
          ApplicationDescription[] _servers_2 = serverList.getServers();
          ApplicationDescription _get_1 = _servers_2[(i).intValue()];
          String[] _discoveryUrls_1 = _get_1.getDiscoveryUrls();
          String _get_2 = _discoveryUrls_1[(j).intValue()];
          serverAddress[(i).intValue()] = _get_2;
        }
      }
      Object _showInputDialog = JOptionPane.showInputDialog(null, "Please choose a name", "Select the server address", 
        JOptionPane.QUESTION_MESSAGE, null, serverAddress, "");
      String _string = _showInputDialog.toString();
      result = _string;
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * To create the staff and don't use it at the start of software
   */
  public Browser() {
    UaClient _uaClient = new UaClient();
    this.client = _uaClient;
  }
  
  /**
   * Create the connection and test it
   * @param URL The final URL (URL of the application)
   */
  private void initConnection(final String URL) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(this.client, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isConnected = this.client.isConnected();
      _and = _isConnected;
    }
    if (_and) {
      this.client.disconnect();
    }
    try {
      UaClient _uaClient = new UaClient(URL);
      this.client = _uaClient;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        FactoryInterface.INFO.setForeground(Color.RED);
        FactoryInterface.INFO.setText("Connection ERROR");
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    this.configConnection();
  }
  
  /**
   * Configuration of the security option.
   * Creation of certificates.
   * Configuration of the server listener.
   */
  private void configConnection() {
    try {
      this.client.setSecurityMode(Browser.SECURITY_MODE);
      this.client.setTimeout(3000);
      ServerStatusListener _serverStatusListener = new ServerStatusListener();
      this.client.addServerStatusListener(_serverStatusListener);
      final PkiFileBasedCertificateValidator validator = new PkiFileBasedCertificateValidator();
      this.client.setCertificateValidator(validator);
      MyCertificateValidationListener _myCertificateValidationListener = new MyCertificateValidationListener();
      validator.setValidationListener(_myCertificateValidationListener);
      final ApplicationDescription appDescription = new ApplicationDescription();
      LocalizedText _localizedText = new LocalizedText("Factory_Interface", Locale.ENGLISH);
      appDescription.setApplicationName(_localizedText);
      appDescription.setApplicationUri("urn:localhost:UA:FactoryInterface");
      appDescription.setProductUri("urn:prosysopc.com:UA:FactoryInterface");
      appDescription.setApplicationType(ApplicationType.Client);
      File _baseDir = validator.getBaseDir();
      File _file = new File(_baseDir, "private");
      ApplicationIdentity identity = ApplicationIdentity.loadOrCreateCertificate(appDescription, "FactoryInterface", 
        "private_key_password", _file, true);
      this.client.setApplicationIdentity(identity);
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(Browser.USERNAME, ""));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(Browser.PASSWORD, ""));
        _and = _notEquals_1;
      }
      if (_and) {
        UserIdentity _userIdentity = new UserIdentity(Browser.USERNAME, Browser.PASSWORD);
        this.client.setUserIdentity(_userIdentity);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Do the connection
   */
  private void connection() {
    try {
      this.client.connect();
    } catch (final Throwable _t) {
      if (_t instanceof ConnectException) {
        final ConnectException ce = (ConnectException)_t;
        FactoryInterface.SERVER_CONNECTION.setText(" Certificate not accepted by the server ");
      } else if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    this.client.setAutoReconnect(true);
  }
  
  /**
   * Do the discnonnection
   */
  public void disconnection() {
    this.client.disconnect();
  }
  
  /**
   * Browse all the node of the server. And add all node to a tree
   * @param addSpace The tools to browse a node
   * @param nodeId The node we want to browse
   * @param processNode The leaf corresponding to the nodeId for the creation of a JTree
   * @param nodes Map to keep information between the nodeId and and Leaf
   */
  public void browse(final AddressSpace addSpace, final NodeId nodeId, final DefaultMutableTreeNode processNode, final Map<DefaultMutableTreeNode, Node> nodes) {
    Object _value = nodeId.getValue();
    String _string = _value.toString();
    String _plus = ("Browse : " + _string);
    InputOutput.<String>println(_plus);
    try {
      List<ReferenceDescription> references = addSpace.browse(nodeId);
      for (final ReferenceDescription dr : references) {
        {
          LocalizedText _displayName = dr.getDisplayName();
          DefaultMutableTreeNode process2 = new DefaultMutableTreeNode(_displayName);
          NamespaceTable _namespaceTable = addSpace.getNamespaceTable();
          ExpandedNodeId _nodeId = dr.getNodeId();
          NodeId _nodeId_1 = _namespaceTable.toNodeId(_nodeId);
          Node _node = new Node(_nodeId_1);
          nodes.put(process2, _node);
          processNode.add(process2);
          NamespaceTable _namespaceTable_1 = addSpace.getNamespaceTable();
          ExpandedNodeId _nodeId_2 = dr.getNodeId();
          NodeId _nodeId_3 = _namespaceTable_1.toNodeId(_nodeId_2);
          this.browse(addSpace, _nodeId_3, process2, nodes);
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        FactoryInterface.INFO.setForeground(Color.BLUE);
        FactoryInterface.INFO.setText("Can\'t browse the tree");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  @Pure
  public UaClient getClient() {
    return this.client;
  }
}
