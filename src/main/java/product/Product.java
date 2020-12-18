package product;

public abstract class Product {
    private static int counter = 0;
    private int id;
    private String model;
    private String maker;
    private int price;

    public Product(String model, String maker, int price) {
        this.id = ++counter;
        this.model = model;
        this.maker = maker;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
