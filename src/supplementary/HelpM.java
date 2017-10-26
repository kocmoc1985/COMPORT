/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplementary;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class HelpM {
    
    public static void err_output_to_file() {
        //Write error stream to a file
        create_dir_if_missing("err_output");
        try {
            String err_file = "err_" + get_date_time() + ".txt";
            String output_path = "err_output/" + err_file;

            PrintStream out = new PrintStream(new FileOutputStream(output_path));
            System.setErr(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HelpM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void create_dir_if_missing(String path_and_folder_name) {
        File f = new File(path_and_folder_name);
        if (f.exists() == false) {
            f.mkdir();
        }
    }
    
    public static String get_date_time() {
        DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }
    
    public static String get_date() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }
    
     public static void run_application_with_associated_application(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    public static Properties properties_load_properties(String path_andOr_fileName, boolean list_properties) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(path_andOr_fileName));
            if (list_properties == true) {
                p.list(System.out);
            }
        } catch (IOException ex) {
            System.out.println("" + ex);
        }
        return p;
    }

    public static String addDay(String date) {
        String[] arr = date.split("-");
        int day = Integer.parseInt(arr[2]);
        day++;
        return "" + arr[0] + "-" + arr[1] + "-" + day;
    }

    public static void copy_file(String file_to_copy, String name_of_duplicate) throws FileNotFoundException, IOException {
        File inputFile = new File(file_to_copy);
        File outputFile = new File(name_of_duplicate);

        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }

        in.close();
        out.close();
    }

    /**
     *VERY IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] filetoByteArray(String path) throws FileNotFoundException, IOException {
        byte[] content;
        FileInputStream p = new FileInputStream(path);
        content = new byte[p.available()];
        p.read(content);
        p.close();
        return content;
    }

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!
     * Verified! Works good with 'filetoByteArray(String path)'
     * @param path
     * @param arr
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static void byteArrayToFile(String path, byte[] arr) throws FileNotFoundException, IOException {
        File f2 = new File(path);
        OutputStream out;
        out = new FileOutputStream(f2);
        out.write(arr);
    }

    public static void copyFile(String file_to_copy, String duplicate_file_name) throws IOException {
        byte[] b_arr = filetoByteArray(file_to_copy);
        byteArrayToFile(duplicate_file_name, b_arr);
        System.out.println("copy files done");
    }
    
    public static String get_proper_date_time_same_format_on_all_computers() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }
    
    public static String get_proper_date_time_same_format_on_all_computers_err_output() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH_mm");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    /**
     * Use 3
     * @param style
     * @return 
     */
    public static String get_proper_date_adjusted_format(int style) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        DateFormat f1 = DateFormat.getDateInstance(style);
        Date d = cal.getTime();
        return f1.format(d);
    }

    public static void main(String[] args) {
        System.out.println("" + addDay("2013-05-12"));
    }
}
