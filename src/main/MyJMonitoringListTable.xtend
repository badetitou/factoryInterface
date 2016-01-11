package main

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.prosysopc.ua.client.MonitoredDataItem
import com.prosysopc.ua.client.Subscription
import com.prosysopc.ua.client.UaClient
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.io.DataOutputStream
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.MalformedURLException
import java.net.ServerSocket
import java.net.Socket
import java.net.URL
import java.text.SimpleDateFormat
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel
import org.eclipse.xtend.lib.annotations.Accessors
import org.opcfoundation.ua.builtintypes.DataValue
import org.opcfoundation.ua.builtintypes.DateTime
import org.opcfoundation.ua.builtintypes.NodeId
import org.opcfoundation.ua.builtintypes.UnsignedInteger
import org.opcfoundation.ua.core.MonitoringMode
import popUp.RemoveKeepValuePopUp
import tools.TranslateAttributeId

/**
 * @autor Benoît Verhaeghe
 * 
 * The second Table where we keep value
 */
@Accessors class MyJMonitoringListTable {

	/**
	 * The time for auto-update
	 */
	public static Integer SERVER_DEFAULT_PORT = 0
	public static String SERVER_DEFAULT_IP_ADDRESS = "0.0.0.0"
	public static String CLIENT_DEFAULT_ADDRESS = "0.0.0.0"
	public static Integer CLIENT_DEFAULT_PORT = 80
	final String USER_AGENT = "Mozilla/5.0"

	/**
	 * The real Jtable
	 */
	@Accessors(NONE) DefaultTableModel model = new DefaultTableModel
	@Accessors(PUBLIC_GETTER) JTable jTable
	@Accessors(PUBLIC_GETTER) JPanel scrollPan

	@Accessors(NONE) JLabel serverInfo = new JLabel("Server Stopped")
	@Accessors(NONE) JLabel clientInfo = new JLabel("Client Stopped")

	@Accessors(NONE) PrintWriter writer
	@Accessors(NONE) boolean serverStarted = false
	@Accessors(NONE) Socket socketServer
	@Accessors(NONE) ServerSocket serverSocket

	@Accessors(NONE) boolean clientStarted = false

	@Accessors(NONE) Thread refreshServer

	/**
	 * Elements you want to keep the value
	 */
	@Accessors(NONE) Subscription subscription = new Subscription

	/**
	 * @param time The new time before every refresh by the subscription
	 * 
	 * Use it to change refresh time for the subscription
	 */
	def setSubscriptionTimeoutDetectionFactor(long time) {
		subscription.timeoutDetectionFactor = time
	}

