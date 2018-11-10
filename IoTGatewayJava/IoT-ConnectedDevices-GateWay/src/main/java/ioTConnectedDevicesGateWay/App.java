package ioTConnectedDevicesGateWay;

import ioTConnectedDevicesGateWay.labs.module6.*;

import org.json.JSONObject;

import ioTConnectedDevicesGateWay.labs.common.*;
import ioTConnectedDevicesGateWay.labs.module5.XmlToSensorDataTest;

public class App 
{
	public static void main( String[] args )
    { 
		
		//String message="{\"totalValue\":63,\"timeData\":\"2018.11.09 18:48.45\",\"avgValue\":21,\"startedTime\":\"2018.11.09 18:48.41\",\"minValue\":10,\"maxValue\":35,\"currValue\":18,\"count\":3,\"type\":\"Temperature\"}"
		String message="";
		SensorData sensorData=new SensorData();

		JSONObject sensorjsonMessage=new JSONObject(message);
		sensorData.fromJson(sensorjsonMessage);
		
		System.out.println(sensorData.toJson().toString());
		
    }	
}
