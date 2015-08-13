package main;

import controler.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("justWork Start Window");
        dialog.setHeaderText("Please, type activity you want to start to continue");
        dialog.setContentText("Activity");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            if(!result.get().equals("")) {
                primaryStage.setTitle("justWork");
                URL location = getClass().getResource("main.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(location);
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainController pMainController = (MainController) fxmlLoader.getController();
                pMainController.onStart(result.get());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                start(primaryStage);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
