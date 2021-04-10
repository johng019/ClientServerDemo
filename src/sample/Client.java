package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static javafx.application.Application.launch;

public class Client extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Panel p to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a number: "));
        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);
        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);
        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client");
        // Set the stage title
        primaryStage.setScene(scene);
        // Place the scene in the stage
        primaryStage.show();
        // Display the stage
        tf.setOnAction(e -> {
            try { // Get the radius from the text field
                int number = Integer.parseInt(tf.getText().trim());
                // Send the radius to the server
                toServer.writeInt(number);
                toServer.flush();
                // Get area from the server
                boolean prime = fromServer.readBoolean();
                // Display to the text area
                ta.appendText("The number sent to the server is " + number + "\n");
                ta.appendText("Does the server say that the number " + number + " is prime? " + prime + '\n');      }
            catch (IOException ex) {
                System.err.println(ex);  }
        });
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 3000);
            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }
    }  /**   * The main method is only needed for the IDE with limited   * JavaFX support. Not needed for running from the command line.   */

    public static void main(String[] args) {
        launch(args);
    }
}

