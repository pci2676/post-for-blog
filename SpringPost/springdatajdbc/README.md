# Spring Data JDBC 사용하기

## 개요

우아한 테크코스 Lv2를 진행하며 Spring Data JDBC를 사용하게 되었습니다.

Spring Data JDBC는 처음 사용해 보았기 때문에 많은 시행착오를 거쳤습니다.

이 글은 [공식문서](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#reference)를 참고하여 작성되었습니다.   
문서의 양이 적지는 않아 시리즈로 작성하려 합니다.

한글 문서가 별로 없는 것 같아 고생하다가 Spring Data JDBC를 처음 사용하는 분들에게 도움이 되길 바랍니다.

만약 더 자세한 내용을 알고 싶으시다면 공식문서를 참고하시면 좋을 것 같습니다.

## 테스트 환경

데이터 베이스는 메모리 DB인 H2를 사용하여 테스트 하였습니다.  
스프링 부트 버전은 2.2.6 RELEASE 입니다.

게시글에 사용된 예제코드와 테스트코드는 [GitHub](https://github.com/pci2676/post-for-blog/tree/master/SpringPost/springdatajdbc)에서 확인하실 수 있습니다.

## 목차

1. [엔티티 생성](#1-엔티티entity-생성)
   - [엔티티 내부 값 주입 과정](#엔티티-내부-값-주입-과정)
     - [witherMethod](#withermethod)
   - [엔티티 생성 가이드 라인](#엔티티-생성-가이드-라인)
2. [엔티티에서 사용 할 수 있는 변수타입]()
   - [1 : 1 관계 (OneToOne)]()
     - [embadded로 VO 표현하기]()
   - [1 : N 관계 (OneToMany)]()
     - [embadded로 일급 컬렉션 표현하기]()
     - [Set이 아닌 다른 Collection 사용하기]()

## 1. 엔티티(Entity) 생성

Spring Data JDBC에서 엔티티 객체를 생성하는 알고리즘은 3가지입니다.

1. 기본 생성자가 있는 경우 기본생성자를 사용합니다.
   - 다른 생성자가 존재해도 무시하고 최우선 기본생성자를 사용합니다.
2. 매개변수가 존재하는 생성자가 하나만 존재한다면 해당 생성자를 사용합니다.
3. 매개변수가 존재하는 생성자가 여러개 있다면 `@PersistenceConstructor` 어노테이션이 적용된 생성자를 사용합니다.
   - `@PersistenceConstructor`가 존재하지 않고, 기본 생성자가 없다면 `org.springframework.data.mapping.model.MappingInstantiationException`이 발생합니다.

여기서 Spring Data JDBC는 `Reflection` 을 이용해서 엔티티 객체를 복사하기 때문에 생성자의 접근제어자는 `private` 이면 안됩니다. `protected` 혹은 `public` 으로 선언후 사용해야 합니다.

### 엔티티 내부 값 주입 과정

Spring Data JDBC는 생성자로 인해 채워지지 않은 필드들에 대해 자동으로 생성된 프로퍼티 접근자(`Property Accessor`)가 다음과 같은 순서로 멤버변수의 값을 채워넣습니다.

1. 엔티티의 식별자를 주입합니다.
2. 엔티티의 식별자를 이용해 참조중인 객체에 대한 값을 주입합니다.
3. `transient` 로 선언된 필드가 아닌 멤버변수에 대한 값을 주입합니다.

엔티티 맴버변수는 다음과 같은 방식으로 주입됩니다.

1. 멤버변수에 `final` 예약어가 있다면(즉, 불변이라면) `wither` 메서드를 이용하여 값을 주입합니다.

   ```java
   // wither method
   public Sample withId(Long id) {
       //내부적으로 기존 생성자를 이용하며 imuttable한 값을 매개변수로 가진다.
       return new Sample(id, this.sampleName);
   }
   ```

   해당 `wither`메서드가 존재하지 않다면 `java.lang.UnsupportedOperationException: Cannot set immutable property ...` 를 발생시킵니다.

2. 해당 멤버변수의 `@AccessType`이 `PROPERTY`라면 `setter`를 사용하여 주입합니다.

   - `setter`가 존재하지 않으면 `java.lang.IllegalArgumentException: No setter available for persistent property`이 발생합니다.

3. 기본적으로 직접 멤버변수에 주입합니다.(`Field 주입`)

#### witherMethod

`withMethod`를 정의하는 경우는 `final`예약어가 있는 `immutable`한 멤버변수가 존재 할 때 입니다.  
이때 주의해야할 점이 있습니다.

만약 `immutable`한 멤버변수가 n개라면 `witherMethod`또한 **n개 작성**해 주어야 합니다.  
단순히 `witherMethod` 한개의 매개변수에 여러개의 `immutable` 필드를 주입한다면 `java.lang.UnsupportedOperationException: Cannot set immutable property`를 보게 됩니다.

또한 `witherMethod`의 이름을 잘못 작성해서는 안됩니다.  
멤버변수의 이름이 `createdAt` 이라면 `witherMethod`의 이름은 멤버변수의 이름을 뒤에 붙힌 `withCreatedAt`으로 작성해야 합니다.  
테이블 컬럼 이름이 아닌 **멤버 변수명을 따라서 작성**해야합니다.



### 엔티티 생성 가이드 라인

Spring Data JDBC 문서를 보면 JDBC를 이용해서 만드는 객체들에 대한 몇가지 [가이드라인](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#mapping.general-recommendations)이 있습니다.

가이드 라인은 다음과 같습니다.

- **불변객체**를 만드려고 노력하라.
- **모든 매개변수를 가진 생성자**(All Arguments Contructor)를 제공하라
  - 모든 매개변수를 가진 생성자를 제공하면 `object mapping` 과정중 Property 주입 방식을 건너뜀으로써 최대 30% 빠른 생성이 가능해진다.
- 생성자 오버로딩으로 인해 `@PersistenceConstructor`을 사용하는 것을 피하기 위해 **팩토리 매서드 패턴**을 사용하라
- 생성자와 자동 생성된 프로퍼티 접근자(`Property Accessor`)가 객체를 생성할 수 있도록 규약을 지켜라
- 자동 생성될 식별자 필드(ex. id)를 `final`과 함께 사용하기 위해 `wither`메서드를 사용하라
- 보일러 플레이트 코드를 피하기 위해 `Lombok`을 사용하라
  - 예를 들어 모든 매개변수를 가진 생성자의 경우 `Lombok`의 `@AllArgsConstructor`가 좋은 방법이 될 수 있다.



여기까지 복잡하다면 복잡하고 단순하다면 단순한 객체의 생성에 대한 설명입니다.  
그렇다면 어떻게 사용하는 것이 단순하고 사용하기 편한방법 일까요?

다음과 같은 Table이 있다고 했을때

```sql
CREATE TABLE CHESSGAME
(
    id     BIGINT auto_increment,
    name   varchar(255),
    active bit default 1,
    primary key (id)
);
```

개인적으로 아래와 같은 방법이 가장 단순하고 사용하기 편한 방법인것 같습니다.

```java
@Table("CHESSGAME")
public class ChessGame {
    @Id
    private Long id;
    private String name;
    private boolean active;

    protected ChessGame() {
    }

}
```

객체 생성을 위한 기본생성자를 선언하고 접근 제어자는 `protected`로 선언해 두고 사용하는 것입니다.  

이후 필요에 따라 아래와 같이 생성자를 추가하거나 정적 팩터리 메서드를 생성하여 사용하면 좋을 것 같습니다.

```java
@Table("CHESSGAME")
public class ChessGame {
    @Id
    private Long id;
    private String name;
    private boolean active;

    protected ChessGame() {
    }
		
  	//실제 사용될 생성자
    public ChessGame(final String name, final boolean active) {
        this.name = name;
        this.active = active;
    }
}
```

물론 성능 향상을 위해 모든 매개변수를 가진 생성자를 제공하는 것도 좋을 것 같습니다.

## 2. 엔티티에서 사용할수 있는 변수타입

- 기본적으로 원시타입과 그 참조타입은 모두 사용 가능합니다.

- Enum또한 사용가능하며 Enum의 name이 저장되게 됩니다.

  - VARCHAR 컬럼만 사용해야합니다.
  
  ```sql
  CREATE TABLE ENUM_ENTITY
  (
      id     BIGINT auto_increment,
      active varchar(255),
      primary key (id)
  
)
  ```
  
  ```java
  public class EnumEntity {
      @Id
      private Long id;
  
      private Active active;
  
      public EnumEntity() {
      }
  
      public EnumEntity(final Active active) {
          this.active = active;
      }
  
      public Long getId() {
          return id;
      }
  
      public Active getActive() {
          return active;
      }
  }
  
  public enum Active {
      Y(true),
      N(false);
  
      private final boolean value;
  
      Active(final boolean value) {
          this.value = value;
      }
  
      public boolean value() {
          return value;
      }
  }
  
  
  public interface EnumEntityRepository extends CrudRepository<EnumEntity, Long> {
      @Query("SELECT * FROM ENUM_ENTITY WHERE active = :active")
      List<EnumEntity> findAllByActive(@Param("active") String active);
  }
  
  ```
  
  아래 테스트는 통과합니다.  
  
- 아쉽게도 Enum으로 조회는 안되서 `name()`을 이용해 조회해야 합니다.
  
```java
      @DisplayName("enum을 필드로 사용하고 저장, 꺼낼수 있다.")
      @Test
      void save() {
          //given
          EnumEntity enumEntity = new EnumEntity(Active.Y);
          EnumEntity save = enumEntityRepository.save(enumEntity);
  
          //when
          EnumEntity entity = enumEntityRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);
  
          //then
          assertThat(entity.getActive()).isEqualTo(Active.Y);
      }
  
      @DisplayName("Enum의 name으로 저장되어 있다.")
      @Test
      void load() {
          //given
          EnumEntity enumEntity = new EnumEntity(Active.Y);
          EnumEntity save = enumEntityRepository.save(enumEntity);
  
          //when
          List<EnumEntity> allByActive = enumEntityRepository.findAllByActive(Active.Y.name());
  
          //then
          assertThat(allByActive).hasSize(1);
      }
  ```
  
- `java.util.Date`, `java.time.LocalDate`, `java.time.LocalDateTime`, 그리고 `java.time.LocalTime`이 사용가능합니다.

- 사용하는 데이터베이스가 지원을 한다면 위에 언급한 타입들의 배열 혹은 Collection 타입을 사용할 수 도 있습니다.

### 1 : 1 관계 (OneToOne)

다른 entity를 참조하는 경우, 1:1, 1:N 연관 관계 혹은 `embedded` 타입인 경우가 있습니다.  
1:1, 1:N 연관 관계인 경우 참조 되는 엔티티의 `id`는 선택사항(optional)입니다.  
참조의 대상이 되는 엔티티는 자신을 참조하는 엔티티의 테이블의 이름과 같은 이름의 Column이름을 가집니다.

```sql
CREATE TABLE SUPER_ONE
(
    id         BIGINT auto_increment,
    super_name varchar(255),
    primary key (id)
);

CREATE TABLE SUB_ONE
(
    id        BIGINT auto_increment,
    super_one BIGINT,
    sub_name  varchar(255),
    primary key (id)
);

ALTER TABLE SUB_ONE
    ADD FOREIGN KEY (super_one)
        REFERENCES SUB_ONE (id);
```

```java
public class SuperOne {
    @Id
    private Long id;
    private String superName;
    private SubOne subOne;

    protected SuperOne() {
    }

    public SuperOne(final String superName, final SubOne subOne) {
        this.superName = superName;
        this.subOne = subOne;
    }

    public Long getId() {
        return id;
    }

    public SubOne getSubOne() {
        return subOne;
    }
}

public class SubOne {
    // Embedded가 아닌 참조 엔티티는 id가 필요하다.
    @Id
    private Long id;
    // 선언할 필요없다. 자신을 참조하는 엔티티의 테이블 명으로 컬럼이 필요하도록 기본전략이 걸려있다.
    // private Long superOne;
    private String subName;

    protected SubOne() {
    }

    public SubOne(final String subName) {
        this.subName = subName;
    }

    public Long getId() {
        return id;
    }
}
```

### Embadded를 이용한 VO 표현하기 

- 하나의 테이블에서 파생된 `embedded` 엔티티의 경우 **`id` 필드는 사용할 수 없습니다.**  
  **같은 테이블**에서 몇가지 컬럼을 모아 `embedded`를 이용해 표현하였기 때문입니다.
  
아래 예시에서는 `MEMBER`의 일부분인 `first_name`과 `last_name`을 `embadded` 엔티티로 표현하고 있습니다.
  
  ```sql
  CREATE TABLE MEMBER
  (
      id BIGINT auto_increment,
      first_name varchar(255),
      last_name  varchar(255),
      primary key (id)
  );
  ```
  
  ```java
  public class Member {
      @Id
      private Long id;
  
      @Embedded.Nullable // Embedded라고 선언해야한다.
      private Name name;
  
      protected Member() {
    }
  
      public Member(final Name name) {
          this.name = name;
      }
  
      public Name getName() {
          return name;
      }
  }
  
  /**
   * embedded Type 이다.
   * VO의 성격을 띈다.
   */
  public class Name {
      // id 필드가 존재해선 안된다.	
      private String firstName;
      private String lastName;
  
      protected Name() {
      }
  
      public Name(final String firstName, final String lastName) {
          this.firstName = firstName;
          this.lastName = lastName;
      }
  
      public String getFirstName() {
          return firstName;
      }
  
      public String getLastName() {
          return lastName;
      }
  }
  ```

### 1 : N 관계 (OneToMany) 

- `Set<Entity>` 는 1 : n 의 연관관계를 나타냅니다.  
  마찬가지로 참조되는 객체는 참조하는 객체의 테이블 이름으로 된 Column을 기본적으로 가집니다.

  ```sql
  CREATE TABLE SET_SINGLE
  (
      id BIGINT auto_increment,
      primary key (id)
  );
  
  CREATE TABLE SET_MANY
  (
      id         BIGINT auto_increment,
      set_single BIGINT,
      many_name  varchar(255),
      primary key (id)
  );
  
  ALTER TABLE SET_MANY
      ADD FOREIGN KEY (set_single)
          REFERENCES SET_SINGLE (id);
  ```

  ```java
  public class SetSingle {
      @Id
      private Long id;
  
      private Set<SetMany> manies;
  
      protected SetSingle() {
      }
  
      public SetSingle(final Set<SetMany> manies) {
          this.manies = manies;
      }
  
      public Long getId() {
          return id;
      }
  
      public Set<SetMany> getManies() {
          return manies;
      }
  }
  
  public class SetMany {
      @Id
      private Long id;
      // 아래 이름으로 된 외래키를 기대한다. 주석처리 하고 사용하여도 무방하다.
      private Long setSingle;
      private String manyName;
  
      public SetMany() {
      }
  
      public SetMany(final String manyName) {
          this.manyName = manyName;
      }
  
      public Long getSetSingle() {
          return setSingle;
      }
  }
  ```

  

- `Map<some type, entity>` 과 `List<entity>`는 두개의 추가 Column이 필요합니다.  
  fk의 대상이 되는 Column의 이름으로 [참조하는 테이블의 이름]이 필요합니다.  
  그리고 Map과 List의 Key로 사용될 Column으로 [참조하는 테이블 이름]_KEY 로 되어있는 Column이 필요합니다.  

  예제. Map

  ```sql
  CREATE TABLE MAP_SINGLE
  (
      id BIGINT auto_increment,
      primary key (id)
  );
  
  CREATE TABLE MAP_MANY
  (
      id         BIGINT auto_increment,
      map_single BIGINT,
      map_single_key    varchar(255),
      content    varchar(255),
      primary key (id)
  );
  
  ALTER TABLE MAP_MANY
      ADD FOREIGN KEY (map_single)
          REFERENCES MAP_SINGLE (id);
  ```

  ```java
  public class MapSingle {
      @Id
      private Long id;
  
      private Map<String, MapMany> mapManyMap;
  
      public MapSingle() {
      }
  
      public MapSingle(final Map<String, MapMany> mapManyMap) {
          this.mapManyMap = mapManyMap;
      }
  
      public Map<String, MapMany> getMapManyMap() {
          return mapManyMap;
      }
  }
  
  public class MapMany {
      @Id
      private Long id;
  
      // 참조하는 객체의 이름으로 된 외래키를 기본적으로 기대한다. 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
      private Long mapSingle;
  
          // suffix 로 [참조하는테이블이름]_KEY 형태의 컬럼을 Map의 key 사용하기 위해 필요하다. 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
      private String mapSingleKey;
  
      private String content;
  
      public MapMany() {
      }
  
      public MapMany(final String content) {
          this.content = content;
      }
  
      public String getContent() {
          return content;
      }
  }
  ```

- `List` 의 경우 `Map<Integer, Entity>` 처럼 동작한다고 보면됩니다.

  ```sql
  CREATE TABLE LIST_SINGLE
  (
      id BIGINT auto_increment,
      primary key (id)
  );
  
  CREATE TABLE LIST_MANY
  (
      id              BIGINT auto_increment,
      list_single     BIGINT,
      list_single_key varchar(255),
      content         varchar(255),
      primary key (id)
  );
  
  ALTER TABLE LIST_MANY
      ADD FOREIGN KEY (list_single)
          REFERENCES LIST_SINGLE (id);
  ```

  ```java
  public class ListSingle {
      @Id
      private Long id;
  
      private List<ListMany> listManyList;
  
      public ListSingle() {
      }
  
      public ListSingle(final List<ListMany> listManyList) {
          this.listManyList = listManyList;
      }
  
      public Long getId() {
          return id;
      }
  
      public List<ListMany> getListManyList() {
          return listManyList;
      }
  }
  
  public class ListMany {
      @Id
      private Long id;
  
      // 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
      private Long listSingle;
  
      // 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
      private Long listSingleKey;
  
      public ListMany() {
      }
  }
  ```

### Embadded를 이용한 일급컬렉션 표현 

- 단일관계가 아닌 `OneToMany`와 같은 상황에서 `Embadded` 를 이용해서 일급컬렉션을 표현할 수 있습니다.

  ```sql
  CREATE TABLE RACING_GAME
  (
      id BIGINT AUTO_INCREMENT,
      primary key (id)
  );
  
  CREATE TABLE RACING_CAR
  (
      id          BIGINT AUTO_INCREMENT,
      racing_game BIGINT,
      car_name    VARCHAR(255),
      primary key (id)
  );
  
  ALTER TABLE RACING_CAR
      ADD FOREIGN KEY (racing_game)
          REFERENCES RACING_GAME (id);
  ```

  ```java
  public class RacingGame {
      @Id
      private Long id;
  
      @Embedded.Nullable
      private RacingCars racingCars;
  
      protected RacingGame() {
      }
  
      public RacingGame(final RacingCars racingCars) {
          this.racingCars = racingCars;
      }
  
      public Long getId() {
          return id;
      }
  
      public Set<RacingCar> getRacingCars() {
          return racingCars.getRacingCars();
      }
  }
  
  //일급컬렉션 객체
  public class RacingCars {
      private Set<RacingCar> racingCars;
  
      public RacingCars(final Set<RacingCar> racingCars) {
          this.racingCars = racingCars;
      }
  
      public Set<RacingCar> getRacingCars() {
          return new HashSet<>(racingCars);
      }
  }
  
  public class RacingCar {
      @Id
      private Long id;
      private String carName;
  
      protected RacingCar() {
      }
  
      public RacingCar(final String carName) {
          this.carName = carName;
      }
  
      public Long getId() {
          return id;
      }
  
      public String getCarName() {
          return carName;
      }
  }
  ```

### 일대다 연관관계에서 Collection을 사용 할 때

데이터베이스에 Spring Data JDBC가 지정한대로 Column 이름이 저장되어 있으면 그리 어렵지 않게 사용 할 수 있습니다.

그런데 참조키 Column이름을 [참조하는테이블이름]으로 짓기보단 `id`가 포함된게 더 보기 좋은것 같습니다.  
Column이름과 기본전략이 상이한 경우 다음과 같이 사용할 수 있습니다.

`@MappedCollection`를 이용하여 문제를 해결할 수 있습니다.

## CustomConversion 사용하기

사실 엔티티에 객체를 맵핑하는것은 그리 큰 문제가 아닙니다.

