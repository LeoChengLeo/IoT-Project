package iotGateWayApp.iotGateWayApp;
import java.util.logging.Logger;
import java.sql.Timestamp;

/*
 * This application is running on local VM which has Mosquitto broker set up. 
 * This app will listen on two variables 1) temperature sensor data 2) air conditioner actuator data. 
 * When temperature sensor data arrive from the device it will do simple analytic (Three Sensor Events) and publish corresponding temperature actuator data result back to gateway broker. 
 * When air conditioner actuator data arrive from cloud, it will transfer this actuator to the local gateway broker. 
 * Device side, Sensor Management App will subscribe to these two variables.
 * 
 */





//This application will be running on local gateway VM with gateway mosquitto broker
public class App 
{
	
	
	private static final Logger _logger= Logger.getLogger("IoTGateWayManagementApp");
	
	
	
    public static void main( String[] args )
    {
        
    	
    	_logger.info("Startig Gateway Management Application "+new Timestamp(System.currentTimeMillis()).toString());
    	
    	MqttSensorEventHandler  mqttSensorEventHandler= new MqttSensorEventHandler("tcp","localhost","34.238.168.201", 1883, "iot/sensorData/temperature", "iot/actuatorData/temperature","iot/cloud/sensorData/temperature");
    	mqttSensorEventHandler.connect();
    	mqttSensorEventHandler.subSensorDataFromGateway(1);
    	
    	MqttAirConActuatorDataTransfer mqttAirConActuatorDataTransfer= new MqttAirConActuatorDataTransfer("tcp", 1883,"localhost", "34.238.168.201", "iot/actuatorData/airConditioner", "iot/actuatorData/airConditioner");
    	mqttAirConActuatorDataTransfer.connect();
    	mqttAirConActuatorDataTransfer.subAirConActuatorDataFromCloud(1);
    	
       	
    }
}
