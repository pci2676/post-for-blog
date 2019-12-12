package solid.isp.after;

import solid.isp.after.copy.CopyDevice;
import solid.isp.after.fax.FaxDevice;
import solid.isp.after.printer.PrinterDevice;

public class SmartMachine implements PrinterDevice, CopyDevice, FaxDevice {
    @Override
    public void print() {
        System.out.println("print");
    }

    @Override
    public void copy() {
        System.out.println("copy");
    }

    @Override
    public void fax() {
        System.out.println("fax");
    }
}
