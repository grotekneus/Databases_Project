package be.kuleuven.dbproject.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class BeheerScherm2Controller {
    public enum Tables {
        Console,
        Customer,
        Donation,
        Game,
        GameInstance,
        Genre,
        Loan,
        Museum,
        Purchase,
        ShopItem,
    }

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;
    @FXML
    private ChoiceBox<Tables> choiceBox;


    public void initialize() {
        initTable(Tables.Console);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow();
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
        choiceBox.getItems().clear();
        choiceBox.getItems().addAll(Tables.Console,Tables.Customer,Tables.Donation,Tables.Game,
                Tables.Customer,Tables.GameInstance,Tables.Genre,Tables.Loan,Tables.Museum,Tables.ShopItem);
        choiceBox.setOnAction(e -> {
            initTable(choiceBox.getSelectionModel().getSelectedItem());
        });
        choiceBox.setValue(Tables.Console);
    }

    private void initTable(Tables type) {
        System.out.println("table initialised");
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        String[] colNames;
        switch(type){
            case Console:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case Customer:
                colNames= new String[]{"customerID","full name","adress","email"};
                break;
            case Donation:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case Game:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case GameInstance:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case Genre:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case Loan:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case Museum:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case Purchase:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            case ShopItem:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            default:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;

        }

        // TODO verwijderen en "echte data" toevoegen!
        int colIndex = 0;
        for(var colName : colNames) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }


        /*for(int i = 0; i < 10; i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList("ding " + i, "categorie 1", i*10 + "", i * 33 + "", ));
        }*/
    }
    private void fillTable(){

    }
    private void addNewRow() {
    }

    private void deleteCurrentRow() {
    }

    private void modifyCurrentRow() {
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void verifyOneRowSelected() {
        if(tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }
}
