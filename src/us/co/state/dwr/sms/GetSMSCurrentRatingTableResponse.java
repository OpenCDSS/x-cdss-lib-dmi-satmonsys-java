
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
 *         &lt;element name="GetSMSCurrentRatingTableResult" type="{http://www.dwr.state.co.us/}RatingTable" minOccurs="0"/>
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
    "getSMSCurrentRatingTableResult"
})
@XmlRootElement(name = "GetSMSCurrentRatingTableResponse")
public class GetSMSCurrentRatingTableResponse {

    @XmlElement(name = "GetSMSCurrentRatingTableResult")
    protected RatingTable getSMSCurrentRatingTableResult;

    /**
     * Gets the value of the getSMSCurrentRatingTableResult property.
     * 
     * @return
     *     possible object is
     *     {@link RatingTable }
     *     
     */
    public RatingTable getGetSMSCurrentRatingTableResult() {
        return getSMSCurrentRatingTableResult;
    }

    /**
     * Sets the value of the getSMSCurrentRatingTableResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingTable }
     *     
     */
    public void setGetSMSCurrentRatingTableResult(RatingTable value) {
        this.getSMSCurrentRatingTableResult = value;
    }

}
