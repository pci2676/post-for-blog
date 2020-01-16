package chap2.item2;

import java.util.EnumSet;
import java.util.Set;

public abstract class Hamburger {
    public enum Patty {PORK, BEEF, CHICKEN, FISH;}

    private final Set<Patty> patties;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Patty> patties = EnumSet.noneOf(Patty.class);

        public T addPatty(Patty patty) {
            patties.add(patty);
            return self();
        }

        abstract Hamburger build();

        protected abstract T self(); // 자식 클래스에서 형변환 없이 메서드 연쇄를 지원한다.
    }

    Hamburger(Builder<?> builder) {
        patties = builder.patties.clone();
    }
}

class McDonaldsHamBurger extends Hamburger {
    private final boolean sauceInside;

    public static class Builder extends Hamburger.Builder {
        private final boolean sauceInside;

        private Builder(boolean sauceInside) {
            this.sauceInside = sauceInside;
        }

        @Override
        public McDonaldsHamBurger build() { // 구체 클래스를 반환하지 않고 구체의 하위 클래스를 반환함으로써 형변환에 신경쓰지 않아도 된다. 이를 공변 변환 타이핑이라 한다.
            return new McDonaldsHamBurger(this);
        }

        @Override
        protected Builder self() {
            return this; // this 를 반환해야 메서드 연쇄가 가능해진다.
        }
    }

    public static Builder builder(boolean sauceInside){
        return new Builder(sauceInside);
    }

    McDonaldsHamBurger(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }
}

class HamburgerStore {
    public Hamburger make(){

        Hamburger hamburger = McDonaldsHamBurger.builder(true)
                .addPatty(Hamburger.Patty.BEEF)
                .addPatty(Hamburger.Patty.CHICKEN)
                .build();

        return hamburger;
    }
}

