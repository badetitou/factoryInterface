package main

import com.prosysopc.ua.ApplicationIdentity
import com.prosysopc.ua.PkiFileBasedCertificateValidator
import com.prosysopc.ua.UserIdentity
import com.prosysopc.ua.client.AddressSpace
import com.prosysopc.ua.client.ConnectException
import com.prosysopc.ua.client.ServerList
import com.prosysopc.ua.client.UaClient
import java.awt.Color
import java.io.File
import java.util.Locale
import java.util.Map
import javax.swing.JOptionPane
import javax.swing.tree.DefaultMutableTreeNode
import org.eclipse.xtend.lib.annotations.Accessors
import org.opcfoundation.ua.builtintypes.LocalizedText
import org.opcfoundation.ua.builtintypes.NodeId
import org.opcfoundation.ua.core.ApplicationDescription
import org.opcfoundation.ua.core.ApplicationType
import org.opcfoundation.ua.transport.security.SecurityMode
import process.Node
import tools.MyCertificateValidationListener
import tools.ServerStatusListener

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * This class is the connection and the exploration of all node of the 
 * server
 */
class Browser {

	/**
	 * The UaClient 
	 */
	@Accessors(PUBLIC_GETTER) UaClient client
	public static boolean FORCE_URL = false
	public static SecurityMode SECURITY_MODE = SecurityMode::BASIC128RSA15_SIGN
	public static String USERNAME
	public static String PASSWORD

	/**
	 * @param URL The URL Of the OPC UA Application
	 */
	new(String URL) {
		if (!FORCE_URL)
			initConnection(chooseServer(URL))
		else
			initConnection(URL)
		connection
	}

	/**
	 * @param URI The URI of the server we want to explore
	 * 
	 * Explore a server to find an OPC UA application
	 */
	private def chooseServer(String URI) {
		var result = ""
		var serverList = new ServerList(URI)
		var serverAddress = newArrayOfSize(serverList.servers.length)
		for (i : 0 ..< serverAddress.length) {
			for (j : 0 ..< serverList.servers.get(i).discoveryUrls.length) {
				serverAddress.set(i, serverList.servers.get(i).discoveryUrls.get(j))
			}
		}
		result = JOptionPane::showInputDialog(null, "Please choose a name", "Select the server address",
			JOptionPane::QUESTION_MESSAGE, null, serverAddress, "").toString
		return result
	}

	/* To create the staff and don't use it at the start of software */
	new() {
		client = new UaClient
	}

	/**
	 * Create the connection and test it
	 * @param URL The final URL (URL of the application)
	 */
	def private initConnection(String URL) {
		if (client != null && client.connected)
			client.disconnect
		try {
			client = new UaClient(URL)
		} catch (Exception e) {
			FactoryInterface::INFO.foreground = Color::RED
			FactoryInterface::INFO.text = "Connection ERROR"
			e.printStackTrace
		}
		configConnection
	}

	/**
	 * Configuration of the security option.
	 * Creation of certificates.
	 * Configuration of the server listener.
	 */
	def private configConnection() {
		client.securityMode = SECURITY_MODE
		client.timeout = 3000
		client.addServerStatusListener(new ServerStatusListener)

		val validator = new PkiFileBasedCertificateValidator
		client.certificateValidator = validator
		validator.validationListener = new MyCertificateValidationListener
		/*
		 * Description of the Application
		 * We need it to use secure connection (password / Certificate)
		 */
		val appDescription = new ApplicationDescription();
		appDescription.setApplicationName(new LocalizedText("Factory_Interface", Locale.ENGLISH))
		appDescription.setApplicationUri("urn:localhost:UA:FactoryInterface")
		appDescription.setProductUri("urn:prosysopc.com:UA:FactoryInterface")
		appDescription.setApplicationType(ApplicationType.Client)
		var identity = ApplicationIdentity::loadOrCreateCertificate(appDescription, "FactoryInterface",
			"private_key_password", new File(validator.getBaseDir(), "private"), true)
		client.applicationIdentity = identity

		/* IF There are an username or a password ask to the server an identification
		 * by login
		 */
		if (USERNAME != "" && PASSWORD != "")
			client.userIdentity = new UserIdentity(USERNAME, PASSWORD)
	}

	/**
	 * Do the connection
	 */
	def private connection() {
		try {
			client.connect
		} catch (ConnectException ce) {
			FactoryInterface::SERVER_CONNECTION.text = " Certificate not accepted by the server "
		} catch (Exception e) {
			e.printStackTrace
		}
		client.autoReconnect = true
	}

	/**
	 * Do the discnonnection
	 */
	def disconnection() {
		client.disconnect
	}

	/**
	 * Browse all the node of the server. And add all node to a tree
	 * @param addSpace The tools to browse a node
	 * @param nodeId The node we want to browse
	 * @param processNode The leaf corresponding to the nodeId for the creation of a JTree
	 * @param nodes Map to keep information between the nodeId and and Leaf
	 */
	def void browse(AddressSpace addSpace, NodeId nodeId, DefaultMutableTreeNode processNode,
		Map<DefaultMutableTreeNode, Node> nodes) {
		println("Browse : " + nodeId.value.toString)
		try {
			var references = addSpace.browse(nodeId)
			for (dr : references) {
				var process2 = new DefaultMutableTreeNode(dr.displayName)
				nodes.put(process2, new Node(addSpace.namespaceTable.toNodeId(dr.nodeId)))
				processNode.add(process2)
				browse(addSpace, addSpace.namespaceTable.toNodeId(dr.nodeId), process2, nodes)
			}
		} catch (Exception e) {
			FactoryInterface::INFO.foreground = Color::BLUE
			FactoryInterface::INFO.text = "Can't browse the tree"
		}
	}

}