package controler;

import io.XmlParser;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DataFormat;
import javafx.util.Duration;
import objects.HistoryObj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Created by robert on 13.08.2015.
 */
public class MainController {
    public String ACTIVITY_NAME = "";
    XmlParser xmlParser;
    private Date timeStamp = null;
    private long sessionHour = 0;
    private long sessionMin = 0;
    private long sessionS = 0;
    private long completeHour = 0;
    private long completeMin = 0;
    private long completeS = 0;

    @FXML
    private Label category_name;
    @FXML
    private Label session_time;
    @FXML
    private TableView<HistoryObj> history_table;
    @FXML
    private Label complete_time;

    private ObservableList<HistoryObj> history = FXCollections.observableArrayList();
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            sessionS++;
            completeS++;
            if (sessionS > 59) {
                sessionMin++;
                sessionS = 0;
            }
            if (sessionMin > 59) {
                sessionHour++;
                sessionMin = 0;
            }
            session_time.setText(Long.toString(sessionHour) + "h "
                    + Long.toString(sessionMin) + "min "
                    + Long.toString(sessionS) + "s");
            if (completeS > 59) {
                completeMin++;
                completeS = 0;
            }
            if (completeMin > 59) {
                completeHour++;
                completeMin = 0;
            }
            complete_time.setText(Long.toString(completeHour) + "h "
                    + Long.toString(completeMin) + "min "
                    + Long.toString(completeS) + "s");
        }
    }));

    public void onStart(String aActivityName) {
        this.ACTIVITY_NAME = aActivityName;
        xmlParser = new XmlParser();
        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<HistoryObj, String>("id"));
        idCol.setPrefWidth(34);
        idCol.setResizable(false);
        TableColumn startCol = new TableColumn("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<HistoryObj, String>("dateStart"));
        startCol.setPrefWidth(115);
        startCol.setResizable(false);
        TableColumn finishCol = new TableColumn("Finish Date");
        finishCol.setCellValueFactory(new PropertyValueFactory<HistoryObj, String>("dateEnd"));
        finishCol.setPrefWidth(115);
        finishCol.setResizable(false);
        TableColumn totalCol = new TableColumn("Activity Time");
        totalCol.setCellValueFactory(new PropertyValueFactory<HistoryObj, String>("timeElapsed"));
        totalCol.setPrefWidth(100);
        totalCol.setResizable(false);
        TableColumn annotationsCol = new TableColumn("Annotations");
        annotationsCol.setCellValueFactory(new PropertyValueFactory<HistoryObj, String>("annotations"));
        annotationsCol.setPrefWidth(215);
        annotationsCol.setResizable(false);
        annotationsCol.setEditable(true);
        history_table.setItems(history);
        history_table.getColumns().addAll(idCol, startCol, finishCol, totalCol, annotationsCol);
        xmlParser.createActivity(ACTIVITY_NAME);
        List<String> completeTemp = xmlParser.getCompleteTime(ACTIVITY_NAME);
        completeHour = Long.parseLong(completeTemp.get(0));
        completeMin = Long.parseLong(completeTemp.get(1));
        completeS = Long.parseLong(completeTemp.get(2));
        complete_time.setText(completeTemp.get(0) + "h " + completeTemp.get(1) + "min " + completeTemp.get(2) + "s");
        category_name.setText(ACTIVITY_NAME);
        List<HistoryObj> pHistoryList = xmlParser.getAllHistory(ACTIVITY_NAME);
        for (int i = 0; i < pHistoryList.size(); i++) {
            history.add(pHistoryList.get(i));
        }
    }

    public void startTimer(ActionEvent actionEvent) {
        timeStamp = Calendar.getInstance().getTime();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void stopTimer(ActionEvent actionEvent) {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
            DateFormat pDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
            HistoryObj pNewHistoryItem = new HistoryObj(Integer.toString(history.size() + 1), pDateFormat.format(timeStamp), pDateFormat.format(Calendar.getInstance().getTime()), session_time.getText(), "Not functional yet");
            history.add(pNewHistoryItem);
            xmlParser.addHistoryItem(pNewHistoryItem, ACTIVITY_NAME);
            sessionMin = 0;
            sessionS = 0;
            sessionHour = 0;
            session_time.setText("0h 0min 0s");
            xmlParser.setCompleteTime(Long.toString(completeHour), Long.toString(completeMin), Long.toString(completeS), ACTIVITY_NAME);
            xmlParser.saveXml();
        }
    }

    public void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Me");
        alert.setHeaderText(null);
        alert.setContentText("justWork 1.0 Open Source\n" +
                "Official Contributors: \nRobert Sreberski and" +
                "Mikolaj Speichert");

        alert.showAndWait();
    }

    public void changeActivity(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("justWork Change Window");
        dialog.setHeaderText("Please, type activity you want to start to continue");
        dialog.setContentText("Activity");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (!result.get().equals("")) {
                ACTIVITY_NAME = result.get();
                history.clear();
                onStart(ACTIVITY_NAME);
            }
        }
    }
}
