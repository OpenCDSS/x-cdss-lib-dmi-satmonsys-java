
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
 *         &lt;element name="GetSMSCurrentShiftCurveResult" type="{http://www.dwr.state.co.us/}ShiftCurve" minOccurs="0"/>
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
    "getSMSCurrentShiftCurveResult"
})
@XmlRootElement(name = "GetSMSCurrentShiftCurveResponse")
public class GetSMSCurrentShiftCurveResponse {

    @XmlElement(name = "GetSMSCurrentShiftCurveResult")
    protected ShiftCurve getSMSCurrentShiftCurveResult;

    /**
     * Gets the value of the getSMSCurrentShiftCurveResult property.
     * 
     * @return
     *     possible object is
     *     {@link ShiftCurve }
     *     
     */
    public ShiftCurve getGetSMSCurrentShiftCurveResult() {
        return getSMSCurrentShiftCurveResult;
    }

    /**
     * Sets the value of the getSMSCurrentShiftCurveResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShiftCurve }
     *     
     */
    public void setGetSMSCurrentShiftCurveResult(ShiftCurve value) {
        this.getSMSCurrentShiftCurveResult = value;
    }

}
