package ioTConnectedDevicesGateWay.labs.module6;

public class MqttSubClientApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MqttClientConnector mqttConnector= new MqttClientConnector("tcp", "192.168.0.8", "1883");
		mqttConnector.connect();
		mqttConnector.subscribeTopic("ILoveCoding",2);
		
	}

}
