/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package hotel.management.system;

import java.sql.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author PC
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField user1;
    @FXML
    private PasswordField pass1;
    @FXML
    private Button login1;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

     @FXML
    private void Login(ActionEvent event) {
        try {
            login(event); // Call database login method
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Login failed: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void login(ActionEvent event) throws SQLException, IOException {
        String username = user1.getText();
        String password = pass1.getText();

        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username);
            prepare.setString(2, password);

            result = prepare.executeQuery();
            Alert alert;

            if (result.next()) {
                // Successful login
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Logged In");
                alert.showAndWait();

                // Open Dashboard window
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Nilima Seaside - Dashboard");
                stage.show();

                // Close login window
                ((Stage)((Button)event.getSource()).getScene().getWindow()).close();

            } else {
                // Wrong credentials
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username or password.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            throw new SQLException("Database error: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
  


