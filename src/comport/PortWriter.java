/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comport;

import supplementary.ModemCommands;
import gnu.io.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PortWriter {

    private OutputStream outStream;

    public PortWriter(SerialPort port) {
        try {
            outStream = port.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(PortWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param command
     * @param append
     */
    public void write(String command, boolean appendRN) {
        //
        StringBuilder builder = new StringBuilder(command);
        //
        if (appendRN) {
            //IMPORTANT
            builder.append("\r\n");
        }
        //
        try {
            outStream.write(builder.toString().getBytes());
            ComportGui.show_info_important("SEND: " + builder.toString());
        } catch (IOException ex) {
            Logger.getLogger(PortWriter.class.getName()).log(Level.SEVERE, null, ex);
            ComportGui.show_info_important("SEND FAILED: " + command);
        }
        //
        synchronized (this) {
            try {
                wait(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PortWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //
    }

    private void write() {
        while (true) {
            try {
                outStream.write(ModemCommands.DISP_COMMANDS_LIST.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(PortWriter.class.getName()).log(Level.SEVERE, null, ex);
            }

            synchronized (this) {
                try {
                    wait(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PortWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
