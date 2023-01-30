package helper;

import com.c195project.c195project.model.Country;
import com.c195project.c195project.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;

public class HelperFunctions {

    /**
     * Shows Error Pop-up when called.
     *
     * @param title Title of error box.
     * @param message Message to be displayed about error that is being handled.
     */
    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void windowLoader(String fxmlDoc, Class classname, Button buttonClicked, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(classname.getResource(fxmlDoc));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = (Stage) buttonClicked.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static ObservableList<Division> filterDivisionByCountry(Country country){
        ObservableList<Division> divisionList = DivisionDAO.getDivisionsList();
        ObservableList<Division> filteredList =
                divisionList.stream()
                        .filter(s -> s.getCountryId() == country.getId())
                        .sorted(Comparator.comparing(Division::getDivisionName))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
        return filteredList;
    }
}