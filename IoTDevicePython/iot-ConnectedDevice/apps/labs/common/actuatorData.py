'''
Created on Sep 29, 2018

@author: Leo
'''
from datetime import datetime

COMMAND_UP =0
COMMAND_DOWN=1

STATUS_IDLE=0
STATUS_ACTIVE=1

ERROR_OK=0
ERROR_COMMAND_FAILD=1
ERROR_NON_RESPONSIBLE=-1


class ActuatorData:
    
    timeStamp=None
    type="not set"
    hasEerror=False
    errCode=0
    statusCode=0
    command=0
    stateData=None
    val=0
    
    def __init__(self):
        self.updateTimeStamp()

    def updateTimeStamp(self):
        self.timeStamp=str(datetime.now())
                               
    def getCommand(self):
        return self.command
    
    def getName(self):
        return self.type

    def getStateData(self):
        return self.stateData

    def getStatusCode(self):
        return self.statusCode

    def getErrorCode(self):
        return self.errCode

    def getValue(self):
        return self.val;

    def hasError(self):
        return self.hasError

    def setCommand(self,command):
        self.command=command
    
    def setName(self, name):
        self.type = name

    def setStateData(self, stateData):
        self.stateData = stateData

    def setStatusCode(self, statusCode):
        self.statusCode = statusCode

    def setErrorCode(self, errCode):
        self.errCode = errCode
        if (self.errCode != 0):
            self.hasError = True
        else:
            self.hasError = False

    def setValue(self, val):
        self.val = val
        
    def updateData(self, data):
        self.command = data.getCommand()
        self.statusCode = data.getStatusCode()
        self.errCode = data.getErrorCode()
        self.stateData = data.getStateData()
        self.val = data.getValue()
                              
    def __str__(self):        
        actuatorStr=str("Name:\t\t"+self.type+"\n"
        +"Time\t\t"+self.timeStamp+"\n"
        +"Command:\t"+str(self.getCommand())+"\n"
        +"StatusCode:\t"+str(self.getStatusCode())+"\n"
        +"ErrorCode:\t"+str(self.getErrorCode())+"\n"
        +"Value:\t\t"+str(self.getValue()))
        
        return actuatorStr
    

    
        
        
    
    
    
    
    
    









