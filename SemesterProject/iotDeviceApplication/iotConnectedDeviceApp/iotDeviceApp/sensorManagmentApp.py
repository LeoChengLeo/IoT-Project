'''
Created on Sep 29, 2018

@author: Leo
'''




from threading import Thread
from time import sleep
from tempSensorAdaptor import TempSensorAdaptor
from configReader import ConfigReader
import mqttClientConnector
import sense_hat




# custom callback when connect to broker
def on_connect(mqttc,obj,flags,rc):
    print("Successfully Connect to GatewayBroker!! rc: "+str(rc))
    
# custom callback when subscribed actuatorData arrive, tiger sensehatLED actuator     
def on_message(mqttc, obj, msg):
    print("TempActuatorData arrived from topic:"+msg.topic + " QoS:" + str(msg.qos) + " Message:" + str(msg.payload.decode("utf-8")))
    sh=sense_hat.SenseHat()
    enableLED=True
    count=0 
    while enableLED:
        sh.show_message(str(msg.payload),scroll_speed=0.01)
        count=count+1
        if count>=2: # show two time on sensehatLED
            enableLED=False  

#custom callback when publish success
def on_publish(mqttc, obj, mid):
    print("Successfully published SensorData to topic iot/sensorData/temperature !! mid: " + str(mid) )


#custom callback when subscribe success
def on_subscribe(mqttc, obj, mid, granted_qos):
    print("Successfully Subscribed to topic: iot/sensorData/temperature !! " + str(mid) + " Granted_QoS:" + str(granted_qos))



class SensorManagementApp(Thread):

    tempSensorAdaptor=None
    tempSensorData=None
    
    deviceConfiReader=None
    pollCycleSecs=None
    
    mqttClient=None
    
    
    def __init__(self,configDir):
        Thread.__init__(self)
        
        #senseHatLED activator threads are started when TempActuatorEmulator instance is initialized..  
        self.tempSensorAdaptor=TempSensorAdaptor(configDir)
        self.tempSensorAdaptor.daemon=True
        self.tempSensorAdaptor.start()
        
        #connect to broker and start callback loop 
        self.mqttClient=mqttClientConnector.MqttClientConnector("192.168.0.31",1883,on_connect, on_message, on_publish, on_subscribe)
        self.mqttClient.connect()
        self.mqttClient.subscribeTopic("iot/actuatorData/temperature", 2)
                                                                            
        self.deviceConfiReader=ConfigReader(configDir,"Device")   
        self.pollCycleSecs=int(self.deviceConfiReader.getProperty("pollCycleSecs"))                   
        
    
    def run(self):
        while True:
            #Read new sensorData and publish to gateway broker        
            self.tempSensorData=self.tempSensorAdaptor.getCurrentSensorData()
            
            self.mqttClient.publishMessage("iot/sensorData/temperature",str(self.tempSensorData.toDict()),2)
                   
            sleep(self.pollCycleSecs) #wait for next poll
                       
                            
           
app=SensorManagementApp("C:/Users/Leo/git/IoTRepository/iot-ConnectedDevice/data/ConnectedDevices.conf")

app.start()

while True:
    pass       
  

      
    
    
    
    
    
      
    
