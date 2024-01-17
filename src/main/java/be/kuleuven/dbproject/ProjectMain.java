package be.kuleuven.dbproject;

import be.kuleuven.dbproject.controller.LoginController;
import be.kuleuven.dbproject.controller.ProjectMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.Console;
import java.io.File;

public class ProjectMain extends Application {

    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }
    private static EntityManager entityManager;

    @Override
    public void start(Stage stage) throws Exception {
        rootStage = stage;
        LoginController loginController = new LoginController(entityManager);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("loginscherm.fxml"));
        loader.setController(loginController);
        var root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("DAdministratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        String dbFilePath = "VGHF.db";
        System.out.println("Bootstrapping JPA/Hibernate...");
        var sessionFactory = Persistence.createEntityManagerFactory("be.kuleuven.dbproject.domain");
        entityManager = sessionFactory.createEntityManager();
        System.out.println("Bootstrapping Repository...");
        entityManager.clear();
        entityManager.close();
        entityManager = sessionFactory.createEntityManager();
        launch();
    }
}
