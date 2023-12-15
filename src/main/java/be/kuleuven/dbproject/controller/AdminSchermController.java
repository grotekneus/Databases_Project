package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.Console;
import be.kuleuven.dbproject.domain.Game;
import be.kuleuven.dbproject.repositories.ConsoleGenreRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.util.List;

public class AdminSchermController implements Controller {
    private EntityManager entityManager;
    private ConsoleGenreRepositoryJpaImpl cgRepo;

    public AdminSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.cgRepo = new ConsoleGenreRepositoryJpaImpl(entityManager);
    }

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
    private Button btnEdit;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;
    @FXML
    private ChoiceBox<Tables> choiceBox;


    public void initialize() {
        initTable(Tables.Console);
        btnAdd.setOnAction(e -> addNewRow());
        btnEdit.setOnAction(e -> {
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
        //choiceBox.setValue(Tables.Console);
    }

    private void initTable(Tables type) {
        System.out.println("table initialised");
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        String[] colNames;
        switch(type){
            case Console:
                showConsoles();
                colNames= new String[]{"Name","consoleID","year"};
                break;
            case Genre:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
            default:
                colNames= new String[]{"Name","consoleID","year","value","museumid"};
                break;
        }
    }

    private void showConsoles() {
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<Console, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Console, String> consoleIDColumn = new TableColumn<>("consoleID");
        TableColumn<Console, String> yearColumn = new TableColumn<>("year");



        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getName())));
        consoleIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getConsoleID())));
        yearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getYear())));



        tblConfigs.getColumns().addAll(nameColumn,consoleIDColumn,yearColumn);
        List<Console> consoles = cgRepo.getAllConsoles();
        tblConfigs.getItems().clear();
        for (Console c : consoles) {
            tblConfigs.getItems().add(c);
        }
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
