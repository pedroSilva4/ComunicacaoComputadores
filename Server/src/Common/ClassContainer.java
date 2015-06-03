/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import Server.ChallengesInfo;
import Server.Clients;


/**
 *
 * @author Pedro
 */
public class ClassContainer {
   public Clients clients;
   public ChallengesInfo chinfo;
   
    public ClassContainer( Clients clients, ChallengesInfo chinfo){
        this.clients = clients;
        this.chinfo = chinfo;
     
    }
}
