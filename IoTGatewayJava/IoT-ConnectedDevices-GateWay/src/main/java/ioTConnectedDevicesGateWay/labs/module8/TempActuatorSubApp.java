package ioTConnectedDevicesGateWay.labs.module8;

public class TempActuatorSubApp {

	public static void main(String[] args) 
	{
				
		runUbidotsSubTest(); 
	 
	}
	
	
	// AWS Cloud Integration Sub Test
	public static void runMyMqttSubTest()
	{
		//connect to the mosquitto broker which running on AWS ec2 instance using a SSL connection
	    MqttClientConnector mqttSubConnector=new MqttClientConnector("leochengleo-cloud-service.me", "c://users/leo/Documents/certs/ca.crt","leoCheng","0000");
		mqttSubConnector.connect();
		mqttSubConnector.subscribeTopic("actuatorData",2);
		
	}
	
	// Ubidots Cloud Integration Sub Test
	public static void runUbidotsSubTest()
	{   
		//connect to the ubidots broker using a SSL connection
		MqttClientConnector mqttSub= new MqttClientConnector("things.ubidots.com", "C:\\Users\\Leo\\Documents\\ubidots.pem.txt","A1E-61Hbn9CAL7B1M8pmDKiOP4GYggAl9c","");
		mqttSub.connect();
		mqttSub.subscribeTopic("/v1.6/devices/homeiotgateway/tempactuator",0);
	}

}
