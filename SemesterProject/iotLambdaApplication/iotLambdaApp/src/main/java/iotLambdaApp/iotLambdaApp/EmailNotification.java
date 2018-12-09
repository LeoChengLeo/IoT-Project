package iotLambdaApp.iotLambdaApp;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONObject;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;




/*
 * This class with method handleRequest will be upload to AWS Lambda. Method only runs when any message publish to ASW SNS Topic 
 * In this case, any temperature sensorData arrive cloud broker will trigger this lambda function to send temperature sensorData email notification to user  
 * */

public class EmailNotification implements RequestHandler<SNSEvent,Object>{
	

	public Object handleRequest(SNSEvent request, Context context)
	{
		String time=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
		
		context.getLogger().log("Invocation started: "+time);
		context.getLogger().log("Number of records: "+request.getRecords().size());
		context.getLogger().log("Record Message: "+request.getRecords().get(0).getSNS().getMessage());
		
		
		String sensorDataJsonStr=request.getRecords().get(0).getSNS().getMessage();
		JSONObject sensorjsonMessage;
		SensorData sensorData= new SensorData();
				
		   try
		   {
			   
			   
		   //New SensorData arrive and fresh new sensorData from json
			sensorjsonMessage=new JSONObject(sensorDataJsonStr);
			sensorData.fromJson(sensorjsonMessage);
			System.out.println("New SensorData arrived....");
			System.out.println(sensorData.toString());
						
			
			//Set awsCredential
	        AWSCredentialsProvider awsCredentialsProvider= new ClasspathPropertiesFileCredentialsProvider();
	        AWSCredentials awsCredentials=awsCredentialsProvider.getCredentials();

	        

	        //Set a send email request
	        String FROM="cheng.li@husky.neu.edu";
	        String TO="leoleoleocheng@gmail.com";
	        String BODY=sensorData.toString();
	        String SUBJECT="Temperature SensorData Notification";
	        Destination destination=new Destination().withToAddresses(TO);
	        Content subject = new Content().withData(SUBJECT);
	        Content textBody= new Content().withData(BODY);
	        Body body= new Body().withText(textBody);
	        Message message= new Message().withSubject(subject).withBody(body);
	        SendEmailRequest emailRequest= new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
			
			//Send Notification Email			
	        AmazonSimpleEmailService emailSender= AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.US_EAST_1).build();
	        emailSender.sendEmail(emailRequest);
            System.out.print("Successfully sent sensorData Notification email....");
	        	        
			
		   }catch (Exception e) 
		   {
			System.out.println("Failed to sned temperature SensorData Notification. SensorMessage: "+sensorDataJsonStr);
		    System.out.println(e.getMessage());
		    return false;
		   }		
		
			
		
		time= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
		context.getLogger().log("Invocation Complete at: "+ time);
		return null;
				
	}

	
	
	
	
	
	
}
