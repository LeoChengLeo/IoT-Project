package ioTConnectedDevicesGateWay;

import javax.print.attribute.SetOfIntegerSyntax;

import ioTConnectedDevicesGateWay.labs.common.SensorData;

public class App 
{
	public static void main( String[] args )
    { 
	
		SensorData sensordata=new SensorData("Temperature");
		sensordata.addNewValue(13);
		System.out.println(sensordata.toJson().toString(2));
		System.out.println(sensordata.toString());
    }	
}
