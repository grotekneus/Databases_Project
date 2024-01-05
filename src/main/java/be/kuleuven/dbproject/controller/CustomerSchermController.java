package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.*;
import be.kuleuven.dbproject.repositories.CustomerRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.DonationRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.GameRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.ShopItemRepositoryJpaImpl;
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
    private ShopItemRepositoryJpaImpl shopItemRepo;

    public CustomerSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.customerRepo = new CustomerRepositoryJpaImpl(entityManager);
        this.donationRepo = new DonationRepositoryJpaImpl(entityManager);
        this.shopItemRepo = new ShopItemRepositoryJpaImpl(entityManager);

    }

    public void initialize() {
        this.state = State.Customers;
        btnShowLoans.setOnAction(e->showLoans());
        btnShowDonations.setOnAction(e -> showDonations());
        btnShowPurchases.setOnAction(e -> {
            btnEdit.setVisible(false);
            showPurchases();
        });
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
                btnEdit.setVisible(true);
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
            TableColumn<Purchase, String> customerIDColumn = new TableColumn<>("CustomerID");
            TableColumn<Purchase, String> itemTypeColumn = new TableColumn<>("ItemType");
            TableColumn<Purchase, String> itemIDColumn = new TableColumn<>("itemID");

            customerIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomer().getCustomerID())));
            itemTypeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemType().toString()));
            itemIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getItemID())));

            tblConfigs.getColumns().addAll(customerIDColumn, itemTypeColumn, itemIDColumn);
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
            btnEdit.setVisible(false);
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
            switch (state) {
                case Customers:
                    controller = new addCustomDialogController(new String[]{"full name", "adress", "email"});
                    break;
                case Loans:
                    var gameRepo = new GameRepositoryJpaImpl(entityManager);
                    controller = new addCustomDialogController(true, new String[]{"game", "Year", "Month", "Day"}, new String[][]{gameRepo.getAllGameNames()});
                    break;
                case Purchases:
                    ItemType[] itemTypes = ItemType.values();
                    String[] itemTypeNames = new String[itemTypes.length];
                    for (int i = 0; i < itemTypes.length; i++) {
                        itemTypeNames[i] = itemTypes[i].name();
                    }
                    controller = new addCustomDialogController(true, new String[]{"item type", "item id"}, new String[][]{itemTypeNames});
                    break;
                case Donations:
                    controller = new addCustomDialogController(new String[]{"Money Donated"});
                    break;
            }
            fxmlLoader.setController(controller);
            DialogPane pane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(pane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.APPLY) {
                    String[] s = controller.getInput();
                    for (String string : s) {
                        if (string == "") {
                            throw new IOException();
                        }
                    }
                    switch (state) {
                        case Customers:
                            Customer customer = new Customer(s[0], s[1], s[2]);
                            customerRepo.addCustomer(customer);
                            showCustomers();
                            break;
                        case Loans:
                            var gameRepo = new GameRepositoryJpaImpl(entityManager);
                            Loan loan = new Loan(selectedCustomer, LocalDate.now(), LocalDate.of(Integer.valueOf(s[1])
                                    , Integer.valueOf(s[2])
                                    , Integer.valueOf(s[3]))
                                    , gameRepo.findGameByName(s[0]));
                            customerRepo.addLoan(loan);
                            showLoans();
                            break;
                        case Purchases:
                            ItemType itemType = ItemType.valueOf(s[0]);

                            try {
                                int itemId = Integer.parseInt(s[1]); // Assuming s[3] is the shop item ID
                                if (shopItemRepo.getShopItemById(itemId)==null) {
                                    showAlert();
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                showAlert();
                                return;
                            }



                            Purchase purchase = new Purchase(selectedCustomer, itemType, Integer.parseInt(s[1]));
                            customerRepo.addPurchase(purchase);
                            showPurchases();
                            break;
                        case Donations:
                            float money = Float.parseFloat(s[0]);
                            LocalDate date = LocalDate.now();//LocalDate.parse(s[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            //LocalDate date = LocalDate.parse(s[1]);
                            //int customerId= Integer.parseInt(s[1]);
                            //Customer customer = customerRepo.getCustomerById(customerId);
                            if (selectedCustomer == null) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Please enter existing customerID");
                                alert.showAndWait();
                                return;
                            } else {
                                Donation donation = new Donation(money, date, selectedCustomer);
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
                var selectedItem = tblConfigs.getSelectionModel().getSelectedItem();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
                switch(state){
                    case Customers:
                        controller = new addCustomDialogController(new String[]{"adress","email"},
                                new String[]{selectedCustomer.getFullName(),selectedCustomer.getAddress(),selectedCustomer.getEmail()});
                        break;
                    case Loans:
                        Loan selectedLoan = (Loan) selectedItem;
                        controller = new addCustomDialogController(new String[]{"gameID","Year","Month","Day"},
                                new String[]{String.valueOf(selectedLoan.getGame().getGameID())
                                        ,String.valueOf(selectedLoan.getDate().getYear())
                                        ,String.valueOf(selectedLoan.getDate().getMonth())
                                        ,String.valueOf(selectedLoan.getDate().getDayOfMonth())});
                        break;
                    case Donations:
                        Donation selectedDonation = (Donation) selectedItem;
                        controller = new addCustomDialogController(new String[]{"Money Donated"},
                                new String[]{String.valueOf(selectedDonation.getMoneyDonated())});
                        break;
                    default:
                        return;
                }
                fxmlLoader.setController(controller);
                DialogPane pane = fxmlLoader.load();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().setContent(pane);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
                Optional<ButtonType> result = dialog.showAndWait();
                if(result.isPresent()){
                    try{
                        if (result.get() == ButtonType.APPLY) {
                            String[] s = controller.getInput();
                            for (String str : s) {
                                if (str == "") {
                                    throw new NumberFormatException();
                                }
                            }
                            if (state == State.Customers) {
                                customerRepo.changeCustomerProperties(selectedCustomer, s[0], s[1]);
                                showCustomers();
                            } else if (state == State.Donations) {
                                var donationRepo = new DonationRepositoryJpaImpl(entityManager);
                                donationRepo.changeDonation((Donation) selectedItem,Integer.parseInt(s[0]));
                                showDonations();
                            } else if (state == State.Loans) {
                                LocalDate date = LocalDate.of(Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]));
                                var gameRepo = new GameRepositoryJpaImpl(entityManager);
                                Game game = gameRepo.findGameByName(s[0]);
                                customerRepo.changeLoan((Loan) selectedItem,game,date);
                                showLoans();
                            }
                        }
                    } catch(NumberFormatException e) {
                        showAlert();
                        add();
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
