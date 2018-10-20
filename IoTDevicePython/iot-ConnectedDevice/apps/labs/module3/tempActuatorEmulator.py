'''
Created on Sep 22, 2018

@author: Leo
'''


from labs.module3.simpleLEDActivator import SimpleLEDActivator
from labs.module3.senseHatLEDActivator import SenseHatLEDActivator
from labs.common.actuatorData import ActuatorData

# COMMAND_UP =0
# COMMAND_DOWN=1
# 
# STATUS_IDLE=0
# STATUS_ACTIVE=1
# 
# ERROR_OK=0
# ERROR_COMMAND_FAILD=1
# ERROR_NON_RESPONSIBLE=-1  

class TempActuatorEmulator:
     
    tempActuatorData=None    
    ledActivator=None
    shMessgeActivator=None
    
    
    def __init__(self,blinkingTime):
        self.tempActuatorData=ActuatorData()
        
        self.ledActivator=SimpleLEDActivator(blinkingTime)
        self.ledActivator.daemon=True
        self.ledActivator.start() 
        
        self.shMessgeActivator=SenseHatLEDActivator()
        self.shMessgeActivator.daemon=True
        self.shMessgeActivator.start()
                     
    def process_message(self,actuatorData):
        self.tempActuatorData.updateData(actuatorData)
        self.ledActivator.setEnableFlag(True)
        self.doAction()
                
    def doAction(self):
                
        if self.tempActuatorData.getCommand()==0:
            message="+"+str(round(self.tempActuatorData.getValue(),2))
            self.shMessgeActivator.showLEDMessage(message,2) #message will show two time
            
        if self.tempActuatorData.getCommand()==1:
            message="-"+str(round(self.tempActuatorData.getValue(),2))
            self.shMessgeActivator.showLEDMessage(message,2)  #message will show two time
                   
    
    
     


    
    
    
    
    
        
        
        
        
        
