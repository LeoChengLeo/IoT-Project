from time import sleep
from threading import Thread
from smbus import SMBus


# accelAddr = 0x1C # address for IMU (accelerometer)
# magAddr = 0x6A # address for IMU (magnetometer)
# pressAddr = 0x5C # address for pressure sensor
# humidAddr = 0x5F # address for humidity sensor


enableControl = 0x2D
enableMeasure = 0x08
begAddr = 0x28

accelAddr=28
magAddr=106
pressAddr=92
humidAddr=95

i2cBus=SMBus(1)

class I2cSenseHatAdaptor(Thread):
    
    accelData=[]
    magData=[]
    pressData=[]
    humidData=[]
    refreshTime=30 # refresh all sensor data every 30 sec (default value)
    
    def __init__(self,refreshTime):
        Thread.__init__(self)
        self.refreshTime=refreshTime
        self.initI2CBus()
        
     
    def initI2CBus(self):
        print("Initialize i2c bus and enabling i2c address....")
        i2cBus.write_byte_data(accelAddr,enableControl,enableMeasure)
        i2cBus.write_byte_data(magAddr,enableControl,enableMeasure) 
        i2cBus.write_byte_data(pressAddr,enableControl,enableMeasure)
        i2cBus.write_byte_data(humidAddr,enableControl,enableMeasure)
        
    
    def get_accelData(self):
        
        return i2cBus.read_i2c_block_data(accelAddr,begAddr,6)
    
    def get_magData(self):
        
        return i2cBus.read_i2c_block_data(magAddr,begAddr,24)
    
    def get_pressData(self):
        
        return i2cBus.read_i2c_block_data(pressAddr,begAddr,1)
    
    def get_humidData(self):
        
        return i2cBus.read_i2c_block_data(humidAddr,begAddr,1)
    
    
        
    def run(self):
        while True:
            print(self.get_accelData())
            print(self.get_humidData())
            print(self.get_magData())
            print(self.get_pressData())
            sleep(self.refreshTime)            
            
                    
