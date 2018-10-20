'''
Created on Sep 15, 2018

@author: Leo
'''





# file=open("C:/Users/Leo/eclipse-workspace/eclipse-python-workspace/iot-ConnectedDevice/data/text.txt","w")
# file.write("hellowworld")
# file.close()
# content=open("C:/Users/Leo/eclipse-workspace/eclipse-python-workspace/iot-ConnectedDevice/data/text.txt","r")
# print(content.read())


# host = Not Set
# port = 465
# fromAddr = Not Set
# toAddr = Not Set
# toMediaAddr = Not Set
# toTxtAddr = Not Set
# authToken = Not Set
# enableAuth = True
# enableCrypt = True
from labs.common.configReader import ConfigReader
from email.mime.text import MIMEText
import smtplib


class SmtpClientConnector:
     
    host=None
    port=465
    sourceAddr=None
    destinAddr=None
    passphrase=None
    enableAuth=True
    enableCrypt=True
    smtpConfigReader=None
    
    def __init__(self,configFile):
        self.smtpConfigReader=ConfigReader(configFile,"smtp.cloud")
        self.host=self.smtpConfigReader.getProperty("host")
        self.port=self.smtpConfigReader.getProperty("port")
        self.sourceAddr=self.smtpConfigReader.getProperty("fromAddr")
        self.destinAddr=str(self.smtpConfigReader.getProperty("toAddr")).split()
        self.passphrase=self.smtpConfigReader.getProperty("authToken")
        
    def sendEmailMessage(self,topic,message):
        for destinAddr in self.destinAddr:
            
            msg=MIMEText(str(message))
            msg["From"]=self.sourceAddr
            msg["to"]=destinAddr
            msg["Subject"]=topic
        
            try:
                mailServer=smtplib.SMTP_SSL(self.host,self.port)
                mailServer.ehlo()
                mailServer.login(self.sourceAddr,self.passphrase)
                mailServer.send_message(msg,self.sourceAddr,destinAddr)
                mailServer.close()
                print("Sent successfully to "+destinAddr)
            except Exception as e:
                print("Failed to send email\n"+str(e))
    
    def printInfo(self):
        print("host:"+self.host+"\nport:"+str(self.port)+"\nFromAddr:"+self.sourceAddr+"\ntoAddr:"+self.destinAddr+"\n"+
              "authToken:"+self.passphrase)


 
    
#     
# msg2=MIMEText("Hi leo\n\nThis is a test email from python script!!\n\nBest,\n\nMySelf")
# msg2["From"]="liyingcheng8377@gmail.com"
# msg2["to"]="leo"
# msg2["Subject"]="Temperature"
# 
# mailServer=smtplib.SMTP_SSL("smtp.gmail.com",465)
# mailServer.ehlo()
# mailServer.login("liyingcheng8377","leo38377")
# mailServer.sendmail()
# mailServer.send_message(msg2,"liyingcheng8377@gmail.com","leoleoleocheng@icloud.com")
# mailServer.close()




        
        
        
        
        

    
    
    
    
    
    
    
    
    
    
    