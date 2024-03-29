package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.*;
import be.kuleuven.dbproject.repositories.MuseumRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.ShopItemRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ShopItemSchermController {

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

    private EntityManager entityManager;
    private ShopItem selectedShopItem;
    private ShopItemSchermController.State state = ShopItemSchermController.State.ShopItems;
    private ShopItemRepositoryJpaImpl shopItemRepo;
    private MuseumRepositoryJpaImpl museumRepo;

    public ShopItemSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.shopItemRepo = new ShopItemRepositoryJpaImpl(entityManager);
        this.museumRepo = new MuseumRepositoryJpaImpl(entityManager);
    }

    public void initialize(){
        this.state = State.ShopItems;
        showShopItems();
        btnAdd.setOnAction(e->{add();});
        btnEdit.setOnAction(e->{edit();});
        btnSearchShopItem.setOnAction(e -> searchShopItem());

        btnChangeLocation.setVisible(false);
        btnChangePrice.setVisible(false);

        btnClose.setOnAction(e ->{
            if(state != State.ShopItems){
                state = state.ShopItems;
                showShopItems();
            }
            else{
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.ShopItems) {
                selectedShopItem = (ShopItem) tblConfigs.getSelectionModel().getSelectedItem();
            }
        });
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        showShopItems();
    }

    private void showShopItems(){

        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<ShopItem, String> itemID = new TableColumn<>("item ID");
        TableColumn<ShopItem, String> addresColumn = new TableColumn<>("addres");
        TableColumn<ShopItem, String> nameColumn = new TableColumn<>("name");
        TableColumn<ShopItem, Float> priceColumn = new TableColumn<>("price");
        TableColumn<ShopItem, ItemType> itemTypeColumn = new TableColumn<>("item type");

        itemID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getItemID())));
        addresColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getMuseum().getAddress()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));
        itemTypeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemType()));

        tblConfigs.getColumns().addAll(itemID, addresColumn, nameColumn, priceColumn, itemTypeColumn);
        List<ShopItem> shopItems = shopItemRepo.getShopItems();
        tblConfigs.getItems().clear();
        for (ShopItem shopItem : shopItems) {
            tblConfigs.getItems().add(shopItem);
        }


    }

    private void add(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            switch(state){
                case ShopItems:
                    ItemType[] itemTypes = ItemType.values();
                    String[] itemTypeNames = new String[itemTypes.length];
                    for (int i = 0; i < itemTypes.length; i++) {
                        itemTypeNames[i] = itemTypes[i].name();
                    }
                    controller = new addCustomDialogController(true, new String[]{"addres","item type","name", "price"}
                            ,new String[][]{museumRepo.getAllMuseumAdresses(),itemTypeNames});
                    break;
            }
            fxmlLoader.setController(controller);
            DialogPane pane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(pane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.APPLY) {
                String[] s = controller.getInput();
                if (state == State.ShopItems) {
                    float price;
                    try {
                        price = Float.parseFloat(s[3]);
                    } catch (NumberFormatException e) {
                        throwError("Please enter a number in price");
                        return;
                    }
                    // Check if a museum is selected
                    if (s[0] == null || s[0].isEmpty()) {
                        throwError("Please select a museum");
                        return;
                    }
                    //check if a item type has been selected
                    if (s[1] == null || s[1].isEmpty()) {
                        throwError("Please select an item type");
                        return;
                    }
                    //check if item has a name
                    if (s[2] == null || s[2].trim().isEmpty()) {
                        throwError("Please enter a name");
                        return;
                    }

                    ItemType itemType = ItemType.valueOf(s[1]);
                    ShopItem shopItem = new ShopItem(s[2], price, museumRepo.getMuseumByAddress(s[0]), itemType);
                    shopItemRepo.addShopItem(shopItem);
                    showShopItems();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void edit(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            switch(state){
                case ShopItems:
                    this.selectedShopItem = (ShopItem) tblConfigs.getSelectionModel().getSelectedItem();
                    if (selectedShopItem == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please select a shop item to edit");
                        alert.showAndWait();
                        return;
                    }
                    controller = new addCustomDialogController(true, new String[]{"addres","price"}
                            ,new String[][]{museumRepo.getAllMuseumAdresses()});
                    break;
            }
            fxmlLoader.setController(controller);
            DialogPane pane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(pane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.APPLY) {
                String[] s = controller.getInput();
                if (state == State.ShopItems) {
                    float price;
                    try {
                        price = Float.parseFloat(s[1]);
                    } catch (NumberFormatException e) {
                        throwError("Please enter a number in price");
                        return;
                    }
                    // Check if a museum is selected
                    if (s[0] == null || s[0].isEmpty()) {
                        throwError("Please select a museum");
                        return;
                    }
                    Museum museum = museumRepo.getMuseumByAddress(s[0]);
                    selectedShopItem.setPrice(price);
                    selectedShopItem.setMuseum(museum);
                    shopItemRepo.updateShopItem(selectedShopItem);
                    showShopItems();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void searchShopItem(){
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search shop item by item ID");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter item ID:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(itemID -> {
                try {
                    int id = Integer.parseInt(itemID);
                    // Perform the search based on the entered item ID
                    ShopItem matchingShopItem = shopItemRepo.getShopItemById(id);

                    if (matchingShopItem == null) {
                        // No matching shop item found
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Search Result");
                        alert.setHeaderText(null);
                        alert.setContentText("No shop item found with the specified item ID.");
                        alert.showAndWait();
                    } else {
                        // Select the matching shop item in the table
                        tblConfigs.getSelectionModel().clearSelection();
                        tblConfigs.getSelectionModel().select(matchingShopItem);
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the entered item ID is not a valid integer
                    showAlert();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        if(state == State.ShopItems){
            alert.setContentText("error");
        }
        else{
            alert.setContentText("Please select a shopitem");
        }
        alert.showAndWait();
    }

    private void throwError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
