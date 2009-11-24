
package us.co.state.dwr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShiftCurve complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShiftCurve">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="curveName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="curvePoints" type="{http://www.dwr.state.co.us/}ArrayOfShiftCurvePoint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShiftCurve", propOrder = {
    "curveName",
    "curvePoints"
})
public class ShiftCurve {

    protected String curveName;
    protected ArrayOfShiftCurvePoint curvePoints;

    /**
     * Gets the value of the curveName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurveName() {
        return curveName;
    }

    /**
     * Sets the value of the curveName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurveName(String value) {
        this.curveName = value;
    }

    /**
     * Gets the value of the curvePoints property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfShiftCurvePoint }
     *     
     */
    public ArrayOfShiftCurvePoint getCurvePoints() {
        return curvePoints;
    }

    /**
     * Sets the value of the curvePoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfShiftCurvePoint }
     *     
     */
    public void setCurvePoints(ArrayOfShiftCurvePoint value) {
        this.curvePoints = value;
    }

}
