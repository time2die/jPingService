package org.time2java.jpingservice;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;

/**
 * @author alex
 */
public class JPingService {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
//        th.start();
        
//        while (scanner.hasNextLine()) {
//            String s = scanner.nextLine();
//        }

    HibernateUtil hibernateUtil = new HibernateUtil();
    hibernateUtil.executeSQLCommand("create table hostrequest (id int,host varchar,port int,path varchar,requeststatus int,reply varchar);");
        
    Session session = hibernateUtil.getSession();

    HostRequest ya = new HostRequest();
    ya.setHost("ya");
    ya.setPort(80);
    ya.setPath("/");
    ya.setStatus(RequestStatus.NEW);
    
    
    session.save(ya);
    session.flush();
    System.out.println(">"+ya.getId());
    
    ya = new HostRequest() ;
    ya.setHost("google.com");
    session.save(ya);
    session.flush();
    
    System.out.println(">>"+ya.getId());
    
    HostRequest surveyInSession = (HostRequest) session.get(HostRequest.class, ya.getId());
    System.out.println(surveyInSession.getHost());

    session.close();
    hibernateUtil.checkData("select * from request");
        
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
