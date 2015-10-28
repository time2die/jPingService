package org.time2java.jpingservice.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.time2java.jpingservice.HostRequest;

/**
 * @author time2java
 */
public class HostRequestDAO {

    Session session;

    //double check singlton impl
    private static volatile HostRequestDAO instance;

    public static HostRequestDAO getInstance() {
        HostRequestDAO localInstance = instance;
        if (localInstance == null) {
            synchronized (HostRequestDAO.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new HostRequestDAO();
                }
            }
        }
        return localInstance;
    }

    public HostRequestDAO() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void updateRequest(HostRequest request) {
        System.out.println("save: "+ request);
    }
    
    public HostRequest getRequest(HostRequest lookingFor){
        return new HostRequest() ;
    }

    private void getStatus(int id) throws Exception {
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
}
