package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.Console;
import be.kuleuven.dbproject.domain.Customer;
import be.kuleuven.dbproject.domain.Game;
import be.kuleuven.dbproject.domain.Genre;
import be.kuleuven.dbproject.repositories.ConsoleGenreRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.annotations.Tables;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminSchermController implements Controller {
    private EntityManager entityManager;
    private ConsoleGenreRepositoryJpaImpl cgRepo;

    public enum Tables {
        Console,
        Genre
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
    private Tables state;


    public AdminSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.cgRepo = new ConsoleGenreRepositoryJpaImpl(entityManager);
    }




    public void initialize() {
        this.state = Tables.Console;
        initTable(Tables.Console);
        btnAdd.setOnAction(e -> add());
        btnEdit.setOnAction(e -> edit());
        btnDelete.setOnAction(e -> {
            if(state == Tables.Console){
                Console console = (Console) tblConfigs.getSelectionModel().getSelectedItem();
                cgRepo.deleteConsole(console);
                showConsoles();
            }
            else{
                Genre genre = (Genre) tblConfigs.getSelectionModel().getSelectedItem();
                cgRepo.deleteGenre(genre);
                showGenres();
            }
        });
        
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
        choiceBox.getItems().clear();
        choiceBox.getItems().addAll(Tables.Console,Tables.Genre);
        choiceBox.setOnAction(e -> {
            state = choiceBox.getValue();
            initTable(choiceBox.getValue());
        });
        choiceBox.setValue(Tables.Console);
    }

    private void edit() {
        try{
            if(tblConfigs.getSelectionModel().getSelectedItem() == null){
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            Console console = null;
            Genre genre = null;
            if(state == Tables.Console) {
                console = (Console) tblConfigs.getSelectionModel().getSelectedItem();
                controller = new addCustomDialogController(new String[]{"name","year"},new String[]{console.getName(),String.valueOf(console.getYear())});
            }
            else{
                genre = (Genre) tblConfigs.getSelectionModel().getSelectedItem();
                controller = new addCustomDialogController(new String[]{"description"},new String[]{genre.getDescription()});
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
                    if(state == Tables.Console) {

                        cgRepo.changeConsole(s[0],Integer.parseInt(s[1]),console);
                        showConsoles();
                    }
                    else{

                        cgRepo.changeGenre(s[0],genre);
                        showGenres();
                    }
                }
            }
        } catch (IOException e) {
            showAlert();
            add();
        }
    }

    private void add() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            if(state == Tables.Console) {
                controller = new addCustomDialogController(new String[]{"name","year"});
            }
            else{
                controller = new addCustomDialogController(new String[]{"description"});
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
                    if(state == Tables.Console) {
                        Console c = new Console(s[0],Integer.parseInt(s[1]), new ArrayList<Game>());
                        cgRepo.addConsole(c);
                        showConsoles();
                    }
                    else{
                        Genre g = new Genre(s[0]);
                        cgRepo.addGenre(g);
                        showGenres();
                    }
                }
            }
        } catch (IOException e) {
            showAlert();
            add();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please enter the right thing in all fields");
        alert.showAndWait();
    }

    private void initTable(Tables type) {
        System.out.println("table initialised");
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        if(type == Tables.Console){
            showConsoles();
        }
        else{
            showGenres();
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
    private void showGenres() {
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<Genre, String> genreIDColumn = new TableColumn<>("GenreID");
        TableColumn<Genre, String> descriptionColumn = new TableColumn<>("description");




        genreIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGenreID())));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getDescription())));




        tblConfigs.getColumns().addAll(genreIDColumn,descriptionColumn);
        List<Genre> genres = cgRepo.getAllGenres();
        tblConfigs.getItems().clear();
        for (Genre g : genres) {
            tblConfigs.getItems().add(g);
        }
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
