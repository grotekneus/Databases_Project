package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.domain.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class addCustomerDialogController {
    @FXML
    private TextField TextName;
    @FXML
    private TextField TextAdress;
    @FXML
    private TextField TextMail;

    public Customer getCustomer(){
        Customer c = new Customer(TextName.getText(),TextAdress.getText(),TextMail.getText());
        return c;
    }






}
