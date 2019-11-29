package solid.srp.production.before;

/**
 * Production 의 역할 : 상품의 정보를 가지고 있다.
 * <p>
 * 상품 역할로써의 책임 :
 * 1. 가격을 업데이트 한다.
 */
public class Production {

    private String name;
    private int price;

    public Production(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void updatePrice(int price) {
        this.price = price;
    }
}
