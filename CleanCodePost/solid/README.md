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

    public void update(Production production, int price) {
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

- update() : 상품의 정보를 변경하는 `Product`의 책임을 호출 한다.

상품의 업데이트를 하는 역할인 `ProductionUpdateService` 의 책임은

- updatePrice() : 상품의 가격을 변경한다.
- validatePrice() : 상품의 유효성을 검사한다.

입니다.

`ProductionUpdateService`의 역할은 `Product`의 내용을 변경하는 책임을 호출하는 책임을 가지고 있습니다.
즉, update()의 책임은 `ProductionUpdateService`의 책임으로 볼 수 있습니다.
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

    public void update(Production production, int price) {
        //update price
        production.updatePrice(price);
    }

}
```

유효성 검증의 책임을 `Production`으로 옮김으로써 `ProductionUpdateService`는 온전히 상품의 정보를 변경하기 위한 코드만 존재하게 되었습니다.

이와같이 SRP를 지키기 위해서는 각 객체가 할 수 있는일과 해야 하는 일을 찾아 책임으로 부여하도록 의식해야 합니다.

다시말해 각 객체(`Product`)가 제어할 수 있는 정보(`price`)에 대한 책임(`validatePrice`)을 올바른 자리에 배치한다면 지킬 수 있는 규칙이라고 생각합니다.





### 2. OCP

Open Close Principle - 개방 폐쇄 원칙

> **소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.**



상품 객체의 구조

```java
public class Production {
    private String name;
    private int price;
    // N(일반) ,E(전자티켓) ,L(지역상품)...
    private String option;

    public Production(String name, int price, String option) {
        this.name = name;
        this.price = price;
        this.option = option;
    }

    public int getNameLength() {
        return name.length();
    }

    public String getOption() {
        return option;
    }
}
```

상품객체에 `option` 이라는 멤버 변수가 생겼습니다.
`option` 에 따른 검증 작업을 진행해야하는 요구사항이 존재한다고 가정 후 코드를 작성했다고 합시다.



**요구사항**

- 일반 상품(N) 이름의 길이는 3글자 보다 길어야 합니다. 



OCP 원칙을 고려하지 않은 코드

```java
public class ProductionValidator {
    public void validateProduction(Production production) throws IllegalAccessException {
        if (production.getNameLength() < 3) {
            throw new IllegalAccessException("일반 상품의 이름은 3글자보다 길어야 합니다.");
        }
    }
}
```

이 상황에서는 문제가 없어 보이지만 다음과 같은 요구사항이 추가되었다면 어떻게 될까요?



**추가 요구사항**

- 전자 티켓(E) 이름의 길이는 10글자 보다 길어야 합니다.
- 지역 상품(L) 이름의 길이는 20글자보다 길어야 합니다.



현재 구조를 유지한다면 다음과 같이 코드를 작성하게 될것 같습니다.

```java
public class ProductionValidator {
    public void validateProduction(Production production) throws IllegalArgumentException {

        if (production.getOption().equals("N")) {
            if (production.getNameLength() < 3) {
                throw new IllegalArgumentException("일반 상품의 이름은 3글자보다 길어야 합니다.");
            }
        } else if (production.getOption().equals("E")) {
            if (production.getNameLength() < 10) {
                throw new IllegalArgumentException("전자티켓 상품의 이름은 10글자보다 길어야 합니다.");
            }
        } else if (production.getOption().equals("L")) {
            if (production.getNameLength() < 20) {
                throw new IllegalArgumentException("지역 상품의 이름은 20글자보다 길어야 합니다.");
            }
        }

    }
}
```

이러한 구조에서 아래와 같은 요구사항을 받게된다면..??

- 상품의 옵션이 계속해서 추가되고
  - 기존 옵션에 대한 검증 작업이 추가
  - 기존 옵션에 대한 검증 작업이 변경
  - 기존 옵션에 대한 검증 작업이 삭제
  - 기존 옵션에 대한 검증 작업이 통합

코드의 수정이 빈번하게 일어나고 `if...else if` 의 향연이 눈앞에 펼쳐질 것 같습니다!
유지보수가 하기 힘들어지고 코드를 파악하기 힘들어 질것입니다.
다시말해 변경에 너무 취약한 구조를 가지고 있습니다.



여기서 OCP를 지키는 구조를 취하게 해본다면 다음과 같이 변할 것 같습니다.

```java
public interface Validator {

    boolean support(Production production);

    void validate(Production production) throws IllegalArgumentException;

}
```

먼저 검증 작업에 대한 책임을 담당할 인터페이스를 작성하고!



```java
public class DefaultValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("N");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 3) {
            throw new IllegalArgumentException("일반 상품의 이름은 3글자보다 길어야 합니다.");
        }
    }
}


public class ETicketValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("E");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 10) {
            throw new IllegalArgumentException("전자티켓 상품의 이름은 10글자보다 길어야 합니다.");
        }
    }
}

public class LocalValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("L");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 20) {
            throw new IllegalArgumentException("지역 상품의 이름은 20글자보다 길어야 합니다.");
        }
    }
}

```

위와 같이 옵션에 대한 검증 작업을 담당하게 할 `Validator`를 구현한다면!



```java
public class ProductValidator {

    private final List<Validator> validators = Arrays.asList(new DefaultValidator(), new ETicketValidator(), new LocalValidator());

    public void validate(Production production) {
        Validator productionValidator = new DefaultValidator();

        for (Validator validator : validators) {
            if (validator.support(production)) {
                productionValidator = validator;
                break;
            }
        }

        productionValidator.validate(production);
    }
}
```

위와 같은 코드가 작성될 수 있을 것 입니다.
이러한 구조에서 새로운 옵션이 생성되어 검증 로직이 추가되야 할 때\
OCP를 지키지 않은 구조와 달리 `ProductValidator` 의 `validate()` 의 수정 없이
해당 검증을 담당할 객체를  추가하여 요구사항을 충족시킬 수 있습니다.

따라서 코드의 변경 없이 확장 가능한 구조가 되었습니다.

> 위 코드는 enum과 stream을 통해 더 간결해 질 수 있습니다.
> 주변 친구들이 enum과 stream을 잘 이해하지 못하는 모습을 보고 사용하지 않았습니다.
> enum을 사용한다면 어떻게 더 간결해 질 수 있는지 공부하면 좋을 것 같습니다.



OCP를 지키지 않은 구조에서 `ProductValidator` 가 너무 하는 일이 많았습니다.
달리 말해 가지고 있는 책임이 너무 많았다고 할수 있을것 같습니다.
어떤 객체의 책임이 너무 무겁지 않은지 의심하고,
SRP를 지키는 것 처럼 책임을 분리하는 작업을 통해 OCP를 지킬 수 있을 것 같습니다.



### 3. LSP

Liskov Substitution Principle - 리스코프 치환 원칙

> "프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다."

