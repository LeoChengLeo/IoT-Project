package iotWebServiceApp.iotWebServiceApp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;




@RestController
public class SensorDataController {

    @Autowired
    SensorDataRepository sensorDataRepository;
    
    @Autowired
    CloudWatchService cloudWatchService;

    
    
    //Handle Get request, return all sensorData history 
    @GetMapping("/iotService/sensorData")
    public List<SensorData> getAllSensorData()
    {

        return sensorDataRepository.findAll();

    }


    //Handle post request, save arrived temperature sensorData to DataBase
    @PostMapping("/iotService/sensorData")
    public ResponseEntity<Object> createNewTransaction(@RequestBody SensorData sensorData)
    {

    	
    	cloudWatchService.putMetricData("SensorData","Temperature",Double.parseDouble(Float.toString(sensorData.getCurrValue())));
    	
        try
        {
            sensorDataRepository.save(sensorData);
            return ResponseEntity.status(HttpStatus.CREATED).body("CREATE SUCCESS");

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    

    
    
    //Handle HTTP request with parameter air conditioner actuator data, will publish this actuator data back to cloud broker  
    @GetMapping("/iotService/actuatorData/airConditioner")
    public ResponseEntity<Object> pubAirConActuatorData(@RequestParam (value="AirConditioner",required = true) String command)
    {

    	
        try
        {

            MqttClientConnector notifier=new MqttClientConnector("tcp","34.238.168.201",1883);
            notifier.connect();
            notifier.publishMessage("iot/actuatorData/airConditioner",0,command.getBytes());
            notifier.disconnect();
            notifier.close();

            return ResponseEntity.status(HttpStatus.CREATED).body("Publish Success!");

        } catch (Exception e)
        {
            System.out.println("Failed to handle Request API /iotService/ActuatorData/AirConditioner.."+e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }



    }