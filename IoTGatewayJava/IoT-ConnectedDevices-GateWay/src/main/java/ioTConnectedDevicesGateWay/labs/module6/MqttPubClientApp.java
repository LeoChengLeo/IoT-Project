package ioTConnectedDevicesGateWay.labs.module6;
import ioTConnectedDevicesGateWay.labs.common.*;

public class MqttPubClientApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		SensorData sensorData = new SensorData("Temperature");
		sensorData.addNewValue(21);	
		
		MqttClientConnector mqttPubConnector=new MqttClientConnector("tcp", "192.168.0.13", "1883");
		
		mqttPubConnector.connect();	
		
		mqttPubConnector.publishMessage("TempSensorData",0,sensorData.toXMLString().getBytes());
		
		mqttPubConnector.publishMessage("TempSensorData",1,sensorData.toXMLString().getBytes());
		
		mqttPubConnector.publishMessage("TempSensorData",2,sensorData.toXMLString().getBytes());

		mqttPubConnector.disconnect();
	}

}
