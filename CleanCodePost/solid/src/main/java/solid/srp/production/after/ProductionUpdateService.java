package solid.srp.production.after;

/**
 * ProductionUpdateService 의 역할 : 가격을 업데이트 한다.
 * <p>
 * 가격을 업데이트하는 역할로써의 책임 :
 * 1. 가격을 업데이트한다. updatePrice
 */
public class ProductionUpdateService {

    public void updatePrice(Production production, int price) {
        //update price
        production.updatePrice(price);
    }

}
