package be.kuleuven.dbproject.controller;





import be.kuleuven.dbproject.domain.Customer;
import be.kuleuven.dbproject.domain.Museum;
import be.kuleuven.dbproject.repositories.MuseumRepositoryJpaImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class MuseumSchermController implements Controller {
    public enum State {
        Loans,
        Museums,
        Purchases,
        Donations
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
    private Button btnRevenue;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView tblConfigs;
    @FXML
    private ChoiceBox<BeheerScherm2Controller.Tables> choiceBox;

    private EntityManager entityManager;
    private Museum selectedMuseum;
    private State state = State.Museums;
    private MuseumRepositoryJpaImpl museumRepo;
    public MuseumSchermController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.museumRepo = new MuseumRepositoryJpaImpl(entityManager);
    }

    public void initialize() {
        this.state = State.Museums;
        btnSearchMuseum.setOnAction(e->searchMuseums());
        btnSearchGame.setOnAction(e -> searchGames());
        btnRevenue.setOnAction(e -> edditRevenue());
        btnAdd.setOnAction(e -> {add();});
        btnEdit.setOnAction(e -> {edit();});
        btnSearchGame.setVisible(false);
        btnRevenue.setVisible(false);
        //btnShowPurchases.setVisible(false);
        btnClose.setOnAction(e ->{
            if(state != State.Museums){
                state = State.Museums;
                showMuseums();
            }
        });

        tblConfigs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && state == State.Museums) {
                btnSearchGame.setVisible(true);
                btnRevenue.setVisible(true);
                //btnShowPurchases.setVisible(true);
            }
        });
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        showMuseums();
    }

    private void searchMuseums(){
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search Museums");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter museum address:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(address -> {
                // Perform the search based on the entered address
                List<Museum> matchingMuseums = museumRepo.getMuseumsByAddress(address);

                if (matchingMuseums.isEmpty()) {
                    // No matching museums found, display a message or handle this case
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Result");
                    alert.setHeaderText(null);
                    alert.setContentText("No museums found with the specified address.");
                    alert.showAndWait();
                } else {
                    // Select the matching museum in the table
                    tblConfigs.getSelectionModel().clearSelection();
                    for (Museum museum : matchingMuseums) {
                        tblConfigs.getSelectionModel().select(museum);
                    }
                }
            });
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }
        /*
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search Museums");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter museum address:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(address -> {
                // Perform the search based on the entered address
                List<Museum> matchingMuseums = museumRepo.getMuseumsByAddress(address);

                if (matchingMuseums.isEmpty()) {
                    // No matching museums found, display a message or handle this case
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Result");
                    alert.setHeaderText(null);
                    alert.setContentText("No museums found with the specified address.");
                    alert.showAndWait();
                } else {
                    // Display the matching museums in the table
                    tblConfigs.getItems().clear();
                    tblConfigs.getItems().addAll(matchingMuseums);
                }
            });
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }

         */
    }

    private void searchGames(){

    }

    private void edditRevenue(){
        try {


            // Get the selected museum from the table
            Museum selectedMuseum = (Museum) tblConfigs.getSelectionModel().getSelectedItem();

            if (selectedMuseum == null) {
                // No museum selected, show an error or return
                // You may want to display an error message or handle this case as appropriate

                // Example: Display an error message using an Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a museum to edit revenue.");
                alert.showAndWait();

                return;
            }

            // Show a dialog to input the new revenue
            TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedMuseum.getRevenue()));
            dialog.setTitle("Edit Revenue");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new revenue:");

            Optional<String> result = dialog.showAndWait();

            // Update the revenue if the user entered a value
            result.ifPresent(newRevenueString -> {
                try {
                    float newRevenue = Float.parseFloat(newRevenueString);

                    // Update the revenue of the selected museum
                    selectedMuseum.setRevenue(newRevenue);

                    // Save the changes to the repository (assuming you have an update method)
                    museumRepo.updateMuseum(selectedMuseum);

                    // Refresh the table to reflect the changes
                    showMuseums();
                } catch (NumberFormatException e) {
                    // Handle the case where the user did not enter a valid float
                    // You may want to display an error message or handle this case as appropriate
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            // Handle other exceptions if necessary
            e.printStackTrace();
        }
    }

    private void edit(){

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
                case Loans:
                    controller = new addCustomDialogController(new String[]{"gameID","Year","Month","Day"});
                    break;
                case Purchases:
                    controller = new addCustomDialogController(new String[]{"item type","item id"});
                    break;
                case Donations:
                    controller = new addCustomDialogController(new String[]{"Money Donated"});
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
                    String[] s = controller.getInput();
                    int visitors = Integer.parseInt(s[1]);
                    float revenue = Float.parseFloat(s[2]);

                    switch(state){
                        case Museums:
                            Museum museum = new Museum(s[0],visitors,revenue,s[3]);
                            museumRepo.addMuseum(museum);
                            showMuseums();
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

}
