package us.co.state.dwr.sms.datastore;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import riverside.datastore.AbstractWebServiceDataStore;
import us.co.state.dwr.sms.ColoradoWaterSMS;

import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;

/**
Data store for ColoradoWaterSMS web service.  This class maintains the web service information in a general way.
This data store was created from a previous "input type" convention.  Consequently, this code is mainly a
wrapper for code that previously was developed.
*/
public class ColoradoWaterSMSDataStore extends AbstractWebServiceDataStore
{

/**
ColoradoWaterSMS instance used as SOAP API.
*/
private ColoradoWaterSMS __coloradoWaterSMS = null;
    
/**
Constructor for web service.
*/
public ColoradoWaterSMSDataStore ( String name, String description, URI serviceRootURI )
throws URISyntaxException, IOException
{
    setName ( name );
    setDescription ( description );
    setServiceRootURI ( serviceRootURI );
    
    // TODO SAM 2012-05-03 This should use the ServiceRootAPI but it does not...
    setColoradoWaterSMS ( new ColoradoWaterSMS(serviceRootURI.toString()) );
}

/**
Factory method to construct a data store connection from a properties file.
@param filename name of file containing property strings
*/
public static ColoradoWaterSMSDataStore createFromFile ( String filename )
throws IOException, Exception
{
    // Read the properties from the file
    PropList props = new PropList ("");
    props.setPersistentName ( filename );
    props.readPersistent ( false );
    String name = IOUtil.expandPropertyForEnvironment(props.getValue("Name"));
    String description = IOUtil.expandPropertyForEnvironment(props.getValue("Description"));
    String serviceRootURI = IOUtil.expandPropertyForEnvironment(props.getValue("ServiceRootURI"));
    
    // Get the properties and create an instance

    ColoradoWaterSMSDataStore ds = new ColoradoWaterSMSDataStore( name, description, new URI(serviceRootURI) );
    return ds;
}

/**
Return the ColoradoWaterSMS instance used by the data store.
@return the ColoradoWaterSMS instance used by the data store
*/
public ColoradoWaterSMS getColoradoWaterSMS ()
{
    return __coloradoWaterSMS;
}

/**
Set the ColoradoWaterSMS instance that is used as the API
*/
private void setColoradoWaterSMS ( ColoradoWaterSMS coloradoWaterSMS )
{
    __coloradoWaterSMS = coloradoWaterSMS;
}

}