package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbException;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.NoDocumentException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class LoginController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private CheckBox adminCheck;
    @FXML
    private Button loginBtn;
    @FXML
    private Button createAccBtn;

    private EntityManager entityManager;

    private static final String AES = "AES";
    private static SecretKey key;

    static {
        try {
            key = createAESKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CouchDbClient client;
    public LoginController(EntityManager entityManager) {
        CouchDbProperties properties = new CouchDbProperties()
                .setDbName("db-project")
                .setCreateDbIfNotExist(true)
                .setProtocol("http")
                .setHost("127.0.0.1")
                .setPort(5984)
                .setUsername("user")
                .setPassword("user")
                .setMaxConnections(100)
                .setConnectionTimeout(0);
        client = new CouchDbClient(properties);
        this.entityManager = entityManager;
    }
    public void initialize(){
        loginBtn.setOnAction(e -> handleLogin());
        createAccBtn.setOnAction(e -> handleCreateAccount());
    }

    private void handleCreateAccount() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isAdmin = adminCheck.isSelected();
        if (!username.isEmpty() && !password.isEmpty()) {
            try{
                String JsonQuery = "{ \"selector\": { \"userName\": \"" + username + "\" } }";
                User user = client.findDocs(JsonQuery,User.class).get(0);
                showDialog("username taken");
            } catch(IndexOutOfBoundsException e){
                String encryptedPassWord = encryptPassword(password);
                User newUser = new User(encryptedPassWord,username,isAdmin);
                client.save(newUser);
                client.shutdown();
                switchToMainWindow(isAdmin);
            }
        }
    }

    private void showDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (!username.isEmpty() || !password.isEmpty()) {
            try{
                String JsonQuery = "{ \"selector\": { \"userName\": \"" + username + "\", \"passWord\": \"" + encryptPassword(password) + "\" } }";
                User user = client.findDocs(JsonQuery,User.class).get(0);
                switchToMainWindow(user.isAdmin());
            } catch(IndexOutOfBoundsException e){
                showDialog("user does not exist");
            }
        }
    }

    public static SecretKey createAESKey()
            throws Exception
    {
        SecureRandom securerandom = new SecureRandom();
        KeyGenerator keygenerator = KeyGenerator.getInstance(AES);
        keygenerator.init(256, securerandom);
        SecretKey key = keygenerator.generateKey();
        return key;
    }

    private String encryptPassword(String password) {
        byte[] encodedBytes = Base64.getEncoder().encode(password.getBytes());
        return new String(encodedBytes);
    }


    private void switchToMainWindow(boolean isAdmin){
        try{
            var stage = new Stage();
            ProjectMainController pjMainController = new ProjectMainController(entityManager,isAdmin);
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
            loader.setController(pjMainController);
            AnchorPane root = (AnchorPane) loader.load();
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Main window");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
