package chap2.item2;

public class JavaBeans {
    private String name;
    private int number;
    private long money;
    private double temperature;

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}

class Beans {
    public JavaBeans create() {
        JavaBeans javaBeans = new JavaBeans();

        javaBeans.setName("name");
        javaBeans.setNumber(0);
        javaBeans.setMoney(0);
        javaBeans.setTemperature(0);

        return javaBeans;
    }
}
