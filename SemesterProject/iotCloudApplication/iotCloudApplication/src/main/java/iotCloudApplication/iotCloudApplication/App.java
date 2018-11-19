package iotCloudApplication.iotCloudApplication;


public class App 
{
    public static void main( String[] args )
    {
		MqttMessageHandler sensorEventHandler= new MqttMessageHandler("tcp", "localhost", 1883, "sensorData", "arn:aws:sns:us-east-1:398590284929:SensorEmailService","csye6225-fall2018-chengl.me");
		sensorEventHandler.connect();
		sensorEventHandler.subscribeSensorData(0);
    }
}
