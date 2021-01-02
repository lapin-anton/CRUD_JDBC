package product;

public class PC extends Product {
    private int speed;
    private int hd;
    private int ram;
    private String cd;

    public PC(int id, String model, String maker, int price, int speed, int hd, int ram, String cd) {
        super(id, model, maker, price);
        this.speed = speed;
        this.hd = hd;
        this.ram = ram;
        this.cd = cd;
    }

    @Override
    public String toString() {
        String out = "PC: #%-3d %-10s %-10s %-5d %-5d %-5d %-5s";
        return String.format(out, this.getId(), this.getModel(), this.getMaker(), this.speed, this.hd, this.ram, this.cd);
    }

    public int getSpeed() {
        return speed;
    }

    public int getHd() {
        return hd;
    }

    public int getRam() {
        return ram;
    }

    public String getCd() {
        return cd;
    }
}
