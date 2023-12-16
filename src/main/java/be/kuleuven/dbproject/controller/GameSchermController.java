package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.domain.*;
import be.kuleuven.dbproject.repositories.ConsoleGenreRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.GameRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.MuseumRepositoryJpaImpl;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GameSchermController implements Controller {
    private final EntityManager entityManager;
    private final GameRepositoryJpaImpl gameRepo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnInstances;
    @FXML
    private TableView tblConfigs;

    private enum State{
        GAMES,
        GAMEINSTANCES
    }
    private State state;

    public GameSchermController(EntityManager entityManager) { 
        this.entityManager = entityManager;
        this.gameRepo = new GameRepositoryJpaImpl(entityManager);
    }
    private Game selectedGame;

    public void initialize(){
        this.state = State.GAMES;
        showGames();
        btnAdd.setOnAction(e -> add());
        btnClose.setOnAction(e ->{
            if(state == State.GAMEINSTANCES){
                showGames();
                state = State.GAMES;
            }
            else{
                var stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
            }
        });
        btnEdit.setOnAction(e -> edit());
        btnInstances.setOnAction(e -> {
            showGameInstances();
            this.state = State.GAMEINSTANCES;
        });
        btnInstances.setVisible(false);
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.GAMES) {
                selectedGame = (Game) tblConfigs.getSelectionModel().getSelectedItem();
                btnInstances.setVisible(true);
            }
        });
    }

    private void showGameInstances() {
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<GameInstance, String> gameIDColumn = new TableColumn<>("GameID");
        TableColumn<GameInstance, String> gameInstanceColumn = new TableColumn<>("GameInstanceID");
        TableColumn<GameInstance, String> museumColumn = new TableColumn<>("MuseumID");

        gameIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGameID())));
        gameInstanceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGameInstanceID())));
        museumColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getMuseumID())));


        tblConfigs.getColumns().addAll(gameIDColumn,gameInstanceColumn,museumColumn);
        List<GameInstance> gameInstances = gameRepo.getAllGameInstances(selectedGame.getGameID());
        tblConfigs.getItems().clear();
        for (GameInstance instance : gameInstances) {
            tblConfigs.getItems().add(instance);
        }

    }

    private void edit() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            var museumRepo = new MuseumRepositoryJpaImpl(entityManager);
            if (state == State.GAMEINSTANCES) {
                controller = new addCustomDialogController(true,new String[]{"museum"},new String[][]{museumRepo.getAllMuseumAdresses()});
            } else {
                controller = new addCustomDialogController(new String[]{"value"},new String[]{String.valueOf(selectedGame.getValue())});
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
                    if (state == State.GAMES){
                        try{
                            if(s[0] == ""){
                                throw new NumberFormatException();
                            }
                            gameRepo.changeGameValue(Integer.parseInt(s[0]),selectedGame);
                        } catch(NumberFormatException e) {
                            showAlert();
                            edit();
                        }
                        showGames();
                    }
                    else{
                        try{
                            if(s[0] == ""){
                                throw new NumberFormatException();
                            }
                            var selectedGInstance = (GameInstance) tblConfigs.getSelectionModel().getSelectedItem();
                            gameRepo.changeGameInstanceMuseum(museumRepo.getMuseumByAddress(s[0]).getMuseumID(),selectedGInstance);
                        } catch(NumberFormatException e) {
                            showAlert();
                            edit();
                        }
                        showGameInstances();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void add() {
        try {
            ConsoleGenreRepositoryJpaImpl cgRepo = new ConsoleGenreRepositoryJpaImpl(entityManager);
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            var museumRepo = new MuseumRepositoryJpaImpl(entityManager);
            if (state == State.GAMEINSTANCES) {
                controller = new addCustomDialogController(true,new String[]{"museum"},new String[][]{museumRepo.getAllMuseumAdresses()});
            } else {
                controller = new addCustomDialogController(true, new String[]{"console","genre","name", "year", "value"}
                                                            ,new String[][]{cgRepo.getAllConsoleNames(),cgRepo.getAllGenreNames()});
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

                    if (state == State.GAMES){
                        try{
                            if(s[0] == ""){
                                throw new NumberFormatException();
                            }
                            Game game = new Game(s[2],cgRepo.getConsole(s[0]),cgRepo.getGenre(s[1]),Integer.parseInt(s[3]),Float.parseFloat(s[4]));
                            gameRepo.addGame(game);
                        } catch(NumberFormatException e) {
                            showAlert();
                            add();
                        }
                        showGames();
                    }
                    else{
                        try{
                            if(s[0] == ""){
                                throw new NumberFormatException();
                            }
                            GameInstance gInstance = new GameInstance(selectedGame.getGameID(),museumRepo.getMuseumByAddress(s[0]).getMuseumID());
                            gameRepo.addGameInstance(gInstance);
                        } catch(NumberFormatException e) {
                            showAlert();
                            add();
                        }
                        showGameInstances();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void showGames() {
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<Game, String> gameIDColumn = new TableColumn<>("GameID");
        TableColumn<Game, String> nameColumn = new TableColumn<>("name");
        TableColumn<Game, String> consoleColumn = new TableColumn<>("Console");
        TableColumn<Game, String> genreColumn = new TableColumn<>("Genre");
        TableColumn<Game, String> yearColumn = new TableColumn<>("year");
        TableColumn<Game, String> valueColumn = new TableColumn<>("value");

        gameIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGameID())));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        consoleColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getConsole().getConsoleID())));
        genreColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGenre().getGenreID())));
        yearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getYear())));
        valueColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(Float.toString(cellData.getValue().getValue())));

        tblConfigs.getColumns().addAll(gameIDColumn,nameColumn,consoleColumn,genreColumn,yearColumn,valueColumn);
        List<Game> games = gameRepo.getAllGames();
        tblConfigs.getItems().clear();
        for (Game game : games) {
            tblConfigs.getItems().add(game);
        }
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        if(state == State.GAMES){

            alert.setContentText("Please enter something for name and a number for year/value");

        }
        else{
            alert.setContentText("Please select a museum");
        }
        alert.showAndWait();
    }
}
