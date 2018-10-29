
from labs.common.configReader import ConfigReader
from labs.common.sensorData import SensorData
import json
from src.json2xml import Json2xml
from pip._vendor.pytoml.writer import long



class DevicePayloadGenerator:
    
    
    deviceConfigReader=None
    sensorData=None
    
    
    def __init__(self,configDir):
        self.deviceConfigReader=ConfigReader(configDir,"Device")
        
        
    
    def attachSensorData(self,sensorData):
        self.sensorData=sensorData
        
        
    def toDict(self):
        deviceID=long(self.deviceConfigReader.getProperty("deviceID"))
        FirmwareVersion=self.deviceConfigReader.getProperty("FirmwareVersion")
        currentState=self.deviceConfigReader.getProperty("currentState")
        powerType=self.deviceConfigReader.getProperty("powerType")
        
        deviceDictData={
            "DeviceID":deviceID,
            "FirmwareVersion":FirmwareVersion,
            "CurrentState":currentState,
            "PowerType":powerType,
            "SensorData":self.sensorData.toDict()
            }
        
        return deviceDictData
    
    
    def toJson(self):
        return json.dumps(self.toDict())
    
    
    def toXML(self):

        data=Json2xml.fromstring(self.toJson()).data
        dataConverter=Json2xml(data)
        return dataConverter.json2xml() 
        
        
    
    
    
 
payload=DevicePayloadGenerator("C:/Users/Leo/Documents/IoT-Project/IoTDevicePython/iot-ConnectedDevice/data/ConnectedDevices.conf")
 
sensorData=SensorData("Temperature")
payload.attachSensorData(sensorData)
 
print(payload.toDict())
print(payload.toJson())
print(payload.toXML())






   
    
    
    