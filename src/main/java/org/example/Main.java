package org.example;

//import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.fxml.MainView;
import org.example.repository.PersonRepository;
import org.example.service.AddressBook;
import org.example.utils.LocaleManager;
import org.example.controllers.MainController;
import org.example.objects.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

@SpringBootApplication
public class Main extends Application /*implements Observer */ {
    private static String[] savedArgs;

    //    private static final String FXML_MAIN = "org/example/fxml/main.fxml";
    @Autowired
    private MainView mainView;

    public static final String BUNDLES_FOLDER = "org.example.bundles.Locale";

    private Stage primaryStage;

    private Parent fxmlMain;

    private MainController mainController;
    private FXMLLoader fxmlLoader;

    private VBox currentRoot;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        applicationContext = SpringApplication.run(getClass(), savedArgs);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

        System.out.println("bean: " + applicationContext.getBean(PersonRepository.class));
        System.out.println("bean: " + applicationContext.getBean(MainController.class));
        System.out.println("bean: " + applicationContext.getBean(AddressBook.class));

//        this.primaryStage = primaryStage;
//        createGUI(LocaleManager.RU_LOCALE);

        try {
            this.primaryStage = primaryStage;
            primaryStage.setScene(new Scene(mainView.getView(LocaleManager.RU_LOCALE)));
            primaryStage.setMinHeight(700);
            primaryStage.setMinWidth(600);
            primaryStage.centerOnScreen();
            primaryStage.setTitle(mainView.getResourceBundle().getString("address_book"));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e);
            stop();
        }

    }

//    @Override
//    public void init() throws Exception {
//        // имитация загрузки
//        for (int i = 0; i < 100; i++) {
//            Thread.sleep(15);
//            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(i));
//        }
//    }

    public static void main(String[] args) {
//        System.setProperty("javafx.preloader", TestPreloader.class.getCanonicalName());
        savedArgs = args;
        launch(savedArgs);

//        LauncherImpl.launchApplication(Main.class, TestPreloader.class, args);
    }

//    @Override
//    public void update(Observable o, Object arg) {
//        Lang lang = (Lang) arg;
//        VBox newNode = loadFXML(lang.getLocale()); // получить новое дерево компонетов с нужной локалью
//        currentRoot.getChildren().setAll(newNode.getChildren());// заменить старые дочерник компонента на новые - с другой локалью
//    }


    // загружает дерево компонентов и возвращает в виде VBox (корневой элемент в FXML)
//    private VBox loadFXML(Locale locale) {
//        VBox node = null;
//
//        try {
//            fxmlLoader = new FXMLLoader();
//
//            fxmlLoader.setLocation(Main.class.getClassLoader().getResource(FXML_MAIN));
//            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLES_FOLDER, locale);
//            fxmlLoader.setResources(bundle);
//
//            node = fxmlLoader.load();
//
//            mainController = fxmlLoader.getController();
//            mainController.addObserver(this);
//            primaryStage.setTitle(fxmlLoader.getResources().getString("address_book"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return node;
//    }

//    private void createGUI(Locale locale) {
//        currentRoot = loadFXML(locale);
//        Scene scene = new Scene(currentRoot, 300, 275);
//        primaryStage.setScene(scene);
//        primaryStage.setMinHeight(700);
//        primaryStage.setMinWidth(600);
//        primaryStage.show();
//    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
}
