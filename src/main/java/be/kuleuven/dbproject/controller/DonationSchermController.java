package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.Donation;
import be.kuleuven.dbproject.domain.Museum;
import be.kuleuven.dbproject.repositories.DonationRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.persistence.EntityManager;
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
    @FXML
    private ChoiceBox<BeheerScherm2Controller.Tables> choiceBox;
    private EntityManager entityManager;
    private Donation selectedDonation;
    private State state = State.Donations;
    private DonationRepositoryJpaImpl donationRepo;
    public DonationSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.donationRepo = new DonationRepositoryJpaImpl(entityManager);
    }

    public void initialize() {
        this.state = State.Donations;
        btnAdd.setOnAction(e->{add();});
        btnEdit.setOnAction(e->{edit();});
        btnFilterByName.setOnAction(e -> filterByName());
        btnFilterByPrice.setOnAction(e -> filterByPrice());

        btnClose.setOnAction(e ->{
            if(state != State.Donations){
                state = State.Donations;
                showDonations();
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

        TableColumn<Donation, Integer > customurColumn = new TableColumn<>("Costumer");
        TableColumn<Donation, LocalDate> dateColumn = new TableColumn<>("Date");
        TableColumn<Donation, Float> MoneyDonatedColumn = new TableColumn<>("Money Donated");
        //TableColumn<Museum, Float> revenueColumn = new TableColumn<>("revenue");
        //TableColumn<Museum, Integer> visitorsColumn = new TableColumn<>("visitors");


        //customurColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getCustomer().getCustomerID())));
        customurColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerId()));
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
                            float money = Float.parseFloat(s[0]);
                            LocalDate date = LocalDate.now();//LocalDate.parse(s[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            //LocalDate date = LocalDate.parse(s[1]);
                            int customerId= Integer.parseInt(s[1]);
                            Donation donation = new Donation(money,date,customerId);
                            donationRepo.addDonation(donation);
                            showDonations();
                            break;
                        /*case Loans:
                            Loan loan = new Loan(selectedCustomer, LocalDate.now(),LocalDate.of(Integer.valueOf(s[1]),
                                    Integer.valueOf(s[2]),
                                    Integer.valueOf(s[3])));
                            customerRepo.addLoan(loan);
                            showLoans();
                            break;
                        case Purchases:

                            break;
                        case Donations:

                            break;

                         */
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
