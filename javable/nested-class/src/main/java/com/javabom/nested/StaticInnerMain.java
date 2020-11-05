package com.javabom.nested;

import java.io.IOException;

public class StaticInnerMain {
    public static void main(String[] args) throws IOException {
        Outer.StaticInner staticInner = getStaticInner();

        System.gc();
        System.out.println("GC 동작 완료"); // VisualVm HeapDump 시점

        System.in.read();

        System.out.println(staticInner);
    }

    private static Outer.StaticInner getStaticInner() {
        return new Outer.StaticInner();
    }
}
