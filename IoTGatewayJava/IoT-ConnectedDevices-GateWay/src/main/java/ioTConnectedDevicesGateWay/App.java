package ioTConnectedDevicesGateWay;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import ioTConnectedDevicesGateWay.labs.common.*;

public class App 
{
    
	public static void main( String[] args )
    {
    
			
		SensorData sensor=new SensorData();
		
		sensor.fromXml("<all>\r\n" + 
				"  <SensorData>\r\n" + 
				"    <avgValue>10.0</avgValue>\r\n" + 
				"    <count>1</count>\r\n" + 
				"    <currValue>10</currValue>\r\n" + 
				"    <maxValue>10</maxValue>\r\n" + 
				"    <minValue>10</minValue>\r\n" + 
				"    <startedTime>2018-10-27 02:44:57</startedTime>\r\n" + 
				"    <timeData>2018-10-27 02:44:57</timeData>\r\n" + 
				"    <totalValue>10</totalValue>\r\n" + 
				"    <type>Temperature</type>\r\n" + 
				"  </SensorData>\r\n" + 
				"</all>");
			
		
    }
	
}
