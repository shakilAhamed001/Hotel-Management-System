/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hotel.management.system;

import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class CheckInController implements Initializable {

    @FXML
    private AnchorPane checkIn_form;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField emailAddress;
    @FXML
    private DatePicker checkIn_Date;
    @FXML
    private DatePicker checkOut_Date;
    @FXML
    private Label customerNumber;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    private Label total;
    @FXML
    private Label totalDays;

    public void customerCheckIn() {

        String insertCustomerData = "INSERT INTO customer(customer_id,firstName,lastName,phoneNumber,email,checkIn,checkOut)"
                + "Values (?,?,?,?,?,?,?)";

        connect = (Connection) database.connectDb();

        try {

            String customerNum = customerNumber.getText();
            String firstN = firstName.getText();
            String lastN = lastName.getText();
            String phoneNum = phoneNumber.getText();
            String email = emailAddress.getText();
            String checkInDate = String.valueOf(checkIn_Date.getValue());
            String checkOutDate = String.valueOf(checkOut_Date.getValue());

            Alert alert;

            if (customerNum == null || firstN == null || lastN == null || phoneNum == null || email == null
                    || checkInDate == null || checkOutDate == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you Sure");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    prepare = connect.prepareStatement(insertCustomerData);
                    prepare.setString(1, customerNum);
                    prepare.setString(2, firstN);
                    prepare.setString(3, lastN);
                    prepare.setString(4, phoneNum);
                    prepare.setString(5, email);
                    prepare.setString(6, checkInDate);
                    prepare.setString(7, checkOutDate);

                    prepare.executeUpdate();

                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Sucessfully check-In!");
                    alert.showAndWait();

                } else {
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void totalDays() {

        Alert alert;

        if (checkOut_Date.getValue().compareTo(checkIn_Date.getValue()) < 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Check-Out Date");
            alert.showAndWait();
        } else {

            getData.totalDays = checkOut_Date.getValue().compareTo(checkIn_Date.getValue());

        }
    }

    public void displayTotal() {
        
        String totalD = String.valueOf(getData.totalDays);
        totalDays.setText(totalD);
        
        
        double totalPayemnt ; 

    }

    public void customerNumber() throws SQLException {

        String CustomerNum = "SELECT customer_id FROM customer";
        connect = (Connection) database.connectDb();

        try {
            prepare = connect.prepareStatement(CustomerNum);
            result = prepare.executeQuery();

            while (result.next()) {
                getData.customerNum = result.getInt("customer_id");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayCoustomerNumber() throws SQLException {

        customerNumber();

        customerNumber.setText(String.valueOf(getData.customerNum + 1));

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            displayCoustomerNumber();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
