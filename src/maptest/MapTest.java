/*
 * NOTE: 
 * copy IniMap.sql in to project root folder since it's the current running directory.
 * create an empty database (in this example, database name is game) before run.
 * the map table will be created at run time.
 * the ScriptRunner.java and Table.java are used to read sql scripts. 
 ***issue: seems like can't handle loop or comments
 *
 * What this does now: 
 * initilize layout and place map blocks.
 * initilize map table in database.
 * What needs to be done
 * dice randomly generates number generater) to select block (row) in map table.
 * put indicater (like flag or color change) on selected block.
 * assign block to the player who first steps on it. 
 * 
 */

package maptest;

/**
 *
 * @author xhf
 */
import javafx.application.Application;
import javafx.scene.Scene;
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
import java.sql.*;
import static javafx.application.Application.launch;
import java.io.*;


public class MapTest extends Application {
  // Statement for executing queries
  private Statement stmt;
  /*
  private TextField tfSSN = new TextField();
  private TextField tfCourseId = new TextField();
  private Label lblStatus = new Label();
  */
  final int blockNum=40;
  private GridPane[] blockArray; 
  private Label turnLabel;// display who's turn is in current round
  private Label diceResult;//display result of dice rolling
  private Button rollDice;
 
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Initialize database connection and create a Statement object
    initializeDB();
   
      //initialize variables
     blockArray = new GridPane[blockNum];
      
    //Initialize user interface   
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(getTop());
    borderPane.setRight(getRight());
    borderPane.setBottom(getBottom());
    borderPane.setLeft(getLeft());
    borderPane.setCenter(getCenter());
  
   // btShowGrade.setOnAction(e -> showGrade());
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(borderPane);
    primaryStage.setTitle("Game"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage   
  }
  
  private HBox getTop(){
      HBox hBox = new HBox();
      for (int i=0;i<10;i++){
          blockArray[i] = new GridPane();
          blockArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockArray[i].setPrefSize(100,100);
          blockArray[i].setStyle("-fx-border-color: black");
          blockArray[i].add(new Label("Block ID"+ (i+1)), 0, 0);
          blockArray[i].add(new Label("Building ID:"), 0, 1);
          blockArray[i].add(new Label("Land Type:"), 0, 2);
          hBox.getChildren().add(blockArray[i]);
      }
      return hBox;
  }
  
   private HBox getBottom(){
      HBox hBox = new HBox();
      for (int i=24;i>14;i--){
          blockArray[i] = new GridPane();
          blockArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockArray[i].setPrefSize(100,100);
          blockArray[i].setStyle("-fx-border-color: black");
          blockArray[i].add(new Label("Block ID"+ (i+1)), 0, 0);
          blockArray[i].add(new Label("Building ID:"), 0, 1);
          blockArray[i].add(new Label("Land Type:"), 0, 2);
          hBox.getChildren().add(blockArray[i]);
      }
      return hBox;
  }
  
  private VBox getRight(){
      VBox vBox = new VBox();
      for (int i=10;i<15;i++){
          blockArray[i] = new GridPane();
          blockArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockArray[i].setPrefSize(100,100);
          blockArray[i].setStyle("-fx-border-color: black");
          blockArray[i].add(new Label("Block ID"+ (i+1)), 0, 0);
          blockArray[i].add(new Label("Building ID:"), 0, 1);
          blockArray[i].add(new Label("Land Type:"), 0, 2);
          vBox.getChildren().add(blockArray[i]);
      }
      return vBox;
  }
  
  private VBox getLeft(){
      VBox vBox = new VBox();
      for (int i=29;i>24;i--){
          blockArray[i] = new GridPane();
          blockArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockArray[i].setPrefSize(100,100);
          blockArray[i].setStyle("-fx-border-color: black");
          blockArray[i].add(new Label("Block ID"+ (i+1)), 0, 0);
          blockArray[i].add(new Label("Building ID:"), 0, 1);
          blockArray[i].add(new Label("Land Type:"), 0, 2);
          vBox.getChildren().add(blockArray[i]);
      }
      return vBox;
  }
  
  private BorderPane getCenter(){
      BorderPane pane = new BorderPane();
      pane.setCenter(getDice());
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
      return pane;
  }
  
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
  
   public static void main(String[] args) {
    launch(args);
  }
}
  
  class CustomPane extends StackPane {
    public CustomPane(String title) {
    getChildren().add(new Label(title));    
    //setStyle("-fx-border-color: red");
    setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
     }
    
  }

  
 // Button btShowGrade = new Button("Show Grade");
    //HBox hBox = new HBox(5);
    //hBox.getChildren().addAll(new Label("SSN"), tfSSN, 
     // new Label("Course ID"), tfCourseId, (btShowGrade));

    //VBox vBox = new VBox(10);
   // vBox.getChildren().addAll(hBox, lblStatus);
    
    //tfSSN.setPrefColumnCount(6);
   // tfCourseId.setPrefColumnCount(6);
 

 
  /*
  private void showGrade() {
    String ssn = tfSSN.getText();
    String courseId = tfCourseId.getText();
    try {
      String queryString = "select firstName, mi, " +
        "lastName, title, grade from Student, Enrollment, Course " +
        "where Student.ssn = '" + ssn + "' and Enrollment.courseId "
        + "= '" + courseId +
        "' and Enrollment.courseId = Course.courseId " +
        " and Enrollment.ssn = Student.ssn";

      ResultSet rset = stmt.executeQuery(queryString);

      if (rset.next()) {
        String lastName = rset.getString(1);
        String mi = rset.getString(2);
        String firstName = rset.getString(3);
        String title = rset.getString(4);
        String grade = rset.getString(5);

        // Display result in a label
        lblStatus.setText(firstName + " " + mi +
          " " + lastName + "'s grade on course " + title + " is " +
          grade);
      } else {
        lblStatus.setText("Not found");
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  */

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
 

