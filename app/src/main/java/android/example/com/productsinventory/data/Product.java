package android.example.com.productsinventory.data;

public class Product {

    private int id;
    private String name;
    private double price;
    private int quantity;
    private String supplier;
    private byte[] image;

    public Product() {

    }

    public Product(String name, double price, int quantity, String supplier, byte[] image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        this.image = image;
    }

    public Product(int id, String name, double price, int quantity, String supplier, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        this.image = image;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public byte[] getImage() {
        return image;
    }
}