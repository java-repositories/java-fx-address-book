package org.example.start;
//
//import com.sun.javafx.application.LauncherImpl;
//import javafx.application.Application;
//import javafx.application.Preloader;
//import javafx.stage.Stage;
//import org.example.preloader.TestPreloader;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//
//import java.util.Arrays;
//
//public abstract class JavaFxSpringIntegrator extends Application {
//
//    private static String[] savedArgs;// аргументы при старте (если они есть)
//
//    // spring контекст для приложения - его нужно связать с javafx контекстом
//    private ConfigurableApplicationContext applicationContext;
//
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        applicationContext = SpringApplication.run(getClass(), savedArgs);
//
//        // главный момент - "присоединяем" экземпляр Application (который стартует javafx приложение) к контексту Spring
//        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
//
////		printBeans();
//
//        // уведомить прелоадер, что загрузка прошла полностью (чтобы скрыть окно инициализации)
//        LauncherImpl.notifyPreloader(this,  new Preloader.ProgressNotification(100));
//    }
//
//    // печать всех spring бинов
//    private void printBeans() {
//        String[] beanNames = applicationContext.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
//    }
//
//
//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        applicationContext.close();
//    }
//
//    // старт javafx приложения
//    protected static void launchSpringJavaFXApp(Class<? extends JavaFxSpringIntegrator> appClass, String[] args) {
//        JavaFxSpringIntegrator.savedArgs = args;
//        // стартуем javafx приложение с прелоадером (окном инициализации)
//        LauncherImpl.launchApplication(appClass, TestPreloader.class, args);
//    }
//}