package com.vidyatechnos.sms;

public class SMSGateway {
	public static void main(String[] args) {
		SerialPortChannel channel = new SerialPortChannel("COM3");
		channel.connect();
		ATCommander commander = new ATCommander(channel.getOutputStream());
		if(channel.isChannelOpen()) {
			try {
				Thread.sleep(5000);
				commander.readAllMessages();
				StringBuilder data = channel.getPortEventListener().getData();
				System.out.println("data:::: " + data);
				channel.close();
				System.exit(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
