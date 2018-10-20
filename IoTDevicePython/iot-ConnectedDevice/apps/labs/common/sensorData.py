'''
Created on Sep 15, 2018

@author: Leo
'''
from datetime import datetime
import json

class SensorData:
    startedTime=None
    timeData=None
    type="No set"
    currValue=None
    avgTemp=0
    minTemp=None
    maxTemp=None
    totalTemp=0
    countTemp=0
        
    def __init__(self,name):
        self.type=name
          
    def addNewValue(self, newValue):
        
        if self.startedTime==None:    
            self.maxTemp=newValue
            self.minTemp=newValue
            self.startedTime=str(datetime.now())[:19]

        self.currValue=newValue
        self.timeData=str(datetime.now())[:19]
        self.countTemp=self.countTemp+1
        self.totalTemp=self.totalTemp+newValue
        self.avgTemp=round(float(self.totalTemp/self.countTemp),3)
        
        if newValue>self.maxTemp:
            self.maxTemp=newValue
        if newValue<self.minTemp:
            self.minTemp=newValue
    
    def printSensorInfo(self):   
        print(self.type+":(Started since "+self.startedTime+")")
        print("\tTime:"+str(self.timeData))
        print("\tCurrent"+self.type+":"+str(self.currValue))
        print("\tAverage"+self.type+":"+str(self.avgTemp))
        print("\tSampleNum:"+str(self.countTemp))
        print("\tMin_"+self.type+":"+str(self.minTemp))
        print("\tMax_"+self.type+":"+str(self.maxTemp)) 
            
    def __str__(self):
        sensorInfo=str(self.type+":\n"
                       +"\tStartedSince: "+self.startedTime+"\n"
                       +"\tTime: "+str(self.timeData)+"\n"
                       +"\tCurrent"+self.type+" :"+str(self.currValue)+"\n"
                       +"\tAverage"+self.type+" :"+str(self.avgTemp)+"\n"
                       +"\tSampleNum: "+str(self.countTemp)+"\n"
                       +"\tMin_"+self.type+": "+str(self.minTemp)+"\n"
                       +"\tMax_"+self.type+": "+str(self.maxTemp))
        return sensorInfo  
    
          
    def getDataInJson(self):
        
        dictSensorData={
                       "type":self.type,
                       "startedTime":self.startedTime,
                       "time":self.timeData,
                       "averageTemperature":self.avgTemp,
                       "minTemperature":self.minTemp,
                       "maxTemperature":self.maxTemp,
                       "sampleNum":self.countTemp
                    }
        
        return json.dumps(dictSensorData)
    
        
        
      
        
        
        
        
        
        