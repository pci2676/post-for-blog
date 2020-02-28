package jacoco;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PositiveNumberJunit5Test {

    @Test
    void notPositiveValueThrowException() {
        long notPositive = 0;

        assertThatThrownBy(() -> new PositiveNumber(notPositive))
                .isInstanceOf(IllegalArgumentException.class);
    }
}