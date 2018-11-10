package ioTConnectedDevicesGateWay.labs.module8;

//This application will be running on the AWS ec2Instance with the remote MQTT broker
public class IoTCloudApp {

	public static void main(String[] args) {
	    
		MqttSensorEventHandler sensorEventHandler= new MqttSensorEventHandler("tcp", "localhost", "1883", "sensorData", "actuatorData");
		sensorEventHandler.connect();
		sensorEventHandler.subscribeSensorData(2);		
	}

}
