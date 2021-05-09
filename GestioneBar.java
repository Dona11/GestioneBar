import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utente
 */
public class GestioneBar {
    
    private static Semaphore semVuoto = new Semaphore(3);
    private static Semaphore semPieno = new Semaphore(0);
    private static int incasso = 0;
    
    public static class Bar{
    
        private int posti;

        public Bar(int posti) {
            this.posti = posti;
            
            System.out.println("Il bar apre\n");
        }
        
        public void clienteEntra(){
        
            posti--;
        }
        
//        public void clienteEsce(){
//        
//            posti++;
//        }
    }
    
    public static class ProduttoreConsumatore extends Thread{
    
        private Bar bar;
        private int caffeOrdinati = (int) (Math.random() * 4) + 1;

        public ProduttoreConsumatore(String nome, Bar bar) throws InterruptedException {
            
            super(nome);
            this.bar = bar;
            start();
        }
        
        public void run(){
            
            while(true){

                try{
                    sleep((int) (Math.random() * 200));
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                try{

                    semVuoto.acquire();
                }catch(InterruptedException ex){

                }

                bar.clienteEntra();
                incasso += caffeOrdinati;
                System.out.println(getName() + " entra, il cliente ordina " + caffeOrdinati + " caffè");
                semPieno.release();
                
                try{

                    sleep((int) (Math.random() * 300));
                }catch(InterruptedException e){

                    e.printStackTrace();
                }

                try{

                    semPieno.acquire();
                }catch(InterruptedException ex){

                }
                
                System.out.println(getName() + " consuma " + caffeOrdinati + " caffè ed esce");
                semVuoto.release();
                try {
                    this.join();
                } catch (InterruptedException ex) {
                    
                }
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException{
    
        Bar bar = new Bar(10);
        
        for(int i = 1; i <= 10; i++){
        
            new ProduttoreConsumatore("Cliente " + 1, bar);
        }
        
        System.out.println("Il bar chiude");
    }
}