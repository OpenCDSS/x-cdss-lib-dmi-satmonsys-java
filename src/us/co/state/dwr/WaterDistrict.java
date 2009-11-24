
package us.co.state.dwr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WaterDistrict complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WaterDistrict">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wd" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="div" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="waterDistrictName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WaterDistrict", propOrder = {
    "wd",
    "div",
    "waterDistrictName"
})
public class WaterDistrict {

    protected int wd;
    protected int div;
    protected String waterDistrictName;

    /**
     * Gets the value of the wd property.
     * 
     */
    public int getWd() {
        return wd;
    }

    /**
     * Sets the value of the wd property.
     * 
     */
    public void setWd(int value) {
        this.wd = value;
    }

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
     * Gets the value of the waterDistrictName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaterDistrictName() {
        return waterDistrictName;
    }

    /**
     * Sets the value of the waterDistrictName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaterDistrictName(String value) {
        this.waterDistrictName = value;
    }

}
