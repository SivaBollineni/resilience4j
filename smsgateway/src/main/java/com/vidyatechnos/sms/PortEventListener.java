package com.vidyatechnos.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.comm.CommPortIdentifier;
import javax.comm.CommPortOwnershipListener;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

public class PortEventListener implements SerialPortEventListener, CommPortOwnershipListener {

	
	private CommPortIdentifier portId;
	private InputStream inputStream = null;
	private StringBuilder data = new StringBuilder();
	
	public PortEventListener(CommPortIdentifier portId, InputStream inputStream) {
		super();
		this.portId = portId;
		this.inputStream = inputStream;
	}
	
	public PortEventListener(CommPortIdentifier portId) {
		super();
		this.portId = portId;
	}
	
	public PortEventListener() {
		super();
	}
	
	public StringBuilder getData() {
		return data;
	}

	public void setData(StringBuilder data) {
		this.data = data;
	}

	public CommPortIdentifier getPortId() {
		return portId;
	}

	public void setPortId(CommPortIdentifier portId) {
		this.portId = portId;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public void ownershipChange(int type) {
		switch (type) {
        case CommPortOwnershipListener.PORT_UNOWNED:
          System.out.println(portId.getName() + ": PORT_UNOWNED");
          break;
        case CommPortOwnershipListener.PORT_OWNED:
          System.out.println(portId.getName() + ": PORT_OWNED");
          break;
        case CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED:
          System.out.println(portId.getName() + ": PORT_INUSED");
          break;
      }
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		switch (serialPortEvent.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:     
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
        case SerialPortEvent.DATA_AVAILABLE:

          try {
            while (inputStream.available() > 0) 
            {
              BufferedReader portReader = new BufferedReader(new InputStreamReader(inputStream));
              
      		  portReader.lines().forEach(line -> data.append(line));
//      		  portReader.lines().forEach(line -> System.out.println("line: " + line));
            }

          } catch (IOException e) {
        	  e.printStackTrace();
          }
          break;
      }
	}

}
