'''
Created on Sep 15, 2018

@author: Leo
'''
import configparser



class ConfigReader:
    confDir=None
    section=None
    parser=configparser.ConfigParser()
    
    def __init__(self,confDir,section):
        self.section=section
        self.confDir=confDir
        self.parser.read(self.confDir)
    
    def getProperty(self, option):
        return self.parser.get(self.section, option)
        
    def setSection(self,section):
        self.section=section
        
    def setConfigFile(self,confDir):
        self.confDir=confDir
        self.parser.read(self.confDir)
        


        
        
        
        
        
        
        
        