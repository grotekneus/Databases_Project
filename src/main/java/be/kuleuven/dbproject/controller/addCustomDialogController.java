package be.kuleuven.dbproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;


public class addCustomDialogController implements Controller {
    @FXML
    private TextField textF1;
    @FXML
    private TextField textF2;
    @FXML
    private TextField textF3;
    @FXML
    private TextField textF4;
    @FXML
    private TextField textF5;
    @FXML
    private TextField textF6;
    @FXML
    private TextField textF7;
    @FXML
    private ComboBox<String> cBox1;
    @FXML
    private ComboBox<String> cBox2;

    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
    @FXML
    private Text text5;
    @FXML
    private Text text6;
    @FXML
    private Text text7;
    private List<TextField> textFs;

    private String[] titles;

    private String[] members;

    private boolean cbox = false;

    private String[][] cBoxItems;

    public addCustomDialogController(String[] titles) {
        this.titles = titles;
    }
    public addCustomDialogController(String[] titles, String[] members) {
        this.titles = titles;
        this.members = members;
    }

    public addCustomDialogController(boolean cbox,String[] titles,String[][] cBoxItems) {
        this.titles = titles;
        this.cbox = cbox;
        this.cBoxItems = cBoxItems;
    }

    public void initialize(){
        cBox1.setValue("");
        cBox2.setValue("");
        List<Text> textList = Arrays.asList(text1, text2, text3, text4, text5, text6, text7);
        textFs = Arrays.asList(textF1, textF2, textF3, textF4, textF5, textF6, textF7);
        for(int i = 0; i<textList.size();i++){
            if(i>= titles.length){
                textList.get(i).setVisible(false);
                textFs.get(i).setVisible(false);
            }
            else{
                textList.get(i).setText(titles[i]);
                if(members != null){
                    textFs.get(i).setText(members[i]);
                }
            }
        }
        if(cbox){
            cBox1.getItems().addAll(cBoxItems[0]);
            textF1.setVisible(false);
            if(cBoxItems.length > 1) {
                cBox2.getItems().addAll(cBoxItems[1]);
                textF2.setVisible(false);
            }
            else{
                cBox2.setVisible(false);
            }
        }
        else{
            cBox1.setVisible(false);
            cBox2.setVisible(false);

        }
    }

    public String[] getInput(){
        String[] result = new String[titles.length];
        if(cbox){
            result[0] = cBox1.getValue();
            if(cBoxItems.length > 1){
                result[1] = cBox2.getValue();
                for(int i = 2; i < titles.length; i++){
                    result[i] = textFs.get(i).getText();
                }
            }
            else{
                for(int i = 1; i < titles.length; i++){
                    result[i] = textFs.get(i).getText();
                }
            }



        }
        else{
            for(int i =0; i < titles.length; i++){
                result[i] = textFs.get(i).getText();
            }
        }

        return result;
    }






}
