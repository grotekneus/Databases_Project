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
    private Button btnFilterID;
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

        btnFilterID.setOnAction(e -> filterByID());
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
                    switch(state){
                        case Donations:
                            float money;
                            try {
                                money = Float.parseFloat(s[0]);
                            } catch (NumberFormatException e) {
                                throwError("Please enter a number in money");
                                return;
                            }
                            LocalDate date = LocalDate.now();  // or parse the date if needed

                            int customerId;
                            try {
                                customerId = Integer.parseInt(s[1]);
                            } catch (NumberFormatException e) {
                                throwError("Please enter a number in customerID");
                                return;
                            }

                            try {
                                Customer customer = customerRepo.getCustomerById(customerId);
                                if (customer == null) {
                                   throwError("Please enter existing customerID");
                                    return;
                                }

                                Donation donation = new Donation(money, date, customer);
                                donationRepo.addDonation(donation);
                                showDonations();
                            } catch (NoResultException e) {
                                throwError("Please enter existing customerID");
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

    private void filterByID() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Filter Donations by Customer ID");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Customer ID:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(customerIdString -> {
                try {
                    int customerId;
                    try {
                        customerId = Integer.parseInt(customerIdString);
                    } catch (NumberFormatException e) {
                        throwError("Please enter a number in customerID");
                        return;
                    }

                    try {
                        Customer customer = customerRepo.getCustomerById(customerId);
                        if (customer == null) {
                            throwError("Please enter an existing customerID");
                            return;
                        }

                        List<Donation> customerDonations = donationRepo.getDonationsByCustomer(customer);

                        // Display the filtered donations in the table
                        tblConfigs.getItems().clear();
                        tblConfigs.getItems().addAll(customerDonations);
                    } catch (NoResultException e) {
                        throwError("Please enter an existing customerID");
                    }
                } catch (Exception e) {
                    // Handle exceptions as needed
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }
    }


    private void filterByPrice(){
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Filter Donations by Price");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Price:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(priceString -> {
                try {
                    float price;
                    try {
                        price = Float.parseFloat(priceString);
                    } catch (NumberFormatException e) {
                        throwError("Please enter a valid number for the price");
                        return;
                    }

                    List<Donation> donationsAbovePrice = donationRepo.getDonationsAbovePrice(price);
                    if (donationsAbovePrice.isEmpty()) {
                        throwError("No donations found above the entered price");
                    } else {
                        tblConfigs.getItems().clear();
                        tblConfigs.getItems().addAll(donationsAbovePrice);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void throwError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }


}
