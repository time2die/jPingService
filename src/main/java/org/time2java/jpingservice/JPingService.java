package org.time2java.jpingservice;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 * @author time2java
 */
public class JPingService {

    public static void main(String[] args) throws Exception {
        new JPingService().work();
    }

    public void work() {
        printHelp();
        readAndProcessCommands();
    }

    private void printHelp() {
        System.out.println("< How to use jPingserver?");
        System.out.println("< commands:");
        System.out.println("< ADD");
        System.out.println("< add host port path");
        System.out.println("< example: add ya.ru 80 /");
        System.out.println("< example: add 192.168.1.1 443 api/v2/ping");
        System.out.println("< SHOW");
        System.out.println("< show host port path");
        System.out.println("< example: show ya.ru 80 /");
        System.out.println("< example: show 192.168.1.1 443 api/v2/ping");
        System.out.println("< QUIT");
        System.out.println("< example quit ");
    }

    private void readAndProcessCommands() {
        Scanner scanner = new Scanner(System.in);

        boolean continueWork = true;
        while (continueWork) {
            System.out.print(">");
            String s = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(s);
            if (st.countTokens() < 1) {
                printHelp();
                continue;
            }

            String iter = st.nextToken();
            switch (iter.toLowerCase()) {
                case "add":
                    procesAddComand(st);
                    break;
                case "show":
                    processShowCommand(st);
                    break;
                case "quit":
                    continueWork = false;
                    break;
                default:
                    printHelp();
                    break;
            }

        }
    }



    private void procesAddComand(StringTokenizer st) {
        HostRequest hr = parseHostRequest(st);
        
    }

    private void processShowCommand(StringTokenizer st) {

    }
    
    private HostRequest parseHostRequest(StringTokenizer st ){
        return new HostRequest() ;
    }

}
