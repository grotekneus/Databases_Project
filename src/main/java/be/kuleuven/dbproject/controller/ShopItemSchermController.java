package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.Donation;
import be.kuleuven.dbproject.domain.Museum;
import be.kuleuven.dbproject.domain.ShopItem;
import be.kuleuven.dbproject.repositories.DonationRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.ShopItemRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.persistence.EntityManager;
import java.util.List;

public class ShopItemSchermController implements Controller {

    public enum State {
        Museums,
        Purchases,
        ShopItems
    }

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnChangeLocation;
    @FXML
    private Button btnChangePrice;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearchShopItem;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView tblConfigs;
    @FXML
    private ChoiceBox<BeheerScherm2Controller.Tables> choiceBox;
    private EntityManager entityManager;
    private ShopItem selectedShopItem;
    private ShopItemSchermController.State state = ShopItemSchermController.State.ShopItems;
    private ShopItemRepositoryJpaImpl ShopItemRepo;
    public ShopItemSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.ShopItemRepo = new ShopItemRepositoryJpaImpl(entityManager);
    }

    public void initialize(){
        this.state = State.ShopItems;
        btnAdd.setOnAction(e->{add();});
        btnEdit.setOnAction(e->{edit();});
        btnChangeLocation.setOnAction(e -> changeLocation());
        btnChangePrice.setOnAction(e -> changePrice());
        btnSearchShopItem.setOnAction(e -> searchShopItem());

        btnChangeLocation.setVisible(false);
        btnChangePrice.setVisible(false);

        btnClose.setOnAction(e ->{
            if(state != State.ShopItems){
                state = State.ShopItems;
                showShopItems();
            }
        });
    }

    private void showShopItems(){
        /*
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<ShopItem, String> itemId = new TableColumn<>("item ID");
        TableColumn<ShopItem, String> addresColumn = new TableColumn<>("addres");
        TableColumn<ShopItem, String> countryColumn = new TableColumn<>("country");
        TableColumn<ShopItem, Float> revenueColumn = new TableColumn<>("revenue");
        TableColumn<ShopItem, Integer> visitorsColumn = new TableColumn<>("visitors");

        MuseumIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getMuseumID())));
        addresColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress()));
        countryColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCountry()));
        revenueColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRevenue()));
        visitorsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getVisitors()));

        tblConfigs.getColumns().addAll(MuseumIDColumn, addresColumn, countryColumn, revenueColumn, visitorsColumn);
        List<Museum> museums = museumRepo.getMuseums();
        tblConfigs.getItems().clear();
        for (Museum museum : museums) {
            tblConfigs.getItems().add(museum);
        }

         */
    }

    private void add(){

    }

    private void edit(){

    }

    private void changeLocation(){

    }

    private void changePrice(){

    }

    private void searchShopItem(){

    }

}
