package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import util.DBConnection;

import java.sql.*;

public class ProductDAO {

    public void addProduct(Product product) {
        String sql = "INSERT INTO products(name, quantity, price) VALUES(?,?,?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getQuantity());
            stmt.setDouble(3, product.getPrice());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Product> getProducts() {
        ObservableList<Product> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public void updateQuantity(String name, int quantity) {
        String sql = "UPDATE products SET quantity=? WHERE name=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setString(2, name);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }public void deleteProduct(String name) {
        String sql = "DELETE FROM products WHERE name=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}