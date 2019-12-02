package solid.srp.production.before;

/**
 * ProductionUpdateService 의 역할 : 상품을 업데이트 한다.
 * <p>
 * 상품의 정보를 업데이트하는 역할로써의 책임 :
 * 1. 상품의 정보를 업데이트 하는 책임을 호출한다. update
 * 2. 업데이트할 정보의 유효성을 판단한다. validatePrice
 */
public class ProductionUpdateService {

    public void update(Production production, int price) {
        //validate price
        validatePrice(price);

        //update
        production.updatePrice(price);
    }

    private void validatePrice(int price) {
        if (price < 1000) {
            throw new IllegalArgumentException("최소가격은 1000원 이상입니다.");
        }
    }

}
