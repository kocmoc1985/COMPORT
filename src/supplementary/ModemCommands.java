/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplementary;

/**
 *
 * @author Administrator
 */
public class ModemCommands {
    //
    //Note: It's obligatory to end the command with "\r\n" 
    //
    public static final String CHECK_STATUS = "AT+CREG?\r\n";
    public static final String DIAL = "ATD0046739224421;\r\n";
    public static final String DISP_COMMANDS_LIST = "AT$;\r\n";
}
