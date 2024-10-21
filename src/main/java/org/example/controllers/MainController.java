package org.example.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.fxml.EditView;
import org.example.service.AddressBook;
import org.example.objects.Lang;
import org.example.entity.Person;
import org.example.utils.DialogManager;
import org.example.utils.LocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

@Controller
public class MainController extends Observable implements Initializable {

    @Autowired
    private AddressBook addressBook;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private TableView tableAddressBook;

    @FXML
    private TableColumn<Person, String> columnFIO;

    @FXML
    private TableColumn<Person, String> columnPhone;

    @FXML
    private Label labelCount;

    @FXML
    private ComboBox comboLocales;

    @Autowired
    private EditView editView;

    @Autowired
    private EditDialogController editDialogController;

    private Stage editDialogStage;

    private ResourceBundle resourceBundle;


    private static final String RU_CODE = "ru";
    private static final String EN_CODE = "en";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        columnFIO.setCellValueFactory(new PropertyValueFactory<Person, String>("fio"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
        fillData();
        initListeners();
    }

    private void fillData() {
        fillTable();
        fillLangComboBox();
        updateCountLabel();
    }

    private void fillTable() {
        tableAddressBook.setItems(addressBook.findAll());
    }

    private void fillLangComboBox() {
        Lang langRU = new Lang(0, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
        Lang langEN = new Lang(1, EN_CODE, resourceBundle.getString("en"), LocaleManager.EN_LOCALE);

        comboLocales.getItems().add(langRU);
        comboLocales.getItems().add(langEN);

        if (LocaleManager.getCurrentLang() == null) {
            LocaleManager.setCurrentLang(langRU);
            comboLocales.getSelectionModel().select(0);
        } else {
            comboLocales.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
        }

    }

    private void initListeners() {

        // слушает изменения в коллекции для обновления надписи "Кол-во записей"
        addressBook.findAll().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                updateCountLabel();
            }
        });


        // слушает двойное нажатие для редактирования записи
        tableAddressBook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
//                    editDialogController.setPerson((Person) tableAddressBook.getSelectionModel().getSelectedItem());
//                    showDialog();
                    btnEdit.fire();
                }
            }
        });

        // слушает изменение языка
        comboLocales.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Lang selectedLang = (Lang) comboLocales.getSelectionModel().getSelectedItem();
                LocaleManager.setCurrentLang(selectedLang);

                // уведомить всех слушателей, что произошла смена языка
                setChanged();
                notifyObservers(selectedLang);
            }
        });
    }

    private void updateCountLabel() {
        labelCount.setText(resourceBundle.getString("count") + ": " + addressBook.findAll().size());
    }

    public void actionButtonPressed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        // если нажата не кнопка - выходим из метода
        if (!(source instanceof Button)) {
            return;
        }

        Person selectedPerson = (Person) tableAddressBook.getSelectionModel().getSelectedItem();


        Button clickedButton = (Button) source;

        boolean research = false;

        switch (clickedButton.getId()) {
            case "btnAdd":
                editDialogController.setPerson(new Person());
                showDialog();

                if (editDialogController.isSaveClicked()) {
                    addressBook.add(editDialogController.getPerson());
                    research = true;
                }


                break;

            case "btnEdit":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                editDialogController.setPerson(selectedPerson);
                showDialog();

                if (editDialogController.isSaveClicked()) {
                    // коллекция в addressBookImpl и так обновляется, т.к. мы ее редактируем в диалоговом окне и сохраняем при нажатии на ОК
                    addressBook.update(selectedPerson);
                    research = true;
                }

                break;

            case "btnDelete":
                if (!personIsSelected(selectedPerson) || !(confirmDelete())) {
                    return;
                }

                research = true;
                addressBook.delete(selectedPerson);
                break;
        }


        if (research) {
            actionSearch(actionEvent);
        }

    }

    private boolean confirmDelete() {
        if (DialogManager.showConfirmDialog(resourceBundle.getString("confirm"), resourceBundle.getString("confirm_delete")).get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }

    }

    private boolean personIsSelected(Person selectedPerson) {
        if (selectedPerson == null) {
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("select_person"));
            return false;
        }
        return true;
    }

    private void showDialog() {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle(resourceBundle.getString("edit"));
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(editView.getView(LocaleManager.getCurrentLang().getLocale())));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(comboLocales.getParent().getScene().getWindow());
        }
        editDialogStage.showAndWait(); // для ожидания закрытия окна
    }

    public void actionSearch(ActionEvent actionEvent) {
        if (txtSearch.getText().trim().isEmpty()) {
            addressBook.findAll();
        } else {
            addressBook.find(txtSearch.getText());
        }
    }
}
