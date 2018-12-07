package ioTConnectedDevicesGateWay.labs.module6;
import ioTConnectedDevicesGateWay.labs.common.*;

public class MqttPubClientApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		SensorData sensorData = new SensorData("Temperature");
		sensorData.addNewValue(30);	
		
		MqttClientConnector mqttPubConnector=new MqttClientConnector("tcp", "192.168.0.54", "1883");
		
		mqttPubConnector.connect();	
		
		mqttPubConnector.publishMessage("iot/sensorData",0,sensorData.toJson().toString().getBytes());
		
//		mqttPubConnector.publishMessage("iot/sensorData",1,sensorData.toJson().toString().getBytes());
//		
//		mqttPubConnector.publishMessage("iot/sensorData",2,sensorData.toJson().toString().getBytes());

		mqttPubConnector.disconnect();
		mqttPubConnector.close();
		
		
	
	}

}
