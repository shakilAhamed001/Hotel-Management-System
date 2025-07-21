/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package hotel.management.system;

import java.sql.Connection;
import java.net.URL;
import java.sql.DriverManager;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static javafx.stage.StageStyle.TRANSPARENT;
import java.sql.Statement;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import static javafx.stage.StageStyle.DECORATED;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class DashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;
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
    private TableView<RoomData> availableRoom_tableView;
    @FXML
    private TableColumn<RoomData, Integer> availableRoom_RoomNumber;
    @FXML
    private TableColumn<RoomData, String> availableRoom_RoomType;
    @FXML
    private TableColumn<RoomData, String> availableRoom_RoomStatus;
    @FXML
    private TableColumn<RoomData, String> availableRoom_RoomPrice;
    @FXML
    private AnchorPane customer_From;
    @FXML
    private TableView<customerData> customer_TableView;
    @FXML
    private TableColumn<customerData, String> customer_CustomerNumber;
    @FXML
    private TableColumn<customerData, String> customer_CustomerFName;
    @FXML
    private TableColumn<customerData, String> customer_CustomerLName;
    @FXML
    private TableColumn<customerData, String> customer_Customerphone;
    @FXML
    private TableColumn<customerData, String> customer_CustomerTotalPayment;
    @FXML
    private TableColumn<customerData, String> customer_CustomerCheckIn;
    @FXML
    private TableColumn<customerData, String> customer_CustomerCheckout;
    @FXML
    private TextField customer_search;

    /**
     * Initializes the controller class.
     */
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public ObservableList<RoomData> availableRoomsListData() {

        ObservableList<RoomData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM room";

        connect = database.connectDb();

        try {

            RoomData roomD;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();

            while (result.next()) {
                roomD = new RoomData(result.getInt("roomNumber"),
                        result.getString("type"),
                        result.getString("status"),
                        result.getDouble("price"));

                listData.add(roomD);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<RoomData> roomDataList;

    public void availableRoomsShowData() {

        roomDataList = availableRoomsListData();
        availableRoom_RoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        availableRoom_RoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        availableRoom_RoomStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableRoom_RoomPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableRoom_tableView.setItems(roomDataList);

    }

    @FXML
    public void availableRoomsSelectData() {

        RoomData roomD = availableRoom_tableView.getSelectionModel().getSelectedItem();
        int num = availableRoom_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {

            return;
        }
        available_roomNumber.setText(String.valueOf(roomD.getRoomNumber()));
        availableRoom_price.setText(String.valueOf(roomD.getPrice()));

    }

    @FXML
    public void availableRoomsSearch() {

        FilteredList<RoomData> filter = new FilteredList<>(roomDataList, e -> true);
        availableRoom_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateRoomData -> {

                if (newValue == null) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateRoomData.getRoomNumber().toString().contains(searchKey)) {
                    return true;
                } else if (predicateRoomData.getRoomType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateRoomData.getPrice().toString().contains(searchKey)) {

                    return true;
                } else if (predicateRoomData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<RoomData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(availableRoom_tableView.comparatorProperty());
        availableRoom_tableView.setItems(roomDataList);
    }

    @FXML
    public void availableRoomAdd() {
        String sql = "INSERT INTO room (roomNumber,type,status,price) VALUES(?,?,?,?)";
        connect = database.connectDb();

        try {
            String roomNumber = (String) available_roomNumber.getText();
            String type = (String) availableRoom_type.getSelectionModel().getSelectedItem();
            String status = (String) availableRoom_status.getSelectionModel().getSelectedItem();
            String price = availableRoom_price.getText();

            prepare = connect.prepareStatement(sql);

            prepare.setString(1, roomNumber);
            prepare.setString(2, type);
            prepare.setString(3, status);
            prepare.setString(4, price);

            Alert alert;

            if (roomNumber == null || type == null || status == null || price == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank feilds");
                alert.showAndWait();

            } else {

                String check = "SELECT roomNumber FROM room WHERE roomNumber = '" + roomNumber + "' ";

                prepare = connect.prepareStatement(check);

                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Room #" + roomNumber + "was already exist!");
                    alert.showAndWait();

                } else {

                    prepare = connect.prepareStatement(sql);

                    prepare.setString(1, roomNumber);
                    prepare.setString(2, type);
                    prepare.setString(3, status);
                    prepare.setString(4, price);

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Succesfully added");
                    alert.showAndWait();

                    availableRoomsShowData();
                    availableRoomsClear();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    public void availableRoomsUpadte() {

        String type1 = (String) availableRoom_type.getSelectionModel().getSelectedItem();
        String status1 = (String) availableRoom_status.getSelectionModel().getSelectedItem();
        String price1 = availableRoom_price.getText();
        String roomNum = available_roomNumber.getText();

        String sql = "UPDATE room SET type = '" + type1 + "', status = '" + status1 + "', price ='" + price1
                + "' WHERE roomNumber = '" + roomNum + "'";

        connect = database.connectDb();
        try {

            Alert alert;
            if (type1 == null || status1 == null || price1 == null || roomNum == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the data First");
                alert.showAndWait();

            } else {
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFORMATION Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated");
                alert.showAndWait();

                availableRoomsShowData();
                availableRoomsClear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void availableRoomsDelete() {

        String type1 = (String) availableRoom_type.getSelectionModel().getSelectedItem();
        String status1 = (String) availableRoom_status.getSelectionModel().getSelectedItem();
        String price1 = availableRoom_price.getText();
        String roomNum = available_roomNumber.getText();

        String deleteData = "DELETE FROM room WHERE roomNumber = '" + roomNum + "'";
        connect = database.connectDb();

        try {
            Alert alert;

            if (type1 == null || status1 == null || price1 == null || roomNum == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Please Select data first");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete Room #" + roomNum + "?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("INFORMATION Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfuly Delete");
                    alert.showAndWait();

                    availableRoomsShowData();
                    availableRoomsClear();
                } else {
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void availableRoomsClear() {

        available_roomNumber.setText("");
        availableRoom_type.getSelectionModel().clearSelection();
        availableRoom_status.getSelectionModel().clearSelection();
        availableRoom_price.setText("");

    }

    @FXML
    public void availableRoomsCheckIn() {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("checkIn.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setMinHeight(400 + 15);
            stage.setMinWidth(300 + 15);

            stage.initStyle(DECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String type[]
            = {"Single Room", "Double Room", "Triple Room", "Quad Room", "King Room"};

    @FXML
    public void availableRoomsRoomType() {

        List<String> listData = new ArrayList<>();
        for (String data : type) {

            listData.add(data);
        }

        ObservableList list = FXCollections.observableArrayList(listData);
        availableRoom_type.setItems(list);
    }
    private String status[]
            = {"Available", "Not Available", "Occupied"};

    @FXML
    public void availableRoomsStatus() {

        List<String> listData = new ArrayList<>();
        for (String data : status) {

            listData.add(data);
        }

        ObservableList list = FXCollections.observableArrayList(listData);
        availableRoom_status.setItems(list);
    }

    public ObservableList<customerData> customerListData() {

        ObservableList<customerData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer";

        connect = database.connectDb();
        try {

            prepare = connect.prepareCall(sql);
            result = prepare.executeQuery();

            customerData custD;
            while (result.next()) {

                custD = new customerData(result.getInt("customer_id"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("phoneNumber"),
                        result.getDouble("total"),
                        result.getDate("checkIn"),
                        result.getDate("checkOut"));

                listData.add(custD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;

    }

    private ObservableList<customerData> listCustomerData;

    public void customerShowData() {

        listCustomerData = customerListData();

        customer_CustomerNumber.setCellValueFactory(new PropertyValueFactory<>("customerNum"));
        customer_CustomerFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        customer_CustomerLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        customer_Customerphone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        customer_CustomerTotalPayment.setCellValueFactory(new PropertyValueFactory<>("total"));

        customer_CustomerCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));

        customer_CustomerCheckout.setCellValueFactory(new PropertyValueFactory<>("checkOut"));

        customer_TableView.setItems(listCustomerData);

    }

    @FXML
    public void customerSearch() {

        FilteredList<customerData> filter = new FilteredList<>(listCustomerData, e -> true);
        customer_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateCustomer -> {

                if (newValue == null && newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();
                if (predicateCustomer.getCustomerNum().toString().contains(searchKey)) {

                    return true;

                } else if (predicateCustomer.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getTotal().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getPhoneNumber().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getCheckIn().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getCheckOut().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<customerData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(customer_TableView.comparatorProperty());
        customer_TableView.setItems(sortList);
    }

    @FXML
    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {
            Dashboard_form.setVisible(true);
            availableRoom_RoomFrom.setVisible(false);
            customer_From.setVisible(false);

        } else if (event.getSource() == aroom_btn) {
            Dashboard_form.setVisible(false);
            availableRoom_RoomFrom.setVisible(true);
            customer_From.setVisible(false);
 availableRoomsSearch(); 
            availableRoomsShowData();
        } else if (event.getSource() == customer_btn) {

            Dashboard_form.setVisible(false);
            availableRoom_RoomFrom.setVisible(false);
            customer_From.setVisible(true);
      customerSearch(); 
            customerShowData();
        }
    }

    private double x = 0;
    private double y = 0;

    @FXML
    public void logout() {

        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to Logout");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getSceneX() - x);
                    stage.setY(event.getSceneY() - y);
                });

                stage.initStyle(TRANSPARENT);
                stage.setScene(scene);
                stage.show();

                logout_btn.getScene().getWindow().hide();;
            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void close() {

        System.exit(0);

    }

    @FXML
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
  public void initialize(URL url, ResourceBundle rb) {
        availableRoomsRoomType();
        availableRoomsStatus();
        
        try{
        //  availableRoomsSearch(); 
        }catch(Exception e){e.printStackTrace();}
        availableRoomsShowData(); // Ensure this is called first
        customerShowData();       // Load customer data
       // availableRoomsSearch();   // Set up room search
      //  customerSearch();         // Set up customer search
    }
}
