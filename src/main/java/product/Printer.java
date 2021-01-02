package product;

public class Printer extends Product {
    private String type;
    private String color;

    public Printer(int id, String model, String maker, int price, String type, String color) {
        super(id, model, maker, price);
        this.type = type;
        this.color = color;
    }

    @Override
    public String toString() {
        String out = "Printer: #%-3d %-10s %-10s %-10s %-5s";
        return String.format(out, this.getId(), this.getModel(), this.getMaker(), this.type, this.color);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }
}
