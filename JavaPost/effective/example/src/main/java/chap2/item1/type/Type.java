package chap2.item1.type;

public interface Type {
    static AType getAType() {
        return new AType();
    }

    static BType getBType() {
        return new BType();
    }
}

class AType implements Type {
}

class BType implements Type {
}
