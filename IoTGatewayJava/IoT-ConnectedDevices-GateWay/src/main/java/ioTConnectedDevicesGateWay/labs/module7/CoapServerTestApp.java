package ioTConnectedDevicesGateWay.labs.module7;

public class CoapServerTestApp {

     
	public static void main(String[] args) throws InterruptedException{
		
		SensorDataResource tempResourceHandler= new SensorDataResource("TempSensorData");
		CoapServerConnector coapServer= new CoapServerConnector(tempResourceHandler);
		coapServer.startCoapServer(); //start coap sever thread
		
	}

}
