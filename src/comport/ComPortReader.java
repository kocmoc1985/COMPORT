/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comport;

import supplementary.GP;
import gnu.io.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ComPortReader implements SerialPortEventListener {

    private Enumeration ports;
    private CommPortIdentifier pID;
    private InputStream inStream;
    private SerialPort serPort;

    static {
        try {
            setJavaLibraryPath();
        } catch (SecurityException ex) {
            Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start() {
        //
        ports = CommPortIdentifier.getPortIdentifiers();
        //
        while (ports.hasMoreElements()) {
            pID = (CommPortIdentifier) ports.nextElement();
//            System.out.println("Port " + pID.getName());
//            ComportGui.show_info("$Port " + pID.getName());

            if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (pID.getName().equals("COM1")) {
                    try {
                        initialize();
                        ComportGui.show_info("Connection to <" + pID.getName() + "> OK");
                    } catch (PortInUseException ex) {
                        Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TooManyListenersException ex) {
                        Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedCommOperationException ex) {
                        Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    System.out.println("COM1 found");
//                    ComportGui.show_info("COM1 found");

                }
            }
        }
    }

    private void initialize() throws PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException {
        serPort = (SerialPort) pID.open("PortReader", 2000);
        inStream = serPort.getInputStream();
        serPort.addEventListener(this);
        //
        serPort.notifyOnDataAvailable(true);
        //
        serPort.setSerialPortParams(GP.COM_PORT, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        //
        ComportGui.show_info("COM port = " + GP.COM_PORT);
    }

    public SerialPort getSerialPort() {
        return this.serPort;
    }

    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
                ComportGui.show_info_other("SerialPortEvent.BI occurred");
            case SerialPortEvent.OE:
                ComportGui.show_info_other("SerialPortEvent.OE occurred");
            case SerialPortEvent.FE:
                ComportGui.show_info_other("SerialPortEvent.FE occurred");
            case SerialPortEvent.PE:
                ComportGui.show_info_other("SerialPortEvent.PE occurred");
            case SerialPortEvent.CD:
                ComportGui.show_info_other("SerialPortEvent.CD occurred");
            case SerialPortEvent.CTS:
                ComportGui.show_info_other("SerialPortEvent.CTS occurred");
            case SerialPortEvent.DSR:
                ComportGui.show_info_other("SerialPortEvent.DSR occurred");
            case SerialPortEvent.RI:
                ComportGui.show_info_other("SerialPortEvent.RI occurred");
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                ComportGui.show_info_other("SerialPortEvent.OUTPUT_BUFFER_EMPTY occurred");
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                //
                ComportGui.show_info_other("SerialPortEvent.DATA_AVAILABLE occurred");
                //
                byte[] readBuffer = new byte[1024];
                //
                synchronized (this) {
                    try {
                        wait(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //
                try {
                    //
                    while (inStream.available() > 0) {
                        int numBytes = inStream.read(readBuffer);
                    }
                    //
                    System.out.print(new String(readBuffer).trim() + "\n");
                    ComportGui.show_info_important("RECIEVED: " + new String(readBuffer).trim());
                } catch (IOException ioe) {
                    Logger.getLogger(ComPortReader.class.getName()).log(Level.SEVERE, null, ioe);
                }
                //
                break;
        }
    }

    

    /**
     * Very useful, when using JavaNative. This method adjusts where the Java
     * looks for "dll" files.
     *
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setJavaLibraryPath() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        // Reset the "sys_paths" field of the ClassLoader to null.
        Class clazz = ClassLoader.class;
        Field field = clazz.getDeclaredField("sys_paths");
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        Object original = field.get(clazz);
        // Reset it to null so that whenever "System.loadLibrary" is called, it will be reconstructed with the changed value.
        field.set(clazz, null);
        try {
            // Change the value and load the library.
            System.setProperty("java.library.path", "lib");//"c:\\tmp"
            System.loadLibrary("rxtxParallel");
            System.loadLibrary("rxtxSerial");

        } finally {
            //Revert back the changes.
            field.set(clazz, original);
            field.setAccessible(accessible);
        }
    }

//    public static void main(String[] args) throws Exception {
//        ComPortReader comReader = new ComPortReader();
//        comReader.start();
//        PortWriter portWriter = new PortWriter(comReader.getSerialPort());
//        portWriter.write();
//    }
}
