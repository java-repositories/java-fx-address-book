package ru.javabegin.training.fastjava2.javafx.start;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.javabegin.training.fastjava2.javafx.controllers.MainController;
import ru.javabegin.training.fastjava2.javafx.objects.Lang;
import ru.javabegin.training.fastjava2.javafx.preloader.TestPreloader;
import ru.javabegin.training.fastjava2.javafx.utils.LocaleManager;

import java.io.IOException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Main extends Application implements Observer {


    private static final String FXML_MAIN = "../fxml/main.fxml";
    public static final String BUNDLES_FOLDER = "ru.javabegin.training.fastjava2.javafx.bundles.Locale";

    private Stage primaryStage;

    private Parent fxmlMain;

    private MainController mainController;
    private FXMLLoader fxmlLoader;

    private VBox currentRoot;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        createGUI(LocaleManager.RU_LOCALE);
    }

    @Override
    public void init() throws Exception {
        // имитация загрузки
        for (int i = 0; i < 100; i++) {
            Thread.sleep(15);
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(i));
        }
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, TestPreloader.class, args);
    }

    @Override
    public void update(Observable o, Object arg) {
        Lang lang = (Lang) arg;
        VBox newNode = loadFXML(lang.getLocale()); // получить новое дерево компонетов с нужной локалью
        currentRoot.getChildren().setAll(newNode.getChildren());// заменить старые дочерник компонента на новые - с другой локалью
    }



    // загружает дерево компонентов и возвращает в виде VBox (корневой элемент в FXML)
    private VBox loadFXML(Locale locale) {
        fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource(FXML_MAIN));
        fxmlLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));

        VBox node = null;

        try {
            node = (VBox) fxmlLoader.load();

            mainController = fxmlLoader.getController();
            mainController.addObserver(this);
            primaryStage.setTitle(fxmlLoader.getResources().getString("address_book"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return node;
    }

    private void createGUI(Locale locale) {
        currentRoot = loadFXML(locale);
        Scene scene = new Scene(currentRoot, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }


}
