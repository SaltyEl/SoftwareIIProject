package com.c195project.c195project.controller;

import com.c195project.c195project.model.User;
import com.c195project.c195project.helpers.HelperFunctions;
import com.c195project.c195project.DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The controller for interaction between User.java, UserDAO.java and login-page.fxml
 *
 * @author Blake Ramsey
 */
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
    public static boolean loginButtonClicked;

    /**
     * This method initializes the LoginPage controller after root element has been processed. If computer language is
     * set to french, the page is initialized so that all labels and buttons are translated to french.
     *
     * @param url Resolves relative paths for root object, or null if location is not known.
     * <br>
     * @param resourceBundle Resources used to localize root object, or null if root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Need to take out and test with actual computer change in language.
        /*Locale french = Locale.CANADA_FRENCH;
        Locale.setDefault(french);*/



        Locale locale = Locale.getDefault();
        timeZoneTxt.setText(ZoneId.systemDefault().toString());
        if(locale.getLanguage().equals("fr")){
            userNameLabel.setText(convertWordToFrenchCA("Username"));
            PasswordLabel.setText(convertWordToFrenchCA("Password"));
            loginBtn.setText(convertWordToFrenchCA("Login"));
        }
    }

    /**
     *
     * @return the userName
     */
    public static String getUserName() {
        return userName;
    }

    /**
     *
     * @return the password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * This method checks to see if userName exists, and then compares it against the password retrieved for that user
     * from the database. If username / password combination are correct, the user is directed to the customer page. If
     * the username / password combination are not correct, the user is given an appropriate error message.
     *
     * @param actionEvent The actionEvent is clicking on the LoginButton.
     * @throws Exception
     */
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
                    HelperFunctions.trackUserLoginAttempts(user, false);
                    return;
                }
                if (password.equals(user.getPassword())){
                    loginButtonClicked = true;
                    currentUser = usernameTxt.getText();
                    HelperFunctions.trackUserLoginAttempts(user, true);
                    HelperFunctions.windowLoader("/com/c195project/c195project/CustomerPage.fxml",
                            LoginPage.class, loginBtn, "Customer",777, 402);
                }else{
                    HelperFunctions.trackUserLoginAttempts(user, false);
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