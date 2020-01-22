package chap2.item3.serializable;

import java.io.Serializable;

public class Singleton implements Serializable {
    private static final Singleton INSTANCE = new Singleton();

    // 역직렬화시 새로운 인스턴스 생성 요인 차단방법 1
    // transient 예약어를 추가하여 직렬화를 차단하여 역직렬화시 새로운 인스턴스가 생성되지 못하도록 한다.
//     transient private static final Singleton INSTANCE = new Singleton();

    public static Singleton getInstance() {
        return INSTANCE;
    }

    private Person person;

    private Singleton() {
        person = new Person();
    }

    // 역직렬화시 새로운 인스턴스 생성 요인 차단방법 2
    // 역직렬화시 readResolve 를 재정의하여 기존 인스턴스를 반환하여 싱글턴을 보장하도록 한다.
    private Object readResolve() {
        return INSTANCE;
    }
}

class Person implements Serializable {
}