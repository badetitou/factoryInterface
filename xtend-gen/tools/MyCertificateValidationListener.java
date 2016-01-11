package tools;

import com.prosysopc.ua.CertificateValidationListener;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import java.awt.Color;
import java.awt.GridLayout;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.EnumSet;
import javax.security.auth.x500.X500Principal;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import main.FactoryInterface;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.utils.CertificateUtils;

/**
 * This class offer a listener for a certificate following OPC UA standard
 * and a UI (PopUp) to valid or not the certificate
 * 
 * @author Benoît Verhaeghe - benoit.verhaeghe59@gmail.com
 */
@SuppressWarnings("all")
public class MyCertificateValidationListener implements CertificateValidationListener {
  /**
   * @param certificate The certificate sent by a server.
   * @param applicattionDescription The description of the OPC UA server
   * @param passedChecks The enumeration of if the Certificate follows OPC UA standard and is not false
   * @return Return if user accepts or not the certificate (all time, one time, never)
   */
  @Override
  public PkiFileBasedCertificateValidator.ValidationResult onValidate(final Cert certificate, final ApplicationDescription applicationDescription, final EnumSet<PkiFileBasedCertificateValidator.CertificateCheck> passedChecks) {
    boolean _containsAll = passedChecks.containsAll(PkiFileBasedCertificateValidator.CertificateCheck.COMPULSORY);
    if (_containsAll) {
      return PkiFileBasedCertificateValidator.ValidationResult.AcceptPermanently;
    }
    GridLayout _gridLayout = new GridLayout(0, 1);
    JPanel panel = new JPanel(_gridLayout);
    X509Certificate _certificate = certificate.getCertificate();
    X500Principal _subjectX500Principal = _certificate.getSubjectX500Principal();
    String _plus = ("Subject   : " + _subjectX500Principal);
    JLabel _jLabel = new JLabel(_plus);
    panel.add(_jLabel);
    X509Certificate _certificate_1 = certificate.getCertificate();
    X500Principal _issuerX500Principal = _certificate_1.getIssuerX500Principal();
    String _plus_1 = ("Issued by : " + _issuerX500Principal);
    JLabel _jLabel_1 = new JLabel(_plus_1);
    panel.add(_jLabel_1);
    X509Certificate _certificate_2 = certificate.getCertificate();
    Date _notBefore = _certificate_2.getNotBefore();
    String _plus_2 = ("Valid from: " + _notBefore);
    JLabel _jLabel_2 = new JLabel(_plus_2);
    panel.add(_jLabel_2);
    X509Certificate _certificate_3 = certificate.getCertificate();
    Date _notAfter = _certificate_3.getNotAfter();
    String _plus_3 = ("to: " + _notAfter);
    JLabel _jLabel_3 = new JLabel(_plus_3);
    panel.add(_jLabel_3);
    boolean _contains = passedChecks.contains(PkiFileBasedCertificateValidator.CertificateCheck.Signature);
    boolean _not = (!_contains);
    if (_not) {
      JLabel notSign = new JLabel();
      notSign.setForeground(Color.RED);
      notSign.setText("The Certificate is NOT SIGNED BY A TRUSTED SIGNER!");
      panel.add(notSign);
    }
    boolean _contains_1 = passedChecks.contains(PkiFileBasedCertificateValidator.CertificateCheck.Validity);
    boolean _not_1 = (!_contains_1);
    if (_not_1) {
      final Date today = new Date();
      X509Certificate _certificate_4 = certificate.getCertificate();
      Date _notAfter_1 = _certificate_4.getNotAfter();
      int _compareTo = _notAfter_1.compareTo(today);
      final boolean isOld = (_compareTo < 0);
      String oldOrYoung = "";
      if (isOld) {
        oldOrYoung = "anymore";
      } else {
        oldOrYoung = "yet";
      }
      JLabel notDate = new JLabel();
      notDate.setForeground(Color.RED);
      notDate.setText((("* The Certificate time interval IS NOT VALID " + oldOrYoung) + "!"));
      panel.add(notDate);
    }
    boolean _contains_2 = passedChecks.contains(PkiFileBasedCertificateValidator.CertificateCheck.Uri);
    boolean _not_2 = (!_contains_2);
    if (_not_2) {
      final JLabel uriNotMatch = new JLabel();
      final JLabel appDescription = new JLabel();
      final JLabel appUriCertificate = new JLabel();
      uriNotMatch.setForeground(Color.RED);
      appDescription.setForeground(Color.RED);
      appUriCertificate.setForeground(Color.RED);
      uriNotMatch.setText("* The Certificate URI DOES NOT MATCH the ApplicationDescription URI!");
      String _applicationUri = applicationDescription.getApplicationUri();
      String _plus_4 = ("    ApplicationURI in ApplicationDescription = " + _applicationUri);
      appDescription.setText(_plus_4);
      try {
        String _applicationUriOfCertificate = CertificateUtils.getApplicationUriOfCertificate(certificate);
        String _plus_5 = ("    ApplicationURI in Certificate : " + _applicationUriOfCertificate);
        appUriCertificate.setText(_plus_5);
      } catch (final Throwable _t) {
        if (_t instanceof CertificateParsingException) {
          final CertificateParsingException e = (CertificateParsingException)_t;
          appUriCertificate.setText("    ApplicationURI in Certificate is INVALID");
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    boolean _contains_3 = passedChecks.contains(PkiFileBasedCertificateValidator.CertificateCheck.SelfSigned);
    if (_contains_3) {
      JLabel selfSign = new JLabel();
      selfSign.setForeground(Color.BLUE);
      selfSign.setText("* The Certificate is self-signed.");
      panel.add(selfSign);
    }
    String[] options = new String[3];
    options[0] = "Always";
    options[1] = "This time";
    options[2] = "Reject";
    Object _get = options[0];
    int result = JOptionPane.showOptionDialog(null, panel, "Certificate server validation", 
      JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, _get);
    if ((result == 0)) {
      return PkiFileBasedCertificateValidator.ValidationResult.AcceptPermanently;
    }
    if ((result == 1)) {
      return PkiFileBasedCertificateValidator.ValidationResult.AcceptOnce;
    } else {
      FactoryInterface.SERVER_CONNECTION.setText("...");
      FactoryInterface.SERVER_ADRESS.setText("...");
      FactoryInterface.INFO.setText("Connection stop");
      return PkiFileBasedCertificateValidator.ValidationResult.Reject;
    }
  }
}
