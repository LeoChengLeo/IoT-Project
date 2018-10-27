'''
Created on Sep 29, 2018

@author: Leo
'''

from labs.common.configReader import ConfigReader
from labs.module2.smtpClientConnector import SmtpClientConnector
from labs.common.actuatorData import ActuatorData
from labs.module3.tempSensorAdaptor import TempSensorAdaptor
from labs.module3.tempActuatorEmulator import TempActuatorEmulator
from threading import Thread
from time import sleep

class SensorManagementApp(Thread):
    
    emailSender=None
    tempSensorAdaptor=None
    tempSensorData=None
    actuatorData=None
    tempActuatorEmulator=None
    deviceConfiReader=None
    
    
    def __init__(self,configDir):
        Thread.__init__(self)
        self.emailSender=SmtpClientConnector(configDir)
        
        self.tempSensorAdaptor=TempSensorAdaptor(configDir)
        self.tempSensorAdaptor.daemon=True
        self.tempSensorAdaptor.start()
        
        self.actuatorData=ActuatorData()
        #simpleLED and senseHatLED activator threads are started when TempActuatorEmulator instance is initialized..                                            
        self.tempActuatorEmulator=TempActuatorEmulator(3) #LED will be blinking about 3 sec when it gets actuatorData update notification
        self.deviceConfiReader=ConfigReader(configDir,"Device")                     
        
    
    def run(self):
        while True:
            #Read new sensorData and refresh all parameters        
            self.tempSensorData=self.tempSensorAdaptor.getCurrentSensorData()
            currValue=self.tempSensorData.currValue    
            print("currentValue"+str(currValue))
            threshold=int(self.deviceConfiReader.getProperty("threshold"))
            nominalTemp=int(self.deviceConfiReader.getProperty("nominalTemp"))
            
            #First sensor event(Check if current temperature surpassed or lower than  currentAverageTemperature +or- threshold )
            if abs(currValue-self.tempSensorData.avgValue)>threshold:
                print("Send TemperatureNotification email...")
                print(self.tempSensorData)
                self.emailSender.sendEmailMessage("TemperatureNotification",self.tempSensorData)
                 
            #Second event(Check if currentTemperature different form the configured nominal temperature then updating relevant actorData)
            if currValue>nominalTemp:
                val=currValue-nominalTemp
                self.actuatorData.setCommand(1)
                self.actuatorData.setValue(val)
            if currValue<nominalTemp:
                val=nominalTemp-currValue
                self.actuatorData.setCommand(0)
                self.actuatorData.setValue(val)
            
               
            self.tempActuatorEmulator.process_message(self.actuatorData) #update new acturatorData 
            
            pollCycleSecs=int(self.deviceConfiReader.getProperty("pollCycleSecs"))    
            sleep(pollCycleSecs) #wait for next poll
                       
                            
            
            
            
        
  

      
    
    
    
    
    
      
    
