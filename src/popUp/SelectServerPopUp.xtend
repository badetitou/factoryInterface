package popUp

import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.ButtonGroup
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextField
import main.Browser
import main.FactoryInterface
import org.eclipse.xtend.lib.annotations.Accessors
import org.opcfoundation.ua.core.Identifiers
import org.opcfoundation.ua.transport.security.SecurityMode
import tools.TextPrompt

import static main.Browser.*

/**
 * @autor Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 * 
 * The pop-up to select the server we want to connect
 */
class SelectServerPopUp implements ActionListener {

	/**
	 * Access to the main part of the program.
	 */
	@Accessors(NONE) FactoryInterface factoryInterface

	/**
	 * @param fi The factory interface to interact with the program
	 */
	new(FactoryInterface fi) {
		factoryInterface = fi
	}

	/**
	 * Display a pop up with just one text field.
	 * If user press OK. Update all the tree with the new Address
	 * @param e Totally useless
	 */
	override actionPerformed(ActionEvent e) {
		/* The panel we will display */
		val panel = new JPanel(new GridLayout(10, 2))
		
		/* The main component  */
		val field1 = new JTextField("")
		val check = new JCheckBox("Force use this address")

		/* Prompt component login */
		val accountUsername = new JTextField
		val promptUsername = new TextPrompt("username", accountUsername)
		promptUsername.changeAlpha(0.5f)
		accountUsername.editable = false
		val accountPassword = new JTextField
		val promptPassword = new TextPrompt("password", accountPassword)
		promptPassword.changeAlpha(0.5f)
		accountPassword.editable = false
		
		/* Component select type of connection */
		val anonymous = new JRadioButton("Anonymous")
		anonymous.selected = true
		anonymous.actionCommand = anonymous.text
		anonymous.addActionListener([ useless |
			accountUsername.editable = false
			accountPassword.editable = false
		])
		val account = new JRadioButton("Username & Password")
		account.actionCommand = account.text
		account.addActionListener([ useless |
			accountUsername.editable = true
			accountPassword.editable = true

		])
		/* NOT Work. Have to add a file selector to select a specific certificate */
		val certificate = new JRadioButton("My Specific Certificate In progress")
		certificate.actionCommand = certificate.text
		certificate.addActionListener([ useless |
			accountUsername.editable = false
			accountPassword.editable = false
		])
		/* The creation of the group */
		val connectionTypeGroup = new ButtonGroup()
		connectionTypeGroup.add(anonymous)
		connectionTypeGroup.add(account)
		connectionTypeGroup.add(certificate)
		
		/* Button and group for security type */
		val none = new JRadioButton("None")
		none.actionCommand = none.text
		none.selected = true
		val basic128 = new JRadioButton("Basic 128 Sign")
		none.actionCommand = none.text
		val basic128Encrypt = new JRadioButton("Basic 128 Sign and Encrypt")
		none.actionCommand = none.text
		val basic256 = new JRadioButton("Basic 256 Sign")
		none.actionCommand = none.text
		val basic256Encrypt = new JRadioButton("Basic 256 Sign and Encrypt")
		none.actionCommand = none.text
		val securityGroup = new ButtonGroup()
		securityGroup.add(none)
		securityGroup.add(basic128)
		securityGroup.add(basic128Encrypt)
		securityGroup.add(basic256)
		securityGroup.add(basic256Encrypt)
		
		/* Disposition of components */
		panel.add(new JLabel("Enter the address"))
		panel.add(field1)
		panel.add(check)
		panel.add(new JLabel)
		panel.add(anonymous)
		panel.add(new JLabel)
		panel.add(account)
		panel.add(accountUsername)
		panel.add(new JLabel)
		panel.add(accountPassword)
		panel.add(certificate)
		panel.add(new JLabel)
		panel.add(new JLabel("Security Mode"))
		panel.add(none)
		panel.add(basic128)
		panel.add(basic256)
		panel.add(basic128Encrypt)
		panel.add(basic256Encrypt)
		
		/* We display the PopUp */
		var result = JOptionPane::showConfirmDialog(null, panel, "Write the address", JOptionPane::OK_CANCEL_OPTION,
			JOptionPane::PLAIN_MESSAGE)
		
		/*If validation */
		if (result == JOptionPane::OK_OPTION) {
			/*Reset the interface */
			factoryInterface.JKeepValueTable.resetSubscription
			factoryInterface.JTableContainer.JTable.removeAll
			factoryInterface.processNode.removeAllChildren
			
			/* Close all node of the JTree*/
			for (i : factoryInterface.jtree.rowCount >.. 0) {
				factoryInterface.jtree.collapseRow(i)
			}
			factoryInterface.browser.disconnection
			
			/* Usefull because DNS Problem. We have to use this option with the IPV4/6 Address */
			if (check.selected) {
				Browser::FORCE_URL = true
			} else {
				Browser::FORCE_URL = false
			}
			/* If the radioButton account is selected, we change value of Browser attribute to activate the
			 * login asking to the server
			 */
			if (account.selected){
				Browser::USERNAME = accountUsername.text
				Browser::PASSWORD = accountPassword.text
			}
			/* Else reset the current value */
			else {
				Browser::USERNAME = ""
				Browser::PASSWORD = ""
			}
			
			/*
			 * none : no certificate
			 * sign : certificate
			 * Encrypt : encrypting data (slowest)
			 */
			if(none.selected){
				Browser::SECURITY_MODE = SecurityMode::NONE
			} else if (basic128.selected){
				Browser::SECURITY_MODE = SecurityMode::BASIC128RSA15_SIGN
			} else if (basic128Encrypt.selected){
				Browser::SECURITY_MODE = SecurityMode::BASIC128RSA15_SIGN_ENCRYPT
			} else if(basic256.selected){
				Browser::SECURITY_MODE = SecurityMode::BASIC256_SIGN
			} else if (basic256Encrypt.selected){
				Browser::SECURITY_MODE = SecurityMode::BASIC256_SIGN_ENCRYPT
			}
			
			/* Create the new connection */
			factoryInterface.browser = new Browser(field1.text)
			/*Browse the server */
			factoryInterface.browser.browse(factoryInterface.browser.client.addressSpace, Identifiers::RootFolder,
				factoryInterface.processNode, factoryInterface.nodes)

		}
	}
}