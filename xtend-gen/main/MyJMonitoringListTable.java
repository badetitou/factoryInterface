package main;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prosysopc.ua.MonitoredItemBase;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.FactoryInterface;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.MonitoringMode;
import popUp.RemoveKeepValuePopUp;
import tools.TranslateAttributeId;

/**
 * @autor Benoît Verhaeghe
 * 
 * The second Table where we keep value
 */
@Accessors
@SuppressWarnings("all")
public class MyJMonitoringListTable {
  /**
   * The time for auto-update
   */
  public static Integer SERVER_DEFAULT_PORT = Integer.valueOf(0);
  
  public static String SERVER_DEFAULT_IP_ADDRESS = "0.0.0.0";
  
  public static String CLIENT_DEFAULT_ADDRESS = "0.0.0.0";
  
  public static Integer CLIENT_DEFAULT_PORT = Integer.valueOf(80);
  
  private final String USER_AGENT = "Mozilla/5.0";
  
  /**
   * The real Jtable
   */
  @Accessors(AccessorType.NONE)
  private DefaultTableModel model = new DefaultTableModel();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private JTable jTable;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private JPanel scrollPan;
  
  @Accessors(AccessorType.NONE)
  private JLabel serverInfo = new JLabel("Server Stopped");
  
  @Accessors(AccessorType.NONE)
  private JLabel clientInfo = new JLabel("Client Stopped");
  
  @Accessors(AccessorType.NONE)
  private PrintWriter writer;
  
  @Accessors(AccessorType.NONE)
  private boolean serverStarted = false;
  
  @Accessors(AccessorType.NONE)
  private Socket socketServer;
  
  @Accessors(AccessorType.NONE)
  private ServerSocket serverSocket;
  
  @Accessors(AccessorType.NONE)
  private boolean clientStarted = false;
  
  @Accessors(AccessorType.NONE)
  private Thread refreshServer;
  
  /**
   * Elements you want to keep the value
   */
  @Accessors(AccessorType.NONE)
  private Subscription subscription = new Subscription();
  
  /**
   * @param time The new time before every refresh by the subscription
   * 
   * Use it to change refresh time for the subscription
   */
  public void setSubscriptionTimeoutDetectionFactor(final long time) {
    this.subscription.setTimeoutDetectionFactor(time);
  }
  
