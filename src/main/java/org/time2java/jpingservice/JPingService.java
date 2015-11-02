package org.time2java.jpingservice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.dao.HostRequestDAO;
import org.time2java.jpingservice.threads.AddProcessor;
import org.time2java.jpingservice.threads.RegisterProcessor;
import org.time2java.jpingservice.threads.ShowProcessor;

/**
 * @author time2java
 */
public class JPingService {

    public static void main(String[] args) throws Exception {
        new JPingService().work();
    }

    private AddProcessor nWorker;
    private ShowProcessor rs;
    private RegisterProcessor rp ;

    public JPingService() {
        nWorker = new AddProcessor(new ConcurrentLinkedQueue<>());
        nWorker.start();

        rs = new ShowProcessor(new ConcurrentLinkedQueue<>());
        rs.start();
        
        rp = new RegisterProcessor(new ConcurrentLinkedQueue(), nWorker) ;
        rp.start();
        
        initAfterShutDown() ;
    }

    public void work() {
        printHelp();
        readAndProcessCommands();
    }

    private void printHelp() {
        System.out.println("help < How to use jPingserver?");
        System.out.println("help < commands:");
        System.out.println("help < ADD");
        System.out.println("help < add host port path");
        System.out.println("help < example: add ya.ru 80 /");
        System.out.println("help < example: add 192.168.1.1 443 api/v2/ping");
        System.out.println("help < SHOW");
        System.out.println("help < show host port path");
        System.out.println("help < example: show ya.ru 80 /");
        System.out.println("help < example: show 192.168.1.1 443 api/v2/ping");
        System.out.println("help < QUIT");
        System.out.println("help < example quit ");
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
                    rp.addHostToQueue(parseHostRequestAndValidate(st));
                    break;
                case "show":
                    rs.addHostToQueue((parseHostRequestAndValidate(st)));
                    break;
                case "quit":
                    processQuit(scanner) ;
                    continueWork = false ;
                break;
                default:
                    printHelp();
                    break;
            }
        }
    }
    
    private void processQuit(Scanner sc){
        sc.close();
        nWorker.interrupt();
        rs.interrupt();
        HostRequestDAO.getInstance().close();
    }

    private HostRequest parseHostRequestAndValidate(StringTokenizer st) {
        HostRequest result = new HostRequest();
        try {
            result.setHost(st.nextToken());
            result.setPort(Integer.valueOf(st.nextToken()));
            result.setPath(st.nextToken());
        } catch (NoSuchElementException | NumberFormatException ex) {
            ex.printStackTrace(); 
            printHelp();
            return null;
        }

        return result;
    }

    private void initAfterShutDown() {
        List<HostRequest> list = HostRequestDAO.getInstance().getAllAdededRequest() ;
        System.out.println("<-->\tadd after reload: "+ list.size());
        rp.addAll(list) ;
    }
}