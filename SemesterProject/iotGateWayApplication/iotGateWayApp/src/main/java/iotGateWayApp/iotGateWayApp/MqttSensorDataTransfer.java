package iotGateWayApp.iotGateWayApp;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;


public class MqttSensorDataTransfer extends MqttClientConnector{

	
   private String subTopicFromGateway;      
   private String pubTopicToCloud;
   
   private SensorData currentSensorData;     
   


	public MqttSensorDataTransfer(String protocol,String host, int port, String subTopic,String pubTopic,String pubTopicToCloud) 
	{
		
		super(protocol,host,port);
		
		this.subTopicFromGateway=subTopic;
		this.pubTopicToCloud=pubTopicToCloud;
		
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
		System.out.println("Successfully publish to topic "+pubTopicToCloud);
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		super.messageArrived(topic, message);
		System.out.println("Message Arrived from Topic "+subTopicFromGateway);
		if(!transferSensorData(message)) {System.out.println("Failed to handle sensorData");}
	}
	
	
    
	
	private boolean transferSensorData(MqttMessage message)
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
			
	  
		return pubSensorDataToCloud(2); //Publish SensorData to remote broker

	}
	
	
		
	public boolean pubSensorDataToCloud(int qos)
	{
		return super.publishMessage(pubTopicToCloud, qos, currentSensorData.toJson().toString().getBytes());
	}
	
	
	public boolean subSensorDataFromGateway(int qos) 
	{		
		return super.subscribeTopic(subTopicFromGateway, qos);
	}
	
}
