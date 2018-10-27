package ioTConnectedDevicesGateWay.labs.common;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class SensorData implements Serializable {
	
	
	
	private static final long serialVersionUID=1L;
	
	private String startedTime=null;
	private String timeData=null;
	private String type="Not Set";
	private float currValue=0.0f;
	private float avgValue=0;
	private float minValue=0.0f;
	private float maxValue=0.0f;
	private float totalValue=0.0f;
	private int count=0;
	
		
	
	public SensorData()
	{
		
	}
	
	public SensorData(String type)
	{
		this.type=type;
	}
	
	
	
	
	public void addNewValue(float newValue)
	{
		if (startedTime==null)
		{
			minValue=newValue;
			maxValue=newValue;
			startedTime= new SimpleDateFormat("yyyy.MM.dd HH:mm.ss").format(new Date());
		}
		
		currValue=newValue;
		timeData=new SimpleDateFormat("yyyy.MM.dd HH:mm.ss").format(new Date());
		count+=1;
		totalValue+=newValue;
		avgValue=totalValue/count;
		
		if(newValue> maxValue) {maxValue=newValue;}
		if(newValue< minValue) {minValue=newValue;}
		
		
	}
	
	

	public float getAvgValue() {
		return avgValue;
	}
	
	public float getCurrValue() {
		return currValue;
	}
	
	public float getMaxValue() {
		return maxValue;
	}
	
	public float getMinValue() {
		return minValue;
	}
	
	public String getTimeData() {
		return timeData;
	}
	
	public String getStartedTime() {
		return startedTime;
	}
	
	public float getTotalValue() {
		return totalValue;
	}
	
	public int getCount() {
		return count;
	}
	public String getType() {
		return type;
	}
	
	
	
	
	public void fromXml(String xmlData)
	{
		try {
            JSONObject jsonData = XML.toJSONObject(xmlData);
  
            
            
            startedTime=jsonData.getJSONObject("all").getJSONObject("SensorData").getString("startedTime");
            timeData=jsonData.getJSONObject("all").getJSONObject("SensorData").getString("timeData");
            type=jsonData.getJSONObject("all").getJSONObject("SensorData").getString("type");
            currValue=jsonData.getJSONObject("all").getJSONObject("SensorData").getFloat("currValue");
            avgValue=jsonData.getJSONObject("all").getJSONObject("SensorData").getFloat("avgValue");
            minValue=jsonData.getJSONObject("all").getJSONObject("SensorData").getFloat("minValue");
            maxValue=jsonData.getJSONObject("all").getJSONObject("SensorData").getFloat("maxValue");
            totalValue=jsonData.getJSONObject("all").getJSONObject("SensorData").getFloat("totalValue");
            count=jsonData.getJSONObject("all").getJSONObject("SensorData").getInt("count");
            
            
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
				
		
		
	}
	
	
	
}
