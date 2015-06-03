/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import Server.ChallengesInfo;
import Server.Clients;
import Server.VirtualChallenges;

/**
 *
 * @author Pedro
 */
public class ClassContainer {
   public Clients clients;
   public ChallengesInfo chinfo;
   public VirtualChallenges virtualInfo;
    
    public ClassContainer( Clients clients, ChallengesInfo chinfo, VirtualChallenges virtualInfo){
        this.clients = clients;
        this.chinfo = chinfo;
        this.virtualInfo = virtualInfo;
    }
}
