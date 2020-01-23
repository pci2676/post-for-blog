package chap2.item7;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemoryLeak {

    public static void main(String[] args) {
        Map<Foo, String> map = new HashMap<>();

        Foo key = new Foo();
        map.put(key, "1");

        key = null;
        System.gc();

        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}

class Foo {
}
