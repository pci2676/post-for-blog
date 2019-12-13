package solid.isp.after.fax;

public class FaxMachine implements FaxDevice {
    @Override
    public void fax() {
        System.out.println("fax");
    }
}
