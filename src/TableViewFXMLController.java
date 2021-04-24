import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.ToggleSwitch;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableViewFXMLController implements Initializable {

    @FXML
    AnchorPane root;
    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Integer, Product> idColumn;
    @FXML
    private TableColumn<String , Product> nameColumn;
    @FXML
    private TableColumn<Double, Product> priceColumn;
    @FXML
    private TableColumn<String, Product> categoryColumn;
    @FXML
    private TextField seacrhTextField;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private ToggleSwitch toggleSwitch;


    private ObservableList<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        toggleSwitch.selectedProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1) {
//                    System.out.println("ON");
                    root.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
                } else {
//                    System.out.println("OFF");
                    root.getStylesheets().clear();
                }
            }
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        products = getProducts();
//        tableView.setItems(products);

        tableView.setOnMouseClicked( event -> {
            if( event.getClickCount() == 2 ) {
                System.out.println( tableView.getSelectionModel().getSelectedItem());
            }});

        choiceBox.getItems().addAll("All", "ID", "Name", "Price", "Category");
        choiceBox.setValue("All");


        FilteredList<Product> filteredProducts = new FilteredList<>(products, b->true);

        seacrhTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredProducts.setPredicate(product -> {


                    // If filter text is empty, display all products.

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    switch (choiceBox.getValue()){
                    case "ID":
                        return String.valueOf(product.getId()).startsWith(newValue);
                    case "Name":
                        return product.getName().toLowerCase().contains(lowerCaseFilter);
                    case "Price":
                        return String.valueOf(product.getPrice()).startsWith(newValue);
                    case "Category":
                        return product.getCategory().toLowerCase().contains(lowerCaseFilter);
                    default:
                        if (String.valueOf(product.getId()).startsWith(newValue)) return true;
                        else if (product.getName().toLowerCase().contains(lowerCaseFilter)) return true;
                        else if (String.valueOf(product.getPrice()).startsWith(newValue)) return true;
                        else return product.getCategory().toLowerCase().contains(lowerCaseFilter);
                }

        }));

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedProducts.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedProducts);


    }

    private ObservableList<Product> getProducts() {
        ArrayList<Product> productArrayList = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("products.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            productArrayList = (ArrayList<Product>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(productArrayList);
    }

}
