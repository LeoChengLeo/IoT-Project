package ioTConnectedDevicesGateWay.labs.module8;

public class TempActuatorSubApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MqttClientConnector mqttConnector= new MqttClientConnector("tcp", "leochengleo-cloud-service.me", "1883");
		mqttConnector.connect();
		mqttConnector.subscribeTopic("actuatorData",2);
		
	}

}
