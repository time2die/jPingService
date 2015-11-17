package org.time2java.jpingservice.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.PropertiesHolder;
import org.time2java.jpingservice.StatisticHandler;
import org.time2java.jpingservice.dao.HostRequestDAO;

/**
 * @author time2java Базовый класс для нашей концепции, у нас есть Producer -
 * Consumer, в главном треде мы читаем команды и отдаем их на исполнение
 *
 */
abstract public class QueueProcessor extends Thread {

    protected int MAX_ELEMENTS_PEAK = PropertiesHolder.getIntProperties("MAX_ELEMENTS_PEAK", 1000);
    protected HostRequestDAO dao;
    protected final ConcurrentLinkedQueue<HostRequest> requestQueue;

    public QueueProcessor(){
        this("QueueProcessor", new ConcurrentLinkedQueue<HostRequest>()) ;
    }
    
    public QueueProcessor(String name, ConcurrentLinkedQueue queue) {
        super(name);
        requestQueue = queue;
        dao = HostRequestDAO.getInstance();
    }

    private boolean canWork = true;

    @Override
    public void run() {
        int iterator = 0;
        List<HostRequest> requestList = new ArrayList<>(MAX_ELEMENTS_PEAK);

        while (canWork) {
            //read first n elements from queue
            while (!requestQueue.isEmpty() && iterator < MAX_ELEMENTS_PEAK) {
                requestList.add(requestQueue.poll());
                iterator++;
            }

            //process elemetns
            requestList.parallelStream().forEach((HostRequest host) -> {
                processElement(host);
            });

            //ready for new chunk
            requestList.clear();
            iterator = 0;

            //need sleep and wait for next elements
            if (requestQueue.isEmpty()) {
                synchronized (requestQueue) {
                    try {
                        requestQueue.wait();
                    } catch (InterruptedException ex) {
                        canWork = false;
                    }
                }
            }
        }
    }

    abstract protected void processElement(HostRequest hr);

    public void addHostToQueue(HostRequest hr) {
        if (hr == null) {
            return;
        }

        requestQueue.add(hr);
        synchronized (requestQueue) {
            requestQueue.notify();
        }
    }

    public void addAll(List<HostRequest> toAdd) {
        if(toAdd !=  null ){
            StatisticHandler.addElements(toAdd.size());
        }
        
        requestQueue.addAll(toAdd);
        synchronized (requestQueue) {
            requestQueue.notify();
        }
    }
}
