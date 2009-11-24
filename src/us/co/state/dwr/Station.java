
package us.co.state.dwr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Station complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Station">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="div" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="wd" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="abbrev" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataProvider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataProviderAbbrev" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UTM_x" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UTM_y" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Station", propOrder = {
    "div",
    "wd",
    "abbrev",
    "stationName",
    "dataProvider",
    "dataProviderAbbrev",
    "utmx",
    "utmy"
})
public class Station {

    protected int div;
    protected int wd;
    protected String abbrev;
    protected String stationName;
    @XmlElement(name = "DataProvider")
    protected String dataProvider;
    @XmlElement(name = "DataProviderAbbrev")
    protected String dataProviderAbbrev;
    @XmlElement(name = "UTM_x")
    protected String utmx;
    @XmlElement(name = "UTM_y")
    protected String utmy;

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

    /**
     * Gets the value of the stationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Sets the value of the stationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationName(String value) {
        this.stationName = value;
    }

    /**
     * Gets the value of the dataProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataProvider() {
        return dataProvider;
    }

    /**
     * Sets the value of the dataProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataProvider(String value) {
        this.dataProvider = value;
    }

    /**
     * Gets the value of the dataProviderAbbrev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataProviderAbbrev() {
        return dataProviderAbbrev;
    }

    /**
     * Sets the value of the dataProviderAbbrev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataProviderAbbrev(String value) {
        this.dataProviderAbbrev = value;
    }

    /**
     * Gets the value of the utmx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUTMX() {
        return utmx;
    }

    /**
     * Sets the value of the utmx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUTMX(String value) {
        this.utmx = value;
    }

    /**
     * Gets the value of the utmy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUTMY() {
        return utmy;
    }

    /**
     * Sets the value of the utmy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUTMY(String value) {
        this.utmy = value;
    }

}
