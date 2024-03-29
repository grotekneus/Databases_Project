package be.kuleuven.dbproject.controller;





import be.kuleuven.dbproject.domain.*;
import be.kuleuven.dbproject.repositories.GameRepositoryJpaImpl;
import be.kuleuven.dbproject.repositories.MuseumRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class MuseumSchermController {


    public enum State {
        Games,
        Museums,
    }

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSearchMuseum;
    @FXML
    private Button btnSearchGame;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView tblConfigs;


    private EntityManager entityManager;

    private final GameRepositoryJpaImpl gameRepo;

    private Museum selectedMuseum;
    private Game selectedGame;
    private State state = State.Museums;
    private MuseumRepositoryJpaImpl museumRepo;
    public MuseumSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.museumRepo = new MuseumRepositoryJpaImpl(entityManager);
        this.gameRepo = new GameRepositoryJpaImpl(entityManager);
    }

    public void initialize() {
        this.state = State.Museums;
        btnSearchMuseum.setOnAction(e->searchMuseum());
        btnSearchGame.setOnAction(e -> searchGames());
        btnAdd.setOnAction(e -> {add();});
        btnEdit.setOnAction(e -> {edit();});
        btnSearchGame.setVisible(false);

        btnClose.setOnAction(e ->{
            if(state != State.Museums){
                state = state.Museums;
                btnAdd.setVisible(true);
                btnSearchMuseum.setVisible(true);
                showMuseums();
            }
            else{
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });

        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.Museums) {
                selectedMuseum = (Museum) tblConfigs.getSelectionModel().getSelectedItem();
                btnSearchGame.setVisible(true);
            }
            if(newSelection != null && state == State.Games) {
                selectedGame = (Game) tblConfigs.getSelectionModel().getSelectedItem();
                btnSearchGame.setVisible(true);
            }
        });
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        showMuseums();
    }

    private void searchMuseum(){
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search Museums");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter museum address:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(address -> {
                if(address == null || address.trim().isEmpty()) {
                    throwError("please enter an address");
                    return;
                }
                Museum matchingMuseum = museumRepo.getMuseumByAddress(address);

                if (matchingMuseum==null) {
                    throwError("museum not found");
                }
                else {    // Select the matching museum in the table
                    tblConfigs.getSelectionModel().clearSelection();
                    tblConfigs.getSelectionModel().select(matchingMuseum);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void searchGames(){
        if(state==State.Games){
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Search Game");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter game name");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(name -> {
                    Game game;
                    if (name == null || name.trim().isEmpty()) {
                        throwError("Please enter a name");
                        return;
                    }

                    try{
                        game=gameRepo.findGameByName(name);
                        List<GameInstance> matchingGames = gameRepo.getGameInstancesByGameAndMuseum(game,selectedMuseum);
                        tblConfigs.getItems().clear();
                        tblConfigs.getItems().addAll(matchingGames);
                    } catch (NoResultException e) {
                        throwError("Game not found");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        state=State.Games;
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();
        btnAdd.setVisible(false);
        btnSearchMuseum.setVisible(false);

        TableColumn<GameInstance, String> gameNameColumn = new TableColumn<>("Game name");
        TableColumn<GameInstance, String> gameInstanceColumn = new TableColumn<>("GameInstanceID");
        TableColumn<GameInstance, String> museumColumn = new TableColumn<>("Museum address");

        gameNameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGame().getName())));
        gameInstanceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getGameInstanceID())));
        museumColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getMuseum().getAddress())));


        tblConfigs.getColumns().addAll(gameNameColumn,gameInstanceColumn,museumColumn);

        if (selectedMuseum != null) {
            List<GameInstance> gameInstances = gameRepo.getAllGameInstancesBasedOnMuseum(selectedMuseum);
            tblConfigs.getItems().addAll(gameInstances);
        } else {
            throwError("select a museum");
        }

    }


    private void edit(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            switch(state){
                case Museums:
                    this.selectedMuseum = (Museum) tblConfigs.getSelectionModel().getSelectedItem();
                    if (selectedMuseum == null) {
                        throwError("Please select a museum to edit");
                        return;
                    }
                    controller = new addCustomDialogController(new String[]{"revenue","visitors"},
                            new String[]{String.valueOf(selectedMuseum.getRevenue()),String.valueOf(selectedMuseum.getVisitors()),selectedMuseum.getAddress()});
                    break;
                case Games:
                    this.selectedGame= (Game) tblConfigs.getSelectionModel().getSelectedItem();
                    if (selectedGame == null) {
                        throwError("Please select a game to edit");
                        return;
                    }
                    controller = new addCustomDialogController(true,new String[]{"museum"},new String[][]{museumRepo.getAllMuseumAdresses()});
                    break;


            }
            fxmlLoader.setController(controller);
            DialogPane pane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(pane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent()){
                if (result.get() == ButtonType.APPLY) {
                    String[] s = controller.getInput();
                    if (state == State.Museums){
                        try{
                            selectedMuseum.setRevenue(Float.parseFloat(s[0]));
                            int visitors;
                            visitors=Integer.parseInt(s[1]);
                            if(visitors<0){
                                throwError("visitors has to be 0 or larger");
                                return;
                            }
                            selectedMuseum.setVisitors(visitors);
                            museumRepo.updateMuseum(selectedMuseum);
                        } catch(NumberFormatException e) {
                            throwError("please enter a number");
                            edit();
                        }
                        showMuseums();
                    }
                    else{
                        try{
                            if(s[0] == ""){
                                throw new NumberFormatException();
                            }
                            var selectedGInstance = (GameInstance) tblConfigs.getSelectionModel().getSelectedItem();
                            gameRepo.changeGameInstanceMuseum(museumRepo.getMuseumByAddress(s[0]),selectedGInstance);
                        } catch(NumberFormatException e) {
                            throwError("please select a museum");
                            edit();
                        }
                        state=State.Museums;
                        searchGames();
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showMuseums(){
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        TableColumn<Museum, String> MuseumIDColumn = new TableColumn<>("Museum ID");
        TableColumn<Museum, String> addresColumn = new TableColumn<>("addres");
        TableColumn<Museum, String> countryColumn = new TableColumn<>("country");
        TableColumn<Museum, Float> revenueColumn = new TableColumn<>("revenue");
        TableColumn<Museum, Integer> visitorsColumn = new TableColumn<>("visitors");

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
    }


    private void add() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            addCustomDialogController controller = null;
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("addcustomdialog.fxml"));
            switch(state){
                case Museums:
                    controller = new addCustomDialogController(new String[]{"address","visitors","revenue","country"});
                    break;
            }
            fxmlLoader.setController(controller);
            DialogPane pane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(pane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,ButtonType.APPLY);
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent()){
                if(result.get() == ButtonType.APPLY){

                    switch(state){
                        case Museums:
                            String[] s = controller.getInput();
                            if (s[0] == null || s[0].trim().isEmpty()) {
                                throwError("Please enter a valid address.");
                                return;
                            }
                            if (!s[0].matches(".*\\d.*")) {
                                throwError("Address must contain a number.");
                                return;
                            }

                            int visitors;
                            try {
                                visitors = Integer.parseInt(s[1]);
                                if(visitors<0){
                                    throwError("visitors has to be 0 or larger");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                throwError("Please enter a valid number for visitors.");
                                return;
                            }

                            float revenue;
                            try {
                                revenue = Float.parseFloat(s[2]);
                            } catch (NumberFormatException e) {
                                throwError("Please enter a valid number for revenue.");
                                return;
                            }

                            if (s[3] == null || s[3].trim().isEmpty()) {
                                throwError("Please enter a valid country.");
                                return;
                            }

                            Museum museum = new Museum(s[0],visitors,revenue,s[3]);
                            museumRepo.addMuseum(museum);
                            showMuseums();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void throwError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
