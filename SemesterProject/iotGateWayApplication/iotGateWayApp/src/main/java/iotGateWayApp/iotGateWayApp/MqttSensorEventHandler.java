package iotGateWayApp.iotGateWayApp;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;


public class MqttSensorEventHandler extends MqttClientConnector{

	
   private String subTopicFromGateway;      
   private String pubTopicToGateway;
   private String pubTopicToCloud;
   
   private MqttClientConnector sensorDataTransfer;
   
   private SensorData currentSensorData;      //tempSensor
   private Float currentActuatorData=0.0f;    //tempActuator
   


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
	
	
	
	@Override
	public void connectionLost(Throwable cause) {
		super.connectionLost(cause);
		System.out.println("Lost connection to broker:"+_brokerAddr);
		
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		super.deliveryComplete(token);
		System.out.println("Successfully publish to topic "+pubTopicToGateway);
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		super.messageArrived(topic, message);
		System.out.println("Message Arrived from Topic "+subTopicFromGateway);
		if(!handleSensorEvent(message)) {System.out.println("Failed to handle gatewayEvent");}
	}
	
	
    
	
	private boolean handleSensorEvent(MqttMessage message)
	{   		
		   JSONObject sensorjsonMessage=null;
		 
		   try
		   {
		   //New SensorData arrive and fresh new sensorData from json
			sensorjsonMessage=new JSONObject(message.toString());
			currentSensorData.fromJson(sensorjsonMessage);
			System.out.println("Succcessfully updatad new SensorData..");
		   }catch (Exception e) 
		   {
			System.out.println("Failed to convert MQTT SensorMessage:"+message.toString()+"to SensorData");
		    System.out.println(e.getMessage());
		    return false;
		   }		
			
	  
		if(currentSensorData.getCurrValue()>25)       // First sensorEvent 
		{
		   currentActuatorData=(currentSensorData.getAvgValue()-currentSensorData.getCurrValue());			   
		}
		else if(currentSensorData.getCurrValue()<20)  // Second sensorEvent 
		{
			currentActuatorData=(currentSensorData.getAvgValue()-currentSensorData.getCurrValue());
		}
		else
		{
			currentActuatorData=0.0f;
		}		
		
		return pubActuatorDataToGateway(2)&&pubSensorDataToCloud(2); //Publish actuatorData back to broker and transfer sensorData to remote cloud 

	}
	
	
	
	//Publish actuatorData back to local Broker
	public boolean pubActuatorDataToGateway(int qos) 
	{                                                  
		return super.publishMessage(pubTopicToGateway, qos,Float.toString(currentActuatorData).getBytes());
	}
	
	
	//Transfer sensorData to cloud by publishing sensorData to remote broker
	public boolean pubSensorDataToCloud(int qos)
	{
		return sensorDataTransfer.publishMessage(pubTopicToCloud, qos, currentSensorData.toJson().toString().getBytes());
	}
	
	
	//Subscribe sensorData from local broker 
	public boolean subSensorDataFromGateway(int qos) 
	{		
		return super.subscribeTopic(subTopicFromGateway, qos);
	}
	
}
