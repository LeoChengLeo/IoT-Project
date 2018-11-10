package ioTConnectedDevicesGateWay.labs.module8;
import ioTConnectedDevicesGateWay.labs.common.*;

public class TempSensorPubApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
	
		SensorData sensorData = new SensorData("Temperature");
		sensorData.addNewValue(10);
		
		MqttClientConnector mqttPubConnector=new MqttClientConnector("tcp", "leochengleo-cloud-service.me", "1883");
		mqttPubConnector.connect();	
		
		sensorData.addNewValue(35); //Test First SensorEvent
		System.out.println("Publish sensorData:"+sensorData.toJson().toString());
		mqttPubConnector.publishMessage("sensorData",2,sensorData.toJson().toString().getBytes());
		
		sensorData.addNewValue(18); //Test Second SensorEvent
		System.out.println("Publish sensorData"+sensorData.toJson().toString());
		mqttPubConnector.publishMessage("sensorData",2,sensorData.toJson().toString().getBytes());
		
		sensorData.addNewValue(25); //Test Third SensorEvent
		System.out.println("Publish sensorData"+sensorData.toJson().toString());
		mqttPubConnector.publishMessage("sensorData",2,sensorData.toJson().toString().getBytes());

	}

}
