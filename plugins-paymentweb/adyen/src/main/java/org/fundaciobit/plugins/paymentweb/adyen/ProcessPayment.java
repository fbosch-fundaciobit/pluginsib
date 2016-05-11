package org.fundaciobit.plugins.paymentweb.adyen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.NavigableMap;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;



/**
 * 
 * @author anadal
 *
 */
public class ProcessPayment  {
  
  
  
  public static  void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      
      String transactionID = "" + System.currentTimeMillis();
      
  String html = "<html>\n"
 + "<body>\n"
 + " <h1>Pagament</h1>\n"
 + " <form method=\"POST\">\n"
 + "     <input type=\"text\"  name=\"merchantReference\" value=\"Payment" + transactionID + "\" /> <br/>\n"
 + "     <input type=\"text\" name=\"paymentAmount\" value=\"1234\" /> <br/>\n"
 + "     <input type=\"text\" name=\"currencyCode\" value=\"EUR\" /> <br/>\n"
 + "   <input type=\"text\" name=\"shopperLocale\" value=\"es_ES\" /> <br/>\n"
 + "   <input type=\"text\" name=\"transactionID\" value=\"" + transactionID + "\" /> <br/><br/>\n"
   
 + "      <input type=\"submit\" name=\"Pagar\" value=\"pagar\" />\n"
     + " </form>\n"
     + " </body>\n"
     + " </html>\n";

