'''
Created on Sep 29, 2018

@author: Leo
'''

from labs.common.configReader import ConfigReader
from labs.module2.senesorEmulator import SensorEmulator
from labs.module2.smtpClientConnector import SmtpClientConnector
from threading import Thread
import sense_hat
from labs.common.sensorData import SensorData
from time import sleep

class TempSensorAdaptor(Thread):
    deviceConfigReader=None
    tempSensorEmulator=None
    enableTempEmulator=False 
    currentTemp=0
    emailSender=None
    currentSensorData=None
    sh=None
    
    def __init__(self,configDir):
        Thread.__init__(self)
        
        self.deviceConfigReader=ConfigReader(configDir,"Device")
        self.sh=sense_hat.SenseHat()
        self.currentSensorData=SensorData("Temperature")
        #SensorEmulator simulates temperature range from 25 degree to 15 degree 
        #temperature is changed after a random period of time in range 10 sec to 1 sec
        self.tempSensorEmulator=SensorEmulator(25, 15, 10, 1)
        self.tempSensorEmulator.daemon=True  
        self.tempSensorEmulator.start() 
        self.emailSender=SmtpClientConnector(configDir)
        
        if self.deviceConfigReader.getProperty("enableEmulator") =="True":
            self.enableTempEmulator=True 
            self.tempSensorEmulator.enableTempEmulator=True
            
                                        
    def getCurrentTemp(self):
        return self.currentTemp
    
    def getCurrentSensorData(self): 
        self.currentSensorData.addNewValue(self.currentTemp) # refresh SensorData only when it is called.
        return self.currentSensorData 

    def run(self):
        while True:
            if self.enableTempEmulator: #If enableTempEmulator is false then using sense_hat to get current temperature!
                self.currentTemp=self.tempSensorEmulator.getCurrValue()
            else:
                self.currentTemp=self.sh.get_temperature()   

            if abs(self.currentTemp-self.currentSensorData.avgValue)>float(self.deviceConfigReader.getProperty("threshold")):
                print("Send TemperatureNotification email...")
                self.emailSender.sendEmailMessage("TemperatureNotification",self.currentSensorData)
            sleep(float(self.deviceConfigReader.getProperty("pollCycleSecs"))) #wait for next poll
            
          
    def setflagEnableTempEmulator(self,enableTempEmulator):
        self.tempSensorEmulator=enableTempEmulator
        self.tempSensorEmulator.enableTempEmulator=enableTempEmulator





# tempSensorAdaptor=TempSensorAdaptor("C:/Users/Leo/Documents/IoT-Project/IoTDevicePython/iot-ConnectedDevice/data/ConnectedDevices.conf")
# tempSensorAdaptor.start()
# while True:
#     pass


    
        
        
        
        
        
        
        