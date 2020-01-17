package chap2.item3.soft;

public interface Validator {
    void validate(int target);
}

class RealValidator implements Validator {
    private static final Validator INSTANCE = new RealValidator();

    private int bound = 10;

    private RealValidator() {
    }

    @Override
    public void validate(int target) {
        if (target > bound) {
            throw new IllegalArgumentException();
        }
    }

}

class MockValidator implements Validator {
    private static final Validator INSTANCE = new MockValidator();

    private int bound;

    private MockValidator() {
    }

    // 테스트를 위한 코드, 이러한 코드는 production 코드에 노출되어선 안되기 때문에 mock 클래스에만 존재한다.
    public void changeMockBound(int mockBound) {
        this.bound = mockBound;
    }

    @Override
    public void validate(int target) {
        if (target > bound) {
            throw new IllegalArgumentException();
        }
    }
}
