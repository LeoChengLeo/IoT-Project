package ioTConnectedDevicesGateWay.labs.module7;

import java.util.logging.Logger;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

public class CoapServerConnector {
	
	private CoapServer coapServer;
	
	public CoapServerConnector() 
	{
		coapServer= new CoapServer();
	}
	
	public CoapServerConnector(CoapResource resource)
	{
		coapServer= new CoapServer();
		coapServer.add(resource);
	}

	public void addResource(CoapResource resource)
	{
		coapServer.add(resource);
	}
	
	
	public void startCoapServer()
	{
	   coapServer.start();	
	}
	
	
	public void stopCoapServer()
	{
		coapServer.stop();
	}
	
	public void addEndPoint()
	{
		
	}
	

}
