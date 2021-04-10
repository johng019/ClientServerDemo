package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();
        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server");
        // Set the stage title
        primaryStage.setScene(scene);
        // Place the scene in the stage
        primaryStage.show();
        // Display the stage
        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(3000);
                Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));
                // Listen for a connection request
                Socket socket = serverSocket.accept();
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    // Receive number from the client
                    int input = inputFromClient.readInt();
                    //Is Prime
                    boolean prime = false;
                    for (int i = 2; i <= input / 2; ++i) {
                        // condition for prime / non prime number
                        if (input % i == 0) {
                            prime = true;
                            break;
                        }
                    }

                    if (!prime)
                        prime = true;
                    else
                        prime = false;

                    // Send area back to the client
                    outputToClient.writeBoolean(prime);

                    boolean finalPrime = prime;
                    Platform.runLater(() -> {
                        ta.appendText("Number received from client: " + input + '\n');
                        ta.appendText("Is this number prime?: " + finalPrime + '\n');
                    });
                }
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();  }
}
