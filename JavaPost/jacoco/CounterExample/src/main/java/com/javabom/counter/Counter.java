package com.javabom.counter;

public class Counter {

    private String counter;

    public Counter(String counter) {
        this.counter = counter;
    }

    public int getCounter() {
        if ("INSTRUCTION".equals(counter)) {
            return 0;
        } else if ("LINE".equals(counter)) {
            return 1;
        } else {
            return -1;
        }
    }
}
