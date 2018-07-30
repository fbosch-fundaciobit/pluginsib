<%@page import="org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest"%>
<%@page import="org.fundaciobit.plugins.validatesignature.api.CertificateInfo"%>
<%@page import="org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<%@ include file="/WEB-INF/views/html_header.jsp"%>

<un:useConstants var="ValidationStatus" className="org.fundaciobit.plugins.validatesignature.api.ValidationStatus" />

<div style="margin:10px;">
<br>

    <h3 class="tabs_involved"><fmt:message key="validatesignature.final.msg1" /></h3>
    <fmt:message key="validatesignature.final.msg2" />
    <table class="table table-bordered" >

       <%
       
    //   ValidateSignatureRequest validateRequest;
    //   validateRequest.signatureRequestedInformation
  //ValidateSignatureResponse validateResponse;
  //validateResponse.getValidationStatus().getErrorMsg()
    //   CertificateInfo ci;
  //validateResponse.getSignatureDetailInfo()[0].getCertificateInfo().
       
       %>
        
            <c:set var="response" value="${infoGlobal.validateResponse}" ></c:set>
            <c:set var="request" value="${infoGlobal.validateRequest}" ></c:set>
            
            <tr>
                <td>
                   <h4>Petici&oacute;</h4>
                   
                  Retornar informació de Tipus, Format i Perfil: <b>${request.signatureRequestedInformation.returnSignatureTypeFormatProfile}</b> <br/>
                  Validate Certificate Revocation: <b>${request.signatureRequestedInformation.validateCertificateRevocation}</b> <br/>
                  Return Certificate Info: <b>${request.signatureRequestedInformation.returnCertificateInfo}</b> <br/>
                  Return Validation Checks: <b>${request.signatureRequestedInformation.returnValidationChecks}</b> <br/>
                  Return Certificates: <b>${request.signatureRequestedInformation.returnCertificates}</b> <br/>
                  Return TimeStamp Info: <b>${request.signatureRequestedInformation.returnTimeStampInfo}</b>
              </td>
            </tr>
            
            <tr>
                <td>
                   
                  <c:set var="status" value="${response.validationStatus}" ></c:set>
                
                  <h4>Estat Validaci&oacute;</h4>
                  

                  Status Number: <b>${status.status}</b> <br/>
                  Estat:
                  <c:choose>
                     <c:when test = "${status.status == ValidationStatus.SIGNATURE_INVALID}">
                        <b style="color:red">INVALID</b><br/>
                        Ra&oacute: <b style="color:red">${status.errorMsg}</b>
           
                     </c:when>
                     <c:when test = "${status.status == ValidationStatus.SIGNATURE_VALID}">
                       <b>VALID</b>
                     </c:when>                     
                     <c:otherwise>
                        Estat Desconegut
                     </c:otherwise>
                  </c:choose>
                  <br/>                  
                </td>
                
            </tr>
            
            <%--DADES BÀSIQUES  --%>
            <tr>
                <td>
                  <h4>Dades Bàsiques</h4>
                      <ul>
                         <li>Tipus: <b>${response.signType }</b></li>
                         <li>Format: <b>${response.signFormat }</b></li>
                         <li>Perfil: <b>${response.signProfile }</b></li>                                 
                      </ul>
                </td>
            </tr>
            
            
            
            <%-- DETALLS DE LES FIRMES  --%>
            <c:set var="arraySignatures" value="${response.signatureDetailInfo}" ></c:set>
            <c:if test="${ not empty arraySignatures }">
            <tr>
                <td>
                  <h4>Informaci&oacute; de les Firmes</h4>
                  <table class="table">
                     <tr>
                     <%
                     /*
                     ValidateSignatureResponse validateResponse;
                      validateResponse.getSignatureDetailInfo()[0].getAlgorithm()
                      validateResponse.getSignatureDetailInfo()[0].getDigestValue()
                      
                      validateResponse.getSignatureDetailInfo()[0].getPolicyIdentifier()
                      
                      validateResponse.getSignatureDetailInfo()[0].getTimeStampInfo()

                      validateResponse.getSignatureDetailInfo()[0].getTimeStampInfo().getAlgorithm()
                      validateResponse.getSignatureDetailInfo()[0].getTimeStampInfo().getCertificateIssuer()
                      validateResponse.getSignatureDetailInfo()[0].getTimeStampInfo().getCertificateSubject()
                      validateResponse.getSignatureDetailInfo()[0].getTimeStampInfo().getCreationTime()
                      */
                       %>  
                      <c:forEach items="${arraySignatures}" var="infosign" varStatus="index">
                    
                            <td>
                              <h5>Firma ${index.count}</h5>
                              
                              <%--  DADES GENERALS --%>
                              <b>Dades Generals</b> <br/>
                              <ul>
                                 <li>Algorithm: <b>${infosign.algorithm }</b></li>
                                 <li>DigestValue: <b>${infosign.digestValue }</b></li>
                                 <li>PolicyIdentifier: <b>${infosign.policyIdentifier }</b></li>                                 
                              </ul>
                             
                              <%-- INFO TIMESTAMP --%>
                             <c:set var="timestamp" value="${infosign.timeStampInfo}" ></c:set>
                             <c:if test="${ not empty timestamp }">
                             <b>TimeStamp Info</b>
                             <ul>
                                 <li>TS::Algorithm: <b>${timestamp.algorithm }</b></li>
                                 <li>TS::CertificateIssuer: <b>${timestamp.certificateIssuer }</b></li>
                                 <li>TS::CertificateSubject: <b>${timestamp.certificateSubject }</b></li>
                                 <li>TS::CreationTime: <b><fmt:formatDate value="${timestamp.creationTime }" pattern="d-MM-yyyy HH:mm:ss"></fmt:formatDate></b></li>
                             </ul>
                             </c:if>
                              
                              
                              <%--  Informació del certificat --%>
                              <c:set var="infoCert" value="${infosign.certificateInfo}" ></c:set>
                              
 
                            <c:if test="${ not empty infoCert }">
                            <b>CertificateInfo</b> <br/>
                            <ul>
                            <c:if test="${ not empty infoCert.apellidosResponsable}">
                               <li>apellidosResponsable:<b> ${ infoCert.apellidosResponsable}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.organizacionEmisora}">
                               <li>organizacionEmisora:<b> ${ infoCert.organizacionEmisora}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.segundoApellidoResponsable}">
                               <li>segundoApellidoResponsable:<b> ${ infoCert.segundoApellidoResponsable}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.versionPolitica}">
                               <li>versionPolitica:<b> ${ infoCert.versionPolitica}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.usoCertificado}">
                               <li>usoCertificado:<b> ${ infoCert.usoCertificado}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.pais}">
                               <li>pais:<b> ${ infoCert.pais}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.subject}">
                               <li>subject:<b> ${ infoCert.subject}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.numeroSerie}">
                               <li>numeroSerie:<b> ${ infoCert.numeroSerie}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.primerApellidoResponsable}">
                               <li>primerApellidoResponsable:<b> ${ infoCert.primerApellidoResponsable}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.nombreApellidosResponsable}">
                               <li>nombreApellidosResponsable:<b> ${ infoCert.nombreApellidosResponsable}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.validoDesde}">
                               <li>validoDesde:<b> ${ infoCert.validoDesde}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.validoHasta}">
                               <li>validoHasta:<b> ${ infoCert.validoHasta}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.idPolitica}">
                               <li>idPolitica:<b> ${ infoCert.idPolitica}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.idEuropeo}">
                               <li>idEuropeo:<b> ${ infoCert.idEuropeo}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.tipoCertificado}">
                               <li>tipoCertificado:<b> ${ infoCert.tipoCertificado}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.email}">
                               <li>email:<b> ${ infoCert.email}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.clasificacion}">
                               <li>clasificacion:<b> ${ infoCert.clasificacion}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.idEmisor}">
                               <li>idEmisor:<b> ${ infoCert.idEmisor}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.nifResponsable}">
                               <li>nifResponsable:<b> ${ infoCert.nifResponsable}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.extensionUsoCertificado}">
                               <li>extensionUsoCertificado:<b> ${ infoCert.extensionUsoCertificado}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.nombreResponsable}">
                               <li>nombreResponsable:<b> ${ infoCert.nombreResponsable}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.politica}">
                               <li>politica:<b> ${ infoCert.politica}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.nifEntidadSuscriptora}">
                               <li>nifEntidadSuscriptora:<b> ${ infoCert.nifEntidadSuscriptora}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.entidadSuscriptora}">
                               <li>entidadSuscriptora:<b> ${ infoCert.entidadSuscriptora}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.unidadOrganizativa}">
                               <li>unidadOrganizativa:<b> ${ infoCert.unidadOrganizativa}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.numeroIdentificacionPersonal}">
                               <li>numeroIdentificacionPersonal:<b> ${ infoCert.numeroIdentificacionPersonal}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.organizacion}">
                               <li>organizacion:<b> ${ infoCert.organizacion}</b></li>
                            </c:if>
                            
                            <c:if test="${ not empty infoCert.puesto}">
                               <li>puesto:<b> ${ infoCert.puesto}</b></li>
                            </c:if>

                            </ul>
                              
                            </c:if>

                            </td>                    
                    </c:forEach>
                    </tr>
               </table>
    
            </td>
                
            </tr>
            </c:if>
    </table>
    

   
    <a href="<c:url value="/" />" class="btn"><fmt:message key="tornar"/></a>
   
</div>

<%@ include file="/WEB-INF/views/html_footer.jsp"%>