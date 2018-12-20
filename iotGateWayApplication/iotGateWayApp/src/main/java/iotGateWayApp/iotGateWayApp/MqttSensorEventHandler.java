package iotGateWayApp.iotGateWayApp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;


/*
 * This sensorEvent handler will subscribe to temperature sensor data. As any temperature sensorData arrive, 
 * will trigger custom callback method to do two things 
 * 1) Make simple decision to update proper temperature actuator and publish this back to gateway broker 
 * 2) Transfer new arrived temperature sensorData to cloud. 
 */

public class MqttSensorEventHandler extends MqttClientConnector{

	
   private String subTopicFromGateway;      
   private String pubTopicToGateway;
   private String pubTopicToCloud;
   
   /* This sensorDataTransfer will transfer the sensorData to cloud by publishing sensorData to Cloud Mqtt broker */
   private MqttClientConnector sensorDataTransfer;  
   
   private SensorData currentSensorData;      //tempSensor
   private Float currentActuatorData=0.0f;    //tempActuator
   
   private static final Logger _logger= Logger.getLogger(MqttSensorEventHandler.class.getName());
	


	public MqttSensorEventHandler(String protocol,String localhost, String remoteHost, int port, String subTopic,String pubTopic,String pubTopicToCloud) 
	{
		
		super(protocol,localhost,port);
		
		this.subTopicFromGateway=subTopic;
		this.pubTopicToGateway=pubTopic;
		this.pubTopicToCloud=pubTopicToCloud;
		
		this.sensorDataTransfer=new MqttClientConnector(protocol,remoteHost,port);
	    this.sensorDataTransfer.connect();
	    
		this.currentSensorData= new SensorData();
	}
	
	
	
	//Overwrite call back method, custom method when connection lost from local gateway broker
	@Override
	public void connectionLost(Throwable cause) {
		
		super.connectionLost(cause);

	}
	
	//Overwrite call back method, custom method when actuatorData successfully publish back to local gateway broker 
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		super.deliveryComplete(token);
		_logger.info("Successfully publish to topic "+pubTopicToGateway);
	}
	
	//Overwrite call back method, custom method when sensorData arrive from local gateway broker, then trigger method "handleSensorEvent"  
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		super.messageArrived(topic, message);
		
		if(!handleSensorEvent(message)) { _logger.log(Level.SEVERE,"Failed to handle gatewayEvent");}
	}
	
	
    
	//This method will be called when call back method "messageArrived" is triggered
	private boolean handleSensorEvent(MqttMessage message)
	{   		
		   JSONObject sensorjsonMessage=null;
		 
		   try
		   {
			   
		   //New SensorData arrive and fresh new sensorData from json String
			sensorjsonMessage=new JSONObject(message.toString());
			currentSensorData.fromJson(sensorjsonMessage);
			_logger.info("Succcessfully updatad new SensorData..");
			
		   }catch (Exception e) 
		   {
			
			_logger.log(Level.SEVERE,"Failed to convert MQTT SensorMessage:"+message.toString()+"to SensorData "+e.getMessage());   
		    return false;
		   }		
			
	  
		if(currentSensorData.getCurrValue()>25)       // First sensorEvent, temperature will be lowered 
		{
		   currentActuatorData=(25-currentSensorData.getCurrValue());
		   _logger.info("Updated new temperature Acutator data...."+"TempActuatorData="+currentActuatorData);
		   
		}
		else if(currentSensorData.getCurrValue()<22)  // Second sensorEvent, temperature will be increased
		{
			currentActuatorData=(22-currentSensorData.getCurrValue());
		   _logger.info("Updated new temperature Acutator data...."+"TempActuatorData="+currentActuatorData);
		   
		}
		else
		{
			currentActuatorData=0.0f;                 // No sensorEvent match
			_logger.info("No sensor events matched "+"TempActuatorData="+currentActuatorData);
			
		}		
		
		return pubActuatorDataToGateway(2)&&pubSensorDataToCloud(2); //Publish actuatorData back to broker and transfer sensorData to remote cloud 

	}
	
	
	
	//Publish actuatorData back to local gateway Broker
	public boolean pubActuatorDataToGateway(int qos) 
	{                                                  
		return super.publishMessage(pubTopicToGateway, qos,Float.toString(currentActuatorData).getBytes());
	}
	
	
	//Subscribe temperature sensorData from local broker 
	public boolean subSensorDataFromGateway(int qos) 
	{		
		return super.subscribeTopic(subTopicFromGateway, qos);
	}
	
	
	//Call sensorDataTransfer to transfer temperature sensorData to cloud by publishing temperature sensorData to cloud broker
	public boolean pubSensorDataToCloud(int qos)
	{
		return sensorDataTransfer.publishMessage(pubTopicToCloud, qos, currentSensorData.toJson().toString().getBytes());
	}
	
	
}
