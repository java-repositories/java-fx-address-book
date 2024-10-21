package org.example;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.fxml.MainView;
import org.example.utils.LocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Preloader {

    private static String[] savedArgs;

    @Autowired
    private MainView mainView;

    private Stage preloaderStage;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        preloaderStage = new Stage();
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.setScene(scene);
        preloaderStage.show();

        Thread launcherThread = new Thread(() -> {
            applicationContext = SpringApplication.run(getClass(), savedArgs);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

            Platform.runLater(() -> {
                primaryStage.setScene(new Scene(mainView.getView(LocaleManager.RU_LOCALE)));
                primaryStage.setMinHeight(700);
                primaryStage.setMinWidth(600);
                primaryStage.centerOnScreen();
                primaryStage.setTitle(mainView.getResourceBundle().getString("address_book"));
                primaryStage.show();
                preloaderStage.close();
            });
        });
        launcherThread.setName("JavaFX-Launcher");
        launcherThread.start();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    public static void main(String[] args) {
        savedArgs = args;
        launch(Main.class, args);
    }

    //----------------------------Preloader----------------------

    private Scene scene;
    private ProgressIndicator bar;

    @Override
    public void init() {
        Platform.runLater(() -> {
            Label title = new Label("Загрузка...");
            title.setTextAlignment(TextAlignment.CENTER);
            bar = new ProgressBar();
            bar.setMinWidth(300);

            VBox root = new VBox(title, bar);
            root.setAlignment(Pos.CENTER);

            scene = new Scene(root, 350, 100);
        });
    }
}
