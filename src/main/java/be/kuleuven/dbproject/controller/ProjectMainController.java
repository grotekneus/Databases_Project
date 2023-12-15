package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.EntityManagerClass;
import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.domain.Loan;
import be.kuleuven.dbproject.repositories.CustomerRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.GameRepositoryJpaImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

public class ProjectMainController {

    @FXML
    private Button btnBeheerScherm1;
    @FXML
    private Button btnBeheerScherm2;
    @FXML
    private Button btnConfigAttaches;
    @FXML
    private Button Museums;
    @FXML
    private Button Customers;
    @FXML
    private Button Donations;
    @FXML
    private Button Games;
    @FXML
    private Button ShopItems;

    private EntityManager entityManager;
    public ProjectMainController(EntityManager em) {
        this.entityManager= em;
    }

    public void initialize() {
        btnBeheerScherm1.setOnAction(e -> showBeheerScherm("scherm1"));
        btnBeheerScherm2.setOnAction(e -> showBeheerScherm("scherm2"));
        btnConfigAttaches.setOnAction(e -> showBeheerScherm("attaches"));
        Customers.setOnAction(e -> showBeheerScherm("customer"));
        ShopItems.setOnAction(e -> showBeheerScherm("shopitem"));
        Games.setOnAction(e -> showBeheerScherm("game"));
        Donations.setOnAction(e -> showBeheerScherm("donation"));
        Museums.setOnAction(e -> showBeheerScherm("museum"));

    }

    private void showBeheerScherm(String id) {
        var resourceName = id.toLowerCase() + "scherm.fxml";;
        Controller controller;
        switch(id){
            case "customer":
                controller = new CustomerSchermController(entityManager);
                break;
            case "game":
                controller = new GameSchermController();
                break;
            case "museum":
                controller = new MuseumSchermController(entityManager);
                break;
            case "donation":
                controller = new DonationSchermController(entityManager);
                break;
            case "shopitem":
                controller = new ShopItemSchermController(entityManager);
                break;
            default:
                controller = new CustomerSchermController(entityManager);
                break;
        }
        try {
            var stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            loader.setController(controller);
            var root = (AnchorPane) loader.load();
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }

    private void showVoegToeDialog(){
        Dialog<Loan> dialog = new Dialog<>();

    }
}
