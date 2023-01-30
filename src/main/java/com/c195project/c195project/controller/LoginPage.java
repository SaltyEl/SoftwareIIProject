package com.c195project.c195project.controller;

import com.c195project.c195project.model.User;
import helper.HelperFunctions;
import helper.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {

    public TextField usernameTxt;
    public PasswordField passwordTxt;

    private static String userName;
    private static String password;
    public Label userNameLabel;
    public Label PasswordLabel;
    public Button loginBtn;
    public Label timeZoneTxt;
    public static String currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Need to take out and test with actual computer change in language.
        /*Locale french = Locale.CANADA_FRENCH;
        Locale.setDefault(french);*/

        timeZoneTxt.setText(ZoneId.systemDefault().toString());

        Locale locale = Locale.getDefault();
        if(locale.getLanguage().equals("fr")){
            userNameLabel.setText(convertWordToFrenchCA("Username"));
            PasswordLabel.setText(convertWordToFrenchCA("Password"));
            loginBtn.setText(convertWordToFrenchCA("Login"));
        }
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws Exception{
        try {
            userName = usernameTxt.getText();
            if(userName.isEmpty()){
                return;
            }
            User user = UserDAO.findUser();
            if (user != null) {
                password = passwordTxt.getText();
                if (password.isEmpty()) {
                    return;
                }
                if (password.equals(user.getPassword())){
                    currentUser = usernameTxt.getText();
                    HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                            LoginPage.class, loginBtn, 632, 402);
                }else{
                    if(Locale.getDefault().getLanguage().equals("fr")) {
                        HelperFunctions.showError(convertWordToFrenchCA("Error"),
                                convertSentenceToFrenchCA("Password is wrong"));
                    }else{
                        HelperFunctions.showError("Error", "Password is incorrect");
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Converts a given word from english to French-Canadian.
     *
     * @param wordToConvert The word to convert to French.
     * @return Returns String of word in French.
     */
    public static String convertWordToFrenchCA(String wordToConvert){
        ResourceBundle rb = ResourceBundle.getBundle(
                "com/c195project/c195project/Translate",
                Locale.getDefault());
        return rb.getString(wordToConvert);
    }

    /**
     * Converts a given sentence from English to French-Canadian.
     *
     * @param sentenceToConvert The sentence to convert to French.
     * @return Returns a String of sentence in French.
     */
    private static String convertSentenceToFrenchCA(String sentenceToConvert){
        ResourceBundle rb = ResourceBundle.getBundle(
                "com/c195project/c195project/Translate",
                Locale.getDefault());
        StringBuilder convertedString = new StringBuilder();
        String[] splitSentence = sentenceToConvert.split("\\s+");
        for(int i=0; i< splitSentence.length;i++){
            splitSentence[i] = splitSentence[i].replaceAll(" ", "");
        }
        for(int i=0;i<splitSentence.length;i++){
            convertedString.append(rb.getString(splitSentence[i]));
            if(i != splitSentence.length - 1){
                convertedString.append(" ");
            }
        }
        return convertedString.toString();
    }
}