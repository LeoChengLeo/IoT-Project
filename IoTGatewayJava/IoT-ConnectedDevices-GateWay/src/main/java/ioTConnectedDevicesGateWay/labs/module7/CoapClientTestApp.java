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
		
		SensorData tempSensorData= new SensorData("Temperature");
		tempSensorData.addNewValue(25);
		
		coap_client.pingServer();
		coap_client.sendPostRequest(tempSensorData.toJson().toString(),"TempSensorData",true);
		coap_client.sendGetRequest("TempSensorData", true);
		coap_client.sendPutRequest(tempSensorData.toString().toString(),"TempSensorData",true);
		coap_client.sendDeleteRequest("TempSensorData",true);
		
	}

}
