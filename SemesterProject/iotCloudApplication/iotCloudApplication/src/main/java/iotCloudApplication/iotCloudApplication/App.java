package iotCloudApplication.iotCloudApplication;

import org.apache.http.HttpResponse;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class App 
{
    public static void main( String[] args )
    {
		MqttMessageHandler sensorEventHandler= new MqttMessageHandler("tcp",
				                                                      "52.207.33.252", 
				                                                      1883, 
				                                                      "iot/cloud/sensorData/temperature", 
				                                                      "arn:aws:sns:us-east-1:398590284929:IoTSensorDataNotification",
				                                                      "https://csye6225-fall2018-chengl.me:8080/csye6225Webapp-1.0-SNAPSHOT/iotService/SensorData"
				                                                      );
		sensorEventHandler.connect();
		sensorEventHandler.subscribeSensorData(1);
         	
    }
    
      

    
    private static void testPostSensorData()
    {
    	SensorData sensorData= new SensorData("Temperature");
    	sensorData.addNewValue(21.23f);
    	
        try 
        {
        	CloseableHttpClient client = HttpClientBuilder.create().build();
        	
            HttpPost request = new HttpPost("https://csye6225-fall2018-chengl.me:8080/csye6225Webapp-1.0-SNAPSHOT/iotService/SensorData");          
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(sensorData.toJson().toString(2)));
            HttpResponse response = client.execute(request);
            System.out.println(response.toString());
            
        }catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    }
    
    
    
    private static void testPublishSNSTopic()
    {
    	
    	SensorData sensorData= new SensorData("Temperature");
    	sensorData.addNewValue(21.23f);
    	
    	
    	//Set awsCredential
        AWSCredentialsProvider awsCredentialsProvider= new ClasspathPropertiesFileCredentialsProvider();
        AWSCredentials awsCredentials=awsCredentialsProvider.getCredentials();
        
        AmazonSNSClient snsClient= new AmazonSNSClient(awsCredentials);
        String topicARN= "arn:aws:sns:us-east-1:398590284929:IoTSensorDataNotification";
       

        PublishRequest publishRequest=new PublishRequest(topicARN,sensorData.toJson().toString());

        try
        {
            PublishResult publishResult=snsClient.publish(publishRequest);
            System.out.println("Successfully publish message to SNS Topic");
           
        }
        catch (Exception e)
        {
            System.out.println("Failed to publish userName and token..."+e.toString()+" "+e.getMessage());
           
        }        
        

    	
    	
    }
    
    
    
    
}
