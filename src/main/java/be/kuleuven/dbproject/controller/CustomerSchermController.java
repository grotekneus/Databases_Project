package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.EntityManagerClass;
import be.kuleuven.dbproject.domain.Customer;
import be.kuleuven.dbproject.repositories.CustomerRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class CustomerSchermController implements Controller {

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
    private ChoiceBox<BeheerScherm2Controller.Tables> choiceBox;
    @FXML
    private Button btnAddToCustomer;

    private EntityManager entityManager;

    private CustomerRepositoryJpaImpl customerRepo;
    public CustomerSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.customerRepo = new CustomerRepositoryJpaImpl(entityManager);
    }

    public void initialize() {
        initTable();
        btnShowLoans.setOnAction(e->showLoans());
        btnShowDonations.setOnAction(e -> showDonations());
        btnAdd.setOnAction(e -> {
            addNewCustomer();
        });
        btnAddToCustomer.setOnAction(e -> {
            addToCustomerDetails();
        });
        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnAddToCustomer.setVisible(true);
            } else {
                btnAddToCustomer.setVisible(false);
            }
        });
        //updateTable();
    }

    private void showLoans() {
    }

    private void showDonations() {
    }


    private void initTable() {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();


        TableColumn<Customer, String> customerIDColumn = new TableColumn<>("Customer ID");
        TableColumn<Customer, String> fullNameColumn = new TableColumn<>("Full Name");
        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
        
        customerIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomerID())));
        fullNameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFullName()));
        addressColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress()));
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEmail()));

        customerIDColumn.setVisible(true);
        fullNameColumn.setVisible(true);
        addressColumn.setVisible(true);
        emailColumn.setVisible(true);
        tblConfigs.getColumns().addAll(customerIDColumn,fullNameColumn,addressColumn,emailColumn);




    }
    private void addNewCustomer() {
        try{
            System.out.println("abracadabra");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomerdialog.fxml"));
            addCustomerDialogController controller = new addCustomerDialogController();
            fxmlLoader.setController(controller);

            DialogPane addCustomerDialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(addCustomerDialogPane);


            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent()){
                if(result.get() == ButtonType.APPLY){

                    Customer c = controller.getCustomer();
                    customerRepo.addCustomer(c);
                    updateTable();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToCustomerDetails(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add window");
        alert.setHeaderText(null);
        ButtonType buttonNewLoan = new ButtonType("Add new Loan to customer");
        ButtonType buttonNewDonation = new ButtonType("Add new donation to customer");
        ButtonType buttonNewPurchase = new ButtonType("Add new purchase to customer");
        alert.getButtonTypes().setAll(buttonNewLoan, buttonNewDonation, buttonNewPurchase);

        alert.showAndWait();
    }

    private void updateTable(){
        List<Customer> customers = customerRepo.getCustomers();
        for (Customer customer : customers) {
            tblConfigs.getItems().add(customer);
        }

    }
}
