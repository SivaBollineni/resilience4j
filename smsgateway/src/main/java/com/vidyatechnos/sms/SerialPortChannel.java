package com.vidyatechnos.sms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

import org.springframework.stereotype.Component;

public class SerialPortChannel{
	private String comPort = "COM3";
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private PortEventListener portEventListener = null;
	private CommPortIdentifier portId = null;
	private SerialPort serialPort = null;
	

	
	public SerialPortChannel(String comPort) {
		this.comPort = comPort;
		this.portId = getPortIdentifier(comPort, CommPortIdentifier.PORT_SERIAL);
	}
	
	public SerialPortChannel() {
		this.portId = getPortIdentifier(this.comPort, CommPortIdentifier.PORT_SERIAL);
	}
	
	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public PortEventListener getPortEventListener() {
		return portEventListener;
	}

	public void setPortEventListener(PortEventListener portEventListener) {
		this.portEventListener = portEventListener;
	}

	private CommPortIdentifier getPortIdentifier(String comPort, int portSerial) {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = portList.nextElement();
	        if (portId.getPortType() == portSerial) 
	          if (portId.getName().equals(comPort)) 
	              return portId;
	    }
		return null;
	}
	
	public void connect() throws NullPointerException {
	      if (this.portId != null) {
	    	this.portEventListener = new PortEventListener(this.portId);
	        try {
	        	this.portId.addPortOwnershipListener(portEventListener);

	        	this.serialPort = (SerialPort) portId.open("MobileGateWay", 2000);
//	          this.serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	        	this.serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }

	        try {
	        	this.inputStream = serialPort.getInputStream();
	        	this.outputStream = serialPort.getOutputStream();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }

	        try {
	          /** These are the events we want to know about*/
	        	this.portEventListener.setInputStream(this.inputStream);
	        	this.serialPort.addEventListener(portEventListener);
	        	this.serialPort.notifyOnDataAvailable(true);
	        	this.serialPort.notifyOnRingIndicator(true);
	        } catch (TooManyListenersException e) {
	          e.printStackTrace();
	        }

	   //Register to home network of sim card

//	        send("ATZ\r\n");
//	        try {
//	            Thread.sleep(1000);
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }

	      } else {
	        throw new NullPointerException("COM Port not found!!");
	      }
	    }
	
	public void close(){
	       this.serialPort.close(); 
	}
	
	public boolean isChannelOpen() {
		if(this.serialPort != null) 
			return true;
		
		return false;
	}
	
}
