package ioTConnectedDevicesGateWay.labs.module6;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class SimpleMqttTest {

	
	private String topic="MqttTest";
	private String content= "TestContent";
	private String broker="tcp://192.168.0.8:1883";
	private MqttClient mqttClient;	
	private MemoryPersistence persistence= new MemoryPersistence();
    private MqttConnectOptions option= new MqttConnectOptions();
    private MqttMessage message= new MqttMessage();
	
	
	
	public void subscriberTest()
	{
		

			
		try		
		{
			
			mqttClient=new MqttClient(broker, MqttClient.generateClientId(),new MemoryPersistence());
			option.setCleanSession(true);
			System.out.println("Connect to Broker");
			mqttClient.connect(option);
			System.out.println("Connected");
			mqttClient.subscribe(topic,1);
			
		}catch(MqttException e)
		{
			System.out.println(e.getMessage()+" "+e.toString());
			
		}
		
	}
	
	
	public void publisherTest()
	{
		
		try
		{   
			
			mqttClient=new MqttClient(broker, MqttClient.generateClientId(),persistence);
			option.setCleanSession(true);
			System.out.println("Connect to Broker:"+broker);
            mqttClient.connect(option);
            System.out.println("Connected!");
            System.out.println("Publish message: "+content);
            message.setPayload(content.getBytes());
            message.setQos(2);
            mqttClient.publish(topic, message);
            System.out.println("Topic published!");
            mqttClient.disconnect();
            System.out.println("Disconnect!");
		
		}
		catch (MqttException e) {
			
			System.out.print(e.toString()+" "+e.getMessage());
		}
		
		
	}
	
	
	
	
	
	
	
	

}
