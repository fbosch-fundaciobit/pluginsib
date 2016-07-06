
package es.caib.digital.ws.api.copiaautentica;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for metadatosDocumentoElectronico complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metadatosDocumentoElectronico">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="CSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eniMetadata" type="{http://service.ws.digital.caib.es/}eniMetadata" minOccurs="0"/>
 *         &lt;element name="fechaCaducidad" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="informacionDocumento" type="{http://service.ws.digital.caib.es/}informacionDocumento" minOccurs="0"/>
 *         &lt;element name="isCarpeta" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="labelMetadatosComplementarios">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="metadatosComplementarios">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="metadatosFirmaDocOriginal" type="{http://service.ws.digital.caib.es/}metadatosFirmaElectronica" minOccurs="0"/>
 *         &lt;element name="metadatosFirmas" type="{http://service.ws.digital.caib.es/}metadatosFirmaElectronica" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nombreFormato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumentoCAIB" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadatosDocumentoElectronico", propOrder = {
    "csv",
    "eniMetadata",
    "fechaCaducidad",
    "informacionDocumento",
    "isCarpeta",
    "labelMetadatosComplementarios",
    "metadatosComplementarios",
    "metadatosFirmaDocOriginal",
    "metadatosFirmas",
    "nombreFormato",
    "tipoDocumentoCAIB",
    "usuario"
})
public class MetadatosDocumentoElectronico
    extends BaseVO
{

    @XmlElement(name = "CSV")
    protected String csv;
    protected EniMetadata eniMetadata;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaCaducidad;
    protected InformacionDocumento informacionDocumento;
    protected Boolean isCarpeta;
    @XmlElement(required = true)
    protected MetadatosDocumentoElectronico.LabelMetadatosComplementarios labelMetadatosComplementarios;
    @XmlElement(required = true)
    protected MetadatosDocumentoElectronico.MetadatosComplementarios metadatosComplementarios;
    protected MetadatosFirmaElectronica metadatosFirmaDocOriginal;
    @XmlElement(nillable = true)
    protected List<MetadatosFirmaElectronica> metadatosFirmas;
    protected String nombreFormato;
    protected String tipoDocumentoCAIB;
    protected String usuario;

    /**
     * Gets the value of the csv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSV() {
        return csv;
    }

    /**
     * Sets the value of the csv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSV(String value) {
        this.csv = value;
    }

    /**
     * Gets the value of the eniMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link EniMetadata }
     *     
     */
    public EniMetadata getEniMetadata() {
        return eniMetadata;
    }

    /**
     * Sets the value of the eniMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniMetadata }
     *     
     */
    public void setEniMetadata(EniMetadata value) {
        this.eniMetadata = value;
    }

    /**
     * Gets the value of the fechaCaducidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * Sets the value of the fechaCaducidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCaducidad(Timestamp value) {
        this.fechaCaducidad = value;
    }

    /**
     * Gets the value of the informacionDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link InformacionDocumento }
     *     
     */
    public InformacionDocumento getInformacionDocumento() {
        return informacionDocumento;
    }

    /**
     * Sets the value of the informacionDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link InformacionDocumento }
     *     
     */
    public void setInformacionDocumento(InformacionDocumento value) {
        this.informacionDocumento = value;
    }

    /**
     * Gets the value of the isCarpeta property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCarpeta() {
        return isCarpeta;
    }

    /**
     * Sets the value of the isCarpeta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCarpeta(Boolean value) {
        this.isCarpeta = value;
    }

    /**
     * Gets the value of the labelMetadatosComplementarios property.
     * 
     * @return
     *     possible object is
     *     {@link MetadatosDocumentoElectronico.LabelMetadatosComplementarios }
     *     
     */
    public MetadatosDocumentoElectronico.LabelMetadatosComplementarios getLabelMetadatosComplementarios() {
        return labelMetadatosComplementarios;
    }

    /**
     * Sets the value of the labelMetadatosComplementarios property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatosDocumentoElectronico.LabelMetadatosComplementarios }
     *     
     */
    public void setLabelMetadatosComplementarios(MetadatosDocumentoElectronico.LabelMetadatosComplementarios value) {
        this.labelMetadatosComplementarios = value;
    }

    /**
     * Gets the value of the metadatosComplementarios property.
     * 
     * @return
     *     possible object is
     *     {@link MetadatosDocumentoElectronico.MetadatosComplementarios }
     *     
     */
    public MetadatosDocumentoElectronico.MetadatosComplementarios getMetadatosComplementarios() {
        return metadatosComplementarios;
    }

    /**
     * Sets the value of the metadatosComplementarios property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatosDocumentoElectronico.MetadatosComplementarios }
     *     
     */
    public void setMetadatosComplementarios(MetadatosDocumentoElectronico.MetadatosComplementarios value) {
        this.metadatosComplementarios = value;
    }

    /**
     * Gets the value of the metadatosFirmaDocOriginal property.
     * 
     * @return
     *     possible object is
     *     {@link MetadatosFirmaElectronica }
     *     
     */
    public MetadatosFirmaElectronica getMetadatosFirmaDocOriginal() {
        return metadatosFirmaDocOriginal;
    }

    /**
     * Sets the value of the metadatosFirmaDocOriginal property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatosFirmaElectronica }
     *     
     */
    public void setMetadatosFirmaDocOriginal(MetadatosFirmaElectronica value) {
        this.metadatosFirmaDocOriginal = value;
    }

    /**
     * Gets the value of the metadatosFirmas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadatosFirmas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadatosFirmas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadatosFirmaElectronica }
     * 
     * 
     */
    public List<MetadatosFirmaElectronica> getMetadatosFirmas() {
        if (metadatosFirmas == null) {
            metadatosFirmas = new ArrayList<MetadatosFirmaElectronica>();
        }
        return this.metadatosFirmas;
    }

    /**
     * Gets the value of the nombreFormato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFormato() {
        return nombreFormato;
    }

    /**
     * Sets the value of the nombreFormato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFormato(String value) {
        this.nombreFormato = value;
    }

    /**
     * Gets the value of the tipoDocumentoCAIB property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumentoCAIB() {
        return tipoDocumentoCAIB;
    }

    /**
     * Sets the value of the tipoDocumentoCAIB property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumentoCAIB(String value) {
        this.tipoDocumentoCAIB = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class LabelMetadatosComplementarios {

        protected List<MetadatosDocumentoElectronico.LabelMetadatosComplementarios.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MetadatosDocumentoElectronico.LabelMetadatosComplementarios.Entry }
         * 
         * 
         */
        public List<MetadatosDocumentoElectronico.LabelMetadatosComplementarios.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<MetadatosDocumentoElectronico.LabelMetadatosComplementarios.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected String key;
            protected String value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class MetadatosComplementarios {

        protected List<MetadatosDocumentoElectronico.MetadatosComplementarios.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MetadatosDocumentoElectronico.MetadatosComplementarios.Entry }
         * 
         * 
         */
        public List<MetadatosDocumentoElectronico.MetadatosComplementarios.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<MetadatosDocumentoElectronico.MetadatosComplementarios.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected String key;
            protected String value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}
