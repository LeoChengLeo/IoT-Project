'''
Created on Nov 30, 2018

@author: Leo
'''

from paho.mqtt.client import Client


class MqttClientConnector():
               
    _host=None
    _port=None
    _brokerAddr=None
    _mqttClient=None
    
    #Allow user to use this constructor to pass custom callBack methods
    def __init__(self,host,port,on_connect,on_message,on_publish,on_subscribe):
                  
        self._brokerAddr=host+":"+str(port)
        self._host=host
        self._port=port
        
        self._mqttClient=Client()
        
        self._mqttClient.on_connect=on_connect
        self._mqttClient.on_message=on_message
        self._mqttClient.on_publish=on_publish
        self._mqttClient.on_subscribe=on_subscribe
   
          
          
    def connect(self):
            
        try:
                    
            print("Connecting to broker:"+self._brokerAddr+".....")
            self._mqttClient.connect(self._host,self._port, 60)
            self._mqttClient.loop_start()
                
        except Exception as e:
                
            print("Cloud not connect to broker "+self._brokerAddr+" "+str(e))
                
            
    def disconnect(self):
            
        self._mqttClient.disconnect()
        self._mqttClient.loop_stop()
        
               
    def publishMessage(self,topic,message,qos):
        
        print("Publishing message:"+message+" to broker: "+self._brokerAddr+" Topic:"+topic)
        self._mqttClient.publish(topic,message,qos)
    
    
    def subscribeTopic(self,topic,qos):
        
        print("Subscribing to topic:"+topic+".....")
        self._mqttClient.subscribe(topic,qos)
   
    
    



    

         

              
          
      
          
          
        
          
          
          
          
          
          
          