
package us.co.state.dwr;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfStationVariables complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfStationVariables">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StationVariables" type="{http://www.dwr.state.co.us/}StationVariables" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfStationVariables", propOrder = {
    "stationVariables"
})
public class ArrayOfStationVariables {

    @XmlElement(name = "StationVariables", nillable = true)
    protected List<StationVariables> stationVariables;

    /**
     * Gets the value of the stationVariables property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stationVariables property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStationVariables().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StationVariables }
     * 
     * 
     */
    public List<StationVariables> getStationVariables() {
        if (stationVariables == null) {
            stationVariables = new ArrayList<StationVariables>();
        }
        return this.stationVariables;
    }

}
