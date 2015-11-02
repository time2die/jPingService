package org.time2java.jpingservice;

/**
 * @author time2java
 */
public class StatisticHandler {
    private static long elementsWasAded = 0 ;
    private static long elementsWasProcessed = 5 ;
    
    // первые n элементов которые мы в любом случаи обрабатываем прежде чем начнем отвечать отказом
    private static long processAnywayFirstelements = PropertiesHolder.getLongProperties("sh.processAnywayFirstelements", 10l) ;
    // коофицент при котором следует прекратить обрабатывать запросы, по дефолту к 2 добавленным запросам нужно успевать обрабатывать только 1
    private static long badWorkK = PropertiesHolder.getLongProperties("sh.processAnywayFirstelements", 2l) ;
    
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
        if(elementsWasProcessed == 0 || elementsWasProcessed == 0 ){
            return false ;
        }
        
        if(elementsWasAded < processAnywayFirstelements){
            return true ;
        }

        if( elementsWasAded / elementsWasProcessed > badWorkK ){
            return false ;
        }
        
        return true ;
    }
}
