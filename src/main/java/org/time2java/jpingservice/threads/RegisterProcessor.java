package org.time2java.jpingservice.threads;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.RequestStatus;
import org.time2java.jpingservice.StatisticHandler;

/**
 * @author time2java
 */
public class RegisterProcessor extends QueueProcessor {

    private AddProcessor ap;

    public RegisterProcessor(ConcurrentLinkedQueue queue, AddProcessor ap) {
        super("Register procesor", queue);

        this.ap = ap;
    }

    @Override
    protected void processElement(HostRequest hr) {
        boolean needMakerequest = saveNewRequestIfNeed(hr);

        if (!needMakerequest) {
            return;
        }

        StatisticHandler.addElement();
        ap.addHostToQueue(hr);
    }

    private boolean saveNewRequestIfNeed(HostRequest request) {
        request.setStatus(RequestStatus.ADDED);
        HostRequest rq = dao.getAddedRequest(request);
        if (rq != null) {
            System.out.println("< " + rq.toLittleString() + " already in base.");
            return false;
        }

        System.out.println("< take in work: " + request.toLittleString());

        return true;
    }

}
