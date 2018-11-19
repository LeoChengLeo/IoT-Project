package ioTConnectedDevicesGateWay.labs.module7;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.json.JSONObject;

import ioTConnectedDevicesGateWay.labs.common.SensorData;


public class SensorDataResource extends CoapResource {

	
	private SensorData sensorDataResource=new SensorData();
	
	public SensorDataResource(String name) {
		super(name);
		setObservable(true);
		setObserveType(Type.CON);
		getAttributes().setObservable();
	}

	
	@Override
	public void handleGET(CoapExchange exchange) 
	{
		System.out.println("GET request from:"+exchange.getSourceAddress());
		
		if(sensorDataResource==null)
		{
			exchange.respond(ResponseCode.NOT_FOUND,"NO SensorData Avaliable");
		}
		else
	    {
			exchange.respond(sensorDataResource.toJson().toString());
	    }
		
	}
	
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		
		System.out.println("Post request from:"+exchange.getSourceAddress());
		String payload=new String(exchange.getRequestPayload());
			
		try
		{   
			JSONObject jsonSensorData= new JSONObject(payload);
			sensorDataResource.fromJson(jsonSensorData);
			System.out.println("New SensorData updated...");
			System.out.println(payload);
			exchange.respond(ResponseCode.CREATED);
			changed(); //notify the observer when new sensorData update
		}
		catch(Exception e)
		{
			System.out.println("Could not convert payload "+payload+"to sensorDataResource..");
			System.out.println(e.getMessage());
			
		}
			
		
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {
	    
		System.out.println("PUT request from:"+exchange.getSourceAddress());
		exchange.respond(ResponseCode.CHANGED);
		
		
	}
	
	@Override
	public void handleDELETE(CoapExchange exchange) {
		
		System.out.println("DELETE request from:"+exchange.getSourceAddress());
		exchange.respond(ResponseCode.DELETED);
		
	}
	
	
	
	
}