  response.getWriter().print(html);
    
  }

  /*
         // Generate date
     Calendar calendar = Calendar.getInstance();
     Date currentDate = calendar.getTime(); // current date
     calendar.add(Calendar.DATE, 1);
     Date sessionDate = calendar.getTime(); // current date + 1 day
     calendar.add(Calendar.DATE, 2);
     Date shippingDate = calendar.getTime(); // current date + 3 days
     
     final byte[] hmacKey =  BaseEncoding.base16().decode("337C7FD976B75D22A64B76F40D51A85CFD110D4552DDF0DBBE83645E0483AE81");
     final String skinCode = "1QcLkuoN";
     final String merchantAccount = "UnsomnitSLCOM";
     final String shopperEmail = "esporleri@gmail.com";

     // Define variables
     
     String paymentAmount = request.getParameter("paymentAmount"); 
     String currencyCode = request.getParameter("currencyCode"); // "EUR";
     String shopperLocale = request.getParameter("shopperLocale"); // "es_ES";

     String transactionID = request.getParameter("transactionID"); // "es_ES";
         String merchantReturnData ="transactionID_" + transactionID;
     
     
     String shipBeforeDate = new SimpleDateFormat("yyyy-MM-dd").format(shippingDate);
     String sessionValidity = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(sessionDate);

     // Calculate merchant signature
     String[] params = new String[] {
       currencyCode,
       merchantAccount,
       merchantReference,
       merchantReturnData,
       paymentAmount, 
       sessionValidity,
       shipBeforeDate,
       shopperEmail, 
       skinCode
     };
     
     String signingString = "";
     for(int i = 0; i < params.length; i++) {
       if(i != 0) {
         signingString = signingString + ":";
       }
       signingString = signingString + escapeVal(params[i]) ;
     }
     

     String merchantSig;
     try {
       merchantSig = calculateHMAC(hmacKey, signingString);
     } catch (Exception e) {
       throw new Exception(e);
     }
 %>
 <html>
 <body>
 <form action="https://test.adyen.com/hpp/pay.shtml" method="POST">
   <input type="text" name="paymentAmount" value="<%=paymentAmount%>" /> <br/>
   <input type="text" name="currencyCode" value="<%=currencyCode%>" /> <br/>
   <input type="text" name="shipBeforeDate" value="<%=shipBeforeDate%>" /> <br/>
   <%--
     <input type="text"  name="shopperLocale" value="<%=shopperLocale%>" /> <br/>
   --%>
     <input type="text" name="merchantReference" value="<%=merchantReference%>" /> <br/>
   <input type="text" name="skinCode" value="<%=skinCode%>" /> <br/>
     <input type="text" name="merchantAccount" value="<%=merchantAccount%>" /> <br/>
     <input type="text" name="sessionValidity" value="<%=sessionValidity%>" /> <br/>
     <input type="text" name="shopperEmail" value="<%=shopperEmail%>"/> <br/>
     <input type="text" name="merchantReturnData" value="<%=merchantReturnData%>"/> <br/>
   <input type="text" name="merchantSig" value="<%=merchantSig%>" /> <br/>
     <input type="text" name="resURL" value="http://localhost:8080/aplec/adyent/result.jsp"/> <br/>
   
   <input type ="submit" name="submit" value="Submit Button" />
 </form>
 <% } %>

 </body>
 </html>
  }
  
  */
  

	public static  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/**
		 * General HPP settings
		 * - hppUrl: URL of the Adyen HPP to submit the form to
		 * - hmacKey: shared secret key used to encrypt the signature
		 * 
		 * Both variables are dependent on the environment which should be used (Test/Live).
		 * HMAC key can be set up: Adyen CA >> Skins >> Choose your Skin >> Edit Tab >> Edit HMAC key for Test & Live.
		 */

		
		// Generate dates
		Calendar calendar = Calendar.getInstance();
		//Date currentDate = calendar.getTime(); // current date
		calendar.add(Calendar.DATE, 1);
		Date sessionDate = calendar.getTime(); // current date + 1 day
		calendar.add(Calendar.DATE, 2);
		Date shippingDate = calendar.getTime(); // current date + 3 days
		
		
		final String hmacKey =  "UNSOMNIT";
    final String skinCode = "1QcLkuoN";
    final String merchantAccount = "UnsomnitSLCOM";
    final String shopperEmail = "esporleri@gmail.com";
    final String returnURL = "http://localhost:8080/adyen/return.jsp";

    // Define variables
    
    String paymentAmount = request.getParameter("paymentAmount"); 
    String currencyCode = request.getParameter("currencyCode"); // "EUR";
    String shopperLocale = request.getParameter("shopperLocale"); // "es_ES";

    final String allowedMethods="card"; // card
    // XXXX
    String transactionID =  request.getParameter("transactionID"); // "1458847705573"; //

    
    
    String merchantReturnData ="transactionID_" + transactionID;
    
		

		// Define variables
		String merchantReference = "Inscripcio_Aplec_" + transactionID;
		
		String shipBeforeDate = new SimpleDateFormat("yyyy-MM-dd").format(shippingDate);
		
		// TODO XYZ "2016-03-25T20:28:28+00:00";
		String sessionValidity =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+01:00").format(sessionDate);
		
		//String orderData = compressString("Orderdata to display on the HPP can be put here");
		String countryCode = "ES";
		String shopperReference = "POSAR NOM COMPRADOR";
		//String allowedMethods = "";
		//String blockedMethods = "";
//		String offset = "";
//		String brandCode ="klarna";
//		String issuerId = "";
		

	
		System.out.println(" paymentAmount: |"+ paymentAmount + "|");
		System.out.println(" currencyCode: |" + currencyCode + "|");
		System.out.println(" shipBeforeDate: |" + shipBeforeDate +"|");
		System.out.println(" merchantReference: |" + merchantReference + "|");
		System.out.println(" skinCode: |" + skinCode +"|");
		System.out.println(" merchantAccount: |" + merchantAccount + "|");
		System.out.println(" sessionValidity: |" + sessionValidity +"|");
		System.out.println(" shopperEmail: |" + shopperEmail +"|");
		System.out.println(" shopperReference: |" + shopperReference + "|");
		System.out.println(" merchantReturnData: |" + merchantReturnData + "|");

		
		// Signing the open invoice data
		// merchantSig
		String signingString = 
				paymentAmount + 
				currencyCode + 
				shipBeforeDate + 
				merchantReference + 
				skinCode +
				merchantAccount + 
				sessionValidity + 
				shopperEmail + 
				shopperReference +
				allowedMethods +
				merchantReturnData;
