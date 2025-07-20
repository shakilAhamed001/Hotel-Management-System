/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hotel.management.system;

import java.sql.Connection;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

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
    private Statement statement;

    @FXML
    private Label total;
    @FXML
    private Label totalDays;
    @FXML
    private ComboBox<?> roomType;
    @FXML
    private ComboBox<?> roomNumber;

    public void customerCheckIn() {

        String insertCustomerData = "INSERT INTO customer(customer_id,roomType,roomNumber,firstName,lastName,phoneNumber,email,checkIn,checkOut)"
                + "Values (?,?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {

            String customerNum = customerNumber.getText();
            String roomT = (String)roomType.getSelectionModel().getSelectedItem();
            String roomN = (String)roomNumber.getSelectionModel().getSelectedItem();
            
            
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
                     prepare.setString(2, roomT);
                      prepare.setString(3, roomN);
                    prepare.setString(4, firstN);
                    prepare.setString(5, lastN);
                    prepare.setString(6, phoneNum);
                    prepare.setString(7, email);
                    prepare.setString(8, checkInDate);
                    prepare.setString(9, checkOutDate);

                    prepare.executeUpdate();

                    
                    String date = String.valueOf(checkIn_Date.getValue());
                   String totalC = String.valueOf(totalP);
                    String customerN = customerNumber.getText();
                    String customerReceipt = "INSERT INTO customer_receipt (customer_num,total,date)"
                            + "VALUES(?,?,?)"; 
                    
                    prepare = connect.prepareStatement(customerReceipt);
                    prepare.setString(1, customerN);
                    prepare.setString(2, totalC);
                         prepare.setString(3, date);
                         
                         
                         prepare.execute();
                         
                         String sqlEditStatus = " UPDATE room SET status = 'Occupied' WHERE roomNumber = '"+roomN+"'";
                         statement = connect.createStatement();
                         statement.executeUpdate(sqlEditStatus);
                         
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

        if (checkOut_Date.getValue().isAfter(checkIn_Date.getValue())) {
            getData.totalDays = checkOut_Date.getValue().compareTo(checkIn_Date.getValue());

        } else {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Check-Out Date");
            alert.showAndWait();
            
        
        }

        displayTotal();
    }

    private double totalP = 0;

    public void displayTotal() {

        String totalD = String.valueOf(getData.totalDays);
        totalDays.setText(totalD);

        String selectItem = (String) roomNumber.getSelectionModel().getSelectedItem();
        String sql = "SELECT * FROM room WHERE roomNumber = '" + selectItem + "'";
        double priceData = 0;

        connect = database.connectDb();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {

                priceData = result.getDouble("price");
            }

            totalP = ((priceData) * getData.totalDays);
            System.out.println("Total Payment: " + totalP);
            System.out.println("PriceData : " + priceData);
            total.setText("$" + String.valueOf(totalP));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void customerNumber() throws SQLException {

        String CustomerNum = "SELECT customer_id FROM customer";
        connect = database.connectDb();

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

    public void roomTypeList() {

        String listType = "SELECT * FROM room WHERE status = 'Available' GROUP BY type ORDER BY type ASC";

        connect = database.connectDb();

        try {

            ObservableList listData = FXCollections.observableArrayList();
            prepare = connect.prepareStatement(listType);
            result = prepare.executeQuery();

            while (result.next()) {
                listData.add(result.getString("type"));

            }

            roomType.setItems(listData);

          roomNumberList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void roomNumberList() {
        
      

        String item = (String) roomType.getSelectionModel().getSelectedItem();
        String availableRoomNumber = "SELECT * FROM room WHERE type = '"+item +"' ORDER BY roomNumber ASC ";

        connect = database.connectDb();

        try {

            ObservableList listData = FXCollections.observableArrayList();

            prepare = connect.prepareStatement(availableRoomNumber);
            result = prepare.executeQuery();

            while (result.next()) {
                listData.add(result.getString("roomNumber"));

            }
           roomNumber.setItems(listData);

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
            
            roomTypeList();
             roomNumberList();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(CheckInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
    
//    public void initialize(URL location, ResourceBundle resources) {
//
//        displayCoustomerNumber();
//            roomTypeList();
//
//            roomNumberList();
//    }
//
//}
