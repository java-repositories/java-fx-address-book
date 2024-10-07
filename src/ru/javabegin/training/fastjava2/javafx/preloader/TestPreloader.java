package ru.javabegin.training.fastjava2.javafx.preloader;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestPreloader extends Preloader {

    private Stage preloaderStage;
    private Scene scene;

    private ProgressBar bar;


    public TestPreloader() {
    }

    @Override
    public void init() throws Exception {

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification) {
            bar.setProgress(((ProgressNotification) info).getProgress() / 100);
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {

        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                break;
            case BEFORE_INIT:
                break;
            case BEFORE_START:
                preloaderStage.hide();
                break;
        }
    }

}
