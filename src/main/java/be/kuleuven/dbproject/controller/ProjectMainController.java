package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.domain.Loan;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;

public class ProjectMainController {



    @FXML
    private Button btnAdmin;
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

    private final boolean isAdmin;
    private EntityManager entityManager;
    public ProjectMainController(EntityManager em,boolean isAdmin) {
        this.entityManager= em;
        this.isAdmin = isAdmin;
    }

    public void initialize() {
        if(isAdmin){
            btnAdmin.setOnAction(e -> showBeheerScherm("admin"));
        }
        else{
            btnAdmin.setVisible(false);
        }

        Customers.setOnAction(e -> showBeheerScherm("customer"));
        ShopItems.setOnAction(e -> showBeheerScherm("shopitem"));
        Games.setOnAction(e -> showBeheerScherm("game"));
        Donations.setOnAction(e -> showBeheerScherm("donation"));
        Museums.setOnAction(e -> showBeheerScherm("museum"));

    }

    private void showBeheerScherm(String id) {
        var resourceName = id.toLowerCase() + "scherm.fxml";
        try {
            var stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
            switch(id) {
                case "customer":
                    CustomerSchermController customerController = new CustomerSchermController(entityManager);
                    loader.setController(customerController);
                    break;
                case "game":
                    GameSchermController gameController = new GameSchermController(entityManager);
                    loader.setController(gameController);
                    break;
                case "admin":
                    AdminSchermController adminController = new AdminSchermController(entityManager);
                    loader.setController(adminController);
                    break;
                case "museum":
                    MuseumSchermController museumController = new MuseumSchermController(entityManager);
                    loader.setController(museumController);
                    break;
                case "donation":
                    DonationSchermController donationController = new DonationSchermController(entityManager);
                    loader.setController(donationController);
                    break;
                case "shopitem":
                    ShopItemSchermController shopItemController = new ShopItemSchermController(entityManager);
                    loader.setController(shopItemController);
                    break;
                default:
                    break;
            }
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
}
