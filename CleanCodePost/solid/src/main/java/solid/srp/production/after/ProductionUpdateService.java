package solid.srp.production.after;

/**
 * ProductionUpdateService 의 역할 : 상품의 정보를 업데이트 한다.
 * <p>
 * 상품의 정보를 업데이트하는 역할로써의 책임 :
 * 1. 상품의 정보를 업데이트 하는 책임을 호출한다. update
 */
public class ProductionUpdateService {

    public void update(Production production, int price) {
        //update
        production.updatePrice(price);
    }

}
