package org.time2java.jpingservice.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.dao.HostRequestDAO;

/**
 * @author time2java
 * Базовый класс для нашей концепции, у нас есть Producer - Consumer, в главном треде мы читаем команды и отдаем их на исполнение
 * 
 */
abstract public class QueueProcessor extends Thread {
    protected int MAX_ELEMENTS_PEAK = 100;
    protected HostRequestDAO dao;
    protected final ConcurrentLinkedQueue<HostRequest> requestQueue;

    public QueueProcessor(String name, ConcurrentLinkedQueue queue) {
        super(name);
        requestQueue = queue;
        dao = HostRequestDAO.getInstance();
    }

    @Override
    public void run() {
        int iterator = 0;
        List<HostRequest> requestList = new ArrayList<>(MAX_ELEMENTS_PEAK);
        while (!this.isInterrupted()) {
            //read first n elements from queue
            while (!requestQueue.isEmpty() && iterator < MAX_ELEMENTS_PEAK) {
                requestList.add(requestQueue.poll());
                iterator++;
            }
            //process elemetns
            requestList.parallelStream().forEach((HostRequest host) -> {
                processElement(host);
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
    
     abstract protected  void processElement(HostRequest hr) ;
}
