package iotGateWayApp.iotGateWayApp;


//This application will be running on local gateway VM with gateway mosquitto broker
public class App 
{
    public static void main( String[] args )
    {
       
    	MqttSensorEventHandler  mqttSensorEventHandler= new MqttSensorEventHandler("tcp", "localhost","52.207.33.252", 1883, "iot/sensorData/temperature", "iot/actuatorData/temperature","iot/cloud/sensorData/temperature");
    	mqttSensorEventHandler.connect();
    	mqttSensorEventHandler.subSensorDataFromGateway(1);
    	
    }
}
