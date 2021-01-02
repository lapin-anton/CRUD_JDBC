package product;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private int id;
    private String model;
    private String maker;
    private int price;

    public Product(int id, String model, String maker, int price) {
        this.id = id;
        this.model = model;
        this.maker = maker;
        this.price = price;
    }

    public int getId() {
        return id;
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
}
