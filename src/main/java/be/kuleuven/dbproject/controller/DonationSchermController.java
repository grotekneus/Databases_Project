package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.Customer;
import be.kuleuven.dbproject.domain.Donation;
import be.kuleuven.dbproject.domain.Museum;
import be.kuleuven.dbproject.repositories.CustomerRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.DonationRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.time.format.DateTimeFormatter;
public class DonationSchermController implements Controller {
    public enum State {
        Customers,
        Donations
    }

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnFilterByPrice;
    @FXML
    private Button btnFilterByName;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView tblConfigs;
    private EntityManager entityManager;
    private Donation selectedDonation;
    private State state = State.Donations;
    private DonationRepositoryJpaImpl donationRepo;
    private CustomerRepositoryJpaImpl customerRepo;

    public DonationSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.donationRepo = new DonationRepositoryJpaImpl(entityManager);
        this.customerRepo = new CustomerRepositoryJpaImpl(entityManager);
    }

    public void initialize() {
        this.state = State.Donations;
        btnAdd.setOnAction(e->{add();});
        btnEdit.setOnAction(e->{edit();});
        btnEdit.setVisible(false);

        btnFilterByName.setOnAction(e -> filterByName());
        btnFilterByPrice.setOnAction(e -> filterByPrice());

        btnClose.setOnAction(e ->{
            if(state != State.Donations){
                state = state.Donations;
                showDonations();
            }
            else{
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
        /*
        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.Donations) {
                btnSearchGame.setVisible(true);
                btnRevenue.setVisible(true);
                //btnShowPurchases.setVisible(true);
            }
        });
         */
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        showDonations();
    }

    private void showDonations(){
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<Donation, String > customurColumn = new TableColumn<>("Costumer");
        TableColumn<Donation, LocalDate> dateColumn = new TableColumn<>("Date");
        TableColumn<Donation, Float> MoneyDonatedColumn = new TableColumn<>("Money Donated");
        customurColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomer().getCustomerID())));

        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate()));
        MoneyDonatedColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getMoneyDonated()));

        tblConfigs.getColumns().addAll(customurColumn, MoneyDonatedColumn, dateColumn);
        List<Donation> donations = donationRepo.getDonations();
        tblConfigs.getItems().clear();
        for (Donation donation : donations) {
            tblConfigs.getItems().add(donation);
        }
    }

    private void add(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            switch(state){
                case Donations:
                    controller = new addCustomDialogController(new String[]{"amount donated","customer ID"});
                    break;
                /*case Loans:
                    controller = new addCustomDialogController(new String[]{"gameID","Year","Month","Day"});
                    break;
                case Purchases:
                    controller = new addCustomDialogController(new String[]{"item type","item id"});
                    break;
                case Donations:
                    controller = new addCustomDialogController(new String[]{"Money Donated"});
                    break;

                 */
            }
            fxmlLoader.setController(controller);
            DialogPane pane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(pane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent()){
                if(result.get() == ButtonType.APPLY){
                    String[] s = controller.getInput();
                    //int visitors = Integer.parseInt(s[1]);
                    //System.out.println("Converted to int: " + visitors);


                    //float revenue = Float.parseFloat(s[2]);
                    //System.out.println("Converted to float: " + revenue);


                    switch(state){
                        case Donations:
                            float money;
                            try {
                                money = Float.parseFloat(s[0]);
                            } catch (NumberFormatException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Please enter a number in money");
                                alert.showAndWait();
                                return;
                            }
                            LocalDate date = LocalDate.now();  // or parse the date if needed

                            int customerId;
                            try {
                                customerId = Integer.parseInt(s[1]);
                            } catch (NumberFormatException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Please enter a number in customerID");
                                alert.showAndWait();
                                return;
                            }

                            try {
                                Customer customer = customerRepo.getCustomerById(customerId);
                                if (customer == null) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Please enter existing customerID");
                                    alert.showAndWait();
                                    //showAlert("Customer with ID " + customerId + " does not exist.");
                                    return;
                                }

                                Donation donation = new Donation(money, date, customer);
                                donationRepo.addDonation(donation);
                                showDonations();
                            } catch (NoResultException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Please enter existing customerID");
                                alert.showAndWait();
                                //showAlert("Customer with ID " + customerId + " does not exist.");
                            }


                            break;
                        
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void edit(){

    }

    private void filterByName(){

    }

    private void filterByPrice(){

    }


}
