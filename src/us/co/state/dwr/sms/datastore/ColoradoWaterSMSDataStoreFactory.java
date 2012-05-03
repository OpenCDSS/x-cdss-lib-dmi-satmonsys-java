package us.co.state.dwr.sms.datastore;

import java.net.URI;

import RTi.Util.IO.PropList;
import riverside.datastore.DataStore;
import riverside.datastore.DataStoreFactory;

/**
Factory to instantiate ColoradoWaterSMSDataStore instances.
@author sam
*/
public class ColoradoWaterSMSDataStoreFactory implements DataStoreFactory
{

/**
Create a ColoradoWaterSMSDataStore instance from properties.
*/
public DataStore create ( PropList props )
{
    String name = props.getValue ( "Name" );
    String description = props.getValue ( "Description" );
    if ( description == null ) {
        description = "";
    }
    String serviceRootURI = props.getValue ( "ServiceRootURI" );
    try {
        return new ColoradoWaterSMSDataStore ( name, description, new URI(serviceRootURI) );
    }
    catch ( Exception e ) {
        throw new RuntimeException ( e );
    }
}

}