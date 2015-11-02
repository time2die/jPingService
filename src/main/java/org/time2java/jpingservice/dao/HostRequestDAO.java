package org.time2java.jpingservice.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.RequestStatus;

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
        
    }

    public void addOrUpdateRequest(HostRequest request) {
        session = sessionFactory.openSession();
        
        HostRequest oldRequest = null;
        try {
            oldRequest = (HostRequest) session.createCriteria(HostRequest.class)
                    .add(Restrictions.eq("host", request.getHost()))
                    .add(Restrictions.eq("port", request.getPort()))
                    .add(Restrictions.eq("path", request.getPath()))
                    .add(Restrictions.eq("status", request.getStatus()))
                    .uniqueResult();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        Transaction tx = session.beginTransaction();
        
        if (oldRequest != null) {
            HostRequest dbRequest = session.load(HostRequest.class, oldRequest.getId()) ;
            
            dbRequest.setCode(request.getCode());
            dbRequest.setDate(request.getDate());
            dbRequest.setReply(request.getReply());
            dbRequest.setStatus(request.getStatus());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>update: " + oldRequest);
            session.merge(dbRequest);
        } else {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>save: " + request);
            session.save(request);
        }

//        session.saveOrUpdate(request);
        tx.commit();
        session.flush();
        session.close() ;
    }

    public HostRequest getAddedRequest(HostRequest lookingFor) {
        session = sessionFactory.openSession();
        HostRequest oldRequest = null;
        try {
            oldRequest = (HostRequest) session.createCriteria(HostRequest.class)
                    .add(Restrictions.eq("host", lookingFor.getHost()))
                    .add(Restrictions.eq("port", lookingFor.getPort()))
                    .add(Restrictions.eq("path", lookingFor.getPath()))
                    .add(Restrictions.eq("status", RequestStatus.ADDED))
                    .addOrder(Order.desc("date")).uniqueResult();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }finally{
            session.close(); 
        }

        return oldRequest;
    }

    public List<HostRequest> getAllAdededRequest() {
        try {
            session = sessionFactory.openSession();
            return (List<HostRequest>) session.createCriteria(HostRequest.class)
                    .add(Restrictions.eq("status", RequestStatus.ADDED))
                    .addOrder(Order.desc("date"));
        } finally {
            session.close();
        }
    }

    public HostRequest getRequest(HostRequest lookingFor) {
        session = sessionFactory.openSession() ;
        HostRequest oldRequest = null;
        try {
            oldRequest = (HostRequest) session.createCriteria(HostRequest.class).add(Restrictions.eq("host", lookingFor.getHost()))
                    .add(Restrictions.eq("port", lookingFor.getPort()))
                    .add(Restrictions.eq("path", lookingFor.getPath()))
                    .add(Restrictions.eq("status", lookingFor.getStatus()))
                    .addOrder(Order.desc("date")).uniqueResult();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }finally{
            session.close();
        }

        return oldRequest;
    }

//    private void getStatus(int id) throws Exception {
//        HostRequest surveyInSession = (HostRequest) session.get(HostRequest.class, add ya.ru Long.valueOf(id));
//        System.out.println(surveyInSession.getHost());
//    }

    public void close() {
        if(session.isConnected()){
            session.close();
        }
        
        sessionFactory.close();
    }
}
