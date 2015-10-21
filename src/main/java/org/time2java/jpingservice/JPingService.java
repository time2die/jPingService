package org.time2java.jpingservice;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author alex
 */
public class JPingService {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        th.start();
        
//        while (scanner.hasNextLine()) {
//            String s = scanner.nextLine();
//        }
        
    }

    static Thread th = new Thread() {

        @Override
        public void run() {
            int i = 0;
            while (i < 200) {
                try {
                    i++;
                    System.out.println("stop");
                    System.out.println("the");
                    System.out.println("fuck");
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JPingService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

}
