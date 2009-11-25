
package us.co.state.dwr;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * Disclaimer: All data presented on the Colorado Division of Water Resources Current Conditions Web Site are provisional and subject to revision. These data include water levels in lakes and reservoirs, and in streams, rivers, and other water courses; computed stream discharges; and various weather parameters (temperature, precipitation, etc.). Most data relayed by satellite or other telemetry have received little or no review prior to posting on the web site.  State of Colorado Hydrographic Program Staff visit satellite monitoring gaging stations on a frequent basis to maintain equipment and ensure correct operation. They also perform discharge measurements for purposes of calibrating the stage-discharge relationship at a gage, as well as take note of physical factors present at the gage which may be affecting the stage-discharge relationship. Nevertheless, inaccuracies in the data may occur because of instrument malfunctions or physical changes at the measurement site. Subsequent review may result in significant revisions to the data.  Data users are cautioned to consider carefully the provisional nature of the information before using it for decisions that concern personal or public safety or the conduct of business that involves substantial monetary or operational consequences.
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ColoradoWaterSMS", targetNamespace = "http://www.dwr.state.co.us/", wsdlLocation = "http://www.dwr.state.co.us/SMS_WebService/ColoradoWaterSMS.asmx?WSDL")
public class ColoradoWaterSMS
    extends Service
{

    private final static URL COLORADOWATERSMS_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(us.co.state.dwr.ColoradoWaterSMS.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = us.co.state.dwr.ColoradoWaterSMS.class.getResource(".");
            url = new URL(baseUrl, "http://www.dwr.state.co.us/SMS_WebService/ColoradoWaterSMS.asmx?WSDL");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://www.dwr.state.co.us/SMS_WebService/ColoradoWaterSMS.asmx?WSDL', retrying as a local file");
            logger.warning(e.getMessage());
        }
        COLORADOWATERSMS_WSDL_LOCATION = url;
    }

    public ColoradoWaterSMS(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ColoradoWaterSMS() {
        super(COLORADOWATERSMS_WSDL_LOCATION, new QName("http://www.dwr.state.co.us/", "ColoradoWaterSMS"));
    }

    /**
     * 
     * @return
     *     returns ColoradoWaterSMSSoap
     */
    @WebEndpoint(name = "ColoradoWaterSMSSoap")
    public ColoradoWaterSMSSoap getColoradoWaterSMSSoap() {
        return super.getPort(new QName("http://www.dwr.state.co.us/", "ColoradoWaterSMSSoap"), ColoradoWaterSMSSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ColoradoWaterSMSSoap
     */
    @WebEndpoint(name = "ColoradoWaterSMSSoap")
    public ColoradoWaterSMSSoap getColoradoWaterSMSSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.dwr.state.co.us/", "ColoradoWaterSMSSoap"), ColoradoWaterSMSSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns ColoradoWaterSMSSoap
     */
    @WebEndpoint(name = "ColoradoWaterSMSSoap12")
    public ColoradoWaterSMSSoap getColoradoWaterSMSSoap12() {
        return super.getPort(new QName("http://www.dwr.state.co.us/", "ColoradoWaterSMSSoap12"), ColoradoWaterSMSSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ColoradoWaterSMSSoap
     */
    @WebEndpoint(name = "ColoradoWaterSMSSoap12")
    public ColoradoWaterSMSSoap getColoradoWaterSMSSoap12(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.dwr.state.co.us/", "ColoradoWaterSMSSoap12"), ColoradoWaterSMSSoap.class, features);
    }

}