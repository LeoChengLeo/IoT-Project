'''
Created on Sep 29, 2018
@author: Leo

'''

from threading import Thread
from time import sleep
from tempSensorAdaptor import TempSensorAdaptor
from configReader import ConfigReader
from tempActuatorEmulator import TempActuatorEmulator
from airConActuatorEmulater import AirConActuatorEmulator
import mqttClientConnector

'''
This application will be running on raspberry pi, which will collect temperature sensor data and publish this to the gateway broker every certain period of time. 
The application will listen on two variables, 1) temperature actuator data 2) air conditioner actuator data. 
As any of these actuator data arrive, will trigger corresponding actuator emulator (TempActuatorEmulator and AirConActuatorEmulator).
'''




class SensorManagementApp(Thread):

    tempSensorAdaptor=None
    tempSensorData=None
    
    tempActuatorEmulator=None
    airConActuatorEmulator=None
      
    mqttClient_TempSensor=None
    mqttClient_AirCon=None
     
    confiReader=None
    pollCycleSecs=None
    mqttGatewayHost=None
    mqttGatewayPort=None
    
       
    
    def __init__(self,configDir):
        
        Thread.__init__(self)
        
        #Set all the custom variables from configuration file
        self.confiReader=ConfigReader(configDir,"Device")   
        self.pollCycleSecs=int(self.confiReader.getProperty("pollCycleSecs")) 
              
        self.confiReader=ConfigReader(configDir,"mqtt.gateway") 
        self.mqttGatewayHost=self.confiReader.getProperty("host")
        self.mqttGatewayPort=int(self.confiReader.getProperty("port")) 
        
        #Start temperature Sensor Adaptor 
        self.tempSensorAdaptor=TempSensorAdaptor(configDir)
        self.tempSensorAdaptor.daemon=True
        self.tempSensorAdaptor.start()
        
        
        #Initialize Temperature Actuator Emulator and AirConditioner Actuator Emulator
        self.tempActuatorEmulator=TempActuatorEmulator()
        self.airConActuatorEmulator=AirConActuatorEmulator()
        
        
        #Initialize a mqttClient_TempSensor with custom call back method and listen on Temperature ActuatorData from gateway broker
        self.mqttClient_TempSensor=mqttClientConnector.MqttClientConnector(self.mqttGatewayHost,self.mqttGatewayPort, self.on_connect, self.on_message_TempActuator, self.on_publish_TempSensorData, self.on_subscribe_TempActuator)
        self.mqttClient_TempSensor.connect()
        self.mqttClient_TempSensor.subscribeTopic("iot/actuatorData/temperature",2)
             
        
        #Initialize a mqttClient_AirCon with custom call back method and listen on AirConditioner ActuatorData from gateway broker                  
        self.mqttClient_AirCon=mqttClientConnector.MqttClientConnector(self.mqttGatewayHost,self.mqttGatewayPort, self.on_connect, self.on_message_AirConActuator, self.on_publish_AirConStatus, self.on_subscribe_AirConActuator)         
        self.mqttClient_AirCon.connect()
        self.mqttClient_AirCon.subscribeTopic("iot/actuatorData/airConditioner",1)
                                                     
    
    
    # custom callback when connect to Gateway broker
    def on_connect(self,mqttc,obj,flags,rc):
        print("Successfully Connect to GatewayBroker!! rc: "+str(rc))
  
  
  
  
    
    #custom callback when subscribed actuatorData arrive, then tiger temperature ActuatorEmulator     
    def on_message_TempActuator(self,mqttc, obj, msg):
        print("TempActuatorData arrived from topic:"+msg.topic + " QoS:" + str(msg.qos) + " Message:" + str(msg.payload.decode("utf-8")))
        self.tempActuatorEmulator.process_message(str(msg.payload))
        
    #custom callback when Temperature SensorData publish success
    def on_publish_TempSensorData(self,mqttc, obj, mid):
        print("Successfully published SensorData to topic iot/sensorData/temperature !! mid: " + str(mid) )       
        
    #custom callback when successfully subscribe to Temperature Actuator
    def on_subscribe_TempActuator(self,mqttc, obj, mid, granted_qos):
        print("Successfully Subscribed to topic: iot/actuatorData/temperature !! " + str(mid) + " Granted_QoS:" + str(granted_qos))
    
    
    
    
    
    #custom callback method when subscribed AirConditionerActuatorData arrive, then tiger air Conditioner ActuatorEmulater      
    def on_message_AirConActuator(self,mqttc, obj, msg):
        print("AirConditionerActuatorData arrived from topic:"+msg.topic + " QoS:" + str(msg.qos) + " Message:" + str(msg.payload.decode("utf-8")))
        self.airConActuatorEmulator.process_message(str(msg.payload))
         
    #custom callback when air conditioner status publish success
    def on_publish_AirConStatus(self,mqttc, obj, mid):
        print("Successfully published AirConditionerStatus to topic iot/status/AirConditioner !! mid: " + str(mid) )
         
    #custom callback when air Conditioner actuatorData subscribe success
    def on_subscribe_AirConActuator(self,mqttc, obj, mid, granted_qos):
        print("Successfully Subscribed to topic: iot/actuatorData/airConditioner !! " + str(mid) + " Granted_QoS:" + str(granted_qos))
     
    
    
    
    def run(self):
        while True:
            #Read new temperature sensorData and publish to gateway broker        
            self.tempSensorData=self.tempSensorAdaptor.getCurrentSensorData()
            
            self.mqttClient_TempSensor.publishMessage("iot/sensorData/temperature",str(self.tempSensorData.toDict()),2)
                   
            sleep(self.pollCycleSecs) #wait for next poll
                       
                            
               
       
app=SensorManagementApp("C:/Users/Leo/Documents/ConnectedDevices.conf")
app.start()
while True:
    pass       
  

      
    
    
    
    
    
      
    
