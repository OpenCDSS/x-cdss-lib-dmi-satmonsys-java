
package us.co.state.dwr.sms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfShiftCurvePoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfShiftCurvePoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShiftCurvePoint" type="{http://www.dwr.state.co.us/}ShiftCurvePoint" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfShiftCurvePoint", propOrder = {
    "shiftCurvePoint"
})
public class ArrayOfShiftCurvePoint {

    @XmlElement(name = "ShiftCurvePoint", nillable = true)
    protected List<ShiftCurvePoint> shiftCurvePoint;

    /**
     * Gets the value of the shiftCurvePoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shiftCurvePoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShiftCurvePoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ShiftCurvePoint }
     * 
     * 
     */
    public List<ShiftCurvePoint> getShiftCurvePoint() {
        if (shiftCurvePoint == null) {
            shiftCurvePoint = new ArrayList<ShiftCurvePoint>();
        }
        return this.shiftCurvePoint;
    }

}