	/**
	 * @param client Client we have to use for the subscription
	 * @param nodeId The node we want to keep
	 * @param attributeId The node's attribute we want to displayed and get recent information 
	 */
	def addSubscription(UaClient client, NodeId nodeId, UnsignedInteger attributeId) {
		
		/*
		 * Can't add two time the same item. 
		 */
		if (isPossible(nodeId.value.toString, TranslateAttributeId::getName(attributeId))) {
			var item = new MonitoredDataItem(nodeId, attributeId, MonitoringMode.Reporting)
			/*
			 * If value get an update we change his value on the table and send new data 
			 */
			item.setDataChangeListener([ MonitoredDataItem sender, DataValue prevValue, DataValue value |
				for (i : 0 ..< jTable.rowCount) {
					/*
					 * If the data corresponding to data on the JTable.
					 * Update values  
					 */
					if (jTable.getValueAt(i, 0) == sender.nodeId.value.toString &&
						jTable.getValueAt(i, 1) == TranslateAttributeId::getName(sender.attributeId)) {
						jTable.setValueAt(value.value, i, 2)
						jTable.setValueAt(value.sourceTimestamp, i, 3)
					}
				}
				sendNewData(formatToJsonObject(nodeId, value, value.sourceTimestamp))
			])

			subscription.addItem(item)
			client.addSubscription(subscription)

			/* Add to the table the new item */
			var row = newArrayOfSize(4)
			row.set(0, nodeId.value.toString)
			row.set(1, TranslateAttributeId::getName(attributeId))
			row.set(2, "Never changed")
			row.set(3, "Never changed")
			model.addRow(row)
		} else {
			JOptionPane.showMessageDialog(null, "Already Kept", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @param nodeName The String corresponding to the node
	 * @param attributeName The String corresponding to the node's attribute 
	 */
	def private isPossible(String nodeName, String attributeName) {
		for (i : 0 ..< jTable.rowCount) {
			if (jTable.getValueAt(i, 0) == nodeName && jTable.getValueAt(i, 1) == attributeName) {
				return false
			}
		}
		true
	}

	/**
	 * Hard Reset. New Subscription
	 */
	def resetSubscription() {
		subscription = new Subscription
	}

	/**
	 * @param position The position of the item on the table
	 * 
	 * Delete from the subscription and the table the item selected
	 */
	def removeSubscription(int position) {
		if (position < jTable.rowCount) {
			subscription.removeItem(subscription.items.get(position))
			model.removeRow(position)
		}
	}

	new() {
		/*Default refresh value for the subscription */
		subscription.timeoutDetectionFactor = 5.0

		/**
		 * Init panel second JTable
		 */
		scrollPan = new JPanel
		jTable = new JTable
		var header = <String>newArrayOfSize(4)
		header.set(0, "NodeId")
		header.set(1, "Attribute")
		header.set(2, "Value")
		header.set(3, "Last Change")
		model.columnIdentifiers = header
		jTable.model = model

		jTable.tableHeader.reorderingAllowed = false /* Avoid error, don't change placement of column */
		scrollPan.layout = new BorderLayout
		scrollPan.add(new JLabel("Monitoring List"), BorderLayout::NORTH)
		scrollPan.add(new JScrollPane(jTable), BorderLayout::CENTER)
		val botPan = new JPanel
		initBottomPan(botPan)
		scrollPan.add(botPan, BorderLayout::SOUTH)

		var listSelectionModel = jTable.selectionModel /* Only one row by one row */
		jTable.selectionMode = ListSelectionModel::SINGLE_SELECTION
		listSelectionModel.addListSelectionListener(new RemoveKeepValuePopUp(this))

		/* Send data when we're a server */
		refreshServer = new Thread([|
			while (true) {
				try {
					socketServer = serverSocket.accept
					writer = new PrintWriter(socketServer.outputStream)
				} catch (Exception exception) {
					println("Ignore this") /* error when a client connect and disconnect. The error stop when an other client be connect */
				}
			}
		])
	}

	/**
	 * @param botPan The panel to dispose the Button
	 * 
	 * Create and place the button at the good position with all them listener
	 */
	private def initBottomPan(JPanel botPan) {
		/* Button export */
		val exportButton = new JButton("Export to JSON")
		exportButton.addActionListener([ e |
			if (clientStarted) {
				sendPost(getJsonsArray)
				println("SERVER SEND DATA")
			} else {
				new Thread([|
					FactoryInterface::INFO.foreground = Color::RED
					FactoryInterface::INFO.text = "You have to start the client before"
					Thread::sleep(4000)
					FactoryInterface::INFO.text = ""
				]).start
			}
		])

		/* Button server */
		val startServer = new JButton("Start Socket Server")
		startServer.addActionListener([ e |
			if (serverStarted) {
				println("Info : stopping server")
				if (writer != null) {
					writer.close
					println("Info : Writer closed")
				}
				if (socketServer != null) {
					socketServer.close
					println("Info : socket closed")
				}
				if (serverSocket != null) {
					try {
						serverSocket.close
					} catch (Exception a) {
						println("Error")
					}
					println("Info : server's socket closed")
				}
				serverStarted = false
				serverInfo.text = "Stop Socket Server"
				startServer.text = "Start Socket Server"
				refreshServer.suspend
			} else {
				println("Info : server starting")
				serverSocket = new ServerSocket(SERVER_DEFAULT_PORT, 50,
					InetAddress::getByName(SERVER_DEFAULT_IP_ADDRESS))
				serverInfo.text = "Server start. Port : " + serverSocket.localPort + " Address : " +
					serverSocket.inetAddress.hostAddress
				serverStarted = true
				startServer.text = "Stop Socket Server"
				if (refreshServer.alive) {
					refreshServer.resume
				} else {
					refreshServer.start
				}
			}
		])

		/* Button client */
		val startClient = new JButton("Start Client Export DATA-HTTP")
		startClient.addActionListener([ e |
			if (!clientStarted) {
				startClient.text = "Stop Client Export DATA-HTTP"
				clientStarted = true
			} else {
				startClient.text = "Start Client Export DATA-HTTP"
				clientStarted = false
			}
		])
		
		/* add component to the panel */
		botPan.layout = new GridLayout(3, 2)
		botPan.add(startServer)
		botPan.add(serverInfo)
		botPan.add(startClient)
		botPan.add(clientInfo)
		botPan.add(exportButton)

	}
	
	/**
	 * @return An Json Array with all data on the Monitoring List
	 */
	def private getJsonsArray() {
		var json = new JsonArray
		for (i : 0 ..< jTable.rowCount) {
			json.add(
				formatToJsonObject(jTable.getValueAt(i, 0).toString, jTable.getValueAt(i, 2).toString,
					jTable.getValueAt(i, 3).toString))
		}
		return json
	}

	/**
	 * Check the status of our client and server and send new data to right entities
	 * 
	 * @param jsonElement Datas we want to send
	 */
	private def sendNewData(JsonElement jsonElement) {
		if (serverStarted && socketServer != null && socketServer.connected && writer != null) {
			writer.println(jsonElement)
			writer.flush
		}
		if (clientStarted) {
			try {
				sendPost(jsonElement)
			} catch (MalformedURLException e) {
				clientInfo.text = "Malformed URL"
			} catch (Exception e) {
				e.printStackTrace
			}
		}
	}
	
	/**
	 * @param jsonElement Data we want to send
	 * 
	 * Send the data to client with a POST standard format
	 */
	def private sendPost(JsonElement jsonElement) throws Exception {

		var url = CLIENT_DEFAULT_ADDRESS
		var obj = new URL(url)
		var con = obj.openConnection as HttpURLConnection

		/* add request header */
		con.requestMethod = "POST"
		con.setRequestProperty("User-Agent", USER_AGENT)
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5")

		// var urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345"
		// var urlParameters =  
		/* Send post request */
		con.doOutput = true
		var wr = new DataOutputStream(con.outputStream)
		wr.writeBytes(jsonElement.toString)
		wr.flush
		wr.close

		// normal java int responseCode = con.getResponseCode()
		var responseCode = con.responseCode
		println("\nSending 'POST' request to URL : " + url)
		println("Post parameters : " + jsonElement.toString)
		println("Response Code : " + responseCode)
		clientInfo.text = "Response Code : " + responseCode

	}

	/**
	 * Don't really work. We have to see with Eduardo how the data have to be export
	 */
	def private formatToJsonObject(NodeId nodeId, DataValue value, DateTime timestamp) {
		var jsonObject = new JsonObject
		jsonObject.addProperty("variable_id", nodeId.value.toString)
		jsonObject.addProperty("value", value.value.toString)
		val sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // dd/MM/yyyy
		jsonObject.addProperty("updated", sdfDate.format(timestamp.localCalendar.time))
		jsonObject
	}

	/**
	 * DON't WORK
	 */
	def private formatToJsonObject(String nodeId, String value, String timestamp) {
		var jsonObject = new JsonObject
		jsonObject.addProperty("variable_id", nodeId)
		jsonObject.addProperty("value", value)
		jsonObject.addProperty("updated", timestamp.substring(0, 17).replaceAll("/", "-"))
		jsonObject
	}

}