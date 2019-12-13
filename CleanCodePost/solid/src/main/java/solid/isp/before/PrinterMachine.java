package solid.isp.before;

public class PrinterMachine implements AllInOneDevice {
    @Override
    public void print() {
        System.out.println("print");
    }

    @Override
    public void copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fax() {
        throw new UnsupportedOperationException();
    }
}
