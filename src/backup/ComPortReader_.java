/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import gnu.io.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class ComPortReader_ implements SerialPortEventListener {

    static Enumeration ports;
    static CommPortIdentifier pID;
    InputStream inStream;
    SerialPort serPort;

    public ComPortReader_() throws Exception {

        serPort = (SerialPort) pID.open("x", 2000);
        inStream = serPort.getInputStream();

        serPort.addEventListener(this);

        serPort.notifyOnDataAvailable(true);

        serPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
    }

    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
                System.out.println("SerialPortEvent.BI occurred");
            case SerialPortEvent.OE:
                System.out.println("SerialPortEvent.OE occurred");
            case SerialPortEvent.FE:
                System.out.println("SerialPortEvent.FE occurred");
            case SerialPortEvent.PE:
                System.out.println("SerialPortEvent.PE occurred");
            case SerialPortEvent.CD:
                System.out.println("SerialPortEvent.CD occurred");
            case SerialPortEvent.CTS:
                System.out.println("SerialPortEvent.CTS occurred");
            case SerialPortEvent.DSR:
                System.out.println("SerialPortEvent.DSR occurred");
            case SerialPortEvent.RI:
                System.out.println("SerialPortEvent.RI occurred");
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("SerialPortEvent.OUTPUT_BUFFER_EMPTY occurred");
                break;
            case SerialPortEvent.DATA_AVAILABLE:
//                System.out.println("SerialPortEvent.DATA_AVAILABLE occurred");
                byte[] readBuffer = new byte[1024];

                try {
                    while (inStream.available() > 0) {
                        int numBytes = inStream.read(readBuffer);
                    }
                    System.out.print(new String(readBuffer));
                } catch (IOException ioe) {
                    System.out.println("Exception " + ioe);
                }
                break;
        }
    }

    /**
     * Very useful, when using JavaNative. This method adjusts where the Java looks for "dll" files.
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

    public static void main(String[] args) throws Exception {
        setJavaLibraryPath();
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()) {
            pID = (CommPortIdentifier) ports.nextElement();
            System.out.println("Port " + pID.getName());

            if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (pID.getName().equals("COM1")) {
                    ComPortReader_ pReader = new ComPortReader_();
                    System.out.println("COM1 found");
                }
            }
        }
    }
}
