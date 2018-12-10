'''
Created on Sep 29, 2018

@author: Leo
'''


'''
This is a shadow class, only for test purpose on IDE
'''


class SenseHat:
    rotateDeg = 270
    clearFlag = False

    def __init__(self):
        self.set_rotation(self.rotateDeg)
        
    def clear(self):
        self.clearFlag = True
        print("senseHatLED CLEAR..")
        
    def get_humidity(self):
        return 48.5

    def get_temperature(self):
        return self.get_temperature_from_humidity()

    def get_temperature_from_humidity(self):
# NOTE: This is just a sample
        return 31.5

    def get_temperature_from_pressure(self):
        return self.get_temperature_from_humidity()

    def get_pressure(self):
# NOTE: This is just a sample
        return 31.5
    def set_rotation(self, rotateDeg):
        self.rotateDeg = rotateDeg

    def show_letter(self, val):
        print(val)
    
    def show_message(self, msg,scroll_speed):
        print(msg)
        
    def set_pixel(self,x,y,color):
        return None
    
    def set_pixels(self,image):
        print("SenseHatLED pixels is printed")
        return None

