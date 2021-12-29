package com.siva.smsgateway.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vidyatechnos.sms.ATCommander;
import com.vidyatechnos.sms.SerialPortChannel;

@RestController
@RequestMapping("/sms")
public class SendSMSController {
	@Autowired
	SerialPortChannel channel;
	
	@Autowired
	ATCommander commander;
	@PostMapping("/send")
	@CrossOrigin(origins="http://localhost:3000")
	public String send(@RequestParam(name = "port") String port, 
			@RequestParam(name = "message") String message,
			@RequestParam(name = "mobileNumber") String mobileNumber){
		System.out.println("To mobileNumber: " + mobileNumber);
		System.out.println("message: " + message);
		channel.setComPort(port);
		channel.connect();
		StringBuilder data = new StringBuilder();
		if(channel.isChannelOpen()) {
			try {
				Thread.sleep(5000);
				commander.setOutputStream(channel.getOutputStream());
				commander.registerToHomeNetwork();
				Thread.sleep(1000);
				commander.checkStatus();
				Thread.sleep(1000);
				commander.setModeToSMSPDU();
//				Thread.sleep(1000);
//				commander.sendMessage(mobileNumber, message);
//				Thread.sleep(1000);
//				data = channel.getPortEventListener().getData();
//				Thread.sleep(60000);
//				System.out.println("data: " + data);
				int numberOfTimes = 10;
				do {
					channel.getPortEventListener().setData(new StringBuilder());
//					data = new StringBuilder();
					Thread.sleep(1000);
					commander.sendMessage(mobileNumber, message);
					Thread.sleep(1000);
					data = channel.getPortEventListener().getData();
					Thread.sleep(80000);
					System.out.println("data: " + data);
					numberOfTimes--;
					System.out.println("Number Of Time : " + numberOfTimes);
					if(numberOfTimes == 0 || data.toString().indexOf("+CMGS:") >= 0)
						break;
				}while(data.toString().indexOf("ERROR") >= 0);
				channel.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return data.toString();
	}
	
	@PostMapping("/sendBulkSms")
	@CrossOrigin(origins="http://localhost:3000")
	public String sendBulkSms(@RequestParam(name = "port") String port, 
			@RequestParam(name = "message") String message,
			@RequestParam(name = "mobileNumbers") List<String> mobileNumbers){
		System.out.println("To mobileNumber: " + mobileNumbers);
		System.out.println("message: " + message);
		channel.setComPort(port);
		channel.connect();
		StringBuilder data = new StringBuilder();
		if(channel.isChannelOpen()) {
			try {
				Thread.sleep(5000);
				commander.setOutputStream(channel.getOutputStream());
				commander.registerToHomeNetwork();
				Thread.sleep(1000);
				for (String mobileNumber : mobileNumbers) {
					commander.checkStatus();
					Thread.sleep(5000);
					commander.setModeToSMSPDU();
					Thread.sleep(5000);
					System.out.println("Sending To mobileNumber: " + mobileNumber);
					commander.sendMessage(mobileNumber, message);
					Thread.sleep(15000);
					commander.hangup();
					Thread.sleep(5000);
				}
				data = channel.getPortEventListener().getData();
				Thread.sleep(2000);
				channel.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return data.toString();
	}
}
