package be.kuleuven.dbproject;

import be.kuleuven.dbproject.controller.ProjectMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;

/**
 * DB Taak 2022-2023: De Vrolijke Zweters
 * Zie https://kuleuven-diepenbeek.github.io/db-course/extra/project/ voor opgave details
 *
 * Deze code is slechts een quick-start om je op weg te helpen met de integratie van JavaFX tabellen en data!
 * Zie README.md voor meer informatie.
 */
public class ProjectMain extends Application {

    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }
    private static EntityManager entityManager;

    @Override
    public void start(Stage stage) throws Exception {
        rootStage = stage;
        ProjectMainController mainController = new ProjectMainController(entityManager);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        loader.setController(mainController);
        var root = (AnchorPane) loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("DAdministratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        String dbFilePath = "examples/java/project-template/VGHF.db";

        // Delete the previous database file if it exists
        File dbFile = new File(dbFilePath);
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Previous database file deleted successfully.");
            } else {
                System.err.println("Failed to delete the previous database file.");
            }
        }
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
