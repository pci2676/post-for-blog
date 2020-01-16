package chap2.item2;

public class Gradation {
    private String name;
    private int number;
    private long money;
    private double temperature;

    public Gradation(String name, int number, long money, double temperature) {
        this.name = name;
        this.number = number;
        this.money = money;
        this.temperature = temperature;
    }

    public Gradation(String name, int number, long money) {
        this(name, number, money, 0);
    }

    public Gradation(String name, int number) {
        this(name, number, 0, 0);
    }

    public Gradation(String name) {
        this(name, 0, 0, 0);
    }

    public Gradation() {
        this("", 0, 0, 0);
    }
}
