
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
 *         &lt;element name="GetSMSTransmittingStationVariablesResult" type="{http://www.dwr.state.co.us/}ArrayOfStationVariables" minOccurs="0"/>
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
    "getSMSTransmittingStationVariablesResult"
})
@XmlRootElement(name = "GetSMSTransmittingStationVariablesResponse")
public class GetSMSTransmittingStationVariablesResponse {

    @XmlElement(name = "GetSMSTransmittingStationVariablesResult")
    protected ArrayOfStationVariables getSMSTransmittingStationVariablesResult;

    /**
     * Gets the value of the getSMSTransmittingStationVariablesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStationVariables }
     *     
     */
    public ArrayOfStationVariables getGetSMSTransmittingStationVariablesResult() {
        return getSMSTransmittingStationVariablesResult;
    }

    /**
     * Sets the value of the getSMSTransmittingStationVariablesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStationVariables }
     *     
     */
    public void setGetSMSTransmittingStationVariablesResult(ArrayOfStationVariables value) {
        this.getSMSTransmittingStationVariablesResult = value;
    }

}
