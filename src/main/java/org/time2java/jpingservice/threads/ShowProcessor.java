package org.time2java.jpingservice.threads;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.HostRequest;
import org.time2java.jpingservice.RequestStatus;

/**
 * @author time2java
 * Просто отдаем результаты ранее заказаных сайтов
 */
public class ShowProcessor extends QueueProcessor{

    public ShowProcessor(ConcurrentLinkedQueue queue) {
        super("ShowProcessor", queue);
    }

    @Override
    protected void processElement(HostRequest hr) {
        hr.setStatus(RequestStatus.FINISHED);
        
        HostRequest needShow = dao.getRequest(hr) ;
        if(needShow != null){
            System.out.println("< "+ needShow);
        }else{
                System.out.println("< code 404");
        }
        System.out.print("< ");
    }

}
