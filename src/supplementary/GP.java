/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplementary;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author KOCMOC
 */
public class GP {

    public static final Properties MAIN_PROPERTIES = HelpM.properties_load_properties("lib/main.properties", false);
    //================================
    public static final URL IMAGE_ICON_URL = GP.class.getResource("icon.png");
    //================================
    public static final int COM_PORT = Integer.parseInt(MAIN_PROPERTIES.getProperty("com_port", "9999"));
    //
    public static final HashMap<String,String> PREDEFINED_COMMANDS = new HashMap<String, String>();

    static {
        //Defining the PREDEFINED_COMMANDS
        //
        String commands = MAIN_PROPERTIES.getProperty("commands", "");
        String[]descriptions_and_commands = commands.split(",");
        for (String curr : descriptions_and_commands) {
            String[]arr = curr.split("/");
            PREDEFINED_COMMANDS.put(arr[0], arr[1]);
        }
    }
}
