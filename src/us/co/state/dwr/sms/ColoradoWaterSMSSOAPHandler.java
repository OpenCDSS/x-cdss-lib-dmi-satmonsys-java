package us.co.state.dwr.sms;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import RTi.Util.Message.Message;

/**
 * Handler for ColoradoWaterSMS SOAP messages, mainly for logging, to help with troubleshooting
 * @author sam
 *
 */
public class ColoradoWaterSMSSOAPHandler implements SOAPHandler<SOAPMessageContext> {
    
    /**
     * Required by SOAPHandler
     */
    public void close ( MessageContext messageContext ) {
    }
    
    /**
     * Required by SOAPHandler
     */
    public boolean handleFault ( SOAPMessageContext smc ) {
        Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            Message.printStatus(2, "ColoradoWaterSMS.handleMessage", "Outbound message (fault)" );
            SOAPMessage message = smc.getMessage();
            Message.printStatus(2, "ColoradoWaterSMS.handleMessage", "Envelope:  " + message.toString() );
        } else {
            Message.printStatus(2, "ColoradoWaterSMS.handleMessage", "Inbound message (fault)" ); 
        }
        return true;
    }
    
    /**
     * Required by SOAPHandler
     */
    public boolean handleMessage ( SOAPMessageContext smc ) {
        Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            Message.printStatus(2, "ColoradoWaterSMS.handleMessage", "Outbound message" );
            SOAPMessage message = smc.getMessage();
            Message.printStatus(2, "ColoradoWaterSMS.handleMessage", "Envelope:  " + message.toString() );
        } else {
            Message.printStatus(2, "ColoradoWaterSMS.handleMessage", "Inbound message" ); 
        }
        return true;
    }
    
    /**
     * Required by SOAPHandler
     */
    public Set<QName> getHeaders () {
        return null;
    }
}