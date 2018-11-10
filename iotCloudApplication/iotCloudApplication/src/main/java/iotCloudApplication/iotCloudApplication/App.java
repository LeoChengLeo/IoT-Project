package iotCloudApplication.iotCloudApplication;


public class App 
{
    public static void main( String[] args )
    {
		MqttSensorEventHandler sensorEventHandler= new MqttSensorEventHandler("tcp", "localhost", "1883", "sensorData", "actuatorData");
		sensorEventHandler.connect();
		sensorEventHandler.subscribeSensorData(2);
    }
}