//				allowedMethods + 
//				blockedMethods +
//				billingAddress.get("billingAddressType") +
//				deliveryAddress.get("deliveryAddressType") +
//				shopper.get("shopperType") + 
//				offset;
		
		String merchantSig;
		try {
			merchantSig = calculateHMAC(hmacKey, signingString);
		} catch (GeneralSecurityException e) {
			throw new ServletException(e);
		}
		
		System.out.println(" signingString: |" + signingString + "|");
		System.out.println(" merchantSig: |" + merchantSig + "|");
		
		
		
		// ================================================
		/*
	// Open invoice data signing string
    // adding the merchantSig to the sortedMap
		HashMap<String, String> invoiceLines = new HashMap<String, String>();
    Map<String, String> mapInvoiceLines = invoiceLines;
    TreeMap<String, String> sortedMap = new TreeMap<String, String>(mapInvoiceLines);
    sortedMap.put("merchantSig", merchantSig);
  
    // open invoice signing string
    String signingOpeninvoicedata = couplingKeys(sortedMap);
        
    // invoice lines signature
    String openinvoicedataSig;
    try {
      openinvoicedataSig = calculateHMAC(hmacKey, signingOpeninvoicedata);
    } catch (GeneralSecurityException e) {
      throw new ServletException(e);
    }
        
    // adding the openinvoicedata.sig to the hasMap invoice lines
    invoiceLines.put("openinvoicedata.sig", openinvoicedataSig);
    */
		
 // ================================================
				
		/*

	
		// Set request parameters for use on the JSP page
		request.setAttribute("hppUrl", hppUrl);
		request.setAttribute("merchantReference", merchantReference);
		request.setAttribute("paymentAmount", paymentAmount);
		request.setAttribute("currencyCode", currencyCode);
		request.setAttribute("shipBeforeDate", shipBeforeDate);
		request.setAttribute("skinCode", skinCode);
		request.setAttribute("merchantAccount", merchantAccount);
		request.setAttribute("sessionValidity", sessionValidity);
		request.setAttribute("shopperLocale", shopperLocale);
		request.setAttribute("orderData", orderData);
		request.setAttribute("countryCode", countryCode);
		request.setAttribute("shopperEmail", shopperEmail);
		request.setAttribute("shopperReference", shopperReference);
		request.setAttribute("allowedMethods", allowedMethods);
		request.setAttribute("blockedMethods", blockedMethods);
		request.setAttribute("offset", offset);
		request.setAttribute("merchantSig", merchantSig);
		request.setAttribute("brandCode", brandCode);
		request.setAttribute("issuerId", issuerId);
	
		*/
		
    
    

    
    // setting attributes from invoice lines to the request
//    String additionalInputs="";
//    Set<Entry<String, String>> invoiceLineshashSet = invoiceLines.entrySet();
//    for(Entry<String, String> entry: invoiceLineshashSet){
//      additionalInputs = additionalInputs  //request.setAttribute( entry.getKey(), entry.getValue());
//          + "     <input type=\"text\" name=\"" + entry.getKey() + "\" value=\"" + entry.getValue()  + "\" /> <br/>\n";
//    }
//    
    
    
    
    
    
    
    
    
		// Set correct character encoding
		response.setCharacterEncoding("UTF-8");

//		



 




//     +

		
		String html = 
		 "<html>\n"
     + "<body>\n"
     + " <form action=\"https://test.adyen.com/hpp/pay.shtml\" method=\"POST\" >\n"
