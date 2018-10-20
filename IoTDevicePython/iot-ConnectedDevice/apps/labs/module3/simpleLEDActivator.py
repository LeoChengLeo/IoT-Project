'''
Created on Sep 29, 2018

@author: Leo
'''
from threading import Thread
from RPi import GPIO
from time import sleep

class SimpleLEDActivator(Thread):
    enableLED=False
    blinkingTime=10
    
    def __init__(self,blinkingTime):
        
        if blinkingTime>0:
            self.blinkingTime=blinkingTime   
        Thread.__init__(self)
            
    def setEnableFlag(self,enable):
        self.enableLED=enable
                    
    def run(self):
        while True:
            count=0
            while self.enableLED:
                print("Update_Success LED is blinking!!")
#                GPIO.output(17,GPIO.HIGH)
                sleep(1)
#                GPIO.output(17, GPIO.LOW)
                sleep(1)
                count=count+2
                if count>=self.blinkingTime:
                    self.enableLED=False
                 
