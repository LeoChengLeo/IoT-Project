package ioTConnectedDevicesGateWay.labs.module6;

public class MqttSubClientApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MqttClientConnector mqttConnector= new MqttClientConnector("tcp", "34.238.168.201", "1883");
		mqttConnector.connect();
		mqttConnector.subscribeTopic("iot/actuatorData/airConditioner",1);
		
	}

	
	
	
}
