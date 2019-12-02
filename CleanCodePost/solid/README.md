# 객체지향설계 5원칙 SOLID의 이해와 예제

### 목표

[SOLID](https://ko.wikipedia.org/wiki/SOLID_(%EA%B0%9D%EC%B2%B4_%EC%A7%80%ED%96%A5_%EC%84%A4%EA%B3%84))에 대한 설명을 하는 글은 여러 블로그에 소개가 되어있습니다.

하지만 대부분의 글이 개념적인 설명을 위주로 하고 있을뿐더러, 너무 추상적이라 이해하기 어렵다는 생각을 했습니다. 

그래서 저는 이 글을

- 실제로 코드상에서 어떠한 방식으로 적용되는지
- 어떻게 의식하고 코드를 작성하는 것이 SOLID를 지킬 수 있는지

이 두 가지에 중점을 두고, 개념적인 설명만 있는게 아닌 예제를 통해 SOLID를 공부하고 이해해 보고자 합니다.

글에서 사용된 예제는 모두 [GitHub](https://github.com/pci2676/post-for-blog/tree/master/CleanCodePost/solid)에 올라가 있습니다. Fork/PR을 통해서 제가 바꾼 방식보다 더 쉽고, 더 좋게 바꾸신 후 피드백을 주시면 양질의 게시글이 되는데 더 도움이 될 것 같습니다!



### 1. SRP

Single Responsibility Principle - 단일 책임의 원칙

> **한 클래스는 하나의 책임을 가져야 한다.**



SRP가 지켜지지 않은 코드

```java
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
```

상품의 역할을 하는 `Production` 과 상품의 가격을 변경하는 `ProductionUpdateService` 가 있습니다.

상품의 역할을 하는 `Production` 의 책임은

- updatePrice() : 상품의 가격을 변경한다.

상품의 업데이트를 하는 역할인 `ProductionUpdateService` 의 책임은

- updatePrice() : 상품의 가격을 변경한다.
- validatePrice() : 상품의 유효성을 검사한다.

입니다.

`ProductionUpdateService`의 역할은 `Product`의 내용을 변경하는 책임을 가지고 있습니다.

즉, updatePrice()의 책임은 `ProductionUpdateService`의 책임으로 볼 수 있습니다.

하지만 가격의 유효성을 검증하는 validatePrice()는 `ProductionUpdateService` 의 책임이라고 볼 수 있을까요?

가격의 유효성을 검증하는 작업은 실제 가격의 정보를 바꾸는 `Product`의 책임으로 보는게 더 맞는것 같다고 생각합니다.

이를 토대로 유효성 검증이라는 책임을 `Product`로 옮긴 코드는 다음과 같습니다.



```java
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

public class ProductionUpdateService {

    public void updatePrice(Production production, int price) {
        //update price
        production.updatePrice(price);
    }

}
```

유효성 검증의 책임을 `Production`으로 옮김으로써 `ProductionUpdateService`는 온전히 상품의 정보를 변경하기 위한 코드만 존재하게 되었습니다.

이와같이 SRP를 지키기 위해서는 각 객체가 할 수 있는일과 해야 하는 일을 찾아 책임으로 부여하도록 의식해야 합니다.

다시말해 각 객체(`Product`)가 제어할 수 있는 정보(`price`)에 대한 책임(`validatePrice`)을 올바른 자리에 배치한다면 지킬 수 있는 규칙이라고 생각합니다.

