/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package maptest;

import java.util.ArrayList;

/**
 *
 * @author mtg
 */
public class TempPlayer {
    private String playerId;
    private int location;
    private ArrayList<String> landIdList;
    
    TempPlayer(){
        location =0;
        landIdList= new ArrayList<String>();
    }
    
    TempPlayer(String id ){
        playerId = id;
        location =0;
        landIdList= new ArrayList<String>();
    }
    
    int getLocation(){
        return location;
    }
    
    void setLocation(int loc){
        this.location=loc;
    }
    
    void addLandId(String newLand){
        landIdList.add(newLand);
    }
    
    ArrayList<String> getLandIdList(){
        return landIdList;
    }
}
