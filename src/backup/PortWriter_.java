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
public class PortWriter_ {

    static Enumeration ports;
    static CommPortIdentifier pID;
    static OutputStream outStream;
    SerialPort serPort;
    static String messageToSend = "message!\n";

    public PortWriter_() throws Exception {
        serPort = (SerialPort) pID.open("PortWriter", 2000);
        outStream = serPort.getOutputStream();
        serPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
    }

    public static void main(String[] args) throws Exception {
        setJavaLibraryPath();
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()) {
            pID = (CommPortIdentifier) ports.nextElement();
            System.out.println("Port " + pID.getName());

            if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (pID.getName().equals("COM1")) {
                    PortWriter_ pWriter = new PortWriter_();
                    System.out.println("COM1 found");
                }
            }
        }
        outStream.write(messageToSend.getBytes());
    }

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
}
