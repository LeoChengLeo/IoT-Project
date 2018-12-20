package iotWebServiceApp.iotWebServiceApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



/*
 * This is application is built in java spring boot framework and running on AWS EC2 instance with tomcat server. 
 * MariaDB will be connected to this web application. There are three API presented in this project. First, allowing client to send Post request with sensor data in Json format (save to Database). 
 * Second, allowing client to send Get request to retrieve sensor data. 
 * Third, allowing user to send HTTP request with parameter Air conditioner actuator (on or off), this API will trigger an MQTT client to publish this air conditioner actuator data back to cloud broker. 
 * Gateway management app has an MQTT client subscribes this Air Conditioner Actuator, and will transfer this actuator data back to the device then triggering air Conditioner Actuator Emulator.
 */


@SpringBootApplication
public class App extends SpringBootServletInitializer{


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(App.class);
    }



    public static void main(String[] args)
    {

        SpringApplication.run(App.class,args);
    }


}