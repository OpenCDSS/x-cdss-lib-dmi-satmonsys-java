
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
 *         &lt;element name="Div" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WD" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Abbrev" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "div",
    "wd",
    "abbrev"
})
@XmlRootElement(name = "GetSMSTransmittingStationVariables")
public class GetSMSTransmittingStationVariables {

    @XmlElement(name = "Div")
    protected int div;
    @XmlElement(name = "WD")
    protected int wd;
    @XmlElement(name = "Abbrev")
    protected String abbrev;

    /**
     * Gets the value of the div property.
     * 
     */
    public int getDiv() {
        return div;
    }

    /**
     * Sets the value of the div property.
     * 
     */
    public void setDiv(int value) {
        this.div = value;
    }

    /**
     * Gets the value of the wd property.
     * 
     */
    public int getWD() {
        return wd;
    }

    /**
     * Sets the value of the wd property.
     * 
     */
    public void setWD(int value) {
        this.wd = value;
    }

    /**
     * Gets the value of the abbrev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbbrev() {
        return abbrev;
    }

    /**
     * Sets the value of the abbrev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbbrev(String value) {
        this.abbrev = value;
    }

}
