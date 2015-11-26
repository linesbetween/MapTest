/*NOTE2:
* RUNTIME ERROR: map exits right after launching.
* display issue still under processing
* good news: structure of dicision is kinda clear now. ctrl+F "TODO" to see
    where to add player/building/event code
*/


/*
 * NOTE: 
 * copy IniMap.sql in to project root folder since it's the current running directory.
 * create an empty database (in this example, database name is game) before run.
 * the map table will be created at run time.
 * the ScriptRunner.java and Table.java are used to read sql scripts. 
 ***issue: looks like it can't handle loop or comments

 *
 * What this does now: 
 * initilize layout and place map blocks.
 * initilize map table in database.
 * dice randomly generates number generater) to select block (row) in map table.
 * put indicater (like flag or color change) on selected block.
 * when player1 lands on a block, change block border to red. reset border color to black when leave.
 ***ISSUE: setStyle method seems to override the former setStyle method calss.
           thus changing border color will mess up with backgroud color 
 * assign block to the player who first steps on it. 
 * stores current map into into array of blocks.
 */

package maptest;

/**
 *
 * @author xhf
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Random;
import java.sql.*;
import static javafx.application.Application.launch;
import java.io.*;


public class MapTest extends Application {
  // Statement for executing queries
  private Statement stmt;
  
  
  //For display
  private GridPane[] blockPaneArray; 
  private Label turnLabel;// display who's turn is in current round
  private Label diceResult;//display result of dice rolling
  private Button rollDice;
  private Button stop_Save;
  
  //For locate player
  private int location1;
  private int location2;
  public static int diceNum;
  private boolean turn;//indicate which player's turn
  
  //Array for map table data
  //Each block as an object 
  final int blockNum=30;
  private Block[] blockData;
  
  //Create list of players to hold all player objects
  //Will detect current player by array index.
  private TempPlayer[] playerList; 
 
  
 
  /***************************************
  /*Main method 
  /****************************************/
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Initialize database connection and create a Statement object
    initializeDB();
      
    blockPaneArray = new GridPane[blockNum]; 
    //Initialize user interface   
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(getTop());
    borderPane.setRight(getRight());
    borderPane.setBottom(getBottom());
    borderPane.setLeft(getLeft());
    borderPane.setCenter(getCenter());
    borderPane.setStyle("-fx-background-color: #FFFFFF;");    
    // Create a scene and place it in the stage
    Scene scene = new Scene(borderPane);
    primaryStage.setTitle("Game"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage   
    
     //initialize variables
    location1 = 0;
    location2=0;
    diceNum=0;
    turn = false;    
    
     //create player objects with player id "P1" and "P2"
  playerList[0] = new TempPlayer("P1");
  playerList[1] = new TempPlayer("P2");
        
    
    //initialize blockData array
        //no reload from last map yet
    blockData = new Block [blockNum];
       for (int i=0;i<blockNum;i++){
        blockData[i]= new Block();
        String newId = "blo"+i;
        blockData[i].setBlockId(newId);
        String newType= "Default";
        //TODO set which block is event type
        if ((i==1) || (i==10) || (i==15) || (i==20) ||(i==25)){
            newType="Event";
            blockData[i].setEventId("");
        }
        else{
            newType="Land";
        }
        
        blockData[i].setLandType(newType);
    }
  }
  
 
  
    public static void main(String[] args) {
    launch(args);
  }
  
  /***************************************
  /*User Interface
  /****************************************/  
  
  private HBox getTop(){
      HBox hBox = new HBox();
      for (int i=0;i<10;i++){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
          Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          hBox.getChildren().add(blockPaneArray[i]);
      }
      return hBox;
  }
  
   private HBox getBottom(){
      HBox hBox = new HBox();
      for (int i=24;i>14;i--){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0 ,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
           Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          hBox.getChildren().add(blockPaneArray[i]);
      }
      return hBox;
  }
  
  private VBox getRight(){
      VBox vBox = new VBox();
      for (int i=10;i<15;i++){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
           Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          vBox.getChildren().add(blockPaneArray[i]);
      }
      return vBox;
  }
  
  private VBox getLeft(){
      VBox vBox = new VBox();
      for (int i=29;i>24;i--){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
           Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          vBox.getChildren().add(blockPaneArray[i]);
      }
      return vBox;
  }
  
  private BorderPane getCenter(){
      BorderPane pane = new BorderPane();
      pane.setCenter(getDice());
      stop_Save =new Button("Stop & Save map to database");
      pane.setBottom(stop_Save);
      stop_Save.setOnAction(e -> stopNSave());
      return pane;
  }
  
    
  private GridPane getDice(){
      GridPane pane= new GridPane();
      turnLabel = new Label("Player's turn");
      diceResult = new Label("default");
      rollDice = new Button("Roll Dice");
      pane.add(turnLabel, 0, 0, 2,1);
      pane.add(rollDice, 0, 1, 1,1);
      pane.add(diceResult, 1, 1, 1,1);
    //  try{
      rollDice.setOnAction(e -> roll_improved());
   //   }
     // catch(SQLException e){e.printStackTrace();}
    //  catch(ClassNotFoundException e){e.printStackTrace();}
      
      return pane;
  }

  /***************************************
  /*Database
  /****************************************/
  
   private void initializeDB() {
    try {
      // Load the JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      System.out.println("Driver loaded");
     
      // Establish a connection
      Connection connection = DriverManager.getConnection
        ("jdbc:mysql://localhost/game", "root", "bhcc"); 
    //sub with your own database name,  username and password

      System.out.println("Database connected");
      
      //Call ScriptRunner to run IniMap.sql
      ScriptRunner scriptRunner = new ScriptRunner(connection, false, true);
      String aSQLScriptFilePath = "IniMap.sql";
      scriptRunner.runScript(new FileReader(aSQLScriptFilePath));
      System.out.println("Script run completed");

      // Create a statement
      stmt = connection.createStatement();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
 
   
  /***************************************
  /*Event Handler
  /*Improved version of roll() 
  /*Most functions called in roll_improved() 
  /* 
  /****************************************/
   
   private void roll_improved(){
        //roll dice
       diceNum=(int)(Math.random()*6+1);
       diceResult.setText(Integer.toString(diceNum));
       int playerIndex= 0 ;
       
       //variables hold data of curren player, shared by all players
       int location;//location of current player
       String playerId;
       String otherPlayerId;
       
       //check whose turn. 
       //currently using boolean toggle for 2 players. will use int for more players.
       //this if-else structure contains duplicate codes for both players because:
       // checking if player has made round trip involves both old and new location
       // display (setStyle) is not working properly, further test needed.
       if (turn==false){
           //set current player as P1 by using array index of playerList
           playerIndex = 0;
             // check if player has made one round trip
           if((playerList[playerIndex].getLocation()+diceNum)%30<playerList[playerIndex].getLocation()){ 
                  //TODO (get benefits etc.)
              }        
           
           //reset border color and width before leaving current block (a.k.a. adding dice number)
           blockPaneArray[playerList[playerIndex].getLocation() %30].setStyle("-fx-border-color: #000000 ;-fx-border-width: 1");
           //update location with dice number
           playerList[playerIndex].setLocation((playerList[playerIndex].getLocation()+diceNum)%30);
           //set curren block border color and width 
             blockPaneArray[playerList[playerIndex].getLocation()].setStyle("-fx-border-color: #ff0000; -fx-border-width: 8");  
       }
       else{
           playerIndex =1;
           
           // check if player has made one round trip
           if((playerList[playerIndex].getLocation()+diceNum)%30<playerList[playerIndex].getLocation()){ 
                  //TODO (get benefits etc.)
              }      
           
           blockPaneArray[playerList[playerIndex].getLocation() %30].setStyle("-fx-border-color: black");
           playerList[playerIndex].setLocation((playerList[playerIndex].getLocation()+diceNum)%30);
           blockPaneArray[playerList[playerIndex].getLocation()].setStyle("-fx-background-color: #00FFFF;");
           
       }  
       
       
       //assign current player's value to shared variables
       location = playerList[playerIndex].getLocation(); 
       playerId= playerList[playerIndex].getPlayerId();
       if (playerIndex==1)
           otherPlayerId=playerList[0].getPlayerId();
       else 
           otherPlayerId=playerList[1].getPlayerId();
       
       //shared functions
       
       
        if (blockData[location].getLandType()!="Event"){ // if block type is land 
            if(blockData[location].getLandType()!=playerId){ // if land is NOT owned by current player
                if(blockData[location].getLandType()!=otherPlayerId){// if land is NOT owned by the other player either
                    //set land owner as current player
                    blockData[location].setOwnerId(playerId);
                    //update location & display on map
                    //blockArray[location1%30].setStyle("-fx-background-color: #FFFFFF;");
                    //blockArray[location1].setStyle("-fx-stroke: black");
                    //set background color for owner
                    blockPaneArray[location].setStyle("-fx-background-color: #FFFF00;");
                }
                else{// if land is owned by the other player.
                    //TODO any building on it?(this decision is missing from the flow )
                    //TODO pay or start war?
                    //TODO pay
                    //TODO war
                }
            }
            else{// if land is owned  by current player
                //TODO any building on it?
                //TODO ...
            }
            
            
            }
            else  {//if block type is event
                //TODO add event 
            }
       
       
       turn = !turn; // switch turn 
       
       
   }
   
   private int roll() { 
       //roll dice
       diceNum=(int)(Math.random()*6+1);
       diceResult.setText(Integer.toString(diceNum));
       
       int location;//current player's location
       String  currentPlayerId;
       if (turn==false){    //if current player is P1    
           //reset border color and width before leaving current block
              blockPaneArray[location1%30].setStyle("-fx-border-color: #000000 ;-fx-border-width: 1"); 
              //blockArray[location1%30].setStyle("-fx-border-width: 1");
              //update location
              if((location1+diceNum)%30<location1){ // check if player has finished one round trip
                  //TODO (get benefits etc.)
              }
            location1=(location1+diceNum)%30;
            //set curren block border color and width 
             blockPaneArray[location1].setStyle("-fx-border-color: #ff0000; -fx-border-width: 8");  
              //blockArray[location1].setStyle("-fx-border-width: 8");
            //update ownerId in mapData array
            if ((location1!=5) && (location1!=10)&&(location1!=15)&&(location1!=20)&&(location1!=25)
                    &&blockData[location1].getOwnerId()==null){ // if block has never been stepped on 
                blockData[location1].setOwnerId("P1");
                //update location & display on map
                //blockArray[location1%30].setStyle("-fx-background-color: #FFFFFF;");
                //blockArray[location1].setStyle("-fx-stroke: black");
                blockPaneArray[location1].setStyle("-fx-background-color: #FFFF00;");
            
            }
            else if ((location1!=5) && (location1!=10)&&(location1!=15)&&(location1!=20)&&(location1!=25)
                    &&blockData[location1].getOwnerId()!=null) {//if block is owned by other players
                //TODO add event 
            }
       }     
       else{ //if current players is P2
                   
            location2=(location2+diceNum)%30;
            if ((location2!=5) && (location2!=10)&&(location2!=15)&&(location2!=20)&&(location2!=25)
                    &&blockData[location2].getOwnerId()==null ){
                blockData[location2].setOwnerId("P2");
                //blockArray[location2%30].setStyle("-fx-background-color: #FFFFFF;");
                blockPaneArray[location2].setStyle("-fx-border-color: black");
                blockPaneArray[location2 ].setStyle("-fx-background-color: #00FFFF;");
            
            }
       }
       turn = !turn; // switch turn.
       return diceNum ; 
       
   }
   
   
   private void stopNSave(){
       
       //display info in output
       System.out.println("Current map data saved");
       for (int i=0;i<blockNum;i++){
           String blockId=blockData[i].getBlockId();
           String landType=blockData[i].getLandType();
           String ownerId=blockData[i].getOwnerId();
           System.out.format("Block%d ID is %s , landType is %s , Owner ID is %s", i, blockId,landType,ownerId);
           System.out.println();
       }
   }
   
   
   private void linkDatabase(){
        /*
            try {
             Connection connection = DriverManager.getConnection
        ("jdbc:mysql://localhost/game", "root", "bhcc"); 
             Statement statement = connection.createStatement();
             //String queryString = "insert into Student (firstName, mi, lastName) " + "values (?, ?, ?)";
             //PreparedStatement preparedStatement = connection.prepareStatement(queryString);
             /** TEMP DELETE
            ResultSet resultSet = statement.executeQuery("update map set ownerId="
                    + " 'p1' where blockId='blo'+ Convert(Varchar,location1) ");
                    
            } catch(SQLException e){e.printStackTrace();}
          */
       
         /*
            try {
              Connection connection = DriverManager.getConnection
        ("jdbc:mysql://localhost/game", "root", "bhcc"); 
             Statement statement = connection.createStatement();
             //String queryString = "insert into Student (firstName, mi, lastName) " + "values (?, ?, ?)";
             //PreparedStatement preparedStatement = connection.prepareStatement(queryString);
             /*
            ResultSet resultSet = statement.executeQuery("update map set ownerId="
                    + " 'p2' where blockId='blo'+ Convert(Varchar,location2) ");
                     
             ResultSet resultSet = statement.executeQuery("update map set ownerId="
                    + " 'p2' where blockId='blo'+ Convert(Varchar,location2) ");
            } catch(SQLException e){e.printStackTrace();}
                    */
   }
   
   
   
}

 

