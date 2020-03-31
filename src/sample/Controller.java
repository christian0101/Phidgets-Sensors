package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Label welcomeLabel;

    @FXML
    ListView listView;

    @FXML
    Label dataLabel;

    public void initialize(URL arg0, ResourceBundle arg1) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        welcomeLabel.setText("Welcome, today is: " + dtf.format(now));

    }

    @FXML
    public void updateData(String newVal) {
        Platform.runLater(() ->
        {
            dataLabel.setText(newVal);
        });
    }
}
