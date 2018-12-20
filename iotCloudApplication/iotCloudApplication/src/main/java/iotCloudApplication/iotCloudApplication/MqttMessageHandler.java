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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MqttMessageHandler extends MqttClientConnector{

	
   private String subscribeTopic;       //Subscribe TempSensorData      
   private String publishTopic;         //AWS SNS Topic
   private SensorData sensorData;       //current TempSensorData
   private String  httpPostServiceURL;  //webServiceApp URL which handle HTTP Post TempSensorData Request 
 
   
   private AmazonSNSClient lambdaNotifier;      //Set aws SNSClient, will trigger lambda function
   private CloseableHttpClient httpClient;      //Set a HTTP Client will send senorData post request to iot webApp
   
   
   private static final Logger _logger= Logger.getLogger(MqttClientConnector.class.getName());

	public MqttMessageHandler(String protocol, String host, int port,String subTopic,String pubTopic,String httpPostServiceURL) {
		
		super(protocol,host,port);
		this.subscribeTopic=subTopic;
		this.publishTopic=pubTopic;
		
		this.httpPostServiceURL=httpPostServiceURL;
		this.sensorData= new SensorData();
		
		//Set awsCredential
        AWSCredentialsProvider awsCredentialsProvider= new ClasspathPropertiesFileCredentialsProvider();
        AWSCredentials awsCredentials=awsCredentialsProvider.getCredentials();
        
		this.httpClient= HttpClientBuilder.create().build();
        this.lambdaNotifier= new AmazonSNSClient(awsCredentials);
		
	}
	
	
	
	
	@Override//Overwrite callback messageArrived, Custom method when new temperature sensorData arrive, then triggering private method "handleMessage"
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		super.messageArrived(topic, message);
		
		if(!handleMessage(message))
		{
			_logger.log(Level.SEVERE,"Failed to handle MQTT message "+message.toString());
		
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
			_logger.info("Succcessfully updatad new SensorData");
			
		   }catch (Exception e) 
		   {
			   
			_logger.log(Level.SEVERE,"Fail to convert MQTT SensorMessage:"+message.toString()+"to SensorData "+e.getMessage());
		    return false;
		   }
		   
		   //Send HTTPPost Request to cloud Web server for saving sensorData to DataBase
		   //Publish SensorData to AWS SNS Topic for triggering lambdaFunction to send notification email 
		   return uploadSensorDataToDB()&&publishSensorDataToSNS(sensorData.toJson().toString());
		   	   
	}
	
	
	
	//Publish SensorData to AWS SNS Topic for triggering lambda function to send senorData Notification email
	private boolean publishSensorDataToSNS(String snsMessage) 
	{                                                  
		
		try
		{
						        
	        //Set aws SNS publish request
	        PublishRequest publishRequest=new PublishRequest(publishTopic,snsMessage);

	        _logger.info("Publishing Temperature sensorData to SNS Topic: "+publishTopic);
	        
	        //Publish temperature sensor Data to aws SNS Topic for triggering EmailNotification lambda function
	        PublishResult publishResult=lambdaNotifier.publish(publishRequest);
	            
	        _logger.info(" Successfully publish message to SNS Topic:"+publishTopic+" "+publishRequest.getMessage());
	           
	        return true;
		}
	    catch (Exception e) 
		{
	
		    _logger.log(Level.SEVERE,"Failed to publish Message to SNS topic "+publishTopic+" "+e.getMessage());   
	 
		    return false;
		}
	}
	
	
	
	
	
	//Send a HTTP POST Request to cloud web server for saving temperature sensorData to the database 
	private boolean uploadSensorDataToDB()
	{
				
		try
		{
						
			//Set a HTTP Post request with content type json
            HttpPost request = new HttpPost(httpPostServiceURL);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(sensorData.toJson().toString())); 
            
            //Send HTTP Post request
            _logger.info("Sending post request "+httpPostServiceURL);
            HttpResponse response = httpClient.execute(request);
        
            //Check if response code is 201 "created"
            if(response.getStatusLine().getStatusCode()==201)
            {
            	_logger.info("Successfully uploaded new temperature sensorData to DataBase");
            	return true;
            }
           
               _logger.log(Level.SEVERE,"Failed to upload temperature sensorData to DataBase "+response.toString());
               return false;
            
		    	
		}
		catch (Exception e) 
		{
			  _logger.log(Level.SEVERE,"Failed to upload temperature sensorData to DataBase "+e.getMessage());		
	         return false;
		}
	}
	
	
	
	//Custom Subscribe Method for listening on any new temperature sensorData 
	public boolean subscribeSensorData(int qos) 
	{		
		return super.subscribeTopic(subscribeTopic, qos);
	}
	
}
