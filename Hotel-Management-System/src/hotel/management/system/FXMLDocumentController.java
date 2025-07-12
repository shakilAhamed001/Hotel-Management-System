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
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField user1;
    @FXML
    private PasswordField pass1;
    @FXML
    private Button login1;
    @FXML
    private Button cl;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if needed
    }

    @FXML
    private void Login(ActionEvent event) {
        try {
            login(event);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Login failed: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void login(ActionEvent event) throws SQLException, IOException {
        String username = user1.getText();
        String password = pass1.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all blank fields", AlertType.ERROR);
            return;
        }

        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username);
            prepare.setString(2, password);
            result = prepare.executeQuery();

            if (result.next()) {
                showAlert("Success", "Successfully Logged In", AlertType.INFORMATION);

                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);

                // Allow window dragging
                root.setOnMousePressed((MouseEvent e) -> {
                    x = e.getSceneX();
                    y = e.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent e) -> {
                    stage.setX(e.getScreenX() - x);
                    stage.setY(e.getScreenY() - y);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setTitle("Nilima Seaside - Dashboard");
                stage.show();

                // Close current login window
                ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
            } else {
                showAlert("Login Failed", "Wrong username or password.", AlertType.ERROR);
            }
        } catch (SQLException e) {
            throw new SQLException("Database error: " + e.getMessage());
        } finally {
            // Clean up resources
            if (result != null) result.close();
            if (prepare != null) prepare.close();
            if (connect != null) connect.close();
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void CLOSE(ActionEvent event) {
        Stage stage = (Stage) cl.getScene().getWindow();
        stage.close();
    }
}
