package org.time2java.jpingservice;

import java.io.IOException;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hibernate.Session;

/**
 * @author time2java
 */
public class JPingService {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

//        th.start();
//        while (scanner.hasNextLine()) {
//            String s = scanner.nextLine();
//            try {
//                Integer i = Integer.valueOf(s);
//                getStatus(i);
//            } catch (NumberFormatException e) {
//                return;
//            }
//
//        }
        HttpClient client = new HttpClient();
        HttpMethod method = null;

        String url = "ya.ru" ;
        //create a method object
        method = new GetMethod(url);
        method.setFollowRedirects(true);

        int code = 0;
        String responseBody = null;
        try {
            code = client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
        } catch (HttpException he) {
            System.err.println("Http error connecting to '" + url + "'");
            System.err.println(he.getMessage());
            System.exit(-4);
        } catch (IOException ioe) {
            System.err.println("Unable to connect to '" + url + "'");
            System.exit(-3);
        } catch (java.lang.IllegalArgumentException ex){
            System.err.println("Illegal url '" + url + "'");
            System.exit(-2);
        }
        System.out.println("---code: "+code +"\n--response start\n"+ responseBody +"\n---response stop");

    }

    private static void getStatus(int id) throws Exception {
        HibernateUtil hibernateUtil = new HibernateUtil();
        Session session = hibernateUtil.getSession();
        HostRequest surveyInSession = (HostRequest) session.get(HostRequest.class, Long.valueOf(id));
        System.out.println(surveyInSession.getHost());
    }

//    HibernateUtil hibernateUtil = new HibernateUtil();
//        
//    Session session = hibernateUtil.getSession();
//    session.beginTransaction();
//
//    HostRequest ya = new HostRequest();
//    ya.setHost("ya");
//    ya.setPort(80);
//    ya.setPath("/");
//    ya.setStatus(RequestStatus.NEW);
//    
//    
//    session.save(ya);
//    session.flush();
//    System.out.println(">"+ya.getId());
//    
//    ya = new HostRequest() ;
//    ya.setHost("google.com");
//    session.save(ya);
//    session.flush();
//    
//    System.out.println(">>"+ya.getId());
//    
//    HostRequest surveyInSession = (HostRequest) session.get(HostRequest.class, ya.getId());
//    System.out.println(surveyInSession.getHost());
//
//    session.getTransaction().commit(); 
//    session.close();
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
