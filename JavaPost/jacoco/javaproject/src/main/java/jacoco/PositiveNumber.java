package jacoco;

public final class PositiveNumber {
    private final long value;

    public PositiveNumber(long value) {
        validate(value);
        this.value = value;
    }

    private void validate(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%d 는 양수가 아닙니다.", value));
        }
    }

    public PositiveNumber add(PositiveNumber positiveNumber) {
        return new PositiveNumber(value + positiveNumber.value);
    }

}
