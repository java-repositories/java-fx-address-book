package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.entity.Person;
import org.example.utils.DialogManager;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class EditDialogController implements Initializable {

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtFIO;

    @FXML
    private TextField txtPhone;

    @Getter
    private Person person;

    private ResourceBundle resourceBundle;

    @Getter
    private boolean saveClicked = false;// для определения нажатой кнопки

    public void setPerson(Person person) {
        if (person == null) {
            return;
        }
        this.person = person;
        saveClicked = false;
    }

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        if (!checkValues()) {
            return;
        }
        person.setFio(txtFIO.getText());
        person.setPhone(txtPhone.getText());
        saveClicked = true;
        actionClose(actionEvent);
    }

    private boolean checkValues() {
        if (txtFIO.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("fill_field"));
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        txtFIO.setText(person.getFio());
        txtPhone.setText(person.getPhone());
    }
}
