
import Server.Sender;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro
 */
public class Slave {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        try {
            // TODO code application logic here
            Socket sc = new Socket("localhost", 50000);
            
            
            while(true){
                System.out.println("Hello world");
                Sender send = new Sender(sc, "ok");
                System.out.println("Hello world2");
                send.start();
                send.join();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
