package ioTConnectedDevicesGateWay.labs.module6;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



public class MqttClientConnector implements MqttCallback {
	
	
	String _protocol;
	String _host;
	String _port;
	
	protected String _clientID;
	protected String _brokerAddr;
	protected MqttClient _mqttClient;
    
	
	
	public MqttClientConnector(String protocol,String host, String port)
	{		
		_protocol=protocol;
		_host=host;
		_port=port;
		
		_clientID=MqttClient.generateClientId();
		_brokerAddr=protocol+"://"+host+":"+port;	
	}
	
	
	
	public void connect() 
	{
		if(_mqttClient==null)
		{
			
			try 
			{
				_mqttClient= new MqttClient(_brokerAddr, _clientID,new MemoryPersistence());
				MqttConnectOptions option= new MqttConnectOptions();
				option.setCleanSession(true);
				_mqttClient.setCallback(this);
				_mqttClient.connect(option);
				System.out.println("Connect to Broker:"+_brokerAddr);
			}
			catch(MqttException e)
			{
				System.out.println(e.toString());
				System.out.println(e.getMessage());
				
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage()+" "+e.toString());
			}
			
		}
		else
		{
			System.out.println("MqttClient has not been intialized");
		}
		
		
	}
	

	
	public void disconnect()
	{
	   try
	   {
		   _mqttClient.disconnect();
		   System.out.println("MqttClient disconnect from:"+_brokerAddr);
	   }
	   catch(MqttException e)
	   {
		   System.out.println(e.toString());
		   System.out.println(e.getMessage());
	   }
	   catch(Exception e)
	   {
			System.out.println(e.getMessage()+" "+e.toString());
	   }
		
		
	}
	
	
	
	public boolean publishMessage(String topic, int qos, byte[] payload)
	{
		
		try
		{ 
			MqttMessage message= new MqttMessage();
			message.setPayload(payload);
			message.setQos(qos);
			System.out.println("Publishing message...");
			_mqttClient.publish(topic, message);
			System.out.println("Published Success! MqttMessageID:"+message.getId());
			return true;
			
		}
		catch (MqttException e)
		{
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()+" "+e.toString());
		}
		
		return false;
		
	}
	
	

	
	public boolean subscribeAll()
	{
		try
		{
			_mqttClient.subscribe("$SYS/#");
			return true;
		}
		catch(MqttException e)
		{
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()+" "+e.toString());
		}
		
		return false;
		
	}


		
	public boolean subscribeTopic(String topic, int qos)
	{
		
        try
        {
		_mqttClient.subscribe(topic,qos);
		System.out.println("Subscribed to Topic:"+topic);
		return true;
        }
        catch(MqttException e)
        {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
        }
		
		return false;
		
	}
	
	
	public boolean unsubscribeTopic(String topic)
	{
		
        try
        {       	
        _mqttClient.unsubscribe(topic);
		System.out.println("Unsubscribed to Topic:"+topic);
		return true;
        }
        catch(MqttException e)
        {
			System.out.println("Failed to unSubscribe Topic:"+topic);
			System.out.println(e.getMessage());
        }
		
		return false;
		
	}
	
	


	public void connectionLost(Throwable cause) {
		
		System.out.println("Connection lost!");
		
	}
	
	public void deliveryComplete(IMqttDeliveryToken token) {
		
		System.out.println("Delievey Complete!");
		
	}
	

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		System.out.println("Message Arrived MessageID:"+message.getId());
		System.out.println(message.toString());
		
	}

}
