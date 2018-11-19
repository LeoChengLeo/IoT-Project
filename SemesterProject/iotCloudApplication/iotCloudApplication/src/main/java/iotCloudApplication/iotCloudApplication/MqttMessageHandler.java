package iotCloudApplication.iotCloudApplication;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;


public class MqttMessageHandler extends MqttClientConnector{

	
   private String subscribeTopic;      
   private String publishTopic;
   private SensorData sensorData; 
   private String  DBserviceHost;
   

	public MqttMessageHandler(String protocol, String host, int port,String subTopic,String pubTopic,String DBserviceHost) {
		super(protocol,host,port);
		this.subscribeTopic=subTopic;
		this.publishTopic=pubTopic;
		this.DBserviceHost=DBserviceHost;
		this.sensorData= new SensorData();
	}
	
	
	
	@Override
	public void connectionLost(Throwable cause) {
		super.connectionLost(cause);
		System.out.println("Lost connection to broker:"+_brokerAddr);
		
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		super.deliveryComplete(token);
		System.out.println("Successfully publish message to topic "+publishTopic);
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		super.messageArrived(topic, message);
		System.out.println("Message Arrived from Topic "+subscribeTopic);
		
		if(!handleMessage(message))
		{
			System.out.println("Failed to handle mqtt message "+message.toString());
		}		
	}
	
	

	
	private boolean handleMessage(MqttMessage message)
	{   		
		   JSONObject sensorjsonMessage=null;
		 
		   try
		   {
		   //New SensorData arrive and fresh new sensorData from json
			sensorjsonMessage=new JSONObject(message.toString());
			sensorData.fromJson(sensorjsonMessage);
			System.out.println("Succcessfully updatad new SensorData..");
		   }catch (Exception e) 
		   {
			System.out.println("Fail to convert MQTT SensorMessage:"+message.toString()+"to SensorData");
		    System.out.println(e.getMessage());
		    return false;
		   }	
		   
		   return (publishSensorDataToSNS(sensorData.toString())&&saveSensorDataToDB());
		   	   
	}
	
	
	
	private boolean publishSensorDataToSNS(String snsMessage) 
	{                                                  
		
		try
		{
			
	    return true;
		}
	    catch (Exception e) 
		{
	
		System.out.println("Failed to publish Message to  SNS topic "+publishTopic+" "+e.getMessage());
		return false;
		}
	}
	
	
	private boolean saveSensorDataToDB()
	{
		try
		{
			
		return true;	
		}
		catch (Exception e) 
		{
	    System.out.println("Failed to save sensorData to DataBase"+e.getMessage());		
	    return false;
		}
	}
	
	
	
	public boolean subscribeSensorData(int qos) 
	{		
		return super.subscribeTopic(subscribeTopic, qos);
	}
	
}
