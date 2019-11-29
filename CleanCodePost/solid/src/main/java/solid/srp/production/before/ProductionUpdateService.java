package solid.srp.production.before;

/**
 * ProductionUpdateService 의 역할 : 가격을 업데이트 한다.
 * <p>
 * 가격을 업데이트하는 역할로써의 책임 :
 * 1. 가격을 업데이트한다. updatePrice
 * 2. 업데이트할 가격의 유효성을 판단한다. validatePrice
 */
public class ProductionUpdateService {

    public void updatePrice(Production production, int price) {
        //validate price
        validatePrice(price);

        //update price
        production.updatePrice(price);
    }

    private void validatePrice(int price) {
        if (price < 1000) {
            throw new IllegalArgumentException("최소가격은 1000원 이상입니다.");
        }
    }

}
