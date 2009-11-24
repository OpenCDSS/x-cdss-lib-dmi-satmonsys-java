
package us.co.state.dwr;

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
 *         &lt;element name="GetSMSProvisionalDataResult" type="{http://www.dwr.state.co.us/}ArrayOfStreamflowTransmission" minOccurs="0"/>
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
    "getSMSProvisionalDataResult"
})
@XmlRootElement(name = "GetSMSProvisionalDataResponse")
public class GetSMSProvisionalDataResponse {

    @XmlElement(name = "GetSMSProvisionalDataResult")
    protected ArrayOfStreamflowTransmission getSMSProvisionalDataResult;

    /**
     * Gets the value of the getSMSProvisionalDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStreamflowTransmission }
     *     
     */
    public ArrayOfStreamflowTransmission getGetSMSProvisionalDataResult() {
        return getSMSProvisionalDataResult;
    }

    /**
     * Sets the value of the getSMSProvisionalDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStreamflowTransmission }
     *     
     */
    public void setGetSMSProvisionalDataResult(ArrayOfStreamflowTransmission value) {
        this.getSMSProvisionalDataResult = value;
    }

}