  /**
   * @param client Client we have to use for the subscription
   * @param nodeId The node we want to keep
   * @param attributeId The node's attribute we want to displayed and get recent information
   */
  public void addSubscription(final UaClient client, final NodeId nodeId, final UnsignedInteger attributeId) {
    try {
      Object _value = nodeId.getValue();
      String _string = _value.toString();
      String _name = TranslateAttributeId.getName(attributeId);
      boolean _isPossible = this.isPossible(_string, _name);
      if (_isPossible) {
        MonitoredDataItem item = new MonitoredDataItem(nodeId, attributeId, MonitoringMode.Reporting);
        final MonitoredDataItemListener _function = new MonitoredDataItemListener() {
          @Override
          public void onDataChange(final MonitoredDataItem sender, final DataValue prevValue, final DataValue value) {
            int _rowCount = MyJMonitoringListTable.this.jTable.getRowCount();
            ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _rowCount, true);
            for (final Integer i : _doubleDotLessThan) {
              boolean _and = false;
              Object _valueAt = MyJMonitoringListTable.this.jTable.getValueAt((i).intValue(), 0);
              NodeId _nodeId = sender.getNodeId();
              Object _value = _nodeId.getValue();
              String _string = _value.toString();
              boolean _equals = Objects.equal(_valueAt, _string);
              if (!_equals) {
                _and = false;
              } else {
                Object _valueAt_1 = MyJMonitoringListTable.this.jTable.getValueAt((i).intValue(), 1);
                UnsignedInteger _attributeId = sender.getAttributeId();
                String _name = TranslateAttributeId.getName(_attributeId);
                boolean _equals_1 = Objects.equal(_valueAt_1, _name);
                _and = _equals_1;
              }
              if (_and) {
                Variant _value_1 = value.getValue();
                MyJMonitoringListTable.this.jTable.setValueAt(_value_1, (i).intValue(), 2);
                DateTime _sourceTimestamp = value.getSourceTimestamp();
                MyJMonitoringListTable.this.jTable.setValueAt(_sourceTimestamp, (i).intValue(), 3);
              }
            }
            DateTime _sourceTimestamp_1 = value.getSourceTimestamp();
            JsonObject _formatToJsonObject = MyJMonitoringListTable.this.formatToJsonObject(nodeId, value, _sourceTimestamp_1);
            MyJMonitoringListTable.this.sendNewData(_formatToJsonObject);
          }
        };
        item.setDataChangeListener(_function);
        this.subscription.addItem(item);
        client.addSubscription(this.subscription);
        String[] row = new String[4];
        Object _value_1 = nodeId.getValue();
        String _string_1 = _value_1.toString();
        row[0] = _string_1;
        String _name_1 = TranslateAttributeId.getName(attributeId);
        row[1] = _name_1;
        row[2] = "Never changed";
        row[3] = "Never changed";
        this.model.addRow(row);
      } else {
        JOptionPane.showMessageDialog(null, "Already Kept", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * @param nodeName The String corresponding to the node
   * @param attributeName The String corresponding to the node's attribute
   */
  private boolean isPossible(final String nodeName, final String attributeName) {
    boolean _xblockexpression = false;
    {
      int _rowCount = this.jTable.getRowCount();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _rowCount, true);
      for (final Integer i : _doubleDotLessThan) {
        boolean _and = false;
        Object _valueAt = this.jTable.getValueAt((i).intValue(), 0);
        boolean _equals = Objects.equal(_valueAt, nodeName);
        if (!_equals) {
          _and = false;
        } else {
          Object _valueAt_1 = this.jTable.getValueAt((i).intValue(), 1);
          boolean _equals_1 = Objects.equal(_valueAt_1, attributeName);
          _and = _equals_1;
        }
        if (_and) {
          return false;
        }
      }
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  /**
   * Hard Reset. New Subscription
   */
  public Subscription resetSubscription() {
    Subscription _subscription = new Subscription();
    return this.subscription = _subscription;
  }
  
  /**
   * @param position The position of the item on the table
   * 
   * Delete from the subscription and the table the item selected
   */
  public void removeSubscription(final int position) {
    try {
      int _rowCount = this.jTable.getRowCount();
      boolean _lessThan = (position < _rowCount);
      if (_lessThan) {
        MonitoredItemBase[] _items = this.subscription.getItems();
        MonitoredItemBase _get = _items[position];
        this.subscription.removeItem(_get);
        this.model.removeRow(position);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public MyJMonitoringListTable() {
    this.subscription.setTimeoutDetectionFactor(5.0);
    JPanel _jPanel = new JPanel();
    this.scrollPan = _jPanel;
    JTable _jTable = new JTable();
    this.jTable = _jTable;
    String[] header = new String[4];
    header[0] = "NodeId";
    header[1] = "Attribute";
    header[2] = "Value";
    header[3] = "Last Change";
    this.model.setColumnIdentifiers(header);
    this.jTable.setModel(this.model);
    JTableHeader _tableHeader = this.jTable.getTableHeader();
    _tableHeader.setReorderingAllowed(false);
    BorderLayout _borderLayout = new BorderLayout();
    this.scrollPan.setLayout(_borderLayout);
    JLabel _jLabel = new JLabel("Monitoring List");
    this.scrollPan.add(_jLabel, BorderLayout.NORTH);
    JScrollPane _jScrollPane = new JScrollPane(this.jTable);
    this.scrollPan.add(_jScrollPane, BorderLayout.CENTER);
    final JPanel botPan = new JPanel();
    this.initBottomPan(botPan);
    this.scrollPan.add(botPan, BorderLayout.SOUTH);
    ListSelectionModel listSelectionModel = this.jTable.getSelectionModel();
    this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RemoveKeepValuePopUp _removeKeepValuePopUp = new RemoveKeepValuePopUp(this);
    listSelectionModel.addListSelectionListener(_removeKeepValuePopUp);
    final Runnable _function = new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Socket _accept = MyJMonitoringListTable.this.serverSocket.accept();
            MyJMonitoringListTable.this.socketServer = _accept;
            OutputStream _outputStream = MyJMonitoringListTable.this.socketServer.getOutputStream();
            PrintWriter _printWriter = new PrintWriter(_outputStream);
            MyJMonitoringListTable.this.writer = _printWriter;
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception exception = (Exception)_t;
              InputOutput.<String>println("Ignore this");
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
    };
    Thread _thread = new Thread(_function);
    this.refreshServer = _thread;
  }
  
  /**
   * @param botPan The panel to dispose the Button
   * 
   * Create and place the button at the good position with all them listener
   */
  private Component initBottomPan(final JPanel botPan) {
    Component _xblockexpression = null;
    {
      final JButton exportButton = new JButton("Export to JSON");
      final ActionListener _function = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          try {
            if (MyJMonitoringListTable.this.clientStarted) {
              JsonArray _jsonsArray = MyJMonitoringListTable.this.getJsonsArray();
              MyJMonitoringListTable.this.sendPost(_jsonsArray);
              InputOutput.<String>println("SERVER SEND DATA");
            } else {
              final Runnable _function = new Runnable() {
                @Override
                public void run() {
                  try {
                    FactoryInterface.INFO.setForeground(Color.RED);
                    FactoryInterface.INFO.setText("You have to start the client before");
                    Thread.sleep(4000);
                    FactoryInterface.INFO.setText("");
                  } catch (Throwable _e) {
                    throw Exceptions.sneakyThrow(_e);
                  }
                }
              };
              Thread _thread = new Thread(_function);
              _thread.start();
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      exportButton.addActionListener(_function);
      final JButton startServer = new JButton("Start Socket Server");
      final ActionListener _function_1 = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          try {
            if (MyJMonitoringListTable.this.serverStarted) {
              InputOutput.<String>println("Info : stopping server");
              boolean _notEquals = (!Objects.equal(MyJMonitoringListTable.this.writer, null));
              if (_notEquals) {
                MyJMonitoringListTable.this.writer.close();
                InputOutput.<String>println("Info : Writer closed");
              }
              boolean _notEquals_1 = (!Objects.equal(MyJMonitoringListTable.this.socketServer, null));
              if (_notEquals_1) {
                MyJMonitoringListTable.this.socketServer.close();
                InputOutput.<String>println("Info : socket closed");
              }
              boolean _notEquals_2 = (!Objects.equal(MyJMonitoringListTable.this.serverSocket, null));
              if (_notEquals_2) {
                try {
                  MyJMonitoringListTable.this.serverSocket.close();
                } catch (final Throwable _t) {
                  if (_t instanceof Exception) {
                    final Exception a = (Exception)_t;
                    InputOutput.<String>println("Error");
                  } else {
                    throw Exceptions.sneakyThrow(_t);
                  }
                }
                InputOutput.<String>println("Info : server\'s socket closed");
              }
              MyJMonitoringListTable.this.serverStarted = false;
              MyJMonitoringListTable.this.serverInfo.setText("Stop Socket Server");
              startServer.setText("Start Socket Server");
              MyJMonitoringListTable.this.refreshServer.suspend();
            } else {
              InputOutput.<String>println("Info : server starting");
              InetAddress _byName = InetAddress.getByName(MyJMonitoringListTable.SERVER_DEFAULT_IP_ADDRESS);
              ServerSocket _serverSocket = new ServerSocket((MyJMonitoringListTable.SERVER_DEFAULT_PORT).intValue(), 50, _byName);
              MyJMonitoringListTable.this.serverSocket = _serverSocket;
              int _localPort = MyJMonitoringListTable.this.serverSocket.getLocalPort();
              String _plus = ("Server start. Port : " + Integer.valueOf(_localPort));
              String _plus_1 = (_plus + " Address : ");
              InetAddress _inetAddress = MyJMonitoringListTable.this.serverSocket.getInetAddress();
              String _hostAddress = _inetAddress.getHostAddress();
              String _plus_2 = (_plus_1 + _hostAddress);
              MyJMonitoringListTable.this.serverInfo.setText(_plus_2);
              MyJMonitoringListTable.this.serverStarted = true;
              startServer.setText("Stop Socket Server");
              boolean _isAlive = MyJMonitoringListTable.this.refreshServer.isAlive();
              if (_isAlive) {
                MyJMonitoringListTable.this.refreshServer.resume();
              } else {
                MyJMonitoringListTable.this.refreshServer.start();
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      startServer.addActionListener(_function_1);
      final JButton startClient = new JButton("Start Client Export DATA-HTTP");
      final ActionListener _function_2 = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          if ((!MyJMonitoringListTable.this.clientStarted)) {
            startClient.setText("Stop Client Export DATA-HTTP");
            MyJMonitoringListTable.this.clientStarted = true;
          } else {
            startClient.setText("Start Client Export DATA-HTTP");
            MyJMonitoringListTable.this.clientStarted = false;
          }
        }
      };
      startClient.addActionListener(_function_2);
      GridLayout _gridLayout = new GridLayout(3, 2);
      botPan.setLayout(_gridLayout);
      botPan.add(startServer);
      botPan.add(this.serverInfo);
      botPan.add(startClient);
      botPan.add(this.clientInfo);
      _xblockexpression = botPan.add(exportButton);
    }
    return _xblockexpression;
  }
  
  /**
   * @return An Json Array with all data on the Monitoring List
   */
  private JsonArray getJsonsArray() {
    JsonArray json = new JsonArray();
    int _rowCount = this.jTable.getRowCount();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _rowCount, true);
    for (final Integer i : _doubleDotLessThan) {
      Object _valueAt = this.jTable.getValueAt((i).intValue(), 0);
      String _string = _valueAt.toString();
      Object _valueAt_1 = this.jTable.getValueAt((i).intValue(), 2);
      String _string_1 = _valueAt_1.toString();
      Object _valueAt_2 = this.jTable.getValueAt((i).intValue(), 3);
      String _string_2 = _valueAt_2.toString();
      JsonObject _formatToJsonObject = this.formatToJsonObject(_string, _string_1, _string_2);
      json.add(_formatToJsonObject);
    }
    return json;
  }
  
  /**
   * Check the status of our client and server and send new data to right entities
   * 
   * @param jsonElement Datas we want to send
   */
  private void sendNewData(final JsonElement jsonElement) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _and_2 = false;
    if (!this.serverStarted) {
      _and_2 = false;
    } else {
      boolean _notEquals = (!Objects.equal(this.socketServer, null));
      _and_2 = _notEquals;
    }
    if (!_and_2) {
      _and_1 = false;
    } else {
      boolean _isConnected = this.socketServer.isConnected();
      _and_1 = _isConnected;
    }
    if (!_and_1) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(this.writer, null));
      _and = _notEquals_1;
    }
    if (_and) {
      this.writer.println(jsonElement);
      this.writer.flush();
    }
    if (this.clientStarted) {
      try {
        this.sendPost(jsonElement);
      } catch (final Throwable _t) {
        if (_t instanceof MalformedURLException) {
          final MalformedURLException e = (MalformedURLException)_t;
          this.clientInfo.setText("Malformed URL");
        } else if (_t instanceof Exception) {
          final Exception e_1 = (Exception)_t;
          e_1.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  /**
   * @param jsonElement Data we want to send
   * 
   * Send the data to client with a POST standard format
   */
  private void sendPost(final JsonElement jsonElement) throws Exception {
    String url = MyJMonitoringListTable.CLIENT_DEFAULT_ADDRESS;
    URL obj = new URL(url);
    URLConnection _openConnection = obj.openConnection();
    HttpURLConnection con = ((HttpURLConnection) _openConnection);
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", this.USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    con.setDoOutput(true);
    OutputStream _outputStream = con.getOutputStream();
    DataOutputStream wr = new DataOutputStream(_outputStream);
    String _string = jsonElement.toString();
    wr.writeBytes(_string);
    wr.flush();
    wr.close();
    int responseCode = con.getResponseCode();
    InputOutput.<String>println(("\nSending \'POST\' request to URL : " + url));
    String _string_1 = jsonElement.toString();
    String _plus = ("Post parameters : " + _string_1);
    InputOutput.<String>println(_plus);
    InputOutput.<String>println(("Response Code : " + Integer.valueOf(responseCode)));
    this.clientInfo.setText(("Response Code : " + Integer.valueOf(responseCode)));
  }
  
  /**
   * Don't really work. We have to see with Eduardo how the data have to be export
   */
  private JsonObject formatToJsonObject(final NodeId nodeId, final DataValue value, final DateTime timestamp) {
    JsonObject _xblockexpression = null;
    {
      JsonObject jsonObject = new JsonObject();
      Object _value = nodeId.getValue();
      String _string = _value.toString();
      jsonObject.addProperty("variable_id", _string);
      Variant _value_1 = value.getValue();
      String _string_1 = _value_1.toString();
      jsonObject.addProperty("value", _string_1);
      final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      GregorianCalendar _localCalendar = timestamp.getLocalCalendar();
      Date _time = _localCalendar.getTime();
      String _format = sdfDate.format(_time);
      jsonObject.addProperty("updated", _format);
      _xblockexpression = jsonObject;
    }
    return _xblockexpression;
  }
  
  /**
   * DON't WORK
   */
  private JsonObject formatToJsonObject(final String nodeId, final String value, final String timestamp) {
    JsonObject _xblockexpression = null;
    {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("variable_id", nodeId);
      jsonObject.addProperty("value", value);
      String _substring = timestamp.substring(0, 17);
      String _replaceAll = _substring.replaceAll("/", "-");
      jsonObject.addProperty("updated", _replaceAll);
      _xblockexpression = jsonObject;
    }
    return _xblockexpression;
  }
  
  @Pure
  public String getUSER_AGENT() {
    return this.USER_AGENT;
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
