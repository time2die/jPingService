package org.time2java.jpingservice.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.RequestStatus;

/**
 * @author time2java
 */
public class NetWorker extends Thread {

    private int MAX_ELEMENTS_PEAK = 100 ;
    private final ConcurrentLinkedQueue<HostRequest> requestQueue ;
    private final HttpClient client ;
    
    
    public NetWorker(ConcurrentLinkedQueue<HostRequest> queue) {
        requestQueue = queue ;
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
    }
    
    @Override
    public void start() {
        
        int iterator = 0 ;
        List<HostRequest> requestList = new ArrayList<HostRequest>(MAX_ELEMENTS_PEAK) ;
     
        
        //read first n elements from queue
        while(!requestQueue.isEmpty() && iterator < MAX_ELEMENTS_PEAK){
            requestList.add(requestQueue.poll()) ;
            iterator++ ;
        }
        
        iterator = 0 ;
        
        requestList.parallelStream().forEach((host) ->{
            getResponseAndSave(host) ;
        } );

        
    }

    private void getResponseAndSave(HostRequest request) {
        HttpMethod method = null;

        //create a method object
        String url = "http://"+request.getHost()+":"+request.getPort()+request.getPath() ;
        
        method = new GetMethod(url);
        method.setFollowRedirects(true);

        int code = 0;
        String responseBody = null;
        
        try {
            code = client.executeMethod(method);
            responseBody = method.getResponseBodyAsString(); 
        }catch(IOException | IllegalArgumentException ex){
        
        }
        
        
        
            System.err.println("Illegal url '" + url + "'");
            System.exit(-2);
        
        
        System.out.println("---code: " + code + "\n--response start\n" + responseBody + "\n---response stop");
        
        
        request.setDate(new Date()); 
        request.setStatus(RequestStatus.FINISHED);
        request.setReply(responseBody);
    }
    
    private void processHttpException(HttpException ex,String url){
            System.err.println("Http error connecting to '" + url + "'");
            System.err.println(ex.getMessage());
            System.exit(-4);
    }
    
    private void processIOException(String url){
            System.err.println("Unable to connect to '" + url + "'");
            System.exit(-3);
    }
    
    
    private void processIllegalException(){
    
    }
    
    
    
}
