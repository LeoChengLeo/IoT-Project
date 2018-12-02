package ioTConnectedDevicesGateWay.labs.module7;

import ioTConnectedDevicesGateWay.labs.common.SensorData;

public class CoapClientTestApp {
    

	public static void main(String[] args)
	{

	   runTest();
	}
	
	
	public static void runTest()
	{
		CoapClientConnector coap_client= new CoapClientConnector("coap", "192.168.0.8","5683");
		
		//Create a Temperature SensorData for Testing
		SensorData tempSensorData= new SensorData("Temperature");
		tempSensorData.addNewValue(25);
		
		coap_client.pingServer();
		
		//Post a new sensorData to CoapServer
		coap_client.sendPostRequest(tempSensorData.toJson().toString(),"TempSensorData",true);
		//Retrieve current SensorData from CoapServer
		coap_client.sendGetRequest("TempSensorData", true);
		//Update a current SensorData from CoapServer
		coap_client.sendPutRequest(tempSensorData.toString().toString(),"TempSensorData",true);
		//Delete a current SensorData from CoapServer
		coap_client.sendDeleteRequest("TempSensorData",true);
		
	}

}
