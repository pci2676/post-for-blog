package jacoco;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class PositiveNumberJunit5Test {

    @DisplayName("양수 생성")
    @Test
    void createTest() {
        long positive = 1;
        PositiveNumber positiveNumber = new PositiveNumber(positive);

        assertThat(positiveNumber).isEqualTo(new PositiveNumber(1));
    }

    @DisplayName("양수 덧셈")
    @Test
    void addTest() {
        //given
        PositiveNumber result = new PositiveNumber(2);

        PositiveNumber positiveNumber1 = new PositiveNumber(1);
        PositiveNumber positiveNumber2 = new PositiveNumber(1);

        //when
        PositiveNumber expect = positiveNumber1.add(positiveNumber2);


        //then
        assertThat(expect).isEqualTo(result);
    }

    @Test
    void reflectionTest() {
        Field[] declaredFields = PositiveNumber.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.isSynthetic()) {
                System.out.println(field + " jacoco가 넣은 필드");
            } else {
                System.out.println(field + " 기존 필드");
            }
        }

        assertThat(declaredFields).hasSize(2);
    }
}