package org.time2java.jpingservice;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * @author time2java
 */
public class ProofOfConcept {

    public static void main(String[] argc) {
        new ProofOfConcept().work();
    }

    ThreadProof th ;
    public ProofOfConcept() {
        th = new ThreadProof(new ConcurrentLinkedQueue<>() ) ;
        th.start();
    }

    private void work() {
        Scanner scanner = new Scanner(System.in);

        boolean continueWork = true;
        while (continueWork) {
            String s = scanner.nextLine();
            if(s.toLowerCase().trim().equals("exit")){
                continueWork = false ;
                th.interrupt();
                
                
                continue; 
            }
            
            System.out.println("> add: "+ s);
            th.addElements(s);
        }
        
        scanner.close();
        System.out.println("> stop main");
    }

    class ThreadProof extends Thread {
        ConcurrentLinkedQueue<String> queue ;
        public ThreadProof(ConcurrentLinkedQueue<String> queue) {
            super("Consumer");
            this.queue = queue ;
        }

        private boolean needWork = true ;
        
        @Override
        public void run() {
            while(needWork){
                while(!queue.isEmpty()){
                    System.out.println("> "+getName()+": "+ " message: "+ queue.poll());
                }
                
                synchronized(queue){
                    try {
                        System.out.println("> "+getName()+": start wait \ti:"+ this.isInterrupted());
                        queue.wait();
                        System.out.println("> "+getName()+": stop wait \ti:"+ this.isInterrupted());
                    } catch (InterruptedException ex) {
                            System.err.println("> "+getName()+": innterupted\ti:"+ this.isInterrupted());
                            needWork = false ;
                            continue; 
                    }
                }
            }
        }
        
        public void addElements(String s){
            queue.add(s) ;
            synchronized(queue){
                queue.notify();
            }
        }
    }
}
