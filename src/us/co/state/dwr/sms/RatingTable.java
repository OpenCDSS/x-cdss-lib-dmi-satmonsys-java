
package us.co.state.dwr.sms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RatingTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RatingTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tablePoints" type="{http://www.dwr.state.co.us/}ArrayOfRatingTablePoint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatingTable", propOrder = {
    "tableName",
    "tablePoints"
})
public class RatingTable {

    protected String tableName;
    protected ArrayOfRatingTablePoint tablePoints;

    /**
     * Gets the value of the tableName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the value of the tableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableName(String value) {
        this.tableName = value;
    }

    /**
     * Gets the value of the tablePoints property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRatingTablePoint }
     *     
     */
    public ArrayOfRatingTablePoint getTablePoints() {
        return tablePoints;
    }

    /**
     * Sets the value of the tablePoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRatingTablePoint }
     *     
     */
    public void setTablePoints(ArrayOfRatingTablePoint value) {
        this.tablePoints = value;
    }

}
