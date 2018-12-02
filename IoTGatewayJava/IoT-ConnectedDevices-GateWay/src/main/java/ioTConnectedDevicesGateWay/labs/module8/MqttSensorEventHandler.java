package ioTConnectedDevicesGateWay.labs.module8;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import ioTConnectedDevicesGateWay.labs.common.SensorData;


//This sensorEventHandler class will subscribe sensorData from local broker 
//and publish relevant actuatorData base on sensorEvent back to local broker

public class MqttSensorEventHandler extends MqttClientConnector{

	
   private String subscribeTopic;      
   private String publishTopic;
   
   private SensorData sensorData;      //tempSensor
   private Float actuatorData=0.0f;    //tempActuator
   


	public MqttSensorEventHandler(String host, String caFilePath, String subTopic,String pubTopic) {
		
		//connect to the local broker using a SSL connection
		super(host,caFilePath,"host","host");
		this.subscribeTopic=subTopic;
		this.publishTopic=pubTopic;
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
		System.out.println("Successfully publish to topic "+publishTopic);
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		super.messageArrived(topic, message);
		System.out.println("Message Arrived from Topic "+subscribeTopic);
		if(!handleSensorEvent(message)) {System.out.println("Failed to handle sensorEvent");}
	}
	
	
    
	
	private boolean handleSensorEvent(MqttMessage message)
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
			System.out.println("Failed to convert MQTT SensorMessage:"+message.toString()+"to SensorData");
		    System.out.println(e.getMessage());
		    return false;
		   }		
			
	  
		if(sensorData.getCurrValue()>30)       // First sensorEvent 
		{
		   actuatorData=(sensorData.getAvgValue()-sensorData.getCurrValue());			   
		}
		else if(sensorData.getCurrValue()<20)  // Second sensorEvent 
		{
			actuatorData=(sensorData.getAvgValue()-sensorData.getCurrValue());
		}
		else
		{
			actuatorData=0.0f;
		}		
		
		return publishActuatorMessage(2); //Publish actuatorData back to Broker

	}
	
	
	//Custom publish method
	public boolean publishActuatorMessage(int qos) 
	{                                                  
		return super.publishMessage(publishTopic, qos,Float.toString(actuatorData).getBytes());
	}
	
	
	//Custom subscribe method
	public boolean subscribeSensorData(int qos) 
	{		
		return super.subscribeTopic(subscribeTopic, qos);
	}
	
}
