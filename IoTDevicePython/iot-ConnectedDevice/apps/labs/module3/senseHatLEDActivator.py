'''
Created on Sep 29, 2018

@author: Leo
'''

from sense_hat import SenseHat
from threading import Thread

class SenseHatLEDActivator(Thread):
    
    enableLED=False
    showingTime=1
    message=None
    sh=None
    
    def __init__(self):
        Thread.__init__(self)
        self.sh=SenseHat()
            
    def setEnableFlag(self,enable):
        self.enableLED=enable
    
    def showLEDMessage(self,message,showingTime):
        self.message=message
        self.showingTime=showingTime
        self.enableLED=True #Start the thread
              
    def run(self):
        while True:
            count=0
            while self.enableLED:
                self.sh.show_message(self.message,scroll_speed=0.01)
                count=count+1
                if count>=self.showingTime:
                    self.enableLED=False  



