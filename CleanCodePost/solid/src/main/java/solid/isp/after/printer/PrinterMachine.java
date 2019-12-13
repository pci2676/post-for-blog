package solid.isp.after.printer;

public class PrinterMachine implements PrinterDevice {
    @Override
    public void print() {
        System.out.println("print");
    }
}
