
package es.caib.digital.ws.api.caratulas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.caib.digital.ws.api.caratulas package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetCaratulaResponse_QNAME = new QName("http://service.ws.digital.caib.es/", "getCaratulaResponse");
    private final static QName _GetCaratula_QNAME = new QName("http://service.ws.digital.caib.es/", "getCaratula");
    private final static QName _CaratulaReturnInfo_QNAME = new QName("http://service.ws.digital.caib.es/", "caratulaReturnInfo");
    private final static QName _ReturnInfo_QNAME = new QName("http://service.ws.digital.caib.es/", "returnInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.caib.digital.ws.api.caratulas
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReturnInfo }
     * 
     */
    public ReturnInfo createReturnInfo() {
        return new ReturnInfo();
    }

    /**
     * Create an instance of {@link GetCaratulaResponse }
     * 
     */
    public GetCaratulaResponse createGetCaratulaResponse() {
        return new GetCaratulaResponse();
    }

    /**
     * Create an instance of {@link MetaDato }
     * 
     */
    public MetaDato createMetaDato() {
        return new MetaDato();
    }

    /**
     * Create an instance of {@link CoverParams.CoverPDFValues }
     * 
     */
    public CoverParams.CoverPDFValues createCoverParamsCoverPDFValues() {
        return new CoverParams.CoverPDFValues();
    }

    /**
     * Create an instance of {@link CoverParams.CoverPDFValues.Entry }
     * 
     */
    public CoverParams.CoverPDFValues.Entry createCoverParamsCoverPDFValuesEntry() {
        return new CoverParams.CoverPDFValues.Entry();
    }

    /**
     * Create an instance of {@link GetCaratula }
     * 
     */
    public GetCaratula createGetCaratula() {
        return new GetCaratula();
    }

    /**
     * Create an instance of {@link CaratulaReturnInfo }
     * 
     */
    public CaratulaReturnInfo createCaratulaReturnInfo() {
        return new CaratulaReturnInfo();
    }

    /**
     * Create an instance of {@link CoverParams }
     * 
     */
    public CoverParams createCoverParams() {
        return new CoverParams();
    }

    /**
     * Create an instance of {@link WsState }
     * 
     */
    public WsState createWsState() {
        return new WsState();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCaratulaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.digital.caib.es/", name = "getCaratulaResponse")
    public JAXBElement<GetCaratulaResponse> createGetCaratulaResponse(GetCaratulaResponse value) {
        return new JAXBElement<GetCaratulaResponse>(_GetCaratulaResponse_QNAME, GetCaratulaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCaratula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.digital.caib.es/", name = "getCaratula")
    public JAXBElement<GetCaratula> createGetCaratula(GetCaratula value) {
        return new JAXBElement<GetCaratula>(_GetCaratula_QNAME, GetCaratula.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CaratulaReturnInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.digital.caib.es/", name = "caratulaReturnInfo")
    public JAXBElement<CaratulaReturnInfo> createCaratulaReturnInfo(CaratulaReturnInfo value) {
        return new JAXBElement<CaratulaReturnInfo>(_CaratulaReturnInfo_QNAME, CaratulaReturnInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.digital.caib.es/", name = "returnInfo")
    public JAXBElement<ReturnInfo> createReturnInfo(ReturnInfo value) {
        return new JAXBElement<ReturnInfo>(_ReturnInfo_QNAME, ReturnInfo.class, null, value);
    }

}
