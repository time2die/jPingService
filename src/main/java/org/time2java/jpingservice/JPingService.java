package org.time2java.jpingservice;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.threads.AddProcessor;
import org.time2java.jpingservice.threads.ShowProcessor;

/**
 * @author time2java
 */
public class JPingService {

    public static void main(String[] args) throws Exception {
        new JPingService().work();
    }

    private ConcurrentLinkedQueue<HostRequest> newHosts;
    private ConcurrentLinkedQueue<HostRequest> showHosts;
    
    public JPingService() {
        newHosts = new ConcurrentLinkedQueue<>() ;
        showHosts = new ConcurrentLinkedQueue<>() ;
        AddProcessor nWorker  = new AddProcessor(newHosts) ;
        nWorker.start();
        
        ShowProcessor rs = new ShowProcessor(newHosts) ;
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
            System.out.print("> ");
            String s = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(s);
            if (st.countTokens() < 1) {
                printHelp();
                continue;
            }

            String iter = st.nextToken();
            switch (iter.toLowerCase()) {
                case "add":
                    procesAddComand(parseHostRequestAndValidate(st));
                    break;
                case "show":
                    processShowCommand(parseHostRequestAndValidate(st));
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

    private HostRequest parseHostRequestAndValidate(StringTokenizer st ){
        HostRequest result = new HostRequest() ;
        try{
            result.setHost(st.nextToken());
            result.setPort(Integer.valueOf(st.nextToken()));
            result.setPath(st.nextToken());
        }catch(NoSuchElementException | NumberFormatException ex){
            return null ;
        }
        
        return new HostRequest() ;
    }

    private void procesAddComand(HostRequest hr) {
        if(hr == null){
            return ;
        }
        newHosts.add(hr);
        
        synchronized(newHosts){
            newHosts.notify(); 
        }
    }

    private void processShowCommand(HostRequest hr) {
        if(hr == null){
            return ;
        }
    }
    

}
