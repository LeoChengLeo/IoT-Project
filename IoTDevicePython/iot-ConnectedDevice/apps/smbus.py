'''
Created on Oct 12, 2018

@author: Leo
'''


class SMBus():
    
    
    a=None
    
    def __init__(self,a):
        self.a=a
    
    def write_byte_data(self,a,b,c):
        return None
    
    
    def read_i2c_block_data(self,a,b,c):
        return [255,255,255,255,255,255]
    