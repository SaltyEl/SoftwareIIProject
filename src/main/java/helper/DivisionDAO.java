package helper;

import com.c195project.c195project.model.Country;
import com.c195project.c195project.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DivisionDAO {

    public static final String COLUMN_DIVISION_NAME = "division";
    public static final String COLUMN_DIVISION_ID = "division_id";
    public static final String COLUMN_DIVISION_COUNTRY_ID = "country_id";
    public static final String TABLE_DIVISIONS = "client_schedule.first_level_divisions";

    public static final String GET_DIVISIONS_LIST = "SELECT " + COLUMN_DIVISION_ID+ ", " +
            COLUMN_DIVISION_NAME + ", " + COLUMN_DIVISION_COUNTRY_ID + " FROM " + TABLE_DIVISIONS;


    public static final String ORDER_BY_DIV_ASC = "ORDER BY division ASC";

    public static ObservableList<Division> getDivisionsList(){
        try{
            JDBC.openConnection();
            String queryString = GET_DIVISIONS_LIST;
            Query.querySQL(queryString);
            ResultSet result = Query.getResult();
            ObservableList<Division> resultsList = FXCollections.observableArrayList();
            while(result.next()) {
                Division division = new Division();
                division.setId(result.getInt(COLUMN_DIVISION_ID));
                division.setDivisionName(result.getString(COLUMN_DIVISION_NAME));
                division.setCountryId(result.getInt(COLUMN_DIVISION_COUNTRY_ID));
                resultsList.add(division);
            }
            return resultsList;

        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        finally{
            JDBC.closeConnection();
        }
        return null;
    }
}
