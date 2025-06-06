/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package hotel.management.system;

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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Login(ActionEvent event) {
         String username = user1.getText();
        String password = pass1.getText();

       
        if (username.equals("user") && password.equals("pass")) { 
            openNewWindow(); // This method is called to open the new window
            // Close the current login window if desired
            ((Stage)(((javafx.scene.control.Button)event.getSource()).getScene().getWindow())).close();
        } else {
            // Failed login
            showAlert("Login Failed", "Invalid username or password.", Alert.AlertType.ERROR);
        }
    }

    private void openNewWindow() {
        try {
            // Load the FXML for the new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml")); // Make sure this path is correct
            Parent root = loader.load();

            // Create a new stage (window)
            Stage stage = new Stage();
            stage.setTitle("Nilima Seaside - Dashboard"); // Set the title for the new window
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the dashboard window.", Alert.AlertType.ERROR);
        }
        
        
    }

    private void showAlert(String error, String could_not_load_the_dashboard_window, Alert.AlertType alertType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    
}
