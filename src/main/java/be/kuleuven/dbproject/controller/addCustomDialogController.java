package be.kuleuven.dbproject.controller;

import javafx.fxml.FXML;
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
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
    @FXML
    private Text text5;
    private List<TextField> textFs;

    private String[] titles;

    private String[] members;

    public addCustomDialogController(String[] titles) {
        this.titles = titles;
    }
    public addCustomDialogController(String[] titles, String[] members) {
        this.titles = titles;
        this.members = members;
    }

    public void initialize(){
        List<Text> textList = Arrays.asList(text1, text2, text3, text4, text5);
        textFs = Arrays.asList(textF1, textF2, textF3, textF4, textF5);
        for(int i = 0; i<textList.size();i++){
            if(i>= titles.length){
                textList.get(i).setVisible(false);
                textFs.get(i).setVisible(false);
            }
            else{
                textList.get(i).setText(titles[i]);
            }
        }


    }

    public String[] getInput(){
        String[] result = new String[titles.length];
        for(int i = 0; i < titles.length; i++){
            result[i] = textFs.get(i).getText();
        }
        return result;
    }






}
