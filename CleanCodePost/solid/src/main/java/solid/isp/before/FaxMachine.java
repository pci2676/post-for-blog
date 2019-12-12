package solid.isp.before;

public class FaxMachine implements AllInOneDevice {
    @Override
    public void print() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fax() {
        System.out.println("fax");
    }
}
