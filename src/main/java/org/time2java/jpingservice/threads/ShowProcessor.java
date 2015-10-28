package org.time2java.jpingservice.threads;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.time2java.jpingservice.HostRequest;

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
        HostRequest needShow = dao.getRequest(hr) ;
        System.out.println("< code: "+ hr.getCode() +"date: "+ hr.getDate() +hr.getHost()+":"+hr.getPort()+hr.getPath()+"\t response: "+ hr.getReply());
    }

}
