package org.fundaciobit.plugins.barcode.qrcode;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;

import org.fundaciobit.plugins.barcode.IBarcodePlugin;
import org.fundaciobit.plugins.userinformation.UserInfo;

import com.itextpdf.text.pdf.BarcodeQRCode;

/**
 * 
 * 
 * @author anadal
 */
public class QrCodePlugin implements IBarcodePlugin {

  @Override
  public String getName() {
    return "QrCode";
  }
  
  @Override
  public boolean isSupportedType(int type) throws Exception {
    switch (type) {
    case TYPE_TEXT:
    case TYPE_URL:
    case TYPE_USER_INFO:
      return true;

    default:
      return false;
    }
  }

  @Override
  public Image generateTextBarCode(String text) throws Exception {
    if (text == null) {
      return null;
    }

    BarcodeQRCode qrcode = new BarcodeQRCode(text, 1, 1, null);

    java.awt.Image img = qrcode.createAwtImage(Color.BLACK, Color.WHITE);

    return img;

  }

  @Override
  public Image generateUrlBarCode(URL url) throws Exception {
    if (url == null) {
      return null;
    }
    return generateTextBarCode(url.toExternalForm());
  }


  @Override
  public Image generateUserInfoBarCode(UserInfo userInfo)  throws Exception {
    if (userInfo == null) {
      return null;
    }
    VCard vcard = userInfo2VCard(userInfo);

    return generateTextBarCode(vcard.toString());

  }

  private VCard userInfo2VCard(UserInfo userInfo) {
    VCard vcard = new VCard();
    String fullName = userInfo.getFullName();

    //System.out.println(" NOM ===== ]" + fullName + "[");

    vcard.setName(fullName);

    if (userInfo.getEmail() != null) {
      vcard.setEmail(userInfo.getEmail());
    }
    if (userInfo.getAddress() != null) {
      vcard.setAddress(userInfo.getAddress());
    }

    if (userInfo.getCompany() != null) {
      vcard.setCompany(userInfo.getCompany());
    }

    if (userInfo.getAddress() != null) {
      vcard.setAddress(userInfo.getAddress());
    }

    if (userInfo.getPhoneNumber() != null) {
      vcard.setPhonenumber(userInfo.getPhoneNumber());
    }

    if (userInfo.getWebsite() != null) {
      vcard.setWebsite(userInfo.getWebsite());
    }
    return vcard;
  }

  @Override
  public Image generateBinaryBarCode(byte[] data) throws Exception {
    return null;
  }

  /**
   * A simple wrapper for vCard data to use with ZXing QR Code generator.
   * <p/>
   * See also http://zxing.appspot.com/generator/ and Contact Information
   * 
   * @author Frederik Hahne <atomfrede@gmail.com>
   */
  public class VCard {

    private static final String NAME = "N:";

    private static final String COMPANY = "ORG:";

    private static final String TITLE = "TITLE:";

    private static final String PHONE = "TEL:";

    private static final String WEB = "URL:";

    private static final String EMAIL = "EMAIL:";

    private static final String ADDRESS = "ADR:";

    private String name;

    private String company;

    private String title;

    private String phonenumber;

    private String email;

    private String address;

    private String website;

    public VCard() {
    }

    public VCard(String name) {
      this.name = name;
    }

    public VCard setName(String name) {
      this.name = name;
      return this;
    }

    public VCard setCompany(String company) {
      this.company = company;
      return this;
    }

    public VCard setPhonenumber(String phonenumber) {
      this.phonenumber = phonenumber;
      return this;
    }

    public VCard setTitle(String title) {
      this.title = title;
      return this;
    }

    public VCard setEmail(String email) {
      this.email = email;
      return this;
    }

    public VCard setAddress(String address) {
      this.address = address;
      return this;
    }

    public VCard setWebsite(String website) {
      this.website = website;
      return this;
    }

    /**
     * Returns the textual representation of this vcard of the form
     * <p/>
     * BEGIN:VCARD N:John Doe ORG:Company TITLE:Title TEL:1234
     * URL:www.example.org EMAIL:john.doe@example.org ADR:Street END:VCARD
     */
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("BEGIN:VCARD\n");
      if (name != null) {
        sb.append(NAME).append(name);
      }
      if (company != null) {
        sb.append("\n" + COMPANY).append(company);
      }
      if (title != null) {
        sb.append("\n" + TITLE).append(title);
      }
      if (phonenumber != null) {
        sb.append("\n" + PHONE).append(phonenumber);
      }
      if (website != null) {
        sb.append("\n" + WEB).append(website);
      }
      if (email != null) {
        sb.append("\n" + EMAIL).append(email);
      }
      if (address != null) {
        sb.append("\n" + ADDRESS).append(address);
      }
      sb.append("\nEND:VCARD");
      return sb.toString();
    }
  }

  @Override
  public com.itextpdf.text.Image generateTextBarCodeIText(String text) throws Exception {
    if (text == null) {
      return null;
    }
    BarcodeQRCode qrcode = new BarcodeQRCode(text, 1, 1, null);
    return qrcode.getImage();
  }

  @Override
  public com.itextpdf.text.Image generateUrlBarCodeIText(URL url) throws Exception {
    if (url == null) {
      return null;
    }
    return generateTextBarCodeIText(url.toExternalForm());
  }

  @Override
  public com.itextpdf.text.Image generateUserInfoBarCodeIText(UserInfo userInfo)
      throws Exception {
    if (userInfo == null) {
      return null;
    }
    VCard vcard = userInfo2VCard(userInfo);

    return generateTextBarCodeIText(vcard.toString());
  }

  @Override
  public com.itextpdf.text.Image generateBinaryBarCodeIText(byte[] data) throws Exception {
    return null;
  }

  @Override
  public boolean requireRotation() {
    return false;
  }

}
