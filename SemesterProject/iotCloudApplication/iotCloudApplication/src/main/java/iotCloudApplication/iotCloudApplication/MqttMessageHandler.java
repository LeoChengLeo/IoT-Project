package iotCloudApplication.iotCloudApplication;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
   private String  httpPostServiceURL;
 
   

	public MqttMessageHandler(String protocol, String host, int port,String subTopic,String pubTopic,String httpPostServiceURL) {
		super(protocol,host,port);
		this.subscribeTopic=subTopic;
		this.publishTopic=pubTopic;
		
		this.httpPostServiceURL=httpPostServiceURL;
		this.sensorData= new SensorData();
	}
	
	
	
	public MqttMessageHandler setSNSTopic(String pubSNSTopic)
	{
		this.publishTopic=pubSNSTopic;
		return this;
	}
	
	
	public MqttMessageHandler setHttpServiceHost(String httpServiceHost)
	{
		this.httpPostServiceURL=httpServiceHost;
		return this;		
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
		   
		   //Publish SensorData to AWS SNS Topic and send HTTPPost Request to cloud Web server
		   return publishSensorDataToSNS(sensorData.toJson().toString())&&uploadSensorDataToDB();
		   	   
	}
	
	
	
	//Publish SensorData to Aws SNS Topic for tiggering lambda function to send senorData Notification email
	private boolean publishSensorDataToSNS(String snsMessage) 
	{                                                  
		
		try
		{
			
			
		   	//Set awsCredential
	        AWSCredentialsProvider awsCredentialsProvider= new ClasspathPropertiesFileCredentialsProvider();
	        AWSCredentials awsCredentials=awsCredentialsProvider.getCredentials();
	        
	        AmazonSNSClient snsClient= new AmazonSNSClient(awsCredentials);
	              
	        PublishRequest publishRequest=new PublishRequest(publishTopic,snsMessage);

	            System.out.println("Publishing Temperature sensorData to SNS Topic....");
	            PublishResult publishResult=snsClient.publish(publishRequest);
	            System.out.println(publishRequest.getMessage());
	            System.out.println("Successfully publish message to SNS Topic");
	           
	            return true;
		}
	    catch (Exception e) 
		{
	
		       System.out.println("Failed to publish Message to SNS topic "+publishTopic+" "+e.getMessage());
		       return false;
		}
	}
	
	
	
	
	
	//Send a HTTP POST Request to cloud web server for saving temperature sensorData to the database 
	private boolean uploadSensorDataToDB()
	{
				
		try
		{
						
			CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(httpPostServiceURL);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(sensorData.toJson().toString())); 
            
            System.out.println("Sending post request to "+httpPostServiceURL);
            
            HttpResponse response = client.execute(request);
        
            if(response.getStatusLine().getStatusCode()==201)
            {
            	System.out.println("Successfully uploaded new sensorData to DataBase....");
            	return true;
            }
           
            System.out.println("Failed to upload sensorData to DataBase "+response.toString());
            return false;
            
		    	
		}
		catch (Exception e) 
		{
	         System.out.println("Failed to upload sensorData to DataBase"+e.getMessage());		
	         return false;
		}
	}
	
	
	
	
	public boolean subscribeSensorData(int qos) 
	{		
		return super.subscribeTopic(subscribeTopic, qos);
	}
	
}
