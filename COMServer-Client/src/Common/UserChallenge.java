/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import Common.ChallengeType;
import java.net.InetAddress;


/**
 *
 * @author Pedro
 */
public class UserChallenge {
    public String name;
    public String data;
    public int makerPort;
    public InetAddress makerAddress;
    public int acceptedPort;
    public InetAddress acceptedAddress;
    public String time;
    public ChallengeType challenge;

    public UserChallenge(String name, String date, String time, ChallengeType get, int makerPort,InetAddress makerAddress) {
       this.name = name;
       this.data =date;
       this.time = time;
       this.challenge = get;
       this.makerPort=makerPort;
       this.makerAddress=makerAddress;   
    }

    public void setAcceptedInfo(InetAddress accpetedAddress,int port) {
       this.acceptedAddress=accpetedAddress;
       this.acceptedPort = port;            
    }
}
