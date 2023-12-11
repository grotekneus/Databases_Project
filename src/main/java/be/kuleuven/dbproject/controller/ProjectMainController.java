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


    public void initialize() {
        btnBeheerScherm1.setOnAction(e -> showBeheerScherm("scherm1"));
        btnBeheerScherm2.setOnAction(e -> showBeheerScherm("scherm2"));
        btnConfigAttaches.setOnAction(e -> showBeheerScherm("attaches"));
        Customers.setOnAction(e -> showBeheerScherm("customer"));
        ShopItems.setOnAction(e -> showBeheerScherm("shopitems"));
        Games.setOnAction(e -> showBeheerScherm("games"));
        Donations.setOnAction(e -> showBeheerScherm("donations"));
        Museums.setOnAction(e -> showBeheerScherm("museums"));

    }

    private void showBeheerScherm(String id) {
        var resourceName = id + "scherm.fxml";
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource(resourceName));
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
