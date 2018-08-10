<%@ include file="/WEB-INF/views/html_header.jsp"
%><%@page language="java" pageEncoding="UTF-8"
%><%@page language="java" contentType="text/html;charset=UTF-8" 
%>

<h3 class="tabs_involved">
    &nbsp;&nbsp;&nbsp;
    <fmt:message key="exempletester" />
</h3>

<form:form modelAttribute="validateSignatureForm" method="post" enctype="multipart/form-data">

    <div style="margin: 20px 20px 20px 20px;" style="width:auto;">

        <div class="module_content" style="width: auto;">
            <div class="tab_container" style="width: auto;">

                <table
                    class="tdformlabel table-condensed table table-bordered table-striped marTop10"
                    style="width: auto;">
                    <tbody>
                    
                        <tr>
                            <td><label>Plugin &nbsp;(*)</label></td>
                            <td><form:errors path="pluginID" cssClass="errorField alert alert-error" />
                            <form:select path="pluginID">
                               <c:forEach items="${plugins}" var="plugin" varStatus="index">
                                    <form:option value="${plugin.pluginID}">${plugin.nom}</form:option>
                               </c:forEach>
                             </form:select></td>
                        </tr>

                       <tr>
                            <td><label><fmt:message key="idiomaui" /> &nbsp;(*)</label></td>
                            <td><form:errors path="langUI"
                                    cssClass="errorField alert alert-error" /> <form:select
                                    path="langUI">
                                    <form:option value="ca" selected="true">Catal&agrave;</form:option>
                                    <form:option value="es">Castell&agrave;</form:option>
                                </form:select></td>
                        </tr>
                        
                        <tr>
                            <td><label><fmt:message
                                        key="document" /> </label></td>
                            <td><form:errors path="document"
                                    cssClass="errorField alert alert-error" /> <form:input
                                    path="document" type="file" /></td>
                        </tr>
                        
                        <tr>
                            <td><label><fmt:message
                                        key="signatura" /></label></td>
                            <td><form:errors path="signature"
                                    cssClass="errorField alert alert-error" /> <form:input
                                    path="signature" type="file" /></td>
                        </tr>
                        

                        <tr>
                            <td><label>Retornar informació de Tipus, Format i Perfil</label></td>
                            <td>
                              <form:errors path="returnSignatureTypeFormatProfile"  cssClass="errorField alert alert-error" /> 
                              <form:checkbox path="returnSignatureTypeFormatProfile" />
                            </td>
                        </tr>
                        
                        <tr>
                            <td><label>Validate Certificate Revocation</label></td>
                            <td>
                              <form:errors path="validateCertificateRevocation"  cssClass="errorField alert alert-error" /> 
                              <form:checkbox path="validateCertificateRevocation" />
                            </td>
                        </tr>
                        
                        <tr>
                            <td><label>Return Certificate Info </label></td>
                            <td>
                              <form:errors path="returnCertificateInfo"  cssClass="errorField alert alert-error" /> 
                              <form:checkbox path="returnCertificateInfo" />
                            </td>
                        </tr>
                        
                        <tr>
                            <td><label>Return Validation Checks </label></td>
                            <td>
                              <form:errors path="returnValidationChecks"  cssClass="errorField alert alert-error" /> 
                              <form:checkbox path="returnValidationChecks" />
                            </td>
                        </tr>
                        
                        <tr>
                            <td><label>Return Certificates</label></td>
                            <td>
                              <form:errors path="returnCertificates"  cssClass="errorField alert alert-error" /> 
                              <form:checkbox path="returnCertificates" />
                            </td>
                        </tr>
                        
                        <tr>
                            <td><label>Return TimeStamp Info</label></td>
                            <td>
                              <form:errors path="returnTimeStampInfo"  cssClass="errorField alert alert-error" /> 
                              <form:checkbox path="returnTimeStampInfo" />
                            </td>
                        </tr>

                    </tbody>
                </table>

                <div style="text-align:center; width:100%">

                    <input id="submitbutton2" name="esborrarcache"
                        type="submit" class="btn btn-warn"
                        value="Esborrar Cache de Plugins">
                    &nbsp;&nbsp;&nbsp;  
                    <input id="submitbutton" name="validar" type="submit"
                        class="btn btn-primary"
                        value="<fmt:message key="validar"/>"> 

                </div>

            </div>
            <%-- Final DIV OPCIONS --%>

        </div>

    </div>

</form:form>

<%@ include file="/WEB-INF/views/html_footer.jsp"%>
