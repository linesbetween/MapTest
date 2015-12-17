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
public class TempBuilding {
    private int buildingId;
    private int location; // landID
    private int buildPrice; // gold to build
    private int upgradePrice;
    private int chargePrice;// gold to charge the othe player
    private int level;
   
    
   
    
     TempBuilding(){
       buildPrice =500;
       chargePrice = 100;
       upgradePrice = 100;
       level=1;
    }
    
    TempBuilding(int id, int location ){
        buildingId = id;
        this.location =location;
        buildPrice =500;
        chargePrice = 100;
        upgradePrice = 100;
        level=1;
        
    }
    
    int getLocation(){
        return location;
    }
    
    void setLocation(int loc){
        this.location=loc;
    }
    
    int getBuildingId(){
        return buildingId;
    }
    
    void setBuildingId(int id){
        buildingId =id;
    }
    
     int getBuildPrice(){
        return buildPrice;
    }
    
    void setBuildPrice(int price){
        buildPrice = price;
    }
    
    int getChargePrice(){
        return chargePrice;
    }
    
    void setChargePrice(int price){
        chargePrice = price;
    }
    
     int getUpgradePrice(){
        return upgradePrice;
    }
    
    void setUpgradePrice(int price){
        upgradePrice = price;
    }
    
    void levelUp(){
        level++;
        chargePrice+=100;
    }
    
}
