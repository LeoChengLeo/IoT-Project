package ioTConnectedDevicesGateWay.labs.module8;
import ioTConnectedDevicesGateWay.labs.common.*;

public class TempSensorPubApp {

	public static void main(String[] args) {
		
				
       runUbidotsBrokerTest();
       runMyBrokerTest();

	}
	
	
	
	public static void runMyBrokerTest()
	{
		SensorData sensorData = new SensorData("Temperature");
		sensorData.addNewValue(10);
		
		//connect to the broker using a SSL connection
		 MqttClientConnector mqttPubConnector=new MqttClientConnector("leochengleo-cloud-service.me", "c:/users/leo/Documents/myMqttca.pem","leoCheng","0000");
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
	
	
	
	public static void runUbidotsBrokerTest()
	{
		
		MqttClientConnector mqttPub= new MqttClientConnector("things.ubidots.com","C:\\Users\\Leo\\Documents\\ubidots.pem.txt","A1E-61Hbn9CAL7B1M8pmDKiOP4GYggAl9c","");
		mqttPub.connect();
		
		mqttPub.publishMessage("/v1.6/devices/homeiotgateway/tempsensor",0, "15".getBytes());
		mqttPub.publishMessage("/v1.6/devices/homeiotgateway/tempsensor",0, "25".getBytes());
		mqttPub.publishMessage("/v1.6/devices/homeiotgateway/tempsensor",0, "35".getBytes());
		
		mqttPub.disconnect();
		
	}

}
