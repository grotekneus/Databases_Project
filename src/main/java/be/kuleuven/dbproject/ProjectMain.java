package be.kuleuven.dbproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage stage) throws Exception {
        rootStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

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
        var entityManager = sessionFactory.createEntityManager();

        System.out.println("Bootstrapping Repository...");


        entityManager.clear();
        entityManager.close();
        entityManager = sessionFactory.createEntityManager();
        launch();
    }
}
