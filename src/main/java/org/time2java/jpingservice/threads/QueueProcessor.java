package org.time2java.jpingservice.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.dao.HostRequestDAO;

/**
 * @author time2java Базовый класс для нашей концепции, у нас есть Producer -
 * Consumer, в главном треде мы читаем команды и отдаем их на исполнение
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

    private boolean work = true;

    @Override
    public void run() {
        int iterator = 0;
        List<HostRequest> requestList = new ArrayList<>(MAX_ELEMENTS_PEAK);
        while (work) {

            //read first n elements from queue
            while (!requestQueue.isEmpty() && iterator < MAX_ELEMENTS_PEAK) {
                System.out.println(getName()+": read");
                requestList.add(requestQueue.poll());
                iterator++;
            }
            //process elemetns
//            System.out.println(getName()+": process "+ requestList.size());
//            requestList.parallelStream().forEach((HostRequest host) -> {
//                processElement(host);
//            });

            //ready for new chunk
            requestList.clear();
            iterator = 0;

            //need sleep and wait for next elements
            if (requestQueue.isEmpty()) {
                try {
                    synchronized (requestQueue) {
                        System.out.println("> " + getName() + ": start wait \ti:" + this.isInterrupted());
                        requestQueue.wait();
                        System.out.println("> " + getName() + ": stop wait \ti:" + this.isInterrupted());
                    }
                } catch (InterruptedException ex) {
                    System.out.println(getName() + " interrupted ");
                    work = false;
                     continue;
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
}
