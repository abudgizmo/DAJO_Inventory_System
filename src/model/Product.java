package model;

public class Product extends Item {

    private int quantity;

    public Product(String name, int quantity, double price) {
        super(name, price);
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
