package com.siva.smsgateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vidyatechnos.sms.ATCommander;
import com.vidyatechnos.sms.SerialPortChannel;

@RestController
@RequestMapping("/sms")
public class ViewSMSController {
	@Autowired
	SerialPortChannel channel;
	
	@Autowired
	ATCommander commander;
	@GetMapping("/all")
	@CrossOrigin(origins="http://localhost:3000")
	public String findAll(@RequestParam(name = "port") String port){
		StringBuilder data = new StringBuilder();
		channel.setComPort(port);
		channel.connect();
		if(channel.isChannelOpen()) {
			try {
				Thread.sleep(5000);
				commander.setOutputStream(channel.getOutputStream());
				commander.readAllMessages();
				Thread.sleep(5000);
				data.append(channel.getPortEventListener().getData());
				Thread.sleep(5000);
				System.out.println("data:::: " + data);
				channel.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		data.append("AT+CMGL=\"ALL\"+CMGL: 1,\"REC READ\",\"+919542641946\",\"\",\"21/02/01,20:57:39+22\"I got it+CMGL: 2,\"REC READ\",\"+917013367556\",\"\",\"21/02/02,10:13:05+22\"Ok boss+CMGL: 3,\"REC READ\",\"+917013367556\",\"\",\"21/02/02,10:16:51+22\"Cool.+CMGL: 4,\"STO SENT\",\"7013367556\",\"\"sE+CMGL: 5,\"STO SENT\",\"6300302047\",\"\"TESTING MEMORY MESSAGE...+CMGL: 6,\"REC READ\",\"+917013367556\",\"\",\"21/02/02,12:24:46+22\"Hmm+CMGL: 7,\"REC READ\",\"VA-ViCARE\",\"\",\"21/02/02,12:34:06+22\" login to our website www.MyVi.in to pay your bill. Please ignore if already paid.+CMGL: 8,\"REC READ\",\"VA-ViCARE\",\"\",\"21/02/02,12:34:06+22\"Hello! Your health is of utmost importance to us. We urge you to use Vi App from the safety of your home to pay outstanding of Rs. 469.18 . Use Vi App or+CMGL: 9,\"REC READ\",\"VA-ViCARE\",\"\",\"21/02/02,13:10:18+22\"Rs50 Cashback on bill payment! Simply pay Vi postpaid bill of Rs200 or more on Paytm App using promo code VIPOSTPAID Offer valid on first such payment. +CMGL: 10,\"REC READ\",\"VA-ViCARE\",\"\",\"21/02/02,13:10:18+22\"T&C apply. Pay Now - https://p.paytm.me/xCTH/VIPP50+CMGL: 11,\"REC READ\",\"VA-DOTAPT\",\"\",\"21/02/03,13:09:55+22\"Dear customer Be aware. Do not fall prey to KYC/clickbait Frauds. Report any suspicious calls or SMS seeking KYC details. Dial 100 and report on TRAI DND+CMGL: 12,\"REC READ\",\"VA-DOTAPT\",\"\",\"21/02/03,13:09:55+22\" 2.0 app issued by Dept. of Telecom public interest\"+CMGL: 13,\"REC READ\",\"573731\",\"\",\"21/02/03,15:55:20+22");
		return data.toString();
	}
}
