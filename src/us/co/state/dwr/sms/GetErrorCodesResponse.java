
package us.co.state.dwr.sms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="GetErrorCodesResult" type="{http://www.dwr.state.co.us/}ErrorCodes" minOccurs="0"/>
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
    "getErrorCodesResult"
})
@XmlRootElement(name = "GetErrorCodesResponse")
public class GetErrorCodesResponse {

    @XmlElement(name = "GetErrorCodesResult")
    protected ErrorCodes getErrorCodesResult;

    /**
     * Gets the value of the getErrorCodesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorCodes }
     *     
     */
    public ErrorCodes getGetErrorCodesResult() {
        return getErrorCodesResult;
    }

    /**
     * Sets the value of the getErrorCodesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorCodes }
     *     
     */
    public void setGetErrorCodesResult(ErrorCodes value) {
        this.getErrorCodesResult = value;
    }

}
