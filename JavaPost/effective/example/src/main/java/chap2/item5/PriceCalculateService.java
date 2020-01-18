package chap2.item5;

interface ExchangeRateParser {
    double getRate(String url);
}

public class PriceCalculateService {
    private static final String EXCHANGE_URL = "https://somewhere.com";
    private static final double FIXED_DISCOUNT_PRICE = 1000;

    private final ExchangeRateParser exchangeRateParser;

    public PriceCalculateService(ExchangeRateParser exchangeRateParser) {
        this.exchangeRateParser = exchangeRateParser;
    }

    public double discount(double price) {
        double exchangeRate = exchangeRateParser.getRate(EXCHANGE_URL);

        double exchangedDiscountPrice = FIXED_DISCOUNT_PRICE * exchangeRate;
        double exchangedPrice = price * exchangeRate;

        return exchangedPrice - exchangedDiscountPrice;
    }
}

class RealExchangeRateParser implements ExchangeRateParser {
    @Override
    public double getRate(String url) {
        // ... 외부 의존성을 가지는 production code 가 위치한다 가정
        return 0;
    }
}

class MockExchangeRateParser implements ExchangeRateParser {
    @Override
    public double getRate(String url) {
        // 외부와 의존성을 가지지 않음. mock 용 코드
        return 1;
    }
}