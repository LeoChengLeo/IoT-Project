'''
Created on Sep 30, 2018

@author: Leo
'''
from labs.module3.sensorManagmentApp import SensorManagementApp
from labs.module2.smtpClientConnector import SmtpClientConnector
from labs.common.sensorData import SensorData
from labs.module3.tempSensorAdaptor import TempSensorAdaptor
from labs.module4.i2cSenseHatAdaptor import I2cSenseHatAdaptor
from time import sleep

def tempSensorAdaptorTest():
    myadaptor=TempSensorAdaptor("C:/Users/Leo/Documents/IoT-Project/IoTDevicePython/iot-ConnectedDevice/data/ConnectedDevices.conf")
    myadaptor.daemon=True
    myadaptor.start()
#Temperature data will be printed out every 2 sec
    while True:
        print(myadaptor.getCurrentSensorData())
        sleep(2)    
      
def smtpClientTest():
    mySensorData=SensorData("Temperature")
    mySensorData.addNewValue(25)
    emailSender=SmtpClientConnector("c://///")
    emailSender.sendEmailMessage("TemperatureTest",mySensorData)


def i2cSenseHatDemo():
    i2cSenseHat=I2cSenseHatAdaptor(3) #Refresh sensor value every 3 sec
    i2cSenseHat.daemon=True
    i2cSenseHat.start()
    while True:
        pass

def MainDemo():
    #Please make sure you put the correct location and path format of your own configuration file!! 
    Myapp=SensorManagementApp("C:/Users/Leo/git/IoTRepository/iot-ConnectedDevice/data/ConnectedDevices.conf")
    Myapp.daemon=True
    Myapp.start()
    while True:
        pass


#linuxPath:/etc/ConnectedDevices.conf




tempSensorAdaptorTest()

#smtpClientTest()

#MainDemo()

#i2cSenseHatDemo()



