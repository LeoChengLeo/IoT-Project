package ioTConnectedDevicesGateWay.labs.module6;
import ioTConnectedDevicesGateWay.labs.common.*;

public class MqttPubClientApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		SensorData sensorData = new SensorData("Temperature");
		sensorData.addNewValue(21);	
		MqttClientConnector mqttPubConnector=new MqttClientConnector("tcp", "192.168.0.8", "1883");
		mqttPubConnector.connect();	
		mqttPubConnector.publishMessage("ILoveCoding",0,sensorData.toXMLString().getBytes());
		
		mqttPubConnector.publishMessage("ILoveCoding",1,sensorData.toXMLString().getBytes());
		
		mqttPubConnector.publishMessage("ILoveCoding",2,sensorData.toXMLString().getBytes());

	}

}
