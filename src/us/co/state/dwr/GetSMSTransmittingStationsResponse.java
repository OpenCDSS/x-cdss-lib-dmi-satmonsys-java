
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
 *         &lt;element name="GetSMSTransmittingStationsResult" type="{http://www.dwr.state.co.us/}ArrayOfStation" minOccurs="0"/>
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
    "getSMSTransmittingStationsResult"
})
@XmlRootElement(name = "GetSMSTransmittingStationsResponse")
public class GetSMSTransmittingStationsResponse {

    @XmlElement(name = "GetSMSTransmittingStationsResult")
    protected ArrayOfStation getSMSTransmittingStationsResult;

    /**
     * Gets the value of the getSMSTransmittingStationsResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStation }
     *     
     */
    public ArrayOfStation getGetSMSTransmittingStationsResult() {
        return getSMSTransmittingStationsResult;
    }

    /**
     * Sets the value of the getSMSTransmittingStationsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStation }
     *     
     */
    public void setGetSMSTransmittingStationsResult(ArrayOfStation value) {
        this.getSMSTransmittingStationsResult = value;
    }

}
