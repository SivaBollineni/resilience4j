package com.siva.smsgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vidyatechnos.sms.ATCommander;
import com.vidyatechnos.sms.SerialPortChannel;

@SpringBootApplication
public class SmsgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsgatewayApplication.class, args);
	}
	
   @Bean
   public SerialPortChannel getSerialPortChannel() {
      return new SerialPortChannel();   
   }
   
   @Bean
   public ATCommander getATCommander() {
      return new ATCommander();   
   }

}
