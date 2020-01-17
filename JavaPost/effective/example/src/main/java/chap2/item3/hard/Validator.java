package chap2.item3.hard;

public class Validator {
    private static final Validator INSTANCE = new Validator();

    public static final Validator getInstance() {
        return INSTANCE;
    }

    private int bound;

    private Validator() {
        this.bound = 10;
    }

    public void validate(int target) {
        if (target > bound) {
            throw new IllegalArgumentException();
        }
    }

}

class Service {

    private final Validator validator = Validator.getInstance();

    public void validate(int target) {
        validator.validate(target); // validator 가 제대로 작동하는지 테스트를 하려는데 bound 값을 바꿔서 테스트하고 싶다면?
    }
}
