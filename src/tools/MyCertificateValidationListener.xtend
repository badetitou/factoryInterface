package tools

import com.prosysopc.ua.CertificateValidationListener
import com.prosysopc.ua.PkiFileBasedCertificateValidator.CertificateCheck
import com.prosysopc.ua.PkiFileBasedCertificateValidator.ValidationResult
import java.awt.Color
import java.awt.GridLayout
import java.security.cert.CertificateParsingException
import java.util.Date
import java.util.EnumSet
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import main.FactoryInterface
import org.opcfoundation.ua.core.ApplicationDescription
import org.opcfoundation.ua.transport.security.Cert
import org.opcfoundation.ua.utils.CertificateUtils

/**
 * This class offer a listener for a certificate following OPC UA standard
 * and a UI (PopUp) to valid or not the certificate
 * 
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com 
 */
class MyCertificateValidationListener implements CertificateValidationListener {

	/**
	 * @param certificate The certificate sent by a server. 
	 * @param applicattionDescription The description of the OPC UA server
	 * @param passedChecks The enumeration of if the Certificate follows OPC UA standard and is not false
	 * @return Return if user accepts or not the certificate (all time, one time, never)
	 */
	override onValidate(Cert certificate, ApplicationDescription applicationDescription,
		EnumSet<CertificateCheck> passedChecks) {

		if (passedChecks.containsAll(CertificateCheck.COMPULSORY))
			return ValidationResult.AcceptPermanently;

		var panel = new JPanel(new GridLayout(0, 1))
		panel.add(new JLabel("Subject   : " + certificate.getCertificate().getSubjectX500Principal()))
		panel.add(new JLabel("Issued by : " + certificate.getCertificate().getIssuerX500Principal()))
		panel.add(new JLabel("Valid from: " + certificate.getCertificate().getNotBefore()))
		panel.add(new JLabel("to: " + certificate.getCertificate().getNotAfter()))

		if (!passedChecks.contains(CertificateCheck.Signature)) {
			var notSign = new JLabel
			notSign.foreground = Color::RED
			notSign.text = "The Certificate is NOT SIGNED BY A TRUSTED SIGNER!"
			panel.add(notSign)
		}
		if (!passedChecks.contains(CertificateCheck.Validity)) {
			val today = new Date();
			val isOld = certificate.getCertificate().getNotAfter().compareTo(today) < 0;
			var oldOrYoung = ""
			if (isOld) {
				oldOrYoung = "anymore"
			} else {
				oldOrYoung = "yet"
			}

			var notDate = new JLabel
			notDate.foreground = Color::RED
			notDate.text = "* The Certificate time interval IS NOT VALID " + oldOrYoung + "!"
			panel.add(notDate)
		}
		if (!passedChecks.contains(CertificateCheck.Uri)) {
			val uriNotMatch = new JLabel
			val appDescription = new JLabel
			val appUriCertificate = new JLabel

			uriNotMatch.foreground = Color::RED
			appDescription.foreground = Color::RED
			appUriCertificate.foreground = Color::RED

			uriNotMatch.text = "* The Certificate URI DOES NOT MATCH the ApplicationDescription URI!"
			appDescription.text = "    ApplicationURI in ApplicationDescription = " +
				applicationDescription.applicationUri
			try {
				appUriCertificate.text = "    ApplicationURI in Certificate : " +
					CertificateUtils.getApplicationUriOfCertificate(certificate)
			} catch (CertificateParsingException e) {
				appUriCertificate.text = "    ApplicationURI in Certificate is INVALID"
			}
		}
		if (passedChecks.contains(CertificateCheck.SelfSigned)) {
			var selfSign = new JLabel
			selfSign.foreground = Color::BLUE
			selfSign.text = "* The Certificate is self-signed."
			panel.add(selfSign)
		}

		var options = newArrayOfSize(3)
		options.set(0, "Always")
		options.set(1, "This time")
		options.set(2, "Reject")
		// Create and display the pop-up
		var result = JOptionPane::showOptionDialog(null, panel, "Certificate server validation",
			JOptionPane::OK_CANCEL_OPTION, JOptionPane::PLAIN_MESSAGE, null, options, options.get(0))
		if (result == 0)
			return ValidationResult::AcceptPermanently
		if (result == 1)
			return ValidationResult::AcceptOnce
		else {
			FactoryInterface::SERVER_CONNECTION.text = "..."
			FactoryInterface::SERVER_ADRESS.text = "..."
			FactoryInterface::INFO.text = "Connection stop"
			return ValidationResult::Reject
		}
	}

}