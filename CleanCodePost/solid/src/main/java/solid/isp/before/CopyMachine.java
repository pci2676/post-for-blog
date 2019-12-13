package solid.isp.before;

public class CopyMachine implements AllInOneDevice {
    @Override
    public void print() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copy() {
        System.out.println("copy");
    }

    @Override
    public void fax() {
        throw new UnsupportedOperationException();
    }
}
