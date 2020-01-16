package chap2.item2;

public class Basic {
    private String name;
    private double height;
    private double weight;
    private int age;

    public static class Builder {
        private final String name; // final 예약어를 붙이게 된다면 필수매개변수가 된다.
        private double height;
        private double weight;
        private int age;

        public Builder(String name) {
            this.name = name;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Basic build() {
            return new Basic(this);
        }
    }

    private Basic(Builder builder) {
        this.name = builder.name;
        this.height = builder.height;
        this.weight = builder.weight;
        this.age = builder.age;
    }
}
