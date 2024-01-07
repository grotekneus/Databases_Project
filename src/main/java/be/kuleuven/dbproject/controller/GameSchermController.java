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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class GameSchermController implements Controller {
    private final EntityManager entityManager;
    private final GameRepositoryJpaImpl gameRepo;
    @FXML
    private Text title;
    @FXML
    private Button btnDelete;
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
    @FXML
    private Button btnGenres;
    @FXML
    private Button btnConsoles;
    private enum State{
        GAMES,
        GENRES,
        CONSOLES,
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
            if(state != State.GAMES){
                showGames();
                state = State.GAMES;
                btnDelete.setVisible(false);
                btnEdit.setVisible(true);
                selectedGame = null;
            }
            else{
                var stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
            }
        });
        btnEdit.setOnAction(e -> edit());
        btnInstances.setOnAction(e -> {
            showGameInstances();
            btnGenres.setVisible(false);
            btnInstances.setVisible(false);
            btnConsoles.setVisible(false);
            this.state = State.GAMEINSTANCES;
        });
        btnGenres.setOnAction(e ->{
            showGenres();
            btnDelete.setVisible(true);
            btnEdit.setVisible(false);
            btnGenres.setVisible(false);
            btnInstances.setVisible(false);
            btnConsoles.setVisible(false);
            btnEdit.setVisible(false);
        });
        btnConsoles.setOnAction(e -> {
            showConsoles();
            btnDelete.setVisible(true);
            btnGenres.setVisible(false);
            btnInstances.setVisible(false);
            btnConsoles.setVisible(false);
            btnEdit.setVisible(false);
        });
        btnDelete.setOnAction(e -> {
            delete();
        });
        btnDelete.setVisible(false);
        btnInstances.setVisible(false);
        btnGenres.setVisible(false);
        btnConsoles.setVisible(false);
        btnEdit.setVisible(false);
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.GAMES) {
                selectedGame = (Game) tblConfigs.getSelectionModel().getSelectedItem();
                btnEdit.setVisible(true);
                btnInstances.setVisible(true);
                btnGenres.setVisible(true);
                btnConsoles.setVisible(true);
            }
        });
    }

    private void delete() {
        if(selectedGame == null || tblConfigs.getSelectionModel().getSelectedItem() == null){
            showAlert();
        }
        GameRepositoryJpaImpl gameRepo = new GameRepositoryJpaImpl(entityManager);
        if(state == State.CONSOLES){
            Console console  = (Console) tblConfigs.getSelectionModel().getSelectedItem();
            gameRepo.deleteConsoleFromGame(selectedGame,console);
            showConsoles();
        }
        else if(state == State.GENRES){
            Genre genre = (Genre) tblConfigs.getSelectionModel().getSelectedItem();
            gameRepo.deleteGenreFromGame(selectedGame,genre);
            showGenres();
        }
    }

    private void showGameInstances() {
        title.setText("Manage GameInstances");
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<GameInstance, String> gameNameColumn = new TableColumn<>("Game name");
        TableColumn<GameInstance, String> gameInstanceColumn = new TableColumn<>("GameInstanceID");
        TableColumn<GameInstance, String> museumColumn = new TableColumn<>("Museum address");
        TableColumn<GameInstance, String> consoleColumn = new TableColumn<>("console");

        consoleColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getConsole().getName())));
        gameNameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGame().getName())));
        gameInstanceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGameInstanceID())));
        museumColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getMuseum().getAddress())));


        tblConfigs.getColumns().addAll(gameNameColumn,gameInstanceColumn,museumColumn,consoleColumn);
        List<GameInstance> gameInstances = gameRepo.getAllGameInstances(selectedGame);
        tblConfigs.getItems().clear();
        for (GameInstance instance : gameInstances) {
            tblConfigs.getItems().add(instance);
        }

    }

    private void edit() {
        try {
            if(tblConfigs.getSelectionModel().getSelectedItem() == null){
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            var museumRepo = new MuseumRepositoryJpaImpl(entityManager);
            var cgRepo = new ConsoleGenreRepositoryJpaImpl(entityManager);
            if (state == State.GAMEINSTANCES) {
                controller = new addCustomDialogController(true,new String[]{"museum","console"},new String[][]{museumRepo.getAllMuseumAdresses(),cgRepo.getAllConsoleNames()});
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
                    if (state == State.GAMES) {
                        try {
                            if (s[0] == "") {
                                throw new NumberFormatException();
                            }
                            gameRepo.changeGameValue(Integer.parseInt(s[0]), selectedGame);
                        } catch (NumberFormatException e) {
                            showAlert();
                            edit();
                        }
                        showGames();
                    } else {
                        try {
                            if (s[0] == "") {
                                throw new NumberFormatException();
                            }
                            var selectedGInstance = (GameInstance) tblConfigs.getSelectionModel().getSelectedItem();
                            gameRepo.changeGameInstanceMuseum(museumRepo.getMuseumByAddress(s[0]), selectedGInstance);
                            gameRepo.changeGameInstanceConsole(selectedGInstance, cgRepo.getConsole(s[1]));
                        } catch (NumberFormatException e) {
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
                controller = new addCustomDialogController(true,new String[]{"museum","console"},new String[][]{museumRepo.getAllMuseumAdresses(),cgRepo.getAllConsoleNames()});
            } else  if(state== State.GAMES){
                controller = new addCustomDialogController(new String[]{"name", "year", "value"});
            } else if(state == State.GENRES){
                controller = new addCustomDialogController(true, new String[]{"description"},new String[][]{cgRepo.getAllGenreNames()});
            } else{
                controller = new addCustomDialogController(true,new String[]{"controller"},new String[][]{cgRepo.getAllConsoleNames()});
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
                    try{
                        if(s[0] == ""){
                            throw new NumberFormatException();
                        }
                        if (state == State.GAMES){
                            Game game = new Game(s[0],new ArrayList<Console>(),new ArrayList<Genre>(),Integer.parseInt(s[1]),Float.parseFloat(s[2]));
                            gameRepo.addGame(game);
                            showGames();
                        }
                        else if(state == state.GAMEINSTANCES){
                            GameInstance gInstance = new GameInstance(selectedGame,museumRepo.getMuseumByAddress(s[0]),cgRepo.getConsole(s[1]));
                            gameRepo.addGameInstance(gInstance);
                            showGameInstances();
                        }
                        else if(state == state.GENRES){
                            gameRepo.addGenreToGame(selectedGame,cgRepo.getGenre(s[0]));
                            showGenres();
                        }
                        else if(state == State.CONSOLES){
                            gameRepo.addConsoleToGame(selectedGame,cgRepo.getConsole(s[0]));
                            showConsoles();
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

    private void showGenres(){
        title.setText("Manage Genres");
        state = State.GENRES;
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();
        TableColumn<Genre, String> genreIDColumn = new TableColumn<>("genreId");
        TableColumn<Genre, String> descriptionColumn = new TableColumn<>("description");
        genreIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGenreID())));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescription()));
        tblConfigs.getColumns().addAll(descriptionColumn,genreIDColumn);
        List<Genre> genres = gameRepo.getGameGenres(selectedGame);
        for (Genre genre : genres) {
            tblConfigs.getItems().add(genre);
        }
    }

    private void showConsoles(){
        title.setText("Manage Consoles");
        state = State.CONSOLES;
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();
        TableColumn<Console, String> consoleIdColumn = new TableColumn<>("consoleId");
        TableColumn<Console, String> nameColumn = new TableColumn<>("name");
        TableColumn<Console, String> yearColumn = new TableColumn<>("year");

        consoleIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getConsoleID())));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        yearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getYear())));
        tblConfigs.getColumns().addAll(consoleIdColumn,nameColumn,yearColumn);
        List<Console> consoles = gameRepo.getGameConsoles(selectedGame);
        for (Console console : consoles) {
            tblConfigs.getItems().add(console);
        }
    }
    private void showGames() {
        title.setText("Manage Games");
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
        //consoleColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getConsole().getConsoleID())));
        //genreColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGenres().getGenreID())));
        yearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getYear())));
        valueColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(Float.toString(cellData.getValue().getValue())));

        tblConfigs.getColumns().addAll(gameIDColumn,nameColumn,yearColumn,valueColumn);
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
            alert.setContentText("Please enter everything");
        }
        alert.showAndWait();
    }
}
