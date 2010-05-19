
package us.co.state.dwr.sms;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrentCondition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrentCondition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="div" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="wd" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="abbrev" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="variable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gageHeight" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="transFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transFlagDescr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="webMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="webMessageDescr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentShift" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentShiftEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ratingTable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentRatingTableEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shiftCurve" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentShiftCurveEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrentCondition", propOrder = {
    "div",
    "wd",
    "abbrev",
    "variable",
    "transDateTime",
    "gageHeight",
    "amount",
    "transFlag",
    "transFlagDescr",
    "webMessage",
    "webMessageDescr",
    "dataSource",
    "currentShift",
    "currentShiftEffectiveDate",
    "ratingTable",
    "currentRatingTableEffectiveDate",
    "shiftCurve",
    "currentShiftCurveEffectiveDate"
})
public class CurrentCondition {

    protected int div;
    protected int wd;
    protected String abbrev;
    protected String variable;
    protected String transDateTime;
    @XmlElement(required = true)
    protected BigDecimal gageHeight;
    @XmlElement(required = true)
    protected BigDecimal amount;
    protected String transFlag;
    protected String transFlagDescr;
    protected String webMessage;
    protected String webMessageDescr;
    protected String dataSource;
    protected String currentShift;
    protected String currentShiftEffectiveDate;
    protected String ratingTable;
    protected String currentRatingTableEffectiveDate;
    protected String shiftCurve;
    protected String currentShiftCurveEffectiveDate;

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
     * Gets the value of the variable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariable(String value) {
        this.variable = value;
    }

    /**
     * Gets the value of the transDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransDateTime() {
        return transDateTime;
    }

    /**
     * Sets the value of the transDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransDateTime(String value) {
        this.transDateTime = value;
    }

    /**
     * Gets the value of the gageHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGageHeight() {
        return gageHeight;
    }

    /**
     * Sets the value of the gageHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGageHeight(BigDecimal value) {
        this.gageHeight = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the transFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransFlag() {
        return transFlag;
    }

    /**
     * Sets the value of the transFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransFlag(String value) {
        this.transFlag = value;
    }

    /**
     * Gets the value of the transFlagDescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransFlagDescr() {
        return transFlagDescr;
    }

    /**
     * Sets the value of the transFlagDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransFlagDescr(String value) {
        this.transFlagDescr = value;
    }

    /**
     * Gets the value of the webMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebMessage() {
        return webMessage;
    }

    /**
     * Sets the value of the webMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebMessage(String value) {
        this.webMessage = value;
    }

    /**
     * Gets the value of the webMessageDescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebMessageDescr() {
        return webMessageDescr;
    }

    /**
     * Sets the value of the webMessageDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebMessageDescr(String value) {
        this.webMessageDescr = value;
    }

    /**
     * Gets the value of the dataSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Sets the value of the dataSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSource(String value) {
        this.dataSource = value;
    }

    /**
     * Gets the value of the currentShift property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentShift() {
        return currentShift;
    }

    /**
     * Sets the value of the currentShift property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentShift(String value) {
        this.currentShift = value;
    }

    /**
     * Gets the value of the currentShiftEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentShiftEffectiveDate() {
        return currentShiftEffectiveDate;
    }

    /**
     * Sets the value of the currentShiftEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentShiftEffectiveDate(String value) {
        this.currentShiftEffectiveDate = value;
    }

    /**
     * Gets the value of the ratingTable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatingTable() {
        return ratingTable;
    }

    /**
     * Sets the value of the ratingTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatingTable(String value) {
        this.ratingTable = value;
    }

    /**
     * Gets the value of the currentRatingTableEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentRatingTableEffectiveDate() {
        return currentRatingTableEffectiveDate;
    }

    /**
     * Sets the value of the currentRatingTableEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentRatingTableEffectiveDate(String value) {
        this.currentRatingTableEffectiveDate = value;
    }

    /**
     * Gets the value of the shiftCurve property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShiftCurve() {
        return shiftCurve;
    }

    /**
     * Sets the value of the shiftCurve property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShiftCurve(String value) {
        this.shiftCurve = value;
    }

    /**
     * Gets the value of the currentShiftCurveEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentShiftCurveEffectiveDate() {
        return currentShiftCurveEffectiveDate;
    }

    /**
     * Sets the value of the currentShiftCurveEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentShiftCurveEffectiveDate(String value) {
        this.currentShiftCurveEffectiveDate = value;
    }

}
