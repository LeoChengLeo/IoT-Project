'''
Created on Sep 15, 2018

@author: Leo
'''
from threading import Thread
from random import uniform, randrange
from time import sleep

class SensorEmulator(Thread):
    
    currValue=0
    max_value=0
    min_value=0
#   time to update new Temperature 
    enableTempEmulator=False
    max_updateTime=0
    min_updateTime=0
    
    def __init__(self,max_temp,min_temp,max_updateTime,min_updateTime):
        Thread.__init__(self)
        self.max_value=max_temp
        self.min_value=min_temp
        self.max_updateTime=max_updateTime
        self.min_updateTime=min_updateTime
        
    def getCurrValue(self):
        return self.currValue
    
    def run(self):
        while True:
            if self.enableTempEmulator:
                self.currValue=round(uniform(self.min_value,self.max_value),2)
                updateTime=randrange(self.min_updateTime,self.max_updateTime)
                sleep(updateTime)
#
        








    


# emulator=SensorEmulator(36,-10, 3, 1)
# 
# 
# 
# emulator.enableTempEmulator=True
# emulator.daemon=True
# print(emulator.isDaemon())
# emulator.start()
# 
# 
# 
# while True:
#     print("CurrentTemperature:"+str(emulator.getCurrValue()))
#     sleep(3)
#  
                
    
    
    
            
            
            
            
            
        
        
    

    
    
    
    
    
    