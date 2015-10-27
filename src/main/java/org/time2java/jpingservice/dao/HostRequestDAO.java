package org.time2java.jpingservice.dao;

import org.time2java.jpingservice.HostRequest;

/**
 * @author time2java
 */
public class HostRequestDAO {

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
    }

    public void updateRequest(HostRequest request) {

    }

}
