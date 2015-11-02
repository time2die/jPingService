package org.time2java.jpingservice;

/**
 * @author time2java
 */
public class StatisticHandler {
    private static long elementsWasAded = 0 ;
    private static long elementsWasProcessed = 1 ;
    
    
    private static long processAnywayFirstelements = 2 ;
    
    
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

        System.out.println("k :"+ elementsWasProcessed / elementsWasAded);
        
        if( elementsWasProcessed / elementsWasAded < 0.5){
            return false ;
        }
        
        return true ;
    }
}
