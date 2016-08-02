
package es.caib.digital.ws.api.caratulas;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for coverParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="coverParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="coverPDFValues">
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
 *         &lt;element name="entityID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="included" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="metadataURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadatos" type="{http://service.ws.digital.caib.es/}metaDato" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numPages" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coverParams", propOrder = {
    "coverPDFValues",
    "entityID",
    "included",
    "metadataURL",
    "metadatos",
    "numPages"
})
public class CoverParams {

    @XmlElement(required = true)
    protected CoverParams.CoverPDFValues coverPDFValues;
    protected Integer entityID;
    protected boolean included;
    protected String metadataURL;
    @XmlElement(nillable = true)
    protected List<MetaDato> metadatos;
    protected Integer numPages;

    /**
     * Gets the value of the coverPDFValues property.
     * 
     * @return
     *     possible object is
     *     {@link CoverParams.CoverPDFValues }
     *     
     */
    public CoverParams.CoverPDFValues getCoverPDFValues() {
        return coverPDFValues;
    }

    /**
     * Sets the value of the coverPDFValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link CoverParams.CoverPDFValues }
     *     
     */
    public void setCoverPDFValues(CoverParams.CoverPDFValues value) {
        this.coverPDFValues = value;
    }

    /**
     * Gets the value of the entityID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEntityID() {
        return entityID;
    }

    /**
     * Sets the value of the entityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEntityID(Integer value) {
        this.entityID = value;
    }

    /**
     * Gets the value of the included property.
     * 
     */
    public boolean isIncluded() {
        return included;
    }

    /**
     * Sets the value of the included property.
     * 
     */
    public void setIncluded(boolean value) {
        this.included = value;
    }

    /**
     * Gets the value of the metadataURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadataURL() {
        return metadataURL;
    }

    /**
     * Sets the value of the metadataURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadataURL(String value) {
        this.metadataURL = value;
    }

    /**
     * Gets the value of the metadatos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadatos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadatos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetaDato }
     * 
     * 
     */
    public List<MetaDato> getMetadatos() {
        if (metadatos == null) {
            metadatos = new ArrayList<MetaDato>();
        }
        return this.metadatos;
    }

    /**
     * Gets the value of the numPages property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumPages() {
        return numPages;
    }

    /**
     * Sets the value of the numPages property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumPages(Integer value) {
        this.numPages = value;
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
    public static class CoverPDFValues {

        protected List<CoverParams.CoverPDFValues.Entry> entry;

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
         * {@link CoverParams.CoverPDFValues.Entry }
         * 
         * 
         */
        public List<CoverParams.CoverPDFValues.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<CoverParams.CoverPDFValues.Entry>();
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
