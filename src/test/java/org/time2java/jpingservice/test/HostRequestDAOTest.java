/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.time2java.jpingservice.test;

import junit.framework.TestCase;
import org.time2java.jpingservice.dao.HostRequestDAO;

/**
 *
 * @author alex
 */
public class HostRequestDAOTest extends TestCase {

    HostRequestDAO dao ;
     
    
    public HostRequestDAOTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        dao = HostRequestDAO.getInstance() ;
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        
        dao.close(); 
    }


}
