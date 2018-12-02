package ioTConnectedDevicesGateWay.labs.module7;

import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.PrinterLocation;



public class CoapClientConnector {

	 
     private static Logger _logger=Logger.getLogger(CoapClientConnector.class.getName());
	 private String _protocol;
	 private String _host;
	 private String _port;
	 private String _serverAddr;
	 private CoapClient coap_client;
	 private boolean _isInitialized;
	 private String _currentURL;
	 
	 
	 public CoapClientConnector(String protocol,String host, String port)
	 {
		 _protocol=protocol;
		 _host=host;
		 _port=port;
		 _serverAddr = _protocol + "://" + _host + ":" + _port;
	     _logger.info("Using URL for server connection: " + _serverAddr);
	     _isInitialized=false; 
	 }

	 
	 
	 public String getCurrentURL()
	 {
		 return _currentURL;
	 }

	 
	 
	 public void initClient(String resourceName)
	 {
	
     //Check if client is initialized if true then shutdown and reinitialize		 
	 if (_isInitialized) 
	 {
		 _logger.info("Reinitialized coap_client....");
	     coap_client.shutdown();
	     coap_client=null;
	 }
	 
	 	 
	 try
	 {	 
		// set URL for Coap client to use
		_currentURL=_serverAddr+"/"+resourceName; 
		coap_client= new CoapClient(_currentURL);
		_logger.info("Connection established with server/resource "+_serverAddr);
		_isInitialized=true;
		 
	 }catch (Exception e) 
	 {
	    _logger.log(Level.SEVERE, "Failed to connect to server/rescource"+getCurrentURL(),e);
	 }
	 
	 }
	 
	 
	 
	 // Implement CoapClient ping method
	 public void pingServer()
	 {
		 _logger.info("ping server.....");
		 
		 initClient(null); 
		 
		 if(coap_client.ping())
		 {
			 _logger.info(_host+" is reachable");
		 }
		 else
		 {
			 _logger.info(_host+" is unreachable");
		 }
		 
	 }
	 
		 
	 //Discover all available resources from CoapServer
	 public void discoverResources()
	 {
		 
		 initClient(null);
		 
		 _logger.info("Descovering avaliable resources");
         
		 Set<WebLink> webLinks=coap_client.discover();
		 
		 if(webLinks!=null)
		 {
			 for(WebLink w: webLinks)
			 {
				 System.out.println("Weblink: "+w.getURI());
			 }
		 }
		 else
		 {
			 _logger.warning("Null DiscoverResponse..");
		 }
		 
	 }
	 
	 
	 //Implement sending Coap GetRequest method
	 public CoapResponse sendGetRequest(String resource, boolean useCON)
	 {
		 
	   initClient(resource);
	   
	   ////Set using CONS OR NONS
	   if(useCON){coap_client.useCONs();}
	   else      {coap_client.useNONs();}
		
	   _logger.info("Sending get request to "+getCurrentURL());
	   //Send Coap GetRequest
	   CoapResponse respone= coap_client.get();
	   
	   if(respone!=null)
	   {
		System.out.println("ResponseCode: "+respone.getCode());
		System.out.println("ResponsePayload:\n"+ new String(respone.getPayload()));
	   }
	   else
	   {
	      _logger.warning("No response received from server/resource "+getCurrentURL());
	    
	   }
	   
	   return respone;
	  
	 }
	 
	 	 

	 //Implement sending Coap PostRequest method
	 public CoapResponse sendPostRequest(String payload,String resource,boolean useCON)
	 {
		 
		 initClient(resource);
		 
		//Set using CONS OR NONS
		 if(useCON){coap_client.useCONs();}
		 else      {coap_client.useNONs();}
	     
		 _logger.info("Sending post request to "+getCurrentURL());
		 CoapResponse respone= coap_client.post(payload, MediaTypeRegistry.TEXT_PLAIN);
		 
		 if(respone!=null)
		 {
			System.out.println("ResponseIsSuccess:"+respone.isSuccess());
			System.out.println("ResponseCode: "+respone.getCode());
		 }
		 else
		 {
		      _logger.warning("No respone received from server/resource "+getCurrentURL());
		 }
		 
		 return respone;
		 	 
	 }
	 
	 
	 //Implement sending Coap DeleteRequest method
	 public CoapResponse sendDeleteRequest(String resource,boolean useCON)
	 {
		 
		 initClient(resource); 
		 
		//Set using CONS OR NONS
		 if(useCON){coap_client.useCONs();}
		 else      {coap_client.useNONs();}
	     
		 _logger.info("Sending Delete request to "+getCurrentURL());
		 CoapResponse respone= coap_client.delete();
		 
		 if(respone!=null)
		 {
			System.out.println("ResponseIsSuccess:"+respone.isSuccess());
			System.out.println("ResponseCode: "+respone.getCode());
		 }
		 else
		 {
		      _logger.warning("No response received from server/resource "+getCurrentURL());
		 }
		 
		 return respone;
		 	 
	 }
	 
	 
	//Implement sending Coap PutRequest method
	 public CoapResponse sendPutRequest(String payload, String resource,boolean useCON)
	 {
		 
		 initClient(resource); 
		 
		//Set using CONS OR NONS
		 if(useCON){coap_client.useCONs();}
		 else      {coap_client.useNONs();}
	     
		 _logger.info("Sending put request to "+getCurrentURL());
		 CoapResponse respone= coap_client.put(payload, MediaTypeRegistry.TEXT_PLAIN);
		 
		 if(respone!=null)
		 {
			System.out.println("ResponseIsSuccess:"+respone.isSuccess());
			System.out.println("ResponseCode: "+respone.getCode());
		 }
		 else
		 {
		      _logger.warning("No respone received from server/resource "+getCurrentURL());
		 }
		 
		 return respone;
		 	 
	 }
	 
	 
	 
     //Sending Coap GetRequest with handler which will be observing the specific resource on the coap Server
	 public CoapResponse sendGetwithHandler(String Resource, boolean useCON, CoapHandler handler,boolean enableWait)
	 {
		 	
		   initClient(Resource);
		   
		   _logger.info("Adding CoapHandler to coap_client....");
		   
		   //Set wait or not
		   if(enableWait) {coap_client.observeAndWait(handler);}
		   else           {coap_client.observe(handler);}
		   
		   //Set using CONS OR NONS
		   if(useCON){coap_client.useCONs();}
		   else      {coap_client.useNONs();}
		   
			
		   _logger.info("Sending get request to "+getCurrentURL());
		   CoapResponse respone= coap_client.get();
		   
		   if(respone==null)
		   {
		      _logger.warning("No response received from server/resource "+getCurrentURL());
		   }
		   
		   return respone;
		 	 
	 }
	 
	 
	 
	 
	 
}
