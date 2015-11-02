package org.time2java.jpingservice;

/**
 * @author time2java
 */
public class StatisticHandler {
    private static long elementsWasAded = 0 ;
    private static long elementsWasProcessed = 0 ;
    
    public static void addElement(){
        elementsWasAded ++ ;
    }
    
    public static void addElements(long newElementsCount){
        elementsWasAded += newElementsCount ;
    }
    
    public static void elementsProcessd() {
        elementsWasProcessed ++ ;
    }
    
    public static boolean canContinueTakeRequest(){
        return true ;
    }
}
