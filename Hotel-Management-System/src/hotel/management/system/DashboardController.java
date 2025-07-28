package hotel.management.system;

import java.sql.Connection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static javafx.stage.StageStyle.DECORATED;
import java.sql.Statement;
import java.util.Date;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import javafx.stage.FileChooser;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class for the hotel management system dashboard.
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
    private Button records_btn;
    @FXML
    private Button logout_btn;
    @FXML
    private AnchorPane Dashboard_form;
    @FXML
    private AnchorPane dashbook_todayCheck;
    @FXML
    private AreaChart<String, Number> dashboard_areaChart;
    @FXML
    private AnchorPane availableRoom_RoomFrom;
    @FXML
    private TextField available_roomNumber;
    @FXML
    private ComboBox<String> availableRoom_type;
    @FXML
    private ComboBox<String> availableRoom_status;
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
    private TableColumn<RoomData, Double> availableRoom_RoomPrice;
    @FXML
    private AnchorPane customer_From;
    @FXML
    private TableView<customerData> customer_TableView;
    @FXML
    private TableColumn<customerData, Integer> customer_CustomerNumber;
    @FXML
    private TableColumn<customerData, String> customer_CustomerFName;
    @FXML
    private TableColumn<customerData, String> customer_CustomerLName;
    @FXML
    private TableColumn<customerData, String> customer_Customerphone;
    @FXML
    private TableColumn<customerData, Double> customer_CustomerTotalPayment;
    @FXML
    private TableColumn<customerData, Date> customer_CustomerCheckIn;
    @FXML
    private TableColumn<customerData, Date> customer_CustomerCheckout;
    @FXML
    private TextField customer_search;
    @FXML
    private Button customer_exportCSV;
    @FXML
    private Button customer_viewDetails;
    @FXML
    private AnchorPane records_form;
    @FXML
    private TableView<RecordData> records_tableView;
    @FXML
    private TableColumn<RecordData, String> records_Period;
    @FXML
    private TableColumn<RecordData, Integer> records_Bookings;
    @FXML
    private TableColumn<RecordData, Double> records_Income;
    @FXML
    private AreaChart<String, Number> records_weeklyChart;
    @FXML
    private AreaChart<String, Number> records_monthlyChart;
    @FXML
    private DatePicker records_startDate;
    @FXML
    private DatePicker records_endDate;
    @FXML
    private Button records_applyFilter;
    @FXML
    private CheckBox records_showBookings;
    @FXML
    private CheckBox records_showIncome;
    @FXML
    private Label dashboard_todayIncome;
    @FXML
    private Label dashboard_totalIncome;
    @FXML
    private Label dashboard_bookToday;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private int count = 0;
    private double sumToday = 0;
    private double overall = 0;

    private final String[] type = {"Single Room", "Double Room", "Triple Room", "Quad Room", "King Room"};
    private final String[] status = {"Available", "Not Available", "Occupied"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Dashboard
        dashboardDisplayIncomeToday();
        dashboardDisplayBookToday();
        dashboardTotalIncome();
        dashboardChart();

        // Initialize Available Rooms
        availableRoomsShowData();
        availableRoomsRoomType();
        availableRoomsStatus();
        availableRoomsSearch();

        // Initialize Customers
        customerShowData();
        customerSearch();

        // Initialize Records
        records_startDate.setValue(LocalDate.now().minusDays(30));
        records_endDate.setValue(LocalDate.now());
        recordsShowData();

        // Set default chart series visibility
        records_showBookings.setSelected(true);
        records_showIncome.setSelected(true);
    }

    public void dashboardCountBookToday() {
        connect = database.connectDb();
        count = 0;
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            String sql = "SELECT COUNT(*) as count FROM customer WHERE DATE(checkIn) = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setDate(1, sqlDate);
            result = prepare.executeQuery();
            if (result.next()) {
                count = result.getInt("count");
            }
        } catch (Exception e) {
            showErrorAlert("Failed to count today's bookings: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    public void dashboardDisplayBookToday() {
        dashboardCountBookToday();
        dashboard_bookToday.setText(String.valueOf(count));
    }

    public void dashboardSumIncomeToday() {
        connect = database.connectDb();
        sumToday = 0;
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try {
            String sql = "SELECT COALESCE(SUM(total), 0) as total_sum FROM customer_receipt WHERE DATE(date) = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setDate(1, sqlDate);
            result = prepare.executeQuery();
            if (result.next()) {
                sumToday = result.getDouble("total_sum");
            }
        } catch (Exception e) {
            showErrorAlert("Failed to calculate today's income: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    public void dashboardDisplayIncomeToday() {
        dashboardSumIncomeToday();
        dashboard_todayIncome.setText("$" + String.format("%.2f", sumToday));
    }

    public void dashboardSumTotalIncome() {
        connect = database.connectDb();
        overall = 0;
        try {
            String sql = "SELECT COALESCE(SUM(total), 0) as total_sum FROM customer_receipt";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                overall = result.getDouble("total_sum");
            }
        } catch (Exception e) {
            showErrorAlert("Failed to calculate total income: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    public void dashboardTotalIncome() {
        dashboardSumTotalIncome();
        dashboard_totalIncome.setText("$" + String.format("%.2f", overall));
    }

    public void dashboardChart() {
        dashboard_areaChart.getData().clear();
        String sql = "SELECT DATE(date) as date, COALESCE(SUM(total), 0) as total FROM customer_receipt WHERE date >= DATE_SUB(CURDATE(), INTERVAL 8 DAY) GROUP BY DATE(date) ORDER BY date ASC";
        connect = database.connectDb();
        XYChart.Series<String, Number> chart = new XYChart.Series<>();
        chart.setName("Income");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        try {
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<Number> incomes = new ArrayList<>();
            for (int i = 7; i >= 0; i--) {
                java.sql.Date sqlDate = new java.sql.Date(new Date().getTime() - i * 24L * 60 * 60 * 1000);
                String dateStr = dateFormat.format(sqlDate);
                dates.add(dateStr);
                incomes.add(0.0);
            }
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                String dateStr = dateFormat.format(result.getDate("date"));
                double total = result.getDouble("total");
                int index = dates.indexOf(dateStr);
                if (index >= 0) {
                    incomes.set(index, total);
                }
            }
            for (int i = 0; i < dates.size(); i++) {
                chart.getData().add(new XYChart.Data<>(dates.get(i), incomes.get(i)));
            }
            dashboard_areaChart.getData().add(chart);
        } catch (Exception e) {
            showErrorAlert("Failed to load dashboard chart: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    public void weeklyRecordsChart(LocalDate startDate, LocalDate endDate) {
        records_weeklyChart.getData().clear();
        String bookingSql = "SELECT DATE(checkIn) as date, COUNT(*) as count FROM customer WHERE checkIn BETWEEN ? AND ? GROUP BY DATE(checkIn) ORDER BY date ASC";
        String incomeSql = "SELECT DATE(date) as date, SUM(total) as total FROM customer_receipt WHERE date BETWEEN ? AND ? GROUP BY DATE(date) ORDER BY date ASC";
        connect = database.connectDb();
        XYChart.Series<String, Number> bookingSeries = new XYChart.Series<>();
        bookingSeries.setName("Bookings");
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income ($)");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        try {
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<Number> bookings = new ArrayList<>();
            ArrayList<Number> incomes = new ArrayList<>();
            long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
            for (int i = 0; i < days; i++) {
                LocalDate date = startDate.plusDays(i);
                java.sql.Date sqlDate = java.sql.Date.valueOf(date);
                String dateStr = dateFormat.format(sqlDate);
                dates.add(dateStr);
                bookings.add(0);
                incomes.add(0.0);
            }
            prepare = connect.prepareStatement(bookingSql);
            prepare.setDate(1, java.sql.Date.valueOf(startDate));
            prepare.setDate(2, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();
            while (result.next()) {
                String dateStr = dateFormat.format(result.getDate("date"));
                int count = result.getInt("count");
                int index = dates.indexOf(dateStr);
                if (index >= 0) {
                    bookings.set(index, count);
                }
            }
            prepare = connect.prepareStatement(incomeSql);
            prepare.setDate(1, java.sql.Date.valueOf(startDate));
            prepare.setDate(2, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();
            while (result.next()) {
                String dateStr = dateFormat.format(result.getDate("date"));
                double total = result.getDouble("total");
                int index = dates.indexOf(dateStr);
                if (index >= 0) {
                    incomes.set(index, total);
                }
            }
            for (int i = 0; i < dates.size(); i++) {
                bookingSeries.getData().add(new XYChart.Data<>(dates.get(i), bookings.get(i)));
                incomeSeries.getData().add(new XYChart.Data<>(dates.get(i), incomes.get(i)));
            }
            if (records_showBookings.isSelected()) {
                records_weeklyChart.getData().add(bookingSeries);
            }
            if (records_showIncome.isSelected()) {
                records_weeklyChart.getData().add(incomeSeries);
            }
        } catch (Exception e) {
            showErrorAlert("Failed to load weekly records chart: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    public void monthlyRecordsChart(LocalDate startDate, LocalDate endDate) {
        records_monthlyChart.getData().clear();
        String bookingSql = "SELECT FLOOR((DATEDIFF(?, checkIn) DIV 7)) as week_num, COUNT(*) as count FROM customer WHERE checkIn BETWEEN ? AND ? GROUP BY week_num ORDER BY week_num DESC";
        String incomeSql = "SELECT FLOOR((DATEDIFF(?, date) DIV 7)) as week_num, SUM(total) as total FROM customer_receipt WHERE date BETWEEN ? AND ? GROUP BY week_num ORDER BY week_num DESC";
        connect = database.connectDb();
        XYChart.Series<String, Number> bookingSeries = new XYChart.Series<>();
        bookingSeries.setName("Bookings");
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income ($)");
        try {
            long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            int weeks = (int) Math.ceil(days / 7.0);
            weeks = Math.max(1, weeks);
            ArrayList<String> weekLabels = new ArrayList<>();
            ArrayList<Number> bookings = new ArrayList<>();
            ArrayList<Number> incomes = new ArrayList<>();
            for (int i = weeks - 1; i >= 0; i--) {
                weekLabels.add("Week " + (weeks - i));
                bookings.add(0);
                incomes.add(0.0);
            }
            prepare = connect.prepareStatement(bookingSql);
            prepare.setDate(1, java.sql.Date.valueOf(endDate));
            prepare.setDate(2, java.sql.Date.valueOf(startDate));
            prepare.setDate(3, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();
            while (result.next()) {
                int weekNum = result.getInt("week_num");
                int count = result.getInt("count");
                if (weekNum < weeks) {
                    bookings.set(weekNum, count);
                }
            }
            prepare = connect.prepareStatement(incomeSql);
            prepare.setDate(1, java.sql.Date.valueOf(endDate));
            prepare.setDate(2, java.sql.Date.valueOf(startDate));
            prepare.setDate(3, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();
            while (result.next()) {
                int weekNum = result.getInt("week_num");
                double total = result.getDouble("total");
                if (weekNum < weeks) {
                    incomes.set(weekNum, total);
                }
            }
            for (int i = 0; i < weekLabels.size(); i++) {
                bookingSeries.getData().add(new XYChart.Data<>(weekLabels.get(i), bookings.get(i)));
                incomeSeries.getData().add(new XYChart.Data<>(weekLabels.get(i), incomes.get(i)));
            }
            if (records_showBookings.isSelected()) {
                records_monthlyChart.getData().add(bookingSeries);
            }
            if (records_showIncome.isSelected()) {
                records_monthlyChart.getData().add(incomeSeries);
            }
        } catch (Exception e) {
            showErrorAlert("Failed to load monthly records chart: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    public ObservableList<RoomData> availableRoomsListData() {
        ObservableList<RoomData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM room";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                RoomData roomD = new RoomData(
                    result.getInt("roomNumber"),
                    result.getString("type"),
                    result.getString("status"),
                    result.getDouble("price")
                );
                listData.add(roomD);
            }
        } catch (Exception e) {
            showErrorAlert("Failed to load room data: " + e.getMessage());
        } finally {
            closeResources();
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
        if (num < 0 || roomD == null) {
            return;
        }
        available_roomNumber.setText(String.valueOf(roomD.getRoomNumber()));
        availableRoom_type.setValue(roomD.getRoomType());
        availableRoom_status.setValue(roomD.getStatus());
        availableRoom_price.setText(String.valueOf(roomD.getPrice()));
    }

    @FXML
    public void availableRoomsSearch() {
        if (roomDataList == null) {
            return;
        }
        FilteredList<RoomData> filter = new FilteredList<>(roomDataList, e -> true);
        availableRoom_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateRoomData -> {
                if (newValue == null || newValue.isEmpty()) {
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
                }
                return false;
            });
        });
        SortedList<RoomData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(availableRoom_tableView.comparatorProperty());
        availableRoom_tableView.setItems(sortList);
    }

    @FXML
    public void availableRoomAdd() {
        String sql = "INSERT INTO room (roomNumber, type, status, price) VALUES(?,?,?,?)";
        connect = database.connectDb();
        try {
            String roomNumber = available_roomNumber.getText();
            String type = availableRoom_type.getSelectionModel().getSelectedItem();
            String status = availableRoom_status.getSelectionModel().getSelectedItem();
            String price = availableRoom_price.getText();
            Alert alert;
            if (roomNumber.isEmpty() || type == null || status == null || price.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String check = "SELECT roomNumber FROM room WHERE roomNumber = ?";
                prepare = connect.prepareStatement(check);
                prepare.setString(1, roomNumber);
                result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Room #" + roomNumber + " already exists!");
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
                    alert.setContentText("Successfully added");
                    alert.showAndWait();
                    availableRoomsShowData();
                    availableRoomsClear();
                    availableRoomsSearch();
                }
            }
        } catch (Exception e) {
            showErrorAlert("Failed to add room: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    @FXML
    public void availableRoomsUpadte() {
        String type = availableRoom_type.getSelectionModel().getSelectedItem();
        String status = availableRoom_status.getSelectionModel().getSelectedItem();
        String price = availableRoom_price.getText();
        String roomNum = available_roomNumber.getText();
        String sql = "UPDATE room SET type = ?, status = ?, price = ? WHERE roomNumber = ?";
        connect = database.connectDb();
        try {
            Alert alert;
            if (type == null || status == null || price.isEmpty() || roomNum.isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the data first");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, type);
                prepare.setString(2, status);
                prepare.setString(3, price);
                prepare.setString(4, roomNum);
                prepare.executeUpdate();
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated");
                alert.showAndWait();
                availableRoomsShowData();
                availableRoomsClear();
                availableRoomsSearch();
            }
        } catch (Exception e) {
            showErrorAlert("Failed to update room: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    @FXML
    public void availableRoomsDelete() {
        String roomNum = available_roomNumber.getText();
        String sql = "DELETE FROM room WHERE roomNumber = ?";
        connect = database.connectDb();
        try {
            Alert alert;
            if (roomNum.isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a room first");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete Room #" + roomNum + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, roomNum);
                    prepare.executeUpdate();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();
                    availableRoomsShowData();
                    availableRoomsClear();
                    availableRoomsSearch();
                }
            }
        } catch (Exception e) {
            showErrorAlert("Failed to delete room: " + e.getMessage());
        } finally {
            closeResources();
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
            showErrorAlert("Failed to open check-in form: " + e.getMessage());
        }
    }

    @FXML
    public void availableRoomsRoomType() {
        ObservableList<String> list = FXCollections.observableArrayList(type);
        availableRoom_type.setItems(list);
    }

    @FXML
    public void availableRoomsStatus() {
        ObservableList<String> list = FXCollections.observableArrayList(status);
        availableRoom_status.setItems(list);
    }

    public ObservableList<customerData> customerListData() {
        ObservableList<customerData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                customerData custD = new customerData(
                    result.getInt("customer_id"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("phoneNumber"),
                    result.getDouble("total"),
                    result.getDate("checkIn"),
                    result.getDate("checkOut")
                );
                listData.add(custD);
            }
        } catch (Exception e) {
            showErrorAlert("Failed to load customer data: " + e.getMessage());
        } finally {
            closeResources();
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
        if (listCustomerData == null) {
            return;
        }
        FilteredList<customerData> filter = new FilteredList<>(listCustomerData, e -> true);
        customer_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateCustomer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                if (predicateCustomer.getCustomerNum().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getFirstName() != null && predicateCustomer.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getLastName() != null && predicateCustomer.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getPhoneNumber() != null && predicateCustomer.getPhoneNumber().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getTotal().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getCheckIn() != null && predicateCustomer.getCheckIn().toString().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getCheckOut() != null && predicateCustomer.getCheckOut().toString().toLowerCase().contains(searchKey)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<customerData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(customer_TableView.comparatorProperty());
        customer_TableView.setItems(sortList);
    }

    @FXML
    public void customerExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Customer Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(customer_TableView.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Customer #,First Name,Last Name,Phone #,Total Payment,Check-in,Check-out");
                ObservableList<customerData> items = customer_TableView.getItems();
                for (customerData customer : items) {
                    writer.println(String.format("%d,%s,%s,%s,%.2f,%s,%s",
                        customer.getCustomerNum(),
                        csvEscape(customer.getFirstName()),
                        csvEscape(customer.getLastName()),
                        csvEscape(customer.getPhoneNumber()),
                        customer.getTotal(),
                        customer.getCheckIn() != null ? customer.getCheckIn().toString() : "",
                        customer.getCheckOut() != null ? customer.getCheckOut().toString() : ""));
                }
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Export Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer data exported to " + file.getAbsolutePath());
                alert.showAndWait();
            } catch (Exception e) {
                showErrorAlert("Failed to export CSV: " + e.getMessage());
            }
        }
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    @FXML
    public void customerViewDetails() {
        customerData selected = customer_TableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer from the table.");
            alert.showAndWait();
            return;
        }
        Stage dialog = new Stage();
        dialog.setTitle("Customer Details");
        dialog.initStyle(DECORATED);
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        Label idLabel = new Label("Customer #: " + selected.getCustomerNum());
        Label nameLabel = new Label("Name: " +
            (selected.getFirstName() != null ? selected.getFirstName() : "") + " " +
            (selected.getLastName() != null ? selected.getLastName() : ""));
        Label phoneLabel = new Label("Phone #: " + (selected.getPhoneNumber() != null ? selected.getPhoneNumber() : ""));
        Label paymentLabel = new Label("Total Payment: $" + String.format("%.2f", selected.getTotal()));
        Label checkInLabel = new Label("Check-in: " + (selected.getCheckIn() != null ? selected.getCheckIn().toString() : ""));
        Label checkOutLabel = new Label("Check-out: " + (selected.getCheckOut() != null ? selected.getCheckOut().toString() : ""));
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());
        vbox.getChildren().addAll(idLabel, nameLabel, phoneLabel, paymentLabel, checkInLabel, checkOutLabel, closeButton);
        Scene scene = new Scene(vbox, 300, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public ObservableList<RecordData> recordsListData(LocalDate startDate, LocalDate endDate) {
        ObservableList<RecordData> listData = FXCollections.observableArrayList();
        connect = database.connectDb();
        try {
            String bookingSql = "SELECT COUNT(*) as bookings FROM customer WHERE checkIn BETWEEN ? AND ?";
            prepare = connect.prepareStatement(bookingSql);
            prepare.setDate(1, java.sql.Date.valueOf(startDate));
            prepare.setDate(2, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();
            int bookings = 0;
            if (result.next()) {
                bookings = result.getInt("bookings");
            }
            String incomeSql = "SELECT COALESCE(SUM(total), 0) as income FROM customer_receipt WHERE date BETWEEN ? AND ?";
            prepare = connect.prepareStatement(incomeSql);
            prepare.setDate(1, java.sql.Date.valueOf(startDate));
            prepare.setDate(2, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();
            double income = 0;
            if (result.next()) {
                income = result.getDouble("income");
            }
            String period = startDate.equals(endDate) ? startDate.toString() :
                startDate.toString() + " to " + endDate.toString();
            listData.add(new RecordData(period, bookings, income));
        } catch (Exception e) {
            showErrorAlert("Failed to load records data: " + e.getMessage());
        } finally {
            closeResources();
        }
        return listData;
    }

    private ObservableList<RecordData> recordDataList;

    public void recordsShowData() {
        LocalDate startDate = records_startDate.getValue();
        LocalDate endDate = records_endDate.getValue();
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().minusDays(30);
            endDate = LocalDate.now();
            records_startDate.setValue(startDate);
            records_endDate.setValue(endDate);
        }
        recordDataList = recordsListData(startDate, endDate);
        records_Period.setCellValueFactory(new PropertyValueFactory<>("period"));
        records_Bookings.setCellValueFactory(new PropertyValueFactory<>("bookings"));
        records_Income.setCellValueFactory(new PropertyValueFactory<>("income"));
        records_tableView.setItems(recordDataList);
        weeklyRecordsChart(startDate, endDate);
        monthlyRecordsChart(startDate, endDate);
    }

    @FXML
    public void recordsApplyFilter() {
        LocalDate startDate = records_startDate.getValue();
        LocalDate endDate = records_endDate.getValue();
        if (startDate == null || endDate == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Date Range");
            alert.setHeaderText(null);
            alert.setContentText("Please select both start and end dates.");
            alert.showAndWait();
            return;
        }
        if (startDate.isAfter(endDate)) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Date Range");
            alert.setHeaderText(null);
            alert.setContentText("Start date cannot be after end date.");
            alert.showAndWait();
            return;
        }
        recordsShowData();
    }

    @FXML
    public void toggleChartSeries() {
        LocalDate startDate = records_startDate.getValue();
        LocalDate endDate = records_endDate.getValue();
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().minusDays(30);
            endDate = LocalDate.now();
            records_startDate.setValue(startDate);
            records_endDate.setValue(endDate);
        }
        weeklyRecordsChart(startDate, endDate);
        monthlyRecordsChart(startDate, endDate);
    }

    @FXML
    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            Dashboard_form.setVisible(true);
            availableRoom_RoomFrom.setVisible(false);
            customer_From.setVisible(false);
            records_form.setVisible(false);
            dashboard_btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            aroom_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            customer_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            records_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            dashboardDisplayIncomeToday();
            dashboardDisplayBookToday();
            dashboardTotalIncome();
            dashboardChart();
        } else if (event.getSource() == aroom_btn) {
            Dashboard_form.setVisible(false);
            availableRoom_RoomFrom.setVisible(true);
            customer_From.setVisible(false);
            records_form.setVisible(false);
            dashboard_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            aroom_btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            customer_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            records_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            availableRoomsShowData();
            availableRoomsSearch();
        } else if (event.getSource() == customer_btn) {
            Dashboard_form.setVisible(false);
            availableRoom_RoomFrom.setVisible(false);
            customer_From.setVisible(true);
            records_form.setVisible(false);
            dashboard_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            aroom_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            customer_btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            records_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            customerShowData();
            customerSearch();
        } else if (event.getSource() == records_btn) {
            Dashboard_form.setVisible(false);
            availableRoom_RoomFrom.setVisible(false);
            customer_From.setVisible(false);
            records_form.setVisible(true);
            dashboard_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            aroom_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            customer_btn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            records_btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            recordsShowData();
        }
    }

    @FXML
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void close() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    public void logout() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent() && option.get() == ButtonType.OK) {
                logout_btn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(DECORATED);
                stage.show();
            }
        } catch (Exception e) {
            showErrorAlert("Failed to logout: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeResources() {
        try {
            if (result != null) result.close();
            if (prepare != null) prepare.close();
            if (connect != null) connect.close();
        } catch (SQLException e) {
            showErrorAlert("Failed to close database resources: " + e.getMessage());
        }
    }

}