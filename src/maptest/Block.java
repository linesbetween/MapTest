/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package maptest;

/**
 *
 * @author mtg
 */
public class Block {
    private String blockId;
    private String landType;
    private String buildingId;
    private String eventId;
    private String ownerId;
    private String visitorId;
    
    public Block(){
        
    }
    
    public Block (String blockId){
        this.blockId=blockId;
    }
    
    public String getBlockId(){
        return blockId;
    }
    
    public  String getLandType(){
        return landType;
    }
     
    public  String getBuildingId(){
        return buildingId;
    }
      
    public   String getEventId(){
        return eventId;
    }
    public    String getOwnerId(){
        return ownerId;
    }
    public     String getVistorId(){
        return visitorId;
    }
    
    public void setBlockId (String blockId){
        this.blockId=blockId;
    }
    
    public void setLandType (String landType){
        this.landType=landType;
    }
    
    public void setEventId (String eventId){
        this.eventId=eventId;
    }
    
    public void setBuidlingId (String buildingId){
        this.buildingId=buildingId;
    }
    
    public void setOwnerId (String ownerId){
        this.ownerId=ownerId;
    }
    
    public void setVisitorId (String visitorId){
        this.visitorId=visitorId;
    }
}
