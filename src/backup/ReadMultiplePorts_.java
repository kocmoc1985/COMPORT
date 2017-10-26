/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import gnu.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;

/**
 *
 * @author Administrator
 */
public class ReadMultiplePorts_ {

    public static void main(String[] argv) throws IOException,NoSuchPortException, PortInUseException,UnsupportedCommOperationException {
        try {
            setJavaLibraryPath();
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(ReadMultiplePorts_.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            java.util.logging.Logger.getLogger(ReadMultiplePorts_.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(ReadMultiplePorts_.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReadMultiplePorts_.class.getName()).log(Level.SEVERE, null, ex);
        }
        new ReadMultiplePorts_();
    }

    /* Constructor */
    public ReadMultiplePorts_() throws IOException, NoSuchPortException,
            PortInUseException, UnsupportedCommOperationException {

        // get list of ports available on this particular computer,
        // by calling static method in CommPortIdentifier.
        Enumeration pList = CommPortIdentifier.getPortIdentifiers();

        // Process the list, processing only serial ports.
        while (pList.hasMoreElements()) {
            CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
            String name = cpi.getName();
            System.out.print("Port " + name + " ");
            if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println("is a Serial Port: " + cpi);

                SerialPort thePort;
                try {
                    thePort = (SerialPort) cpi.open("Logger", 1000);
                } catch (PortInUseException ev) {
                    System.err.println("Port in use: " + name);
                    continue;
                }

                // Tell the Comm API that we want serial events.
                thePort.notifyOnDataAvailable(true);
                try {
                    thePort.addEventListener(new Logger(cpi.getName(), thePort));
                } catch (TooManyListenersException ev) {
                    // "CantHappen" error
                    System.err.println("Too many listeners(!) " + ev);
                    System.exit(0);
                }
            }
        }
    }

    /** Handle one port. */
    public class Logger implements SerialPortEventListener {

        String portName;
        SerialPort thePort;
        BufferedReader ifile;

        public Logger(String name, SerialPort port) throws IOException {
            portName = name;
            thePort = port;
            // Make a reader for the input file.
            ifile = new BufferedReader(new InputStreamReader(thePort.getInputStream()));
        }

        public void serialEvent(SerialPortEvent ev) {
            String line;
            try {
                line = ifile.readLine();
                if (line == null) {
                    System.out.println("EOF on serial port.");
                    System.exit(0);
                }
                System.out.println(portName + ": " + line);
            } catch (IOException ex) {
                System.err.println("IO Error " + ex);
            }
        }
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
