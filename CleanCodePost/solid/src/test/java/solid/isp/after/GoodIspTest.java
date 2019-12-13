package solid.isp.after;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import solid.isp.after.copy.CopyDevice;
import solid.isp.after.copy.CopyMachine;
import solid.isp.after.fax.FaxDevice;
import solid.isp.after.fax.FaxMachine;
import solid.isp.after.printer.PrinterDevice;
import solid.isp.after.printer.PrinterMachine;

class GoodIspTest {

    @DisplayName("여러개의 기능을 원한다면 여러개의 인터페이스를 구현하자")
    @Test
    void smart() {
        SmartMachine smartMachine = new SmartMachine();
        smartMachine.copy();
        smartMachine.print();
        smartMachine.fax();
    }

    @DisplayName("하나의 기능만을 필요로 한다면 하나의 인터페이스만 구현하도록 하자")
    @Test
    void singleInterface() {
        PrinterDevice printer = new PrinterMachine();
        FaxDevice faxMachine = new FaxMachine();
        CopyDevice copyMachine = new CopyMachine();

        printer.print();
        faxMachine.fax();
        copyMachine.copy();
    }
}