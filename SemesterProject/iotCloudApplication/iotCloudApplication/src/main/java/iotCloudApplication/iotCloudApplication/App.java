package iotCloudApplication.iotCloudApplication;

import java.util.logging.Logger;
import java.sql.Timestamp;


/*
 * This application is running on an AWS ec2 instance with Mosquitto broker set up. 
 * This app is responsible for bridging local application and data to cloud base services (HTTP Web Service, Data Storage, Lambda). 
 * This app will listen on variable “temperature sensor data”. As temperature sensor data arrive from local gateway, 
 * it will trigger a HTTP client to send a post request to IoT Web Service Server for saving a new sensor data to the database 
 * and as the post request send, it will publish this new temperature sensor data to AWS SNS Topic for triggering lambda function to send email notification (SMTP) to user.      
 * 
 */


public class App 
{
	
	private static final Logger _logger= Logger.getLogger("IoTCloudManagementApp");
	
    public static void main( String[] args )
    {
    	
    	
    	_logger.info("Starting Cloud Management Application"+new Timestamp(System.currentTimeMillis()).toString());
    	
		MqttMessageHandler sensorEventHandler= new MqttMessageHandler("tcp",
				                                                      "34.238.168.201", 
				                                                      1883, 
				                                                      "iot/cloud/sensorData/temperature", 
				                                                      "arn:aws:sns:us-east-1:398590284929:IoTSensorDataNotification",
				                                                      "https://csye6225-fall2018-chengl.me:8080/csye6225Webapp-1.0-SNAPSHOT/iotService/sensorData"
				                                                      );
		sensorEventHandler.connect();
		sensorEventHandler.subscribeSensorData(1); //Start listening on any new temperature sensorData
    	
    	
    }    
    
}
