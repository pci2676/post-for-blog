package com.javabom.nested;

import java.io.IOException;

public class NonStaticInnerMain {
    public static void main(String[] args) throws IOException {
        Outer.NonStaticInner nonStaticInner = getNonStaticInner();

        System.gc();
        System.out.println("GC 동작 완료");

        System.in.read();

        System.out.println(nonStaticInner); // VisualVm HeapDump 시점
    }

    private static Outer.NonStaticInner getNonStaticInner() {
        return new Outer().new NonStaticInner();
    }
}
