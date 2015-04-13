/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.Challenge;
import java.net.InetAddress;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Pedro
 */
class UserChallenge {
    String name;
    Date data;
    int makerPort;
    InetAddress makerAddress;
    int acceptedPort;
    InetAddress acceptedAddress;
    Time time;
    Challenge challenge;

    UserChallenge(String name, Date date, Time time, Challenge get, int makerPort,InetAddress makerAddress) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setAcceptedInfo(InetAddress accpetedAddress,int port) {
       this.acceptedAddress=accpetedAddress;
       this.acceptedPort = port;            
    }
}
