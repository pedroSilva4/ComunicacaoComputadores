
import Server.ReceiverThread;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro
 */
public class Master {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ServerSocket ss = new ServerSocket(50000);
            Socket sc;
            while(true){
                sc = ss.accept();
                System.out.println("Hello world");
                ReceiverThread t = new ReceiverThread(sc);
                System.out.println("Hello world2");
                t.start();
                System.out.println("Hello world3");
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class ServerHandler {
        Socket sc;
        ReceiverThread rec;
        public ServerHandler(Socket sc) {
            this.sc = sc;
        
        }
    }

   
}
