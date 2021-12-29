package com.vidyatechnos.sms;

import java.io.IOException;
import java.io.OutputStream;

public class ATCommander {
	private OutputStream outputStream = null;
	
	public ATCommander(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
	}
	public ATCommander() {
		super();
	}


	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	private void send(String cmd) {
	      try {
	        this.outputStream.write(cmd.getBytes());
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	}
	
	public void sendMessage(String phoneNumber, String message) {
	    char quotes ='"';
	    send("AT+CMGS="+quotes + phoneNumber +quotes+ "\r\n");
	    try {
	    	Thread.sleep(2000);
	    } catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	    send(message + '\032');
//	    try {
//	    	Thread.sleep(50000);
//	    } catch (InterruptedException e) {
//	    	e.printStackTrace();
//	    }
	    System.out.println("Message Sent");
     }

	  public void hangup() {
	      send("ATH\r\n");
	  }
	  
	  public void registerToHomeNetwork() {
		  send("ATZ\r\n");
	  }
	  
	  public void checkStatus() {
	      send("AT+CREG?\r\n");
	    }
	    
      public void setModeToSMSText() {
        send("AT+CMGF=0\r\n");
      }
    
      public void setModeToSMSPDU() {
        send("AT+CMGF=1\r\n");
      }
      
      public void readAllMessages() {
          char quotes ='"';
          String all = "ALL";
          send("AT+CMGL=" + quotes + all + quotes + "\r\n");
 		  try {
 		      Thread.sleep(2000);
 		  } catch (InterruptedException e) {
 		      e.printStackTrace();
 		  }
    }
}
