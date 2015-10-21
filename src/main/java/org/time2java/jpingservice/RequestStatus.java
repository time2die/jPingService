package org.time2java.jpingservice;

/**
 *
 * @author time2java
 */
public enum RequestStatus {
    NEW,        // только считали
    ADDED,      // добавили в потом обработки
    FINISHED;   // результат известен
}
