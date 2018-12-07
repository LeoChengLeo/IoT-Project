'''
Created on Sep 22, 2018

@author: Leo
'''


from sense_hat import SenseHat


'''
This is a virtual Actuator for presenting 
temperature is being lowered or increased 
by showing the delta between current Temperature 
and expected Temperature n SenseHatLED.
'''

class TempActuatorEmulator:
     
    tempActuatorData=None    
    senseHat=None
    
    def __init__(self):
        
        self.senseHat=SenseHat()
                     
    def process_message(self,actuatorData):
        
        self.tempActuatorData=round(float(actuatorData.split("'")[1]),2) #update new temperature actuatorData 
        self.doAction()
                
    def doAction(self):
        
        enableLED=True
        count=0 
        while enableLED:
            self.senseHat.show_message(str(self.tempActuatorData),scroll_speed=0.01)
            count=count+1
            if count>=2: # show two time on sensehatLED
                enableLED=False 
     


    
    
    
    
    
        
        
        
        
        
