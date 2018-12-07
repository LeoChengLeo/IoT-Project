package iotCloudApplication.iotCloudApplication;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;




public class MqttClientConnector implements MqttCallback {
	
	
	String _protocol;
	String _host;
	String _port;
	
	protected String _clientID;
	protected String _brokerAddr;
	protected MqttClient _mqttClient;
    protected boolean isSecure;
	protected String _pemFileName;
	
	private static final Logger _logger= Logger.getLogger(MqttClientConnector.class.getName());
	
	//use this constructor to connect the remote broker with clean session 
	public MqttClientConnector(String protocol,String host, int port)
	{				
		isSecure=false;
		_protocol=protocol;
		_host=host;
		_port=Integer.toString(port);
		_clientID=MqttClient.generateClientId();
		_brokerAddr=protocol+"://"+host+":"+port;
	}
	
	
	//use this constructor to connect the broker using a SSL connection
	public MqttClientConnector(String host, String caFilePath)
	{
		isSecure=true;
		_protocol="ssl";
		_host=host;
		_port="8883";
		_clientID=MqttClient.generateClientId();
		_brokerAddr=_protocol+"://"+host+":8883";
		
	}
	
	
	public void connect() 
	{
		if(_mqttClient==null)
		{
			
			try 
			{
				_mqttClient= new MqttClient(_brokerAddr, _clientID,new MemoryPersistence());
				MqttConnectOptions option= new MqttConnectOptions();
				
				
				if(isSecure)
				{
					initSecureConnection(option);
				}
				else
				{
					option.setCleanSession(true);
				}
				
				
				_mqttClient.setCallback(this);
				_mqttClient.connect(option);
				
				_logger.info("Successfully connect to Broker:"+_brokerAddr);			 
			}
			catch(MqttException e)
			{
				
				_logger.info("Failed to connect to broker "+_brokerAddr+" "+e.getMessage());
				
			}
			catch(Exception e)
			{
				_logger.info("Failed to connect to broker "+_brokerAddr+" "+e.getMessage());
			}
			
		}
		else
		{
			_logger.warning("MqttClient has not been intialized");
		}
		
		
	}
	

	
	
	private void initSecureConnection(MqttConnectOptions options)
    {
	 try {
		 
		 	SSLContext sslContext = SSLContext.getInstance("SSL");
		 	KeyStore keyStore = readCertificate();
		    TrustManagerFactory trustManagerFactory =
		    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		    trustManagerFactory.init(keyStore);
		    sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
		    options.setSocketFactory(sslContext.getSocketFactory());
		    
	 } catch (Exception e) 
	 {
	 
		 _logger.log(Level.SEVERE,"Failed to initialize secure MQTT connection "+e.getMessage());

	 }
	 
	 }
	
	
	
	
	 private KeyStore readCertificate() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	 {
		 
		 KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		 FileInputStream fis = new FileInputStream(_pemFileName);
		 BufferedInputStream bis = new BufferedInputStream(fis);
		 CertificateFactory cf = CertificateFactory.getInstance("X.509");
		 ks.load(null);
		 while (bis.available() > 0) 
		 {
			 Certificate cert = cf.generateCertificate(bis);
			 ks.setCertificateEntry("adk_store" + bis.available(), cert);
	     }
	     return ks;
	 }

	
	
	
	public void disconnect()
	{
	   try
	   {
		   _mqttClient.disconnect();
		   _logger.info("MqttClient disconnected from:"+_brokerAddr);
		   
	   }
	   catch(MqttException e)
	   {		  
		   _logger.log(Level.SEVERE,"Failed to disconnect from broker "+_brokerAddr+" "+e.getMessage());
		   
	   }
	   catch(Exception e)
	   {
		   _logger.log(Level.SEVERE,"Failed to disconnect from broker "+_brokerAddr+" "+e.getMessage());
	   }
			
	}
	
	
	
	
	
	public boolean publishMessage(String topic, int qos, byte[] payload)
	{
		
		try
		{ 
			MqttMessage message= new MqttMessage();
			message.setPayload(payload);
			message.setQos(qos);
			
			_logger.info("Publishing message to topic:"+topic+"...");
			
			_mqttClient.publish(topic, message);
			
			_logger.info("Successfully published message to topic:"+topic+" MqttMessageID:"+message.getId());
			
			return true;
			
		}
		catch (MqttException e)
		{
						
		   _logger.log(Level.SEVERE,"Failed to publish message to broker "+ _brokerAddr+ " topic:"+topic+" "+e.getMessage());
		
		}
		catch(Exception e)
		{
			_logger.log(Level.SEVERE,"Failed to publish message to broker "+ _brokerAddr+ " topic:"+topic+" "+e.getMessage());
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
						
			_logger.log(Level.SEVERE,"Failed to subscribe all topic on broker "+ _brokerAddr+" "+e.getMessage());

		}
		catch(Exception e)
		{
			_logger.log(Level.SEVERE,"Failed to subscribe all topic on broker "+ _brokerAddr+" "+e.getMessage());

		}
		
		return false;
		
	}


		
	public boolean subscribeTopic(String topic, int qos)
	{
		
        try
        {
		_mqttClient.subscribe(topic,qos);
		
		_logger.info("Subscribed to Topic:"+topic+" Broker address: "+_brokerAddr);
		
		return true;
        }
        catch(MqttException e)
        {
        _logger.log(Level.SEVERE,"Failed to subscribe all topic on broker "+ _brokerAddr+" "+e.getMessage());
        }
		
		return false;
		
	}
	
	

	public void connectionLost(Throwable cause) 
	{
		
		_logger.warning("Connection lost from broker: "+_brokerAddr);
		
	}
	
	public void deliveryComplete(IMqttDeliveryToken token) 
	{
				
		_logger.info("Delievey to"+ this._brokerAddr+" Complete");
		
	}
	

	public void messageArrived(String topic, MqttMessage message) throws Exception 
	{
			
		_logger.info("Message Arrived from broker:"+_brokerAddr+" Topic:"+topic+" ID:"+message.getId()+" Message:"+message.toString());
		
		
	}

}
