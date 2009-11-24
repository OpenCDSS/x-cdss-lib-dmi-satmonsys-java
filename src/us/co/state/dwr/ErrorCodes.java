
package us.co.state.dwr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorCodes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ErrorCodes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WebServiceException" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ParameterFormatInvalid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ParameterOutOfRange" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ParameterMissing" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SearchErrors" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SoapErrors" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorCodes", propOrder = {
    "webServiceException",
    "parameterFormatInvalid",
    "parameterOutOfRange",
    "parameterMissing",
    "searchErrors",
    "soapErrors"
})
public class ErrorCodes {

    @XmlElement(name = "WebServiceException")
    protected int webServiceException;
    @XmlElement(name = "ParameterFormatInvalid")
    protected int parameterFormatInvalid;
    @XmlElement(name = "ParameterOutOfRange")
    protected int parameterOutOfRange;
    @XmlElement(name = "ParameterMissing")
    protected int parameterMissing;
    @XmlElement(name = "SearchErrors")
    protected int searchErrors;
    @XmlElement(name = "SoapErrors")
    protected int soapErrors;

    /**
     * Gets the value of the webServiceException property.
     * 
     */
    public int getWebServiceException() {
        return webServiceException;
    }

    /**
     * Sets the value of the webServiceException property.
     * 
     */
    public void setWebServiceException(int value) {
        this.webServiceException = value;
    }

    /**
     * Gets the value of the parameterFormatInvalid property.
     * 
     */
    public int getParameterFormatInvalid() {
        return parameterFormatInvalid;
    }

    /**
     * Sets the value of the parameterFormatInvalid property.
     * 
     */
    public void setParameterFormatInvalid(int value) {
        this.parameterFormatInvalid = value;
    }

    /**
     * Gets the value of the parameterOutOfRange property.
     * 
     */
    public int getParameterOutOfRange() {
        return parameterOutOfRange;
    }

    /**
     * Sets the value of the parameterOutOfRange property.
     * 
     */
    public void setParameterOutOfRange(int value) {
        this.parameterOutOfRange = value;
    }

    /**
     * Gets the value of the parameterMissing property.
     * 
     */
    public int getParameterMissing() {
        return parameterMissing;
    }

    /**
     * Sets the value of the parameterMissing property.
     * 
     */
    public void setParameterMissing(int value) {
        this.parameterMissing = value;
    }

    /**
     * Gets the value of the searchErrors property.
     * 
     */
    public int getSearchErrors() {
        return searchErrors;
    }

    /**
     * Sets the value of the searchErrors property.
     * 
     */
    public void setSearchErrors(int value) {
        this.searchErrors = value;
    }

    /**
     * Gets the value of the soapErrors property.
     * 
     */
    public int getSoapErrors() {
        return soapErrors;
    }

    /**
     * Sets the value of the soapErrors property.
     * 
     */
    public void setSoapErrors(int value) {
        this.soapErrors = value;
    }

}
