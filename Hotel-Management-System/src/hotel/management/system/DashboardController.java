/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hotel.management.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class DashboardController implements Initializable {

    @FXML
    private Button closebtn;
    @FXML
    private Button minimization;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button aroom_btn;
    @FXML
    private Button customer_btn;
    @FXML
    private Button logout_btn;
    @FXML
    private AnchorPane Dashboard_form;
    @FXML
    private AnchorPane dashbook_todayCheck;
    @FXML
    private Label dashboard_todayIncome;
    @FXML
    private AnchorPane dashboard_totalIncome;
    @FXML
    private AreaChart<?, ?> dashboard_areaChart;
    @FXML
    private AnchorPane availableRoom_RoomFrom;
    @FXML
    private TextField available_roomNumber;
    @FXML
    private ComboBox<?> availableRoom_type;
    @FXML
    private ComboBox<?> availableRoom_status;
    @FXML
    private TextField availableRoom_price;
    @FXML
    private Button availableRoom_addbtn;
    @FXML
    private Button availableRoom_updatebtn;
    @FXML
    private Button availableRoom_deletebtn;
    @FXML
    private Button availableRoom_clearbtn;
    @FXML
    private Button availableRoom_checkbtn;
    @FXML
    private TextField availableRoom_search;
    @FXML
    private TableView<?> availableRoom_tableView;
    @FXML
    private TableColumn<?, ?> availableRoom_RoomNumber;
    @FXML
    private TableColumn<?, ?> availableRoom_RoomType;
    @FXML
    private TableColumn<?, ?> availableRoom_RoomStatus;
    @FXML
    private TableColumn<?, ?> availableRoom_RoomPrice;
    @FXML
    private AnchorPane customer_From;
    @FXML
    private TableView<?> customer_TableView;
    @FXML
    private TableColumn<?, ?> customer_CustomerNumber;
    @FXML
    private TableColumn<?, ?> customer_CustomerFName;
    @FXML
    private TableColumn<?, ?> customer_CustomerLName;
    @FXML
    private TableColumn<?, ?> customer_Customerphone;
    @FXML
    private TableColumn<?, ?> customer_CustomerTotalPayment;
    @FXML
    private TableColumn<?, ?> customer_CustomerCheckIn;
    @FXML
    private TableColumn<?, ?> customer_CustomerCheckout;
    @FXML
    private TextField customer_search;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
