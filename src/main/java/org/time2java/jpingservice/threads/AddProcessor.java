package org.time2java.jpingservice.threads;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.RequestStatus;

/**
 * @author time2java
 */
public class AddProcessor extends QueueProcessor {

    private final HttpClient client;

    public AddProcessor(ConcurrentLinkedQueue<HostRequest> queue) {
        super("NetWorker", queue);
        
        final int timeout = 1 ; //1s
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        
        client.getParams().setParameter("http.socket.timeout", timeout * 1000);
        client.getParams().setParameter("http.connection.timeout", timeout * 1000);
        client.getParams().setParameter("http.connection-manager.timeout", timeout * 1000L);
        client.getParams().setParameter("http.protocol.head-body-timeout", timeout * 1000);
    }


    @Override
    protected void processElement(HostRequest request) {
        HttpMethod method = null;

        //create a method object
        String url = "http://" + request.getHost() + ":" + request.getPort() + request.getPath();

        method = new GetMethod(url);
        method.setFollowRedirects(true);
        

        String responseBody = null;

        int code = 500 ; 
        try {
            code = client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
            method.releaseConnection(); 
        } catch (IOException | IllegalArgumentException ex) {
            processException(ex, url);
        }

        saveRequestResult(request, responseBody, code);
    }

    private void processException(Exception ex, String url) {
        if (ex instanceof IllegalArgumentException) {
            System.err.println("Illegal url '" + url + "'");
            System.err.println(ex.getMessage());
            return;
        }

        if (ex instanceof HttpException) {
            System.err.println("Http error connecting to '" + url + "'");

            return;
        }

        System.err.println("Unable to connect to '" + url + "'");
        System.err.println(ex.getMessage());
    }

    private void saveRequestResult(HostRequest request, String responseBody, int code) {
        request.setDate(new Date());
        request.setCode(code);
        request.setReply(responseBody);
        request.setStatus(RequestStatus.FINISHED);
        dao.addOrUpdateRequest(request);
    }
}
