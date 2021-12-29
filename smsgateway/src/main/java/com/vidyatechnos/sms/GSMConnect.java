package com.vidyatechnos.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.CommPortOwnershipListener;
import javax.comm.NoSuchPortException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

public class GSMConnect implements SerialPortEventListener, 
CommPortOwnershipListener {

	private static String comPort = "COM3"; // This COM Port must be connect with GSM Modem or your mobile phone
    private String messageString = "";
    private CommPortIdentifier portId = null;
    private Enumeration portList;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private SerialPort serialPort;
    String readBufferTrial = "";
    /** Creates a new instance of GSMConnect */
    public GSMConnect(String comm) {

      this.comPort = comm;

    }

    public boolean init() {
      portList = CommPortIdentifier.getPortIdentifiers();
      while (portList.hasMoreElements()) {
        portId = (CommPortIdentifier) portList.nextElement();
        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
          if (portId.getName().equals(comPort)) {
              System.out.println("Got PortName");
            return true;
          }
        }
      }
      return false;
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



    public void send(String cmd) {
      try {
        outputStream.write(cmd.getBytes());
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
       // TODO Auto-generated catch block
       e.printStackTrace();
   }
       //   send("AT+CMGS=\""+ phoneNumber +"\"\r\n");
      send(message + '\032');
      System.out.println("Message Sent");
    }

    public void hangup() {
      send("ATH\r\n");
    }

    public void connect() throws NullPointerException {
      if (portId != null) {
        try {
          portId.addPortOwnershipListener(this);

          serialPort = (SerialPort) portId.open("MobileGateWay", 2000);
//          serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
          serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          inputStream = serialPort.getInputStream();
          outputStream = serialPort.getOutputStream();

        } catch (IOException e) {
          e.printStackTrace();
        }

        try {
          /** These are the events we want to know about*/
          serialPort.addEventListener(this);
          serialPort.notifyOnDataAvailable(true);
          serialPort.notifyOnRingIndicator(true);
        } catch (TooManyListenersException e) {
          e.printStackTrace();
        }

   //Register to home network of sim card

        send("ATZ\r\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      } else {
        throw new NullPointerException("COM Port not found!!");
      }
    }

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

//        	byte[] readBuffer = new byte[3072];
//          byte[] readBuffer = new byte[2048];
          byte[] readBuffer = new byte[1024];
          try {
            while (inputStream.available() > 0) 
            {
//              int numBytes = inputStream.read(readBuffer);
//	              try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
              BufferedReader portReader = new BufferedReader(new InputStreamReader(inputStream));

      		try {
      		    portReader.lines().forEach(line -> System.out.println("line: " + line));
//      		    System.out.println("line: " + line);
      		} catch(Exception e) { 
      			e.printStackTrace();
      		}
//              String availableData = new String(readBuffer, "UTF-8");
//              availableData = availableData.replaceAll(" ", "");
//              System.out.print("availableData: " + availableData);
//              System.out.println("UTF-8 readBuffer: " + new String(readBuffer, "UTF-8"));
//              try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//              if(new String(readBuffer, "UTF-8").contains("OK")) {
//            	  System.out.println("UTF-8 readBuffer: " + new String(readBuffer, "UTF-8"));
//              }
//              System.out.print("numBytes: " + numBytes + "; readBuffer.toString: " + readBuffer.toString());
//              if((readBuffer.toString()).contains("RING")){
////              System.out.println("Enter Inside if RING Loop");    
//
//
//
//              }
              
            }

//            System.out.print("available data: " + new String(readBuffer));
          } catch (IOException e) {
        	  e.printStackTrace();
          }
          break;
      }
    }
    public void outCommand(){
        System.out.print(readBufferTrial);
    }
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
    public void closePort(){

       serialPort.close(); 
    }

    public static void main(String args[]) {
      GSMConnect gsm = new GSMConnect(comPort);
      if (gsm.init()) {
        try {
            System.out.println("Initialization Success");
          gsm.connect();
          Thread.sleep(5000);
          gsm.checkStatus();
          Thread.sleep(5000);
          gsm.setModeToSMSPDU();
          Thread.sleep(5000);

//          gsm.sendMessage("7013367556", "Message11 from a java program to a group of numbers 7013367556.");
//          Thread.sleep(5000);
//          gsm.sendMessage("6300302047", "Message from a java program to a group of numbers 6300302047.");
//          gsm.sendMessage("6300302047", "Message from a java program to number3.");
//          gsm.sendMessage("7013367556", "Message from a java program to number4.");
//          gsm.sendMessage("9542641946", "Message from a java program to number3.");

          Thread.sleep(1000);
          gsm.readAllMessages();
          Thread.sleep(10000);
          Thread.sleep(10000);
          Thread.sleep(10000);
          Thread.sleep(10000);

          gsm.hangup();
          Thread.sleep(1000);
          gsm.closePort();
          gsm.outCommand();
          System.exit(1);


        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Can't init this card");
      }
    }

	 public void readAllMessages() {
         char quotes ='"';
         String all = "ALL";
         send("AT+CMGL="+quotes + all +quotes+ "\r\n");
		  try {
		      Thread.sleep(2000);
		  } catch (InterruptedException e) {
		      e.printStackTrace();
		  }
      //   send("AT+CMGS=\""+ phoneNumber +"\"\r\n");
	  //     send(message + '\032');
   }



}
