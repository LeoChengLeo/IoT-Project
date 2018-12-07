package iotGateWayApp.iotGateWayApp;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import iotGateWayApp.iotGateWayApp.MqttClientConnector;





public class MqttAirConActuatorDataTransfer extends MqttClientConnector{

	
	
	private String subTopicFromCloud;
	private String pubTopicToGateway;
	
	private MqttClientConnector actuatorDataTransfer;
	
	private static final Logger _logger= Logger.getLogger(MqttAirConActuatorDataTransfer.class.getName());
	
	public  MqttAirConActuatorDataTransfer(String protocol, int port, String localhost, String remoteHost,String subTopicFromCloud,String pubTopicToGateway) 
	{   
		super(protocol,remoteHost,port);
		
		this.subTopicFromCloud=subTopicFromCloud;
		this.pubTopicToGateway=pubTopicToGateway;
       
		//Connect to the local gateway broker
		actuatorDataTransfer= new MqttClientConnector(protocol,localhost,port);
		actuatorDataTransfer.connect();
        		
	}
	
	
	/*
	 Custom CallBack method when airConditioner ActuatorData arrive from cloud, 
	 then tiger actuatorDataTransfer transfer the new arrived actuator to local 
	 gateway broker. 
	*/
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		super.messageArrived(topic, message);
		
		_logger.info("New AirConditioner ActuatorData:"+message.toString()+" arrived from topic: "+topic);
		_logger.info("Transfering AirConditioner ActuatorData to loacl Gateway....");
		

		
		//Transfer the airConditioner ActuatorData to local gateway broker
        if(!actuatorDataTransfer.publishMessage(pubTopicToGateway, 1, message.toString().getBytes()))
        {
        	_logger.log(Level.SEVERE,"Failed to transfer AirConditioner ActuatorData:"+message.toString());
        	
        }		
	}
	

	
	//listen on AirConditioner ActuatorData from cloud
	public boolean subAirConActuatorDataFromCloud(int qos)
	{
		return super.subscribeTopic(subTopicFromCloud, qos);
		
	}
	
	
	
	
	
}
