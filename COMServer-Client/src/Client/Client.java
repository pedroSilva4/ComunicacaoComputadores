package Client;


import Common.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    static int ComunicationPort;
    public static void main(String[] args) throws SocketException, IOException, InterruptedException {
       
      
        String Server_host = "localhost";
        DatagramSocket socket = new DatagramSocket(4444);
        int label  = 1;
        HelloMessenger hellom = new HelloMessenger(socket,Server_host,label);
        hellom.start();
        
        
        DatagramPacket ACK = new DatagramPacket(new byte[256], 256);
        socket.receive(ACK);
        PDU reply = PDU.fromBytes(ACK.getData());
        if(reply.getType()==0)
        {
            System.out.println(ACK.getAddress().getHostAddress()+": "+ ACK.getPort() + " -->  says it's okay");
            ComunicationPort = ACK.getPort();
            hellom.hello_time = false;
            hellom.join();
        }
               
           socket.connect(InetAddress.getByName(Server_host), ComunicationPort);
            
           while(true){
            PDU pdu =  PDU_Builder.REGISTER_PDU("Pedro", "pedrosilva", "1234".getBytes(), label);

            byte[] data= PDU.toBytes(pdu);
            DatagramPacket request = new DatagramPacket(data, data.length);
            socket.send(request);
            System.out.println("just Sended");
            ACK.setData(new byte[1024]);
            ACK.setLength(1024);
            socket.receive(ACK);
            reply = PDU.fromBytes(ACK.getData());
            
            System.out.println(reply.getLabel());
            System.out.println(reply.getType());
           }
    }
    
    
                
    
}
    

