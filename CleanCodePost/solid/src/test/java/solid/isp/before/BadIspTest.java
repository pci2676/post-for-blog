package solid.isp.before;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class BadIspTest {

    @DisplayName("범용 인터페이스의 기능이 모두 구현된 경우")
    @Test
    void allInOne() {
        AllInOneDevice allInOneDevice = new SmartMachine();

        allInOneDevice.print();
        allInOneDevice.copy();
        allInOneDevice.fax();
    }

    @DisplayName("프린터만 사용하고 싶은경우, copy와 fax의 기능이 불필요하게 노출된다.")
    @Test
    void unsupported() {
        AllInOneDevice printer = new PrinterMachine();

        printer.print();
        assertAll(
                () -> assertThatThrownBy(() -> printer.copy())
                        .isInstanceOf(UnsupportedOperationException.class),
                () -> assertThatThrownBy(() -> printer.fax())
                        .isInstanceOf(UnsupportedOperationException.class)
        );
    }
}