//   paymentAmount + 
     + "	   <input type=\"text\" name=\"paymentAmount\" value=\"" + paymentAmount + "\" /> <br/>\n"
     //    currencyCode + 
     + "    <input type=\"text\" name=\"currencyCode\" value=\"" + currencyCode + "\" /> <br/>\n"
     //    shipBeforeDate + 
     + "     <input type=\"text\" name=\"shipBeforeDate\" value=\"" + shipBeforeDate + "\" /> <br/>\n"
     //    merchantReference +
    + "     <input type=\"text\" name=\"merchantReference\" value=\"" + merchantReference + "\" /> <br/>\n"
    //    skinCode +
    + "    <input type=\"text\" name=\"skinCode\" value=\"" + skinCode + "\" /> <br/>\n"
    //    merchantAccount + 
    + "     <input type=\"text\" name=\"merchantAccount\" value=\"" + merchantAccount + "\" /> <br/>\n"
    //    sessionValidity + 
    + "     <input type=\"text\" name=\"sessionValidity\" value=\"" + sessionValidity + "\" /> <br/>\n"
    //    shopperEmail + 
    + "     <input type=\"text\" name=\"shopperEmail\" value=\"" + shopperEmail + "\" /> <br/>\n"
    
    // + allowedMethods
    + "     <input type=\"text\" name=\"allowedMethods\" value=\"" + allowedMethods + "\" /> <br/>\n"
    
    
    //   shopperLocale
    + "     <input type=\"text\" name=\"shopperLocale\" value=\"" + shopperLocale + "\" /> <br/>\n"
    
    // shopperReference
    + "     <input type=\"text\" name=\"shopperReference\" value=\"" + shopperReference + "\" /> <br/>\n"
    // countryCode
    + "     <input type=\"text\" name=\"countryCode\" value=\"" + countryCode + "\" /> <br/>\n"
    
    
    //    merchantReturnData;
    + "    <input type=\"text\" name=\"merchantReturnData\" value=\"" + merchantReturnData + "\" /> <br/>\n"
    
    
    //+ "     <input type=\"text\" name=\"signingString\" value=\"" + signingString + "\" /> <br/>\n"
    + "     <input type=\"text\" name=\"merchantSig\" value=\"" + merchantSig + "\" /> <br/>\n"
    + "     <input type=\"text\" name=\"resURL\" value=\"" + returnURL + "\" /> <br/>\n"
    //+ additionalInputs
    + "<input type =\"submit\" name=\"submit\" value=\"Submit Button\" />\n"
        + "</form>\n"
		 + " </body>\n"
     + " </html>\n";

  response.getWriter().print(html);
	}

	/**
	 * Generates GZIP compressed and Base64 encoded string.
	 */
	/*
	private String compressString(String input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(output);

		gzip.write(input.getBytes("UTF-8"));
		gzip.close();
		output.close();

		return Base64.encodeBase64String(output.toByteArray());
	}
	*/

	/**
	 * Computes the Base64 encoded signature using the HMAC algorithm with the SHA-1 hashing function.
	 */
	public static String calculateHMAC(String hmacKey, String signingString) throws GeneralSecurityException, UnsupportedEncodingException {
		SecretKeySpec keySpec = new SecretKeySpec(hmacKey.getBytes(), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);

		byte[] result = mac.doFinal(signingString.getBytes("UTF-8"));
		return Base64.encodeBase64String(result);
	}

	/** 
	 * Coupling the key pairs to sign the open invoice data over
	 * @param sortedMap of the key value pairs that need to be coupled
	 * @return String of keys
	 */
	
	public static  String couplingKeys(NavigableMap<String, String> sortedMap){
		
		// coupling key names
		String keys = "";
		
		// Set lastItem variable to check when at last item
		Entry<String, String> lastItem = sortedMap.lastEntry();
						
		for (Entry<String, String> entry : sortedMap.entrySet()) {
			if(!lastItem.equals(entry)) {
				keys += entry.getKey().trim() + ":";
			} else{
				keys += entry.getKey().trim() + "|";
			}
		}
		
		for (Entry<String, String> entry : sortedMap.entrySet()) {
			if(!lastItem.equals(entry)) {
				keys += entry.getValue().trim() + ":";
			} else{
				keys += entry.getValue().trim() ;
			}
		}
		
		return keys;
	} 
	

}
