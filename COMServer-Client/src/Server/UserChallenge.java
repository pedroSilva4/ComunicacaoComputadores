/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.InetAddress;


/**
 *
 * @author Pedro
 */
class UserChallenge {
    String name;
    String data;
    int makerPort;
    InetAddress makerAddress;
    int acceptedPort;
    InetAddress acceptedAddress;
    String time;
    Challenge challenge;

    UserChallenge(String name, String date, String time, Challenge get, int makerPort,InetAddress makerAddress) {
       this.name = name;
       this.data =date;
       this.time = time;
       this.challenge = get;
       this.makerPort=makerPort;
       this.makerAddress=makerAddress;   
    }

    void setAcceptedInfo(InetAddress accpetedAddress,int port) {
       this.acceptedAddress=accpetedAddress;
       this.acceptedPort = port;            
    }
}
