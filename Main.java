import dao.ProductDAO;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;

public class Main extends Application {

    ProductDAO dao = new ProductDAO();
    TableView<Product> table = new TableView<>();


    boolean isDarkMode = false;

    @Override
    public void start(Stage stage) {

        // 🔹 Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        // 🔹 Buttons
        Button addBtn = new Button("Add Product");
        Button updateBtn = new Button("Update Stock");
        Button deleteBtn = new Button("Delete Product");
        Button toggleThemeBtn = new Button("Dark Mode");

        Label message = new Label();

        // 🔹 Assign IDs (for CSS)
        addBtn.setId("addBtn");
        updateBtn.setId("updateBtn");
        deleteBtn.setId("deleteBtn");

        // 🔹 Table columns
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()).asObject());

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        table.getColumns().addAll(nameCol, qtyCol, priceCol);

        // 🔴 Highlight zero quantity
        table.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.getQuantity() == 0) {
                    setStyle("-fx-background-color: #ff4d4d;");
                } else {
                    setStyle("");
                }
            }
        });

        // 🔹 Load data
        loadTable();

        // 🔹 Add Product
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());

                if (name.isEmpty()) {
                    message.setText("Name cannot be empty!");
                    return;
                }

                dao.addProduct(new Product(name, qty, price));
                loadTable();
                message.setText("Product added!");

            } catch (NumberFormatException ex) {
                message.setText("Enter valid numbers!");
            }
        });

        // 🔹 Update Stock
        updateBtn.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                try {
                    int newQty = Integer.parseInt(qtyField.getText());
                    dao.updateQuantity(selected.getName(), newQty);
                    loadTable();
                    message.setText("Stock updated!");
                } catch (Exception ex) {
                    message.setText("Invalid quantity!");
                }
            } else {
                message.setText("Select a product first!");
            }
        });

        // 🔹 Delete Product
        deleteBtn.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                dao.deleteProduct(selected.getName());
                loadTable();
                message.setText("Product deleted!");
            } else {
                message.setText("Select a product!");
            }
        });

        // 🔹 Layout
        VBox root = new VBox(10,
                toggleThemeBtn,
                nameField,
                qtyField,
                priceField,
                addBtn,
                updateBtn,
                deleteBtn,
                message,
                table
        );
        root.setPadding(new Insets(10));

        // 🔹 Scene
        Scene scene = new Scene(root, 500, 500);

        // ✅ Load default LIGHT mode
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // 🌙 Toggle Dark Mode
        toggleThemeBtn.setOnAction(e -> {
            scene.getStylesheets().clear();

            if (!isDarkMode) {
                scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
                toggleThemeBtn.setText("Light Mode");
                isDarkMode = true;
            } else {
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                toggleThemeBtn.setText("Dark Mode");
                isDarkMode = false;
            }
        });

        // 🔹 Stage
        stage.setScene(scene);
        stage.setTitle("Inventory System");
        stage.show();
    }

    private void loadTable() {
        ObservableList<Product> list = dao.getProducts();
        table.setItems(list);
    }

    public static void main(String[] args) {
        launch();
    }
}