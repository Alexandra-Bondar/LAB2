package sample;

import javafx.application.Application;
import javafx.application.Platform;
//import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

    // peremennaya zadauschaya server or client
    // if you want create a connection you must write "false" becouse the programm for client and server not have difference.
    // When ypu write for server connection - server get port and set ip for client
    private boolean isServer = true;

    //connect to server from client
    private TextArea messages = new TextArea();
    private NetworkConnection connection = isServer ? createServer():createClient();

    //create a connection
    private Parent createContent(){
        // pole vvoda sms i ego rabota
        messages.setPrefHeight(450);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "User1: " : "User2: ";
           //message = color("Red");
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            try{
                connection.send(message);
            }
            catch(Exception e) {
                messages.appendText("Failed to send\n");
            }
        });

        VBox root = new VBox(20, messages, input); //!!!!!!!!!!!! pomestit' v eto pole smile's
        root.setPrefSize(300, 500);
        return root;

    }

    @Override
    public void init() throws Exception{
    connection.startConnection();
}

    @Override
    public void start(Stage primaryStage) throws Exception{
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ChatClick");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws  Exception{
            connection.closeConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }


    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
