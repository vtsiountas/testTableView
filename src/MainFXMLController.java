import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFXMLController {

    @FXML
    private Button viewProductsButton;


    public void viewProductsScene() throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("TableView.fxml"));
        Stage primaryStage = (Stage) viewProductsButton.getScene().getWindow();


        primaryStage.setScene(new Scene(tableViewParent, 800, 800));
    }
}
