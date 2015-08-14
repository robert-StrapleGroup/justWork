package main;

import controler.MainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

//        Dialog<String> dialog = new Dialog<>();
//        dialog.setTitle("Login Dialog");
//        dialog.setHeaderText(null);
//        ButtonType loginButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));
//        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(
//                "First", "Second", "Third"));
//        TextField textField = new TextField();
//        textField.setPromptText("New activity");
//        Button addButton = new Button("Add");
//        grid.add(new Label("Choose activity"), 0, 0);
//        grid.add(choiceBox, 1, 0);
//        grid.add(new Label("--or--"), 0, 1);
//        grid.add(new Label("Add new one:"), 0, 2);
//        grid.add(textField, 1, 2);
//        grid.add(addButton, 2, 2);
//        dialog.getDialogPane().setContent(grid);
//        Optional<String> result = dialog.showAndWait();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("justWork Start Window");
        dialog.setHeaderText("Please, type activity you want to start to continue");
        dialog.setContentText("Activity");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            if(!result.get().equals("")) {
                primaryStage.setTitle("justWork");
                Font.loadFont(Main.class.getResource("/res/vcr.ttf").toExternalForm(), 10);
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
