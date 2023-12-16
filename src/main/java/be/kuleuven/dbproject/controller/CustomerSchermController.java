package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.*;
import be.kuleuven.dbproject.repositories.CustomerRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.DonationRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class CustomerSchermController implements Controller {
    public enum State {
        Loans,
        Customers,
        Purchases,
        Donations
    }

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnShowLoans;
    @FXML
    private Button btnShowDonations;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnShowPurchases;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView tblConfigs;
    @FXML
    private ChoiceBox<AdminSchermController.Tables> choiceBox;

    private EntityManager entityManager;
    private Customer selectedCustomer;
    private State state = State.Customers;
    private CustomerRepositoryJpaImpl customerRepo;
    private DonationRepositoryJpaImpl donationRepo;

    public CustomerSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.customerRepo = new CustomerRepositoryJpaImpl(entityManager);
        this.donationRepo = new DonationRepositoryJpaImpl(entityManager);

    }

    public void initialize() {
        this.state = State.Customers;
        btnShowLoans.setOnAction(e->showLoans());
        btnShowDonations.setOnAction(e -> showDonations());
        btnShowPurchases.setOnAction(e -> showPurchases());
        btnAdd.setOnAction(e -> {
            add();
        });
        btnEdit.setOnAction(e -> {
            edit();
        });
        btnShowLoans.setVisible(false);
        btnShowDonations.setVisible(false);
        btnShowPurchases.setVisible(false);
        btnClose.setOnAction(e ->{
            if(state != State.Customers){
                state = State.Customers;
                showCustomers();
            }
            else{
                var stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
            }
            selectedCustomer = null;
        });

        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.Customers) {
                selectedCustomer = (Customer) tblConfigs.getSelectionModel().getSelectedItem();
                btnShowLoans.setVisible(true);
                btnShowDonations.setVisible(true);
                btnShowPurchases.setVisible(true);
            }
        });
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        showCustomers();
    }




    private void showPurchases() {

        if (selectedCustomer != null) {
            this.state = State.Purchases;
            btnShowPurchases.setVisible(false);
            btnShowLoans.setVisible(false);
            btnShowDonations.setVisible(false);

            tblConfigs.getColumns().clear();
            tblConfigs.getItems().clear();
            TableColumn<Loan, String> dateColumn = new TableColumn<>("date");
            TableColumn<Loan, String> returneDateColumn = new TableColumn<>("return date");
            TableColumn<Loan, String> customerIDColumn = new TableColumn<>("CustomerID");
            TableColumn<Loan, String> gameIDColumn = new TableColumn<>("GameID");

            customerIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomer().getCustomerID())));
            returneDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReturned().toString()));
            dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate().toString()));
            gameIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGame().getGameID())));

            tblConfigs.getColumns().addAll(dateColumn, returneDateColumn, customerIDColumn, gameIDColumn);
            List<Purchase> purchases = customerRepo.getPurchases(selectedCustomer);
            for (Purchase purchase : purchases) {
                tblConfigs.getItems().add(purchase);
            }
        }
    }

    private void showLoans() {

        if (selectedCustomer != null) {
            this.state = State.Loans;
            btnShowPurchases.setVisible(false);
            btnShowLoans.setVisible(false);
            btnShowDonations.setVisible(false);
            tblConfigs.getColumns().clear();
            tblConfigs.getItems().clear();
            TableColumn<Loan, String> dateColumn = new TableColumn<>("date");
            TableColumn<Loan, String> returneDateColumn = new TableColumn<>("return date");
            TableColumn<Loan, String> customerIDColumn = new TableColumn<>("CustomerID");
            TableColumn<Loan, String> gameIDColumn = new TableColumn<>("GameID");

            customerIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomer().getCustomerID())));
            returneDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReturned().toString()));
            dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate().toString()));
            gameIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGame().getGameID())));

            tblConfigs.getColumns().addAll(dateColumn, returneDateColumn, customerIDColumn, gameIDColumn);
            List<Loan> loans = customerRepo.getLoans(selectedCustomer);
            for (Loan loan : loans) {
                tblConfigs.getItems().add(loan);
            }
        }
    }

    private void showDonations() {

        if (selectedCustomer != null) {
            this.state = State.Donations;
            btnShowPurchases.setVisible(false);
            btnShowLoans.setVisible(false);
            btnShowDonations.setVisible(false);
            tblConfigs.getColumns().clear();
            tblConfigs.getItems().clear();
            TableColumn<Donation, String> dateColumn = new TableColumn<>("date");
            TableColumn<Donation, String> returneDateColumn = new TableColumn<>("amount donated");
            TableColumn<Donation, String> customerIDColumn = new TableColumn<>("CustomerID");

            customerIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomer().getCustomerID())));
            returneDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getMoneyDonated())));
            dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate().toString()));

            tblConfigs.getColumns().addAll(dateColumn,returneDateColumn,customerIDColumn);
            List<Donation> donations = customerRepo.getDonations(selectedCustomer);
            for (Donation donation : donations) {
                tblConfigs.getItems().add(donation);
            }

        }

    }
    
    private void add() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            switch(state){
                case Customers:
                    controller = new addCustomDialogController(new String[]{"full name","adress","email"});
                    break;
                case Loans:
                    controller = new addCustomDialogController(new String[]{"gameID","Year","Month","Day"});
                    break;
                case Purchases:
                    controller = new addCustomDialogController(new String[]{"item type","item id"});
                    break;
                case Donations:
                    controller = new addCustomDialogController(new String[]{"Money Donated"});
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
                    for(String string : s){
                        if(string==""){
                            throw new IOException();
                        }
                    }
                    switch(state){
                        case Customers:
                            Customer customer = new Customer(s[0],s[1],s[2]);
                            customerRepo.addCustomer(customer);
                            showCustomers();
                            break;
                        case Loans:
                            /*Loan loan = new Loan(selectedCustomer, LocalDate.now(),LocalDate.of(Integer.valueOf(s[1])
                                                                              ,Integer.valueOf(s[2])
                                                                              ,Integer.valueOf(s[3])));
                            customerRepo.addLoan(loan);*/
                            showLoans();
                            break;
                        case Purchases:

                            break;
                        case Donations:
                            float money = Float.parseFloat(s[0]);
                            LocalDate date = LocalDate.now();//LocalDate.parse(s[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            //LocalDate date = LocalDate.parse(s[1]);
                            //int customerId= Integer.parseInt(s[1]);
                            //Customer customer = customerRepo.getCustomerById(customerId);
                            if(selectedCustomer==null){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Please enter existing customerID");
                                alert.showAndWait();
                                return;
                            }
                            else{
                                Donation donation = new Donation(money,date,selectedCustomer);
                                donationRepo.addDonation(donation);
                                showDonations();
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            showAlert();
            add();
        }
    }

    private void edit() {
        try{
            if(selectedCustomer != null){
                FXMLLoader fxmlLoader = new FXMLLoader();
                addCustomDialogController controller = null;
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
                switch(state){
                    case Customers:
                        controller = new addCustomDialogController(new String[]{"adress","email"},
                                new String[]{selectedCustomer.getFullName(),selectedCustomer.getAddress(),selectedCustomer.getEmail()});
                        break;
                    case Loans:
                        controller = new addCustomDialogController(new String[]{"gameID","Year","Month","Day"},
                                new String[]{selectedCustomer.getFullName(),selectedCustomer.getAddress(),selectedCustomer.getEmail()});
                        break;
                    case Purchases:
                        controller = new addCustomDialogController(new String[]{"item type","item id"},
                                new String[]{selectedCustomer.getFullName(),selectedCustomer.getAddress(),selectedCustomer.getEmail()});
                        break;
                    case Donations:
                        controller = new addCustomDialogController(new String[]{"Money Donated"},
                                new String[]{selectedCustomer.getFullName(),selectedCustomer.getAddress(),selectedCustomer.getEmail()});
                        break;
                }
                fxmlLoader.setController(controller);
                DialogPane pane = fxmlLoader.load();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().setContent(pane);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
                Optional<ButtonType> result = dialog.showAndWait();
                if(result.isPresent()){
                    if (result.get() == ButtonType.APPLY) {
                        String[] s = controller.getInput();
                        if (state == State.Customers){
                            try{
                                if(s[0] == ""){
                                    throw new NumberFormatException();
                                }
                                customerRepo.changeCustomerProperties(selectedCustomer,s[0],s[1]);
                            } catch(NumberFormatException e) {
                                showAlert();
                                add();
                            }
                            showCustomers();
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please enter the right thing in all fields");
        alert.showAndWait();
    }


    private void showCustomers(){
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<Customer, String> customerIDColumn = new TableColumn<>("Customer ID");
        TableColumn<Customer, String> fullNameColumn = new TableColumn<>("Full Name");
        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");

        customerIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomerID())));
        fullNameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFullName()));
        addressColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress()));
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEmail()));

        tblConfigs.getColumns().addAll(customerIDColumn,fullNameColumn,addressColumn,emailColumn);
        List<Customer> customers = customerRepo.getAllCustomers();
        tblConfigs.getItems().clear();
        for (Customer customer : customers) {
            tblConfigs.getItems().add(customer);
        }
    }
}
