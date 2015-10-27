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
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.RequestStatus;
import org.time2java.jpingservice.dao.HostRequestDAO;

/**
 * @author time2java
 */
public class NetWorker extends Thread {

    private HostRequestDAO dao;

    private int MAX_ELEMENTS_PEAK = 100;
    private final ConcurrentLinkedQueue<HostRequest> requestQueue;
    private final HttpClient client;

    public NetWorker(ConcurrentLinkedQueue<HostRequest> queue) {
        System.out.println("start netWorker");
        requestQueue = queue;
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        dao = HostRequestDAO.getInstance();
    }

    @Override
    public void start() {

        int iterator = 0;
        List<HostRequest> requestList = new ArrayList<>(MAX_ELEMENTS_PEAK);

        while (!this.isInterrupted()) {

            //read first n elements from queue
            while (!requestQueue.isEmpty() && iterator < MAX_ELEMENTS_PEAK) {
                requestList.add(requestQueue.poll());
                iterator++;
            }

            //process elemetns
            requestList.parallelStream().forEach((host) -> {
                getResponseAndSave(host);
            });

            requestList.clear();

            //need sleep and wait for next elements
            if (iterator == MAX_ELEMENTS_PEAK) {
                try {
                    requestQueue.wait();
                } catch (InterruptedException ex) {
                }
            }

            iterator = 0;
        }
    }

    private void getResponseAndSave(HostRequest request) {
        HttpMethod method = null;

        //create a method object
        String url = "http://" + request.getHost() + ":" + request.getPort() + request.getPath();

        method = new GetMethod(url);
        method.setFollowRedirects(true);

        String responseBody = null;

        try {
            client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
        } catch (IOException | IllegalArgumentException ex) {
            processException(ex, url);
        }

        saveRequestResult(request, responseBody, method.getStatusCode());
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
        dao.updateRequest(request);
    }
}
