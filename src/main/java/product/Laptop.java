package product;

public class Laptop extends Product {
    private int speed;
    private int hd;
    private int ram;

    public Laptop(String model, String maker, int price, int speed, int hd, int ram) {
        super(model, maker, price);
        this.speed = speed;
        this.hd = hd;
        this.ram = ram;
    }

    @Override
    public String toString() {
        String out = "Laptop: #%-3d %-10s %-10s %-5d %-5d %-5d";
        return String.format(out, this.getId(), this.getModel(), this.getMaker(), this.speed, this.hd, this.ram);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHd() {
        return hd;
    }

    public void setHd(int hd) {
        this.hd = hd;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }
}
