package solid.srp.production.after;

/**
 * Production 의 역할 : 상품의 역할
 * <p>
 * 상품 역할로써의 책임 :
 * 1. 가격을 업데이트 한다.
 * 2. 업데이트할 가격의 유효성을 판단한다. validatePrice
 */
public class Production {

    private static final int MINIMUM_PRICE = 1000;

    private String name;
    private int price;

    public Production(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void updatePrice(int price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(int price) {
        if (price < MINIMUM_PRICE) {
            throw new IllegalArgumentException(String.format("최소가격은 %d원 이상입니다.", MINIMUM_PRICE));
        }
    }
}
