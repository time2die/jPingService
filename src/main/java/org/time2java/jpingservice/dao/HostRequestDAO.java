package org.time2java.jpingservice.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
    private SessionFactory sessionFactory;

    public HostRequestDAO() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void addOrUpdateRequest(HostRequest request) {
        HostRequest oldRequest = null ;
        try{
        oldRequest = (HostRequest) session.createCriteria(HostRequest.class).add(Restrictions.eq("host", request.getHost()))
                                                .add(Restrictions.eq("port", request.getPort()))
                                                .add(Restrictions.eq("path", request.getPath())).addOrder(Order.desc("date")).uniqueResult();
        }catch(HibernateException ex){
            ex.printStackTrace();
        }

        Transaction tx =  session.beginTransaction();
        
        if(oldRequest != null){
            oldRequest.setCode(request.getCode());
            oldRequest.setDate(request.getDate());
            oldRequest.setReply(request.getReply());
            oldRequest.setStatus(request.getStatus());
            System.out.println("update: " + oldRequest);
        }else{
            oldRequest = request ;
            System.out.println("save: " + request);
        }

        session.saveOrUpdate(request);
        tx.commit(); 
    }

    public HostRequest getRequest(HostRequest lookingFor) {
        
        HostRequest oldRequest = null ;
        try{
        oldRequest = (HostRequest) session.createCriteria(HostRequest.class).add(Restrictions.eq("host", lookingFor.getHost()))
                                                .add(Restrictions.eq("port", lookingFor.getPort()))
                                                .add(Restrictions.eq("path", lookingFor.getPath())).addOrder(Order.desc("date")).uniqueResult();
        }catch(HibernateException ex){
            ex.printStackTrace();
        }
        
        return oldRequest;
    }

    private void getStatus(int id) throws Exception {
        HostRequest surveyInSession = (HostRequest) session.get(HostRequest.class, Long.valueOf(id));
        System.out.println(surveyInSession.getHost());
    }

    public void close() {
        session.close();
        sessionFactory.close();
    }
}
