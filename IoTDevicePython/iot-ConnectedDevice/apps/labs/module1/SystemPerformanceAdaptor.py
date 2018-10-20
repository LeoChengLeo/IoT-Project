'''
Created on Sep 14, 2018

@author: Leo
'''


import psutil
from threading import Thread
from time import sleep

class SystemPerformanceAdaptor(Thread):
    
    def __init__(self):
        Thread.__init__(self)
        self.enableAdaptor=False
        self.rateInSec=2
        
    def run(self):
        while True:
            if self.enableAdaptor:
                print("\n-----------------")
                print("System performance update:")
                print(" "+str(psutil.cpu_stats()))
                print(" "+str(psutil.virtual_memory()))           
            sleep(self.rateInSec)

# sys1=SystemPerformanceAdaptor()
# sys1.enableAdaptor=True
# sys1.daemon=True
# sys1.rateInSec=3
# sys1.start()
# while True:
#     sleep(1)
#     pass

    

    





