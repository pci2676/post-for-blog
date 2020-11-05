package com.javabom.nested;

public class Outer {
    private int out;

    public static class StaticInner {
        private int in;
    }

    public class NonStaticInner {
        private int in;
    }
}
