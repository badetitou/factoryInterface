package tools

import com.prosysopc.ua.client.UaClient
import java.awt.Color
import main.FactoryInterface
import org.opcfoundation.ua.builtintypes.LocalizedText
import org.opcfoundation.ua.core.ServerState
import org.opcfoundation.ua.core.ServerStatusDataType

/**
 * This class is a listener for the OPC UA server. When the server change of status this class decide
 * what is the good behavior.  
 * 
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
class ServerStatusListener implements com.prosysopc.ua.client.ServerStatusListener {
	
	/**
	 * The method is called when the server is detected as shutdown
	 * @param client The client connected to the server
	 * @param secondShutDown Time between the server shutdown and the client find the new status
	 * @param reason The reason why the server is shutdown
	 */
	override onShutdown(UaClient client, long secondShutDown, LocalizedText reason) {
		FactoryInterface::SERVER_CONNECTION.foreground = Color::RED
		FactoryInterface::SERVER_CONNECTION.text = " Server shutdown in " + secondShutDown + " seconds. Reason: " +
			reason
	}

	/**
	 * This method is call when the server already have a status and change
	 * @param client The client connects to the server
	 * @param oldState The old status of the server
	 * @param newState The actual status of the server
	 */
	override onStateChange(UaClient client, ServerState oldState, ServerState newState) {
		if (newState.equals(ServerState.Unknown)){
			FactoryInterface::SERVER_CONNECTION.foreground = Color::RED
			FactoryInterface::SERVER_CONNECTION.text = " ServerStatusError: " + client.serverStatusError
		}
		else if (newState === ServerState::Shutdown){
			onShutdown(client, 1,new LocalizedText("Unknown reason"))
		}
	}

	/**
	 * This method is called the first time we get a status from the server
	 * @param client The client connects to the server
	 * @param status The current status of the server
	 */
	override onStatusChange(UaClient client, ServerStatusDataType status) {
		FactoryInterface::SERVER_CONNECTION.foreground = Color::GREEN
		FactoryInterface::SERVER_CONNECTION.text = " Server Status : " + status.state
		FactoryInterface::SERVER_ADRESS.text = " " + client.uri + " "
		FactoryInterface::INFO.text = " " + client.lastResponseTimestamp + " "
	}

}