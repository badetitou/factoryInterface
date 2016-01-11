package tools;

import com.prosysopc.ua.client.UaClient;
import java.awt.Color;
import main.FactoryInterface;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.ServerStatusDataType;

/**
 * This class is a listener for the OPC UA server. When the server change of status this class decide
 * what is the good behavior.
 * 
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
@SuppressWarnings("all")
public class ServerStatusListener implements com.prosysopc.ua.client.ServerStatusListener {
  /**
   * The method is called when the server is detected as shutdown
   * @param client The client connected to the server
   * @param secondShutDown Time between the server shutdown and the client find the new status
   * @param reason The reason why the server is shutdown
   */
  @Override
  public void onShutdown(final UaClient client, final long secondShutDown, final LocalizedText reason) {
    FactoryInterface.SERVER_CONNECTION.setForeground(Color.RED);
    FactoryInterface.SERVER_CONNECTION.setText((((" Server shutdown in " + Long.valueOf(secondShutDown)) + " seconds. Reason: ") + reason));
  }
  
  /**
   * This method is call when the server already have a status and change
   * @param client The client connects to the server
   * @param oldState The old status of the server
   * @param newState The actual status of the server
   */
  @Override
  public void onStateChange(final UaClient client, final ServerState oldState, final ServerState newState) {
    boolean _equals = newState.equals(ServerState.Unknown);
    if (_equals) {
      FactoryInterface.SERVER_CONNECTION.setForeground(Color.RED);
      StatusCode _serverStatusError = client.getServerStatusError();
      String _plus = (" ServerStatusError: " + _serverStatusError);
      FactoryInterface.SERVER_CONNECTION.setText(_plus);
    } else {
      if ((newState == ServerState.Shutdown)) {
        LocalizedText _localizedText = new LocalizedText("Unknown reason");
        this.onShutdown(client, 1, _localizedText);
      }
    }
  }
  
  /**
   * This method is called the first time we get a status from the server
   * @param client The client connects to the server
   * @param status The current status of the server
   */
  @Override
  public void onStatusChange(final UaClient client, final ServerStatusDataType status) {
    FactoryInterface.SERVER_CONNECTION.setForeground(Color.GREEN);
    ServerState _state = status.getState();
    String _plus = (" Server Status : " + _state);
    FactoryInterface.SERVER_CONNECTION.setText(_plus);
    String _uri = client.getUri();
    String _plus_1 = (" " + _uri);
    String _plus_2 = (_plus_1 + " ");
    FactoryInterface.SERVER_ADRESS.setText(_plus_2);
    DateTime _lastResponseTimestamp = client.getLastResponseTimestamp();
    String _plus_3 = (" " + _lastResponseTimestamp);
    String _plus_4 = (_plus_3 + " ");
    FactoryInterface.INFO.setText(_plus_4);
  }
}
