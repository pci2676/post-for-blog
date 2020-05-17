# Spring Data JDBC 사용하기

## 개요

우아한 테크코스 Lv2를 진행하며 Spring Data JDBC를 사용하게 되었습니다.

Spring Data JDBC는 처음 사용해 보았기 때문에 많은 시행착오를 거쳤습니다.

이 글은 [공식문서](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#reference)를 참고하여 작성되었습니다.   
문서의 양이 적지는 않아 시리즈로 작성하려 합니다.

한글 문서가 별로 없어서 고생하다가 Spring Data JDBC를 필자처럼 처음 사용하는 분들에게 도움이 되길 바라는 마음으로 작성하였습니다.

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
2. [엔티티에서 사용 할 수 있는 변수타입](#2-엔티티에서-사용할수-있는-변수타입)
   - [1 : 1 관계 (OneToOne)](#1--1-관계-onetoone)
     - [embedded로 VO 표현하기](#embedded를-이용한-vo-표현하기)
   - [1 : N 관계 (OneToMany)](#1--n-관계-onetomany)
     - [embedded로 일급 컬렉션 표현하기](#embedded를-이용한-일급컬렉션-표현)

## 1. 엔티티(Entity) 생성 (1.1.7 RELEASE)

Spring Data JDBC에서 엔티티 객체를 생성하는 알고리즘은 3가지입니다.

1. 기본 생성자가 있는 경우 기본생성자를 사용합니다.
   - 다른 생성자가 존재해도 무시하고 최우선 기본생성자를 사용합니다.
2. 매개변수가 존재하는 생성자가 하나만 존재한다면 해당 생성자를 사용합니다.
3. 매개변수가 존재하는 생성자가 여러개 있다면 `@PersistenceConstructor` 어노테이션이 적용된 생성자를 사용합니다.
   - `@PersistenceConstructor`가 존재하지 않고, 기본 생성자가 없다면 `org.springframework.data.mapping.model.MappingInstantiationException`이 발생합니다.

여기서 기본 생성자를 `private`접근 제어자로 선언해도 정상적으로 잘 작동합니다

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

**롬복을 사용하지 않는다면** 개인적으로 아래와 같은 방법이 가장 단순하고 사용하기 편한 방법인것 같습니다.

```java
@Table("CHESSGAME")
public class ChessGame {
    @Id
    private Long id;
    private String name;
    private boolean active;

    private ChessGame() {
    }

}
```

객체 생성을 위한 기본생성자를 선언하고 접근 제어자는 `private`로 선언해 두고 사용하는 것입니다.  

이후 필요에 따라 아래와 같이 생성자를 추가하거나 정적 팩터리 메서드를 생성하여 사용하면 좋을 것 같습니다.

```java
@Table("CHESSGAME")
public class ChessGame {
    @Id
    private Long id;
    private String name;
    private boolean active;

    private ChessGame() {
    }
		
  	//실제 사용될 생성자
    public ChessGame(final String name, final boolean active) {
        this.name = name;
        this.active = active;
    }
}
```

다만 **롬복을 사용한다면** 아래와 같이 전체 맴버변수를 가지는 생성자를 항상 최신화 하며 `@Builder`를 사용하고 `@PersistenceConstructor` 를 같이 사용하는것이 성능 면에서 좋을 것 같습니다.

```java
@Table("CHESSGAME")
public class ChessGame {
    @Id
    private Long id;
    private String name;
    private boolean active;

  	@Builder
  	@PersistenceConstructor
    public ChessGame(final Long id, final String name, final boolean active) {
				this.id = id
      	this.name = name;
        this.active = active;
    }
  
    public ChessGame(final String name, final boolean active) {
        this.name = name;
        this.active = active;
    }
}
```



## 2. 엔티티에서 사용할수 있는 변수타입 (1.1.7 RELEASE)

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

    private SuperOne() {
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

    private SubOne() {
    }

    public SubOne(final String subName) {
        this.subName = subName;
    }

    public Long getId() {
        return id;
    }
}
```

### Embedded를 이용한 VO 표현하기 

- 하나의 테이블에서 파생된 `embedded` 엔티티의 경우 당연하게도 **`id` 필드는 사용할 수 없습니다.**  
  **같은 테이블**에서 몇가지 컬럼을 모아 `embedded`를 이용해 표현하였기 때문입니다.

아래 예시에서는 `MEMBER`의 일부분인 `first_name`과 `last_name`을 `embedded` 엔티티로 표현하고 있습니다.

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
  
      private Member() {
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
  
      private Name() {
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
  
      private SetSingle() {
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

  - 이 경우 `key` 로 사용되는 컬럼이 `List`의 순서를 보장해줍니다.
  
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

### Embedded를 이용한 일급컬렉션 표현 

- 단일관계가 아닌 `OneToMany`와 같은 상황에서 `Embedded` 를 이용해서 일급컬렉션을 표현할 수 있습니다.

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
  
      private RacingGame() {
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
  
      private RacingCar() {
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

### 일대다 연관관계에서 @CreatedDate, @LastModifiedDate 사용하기

JPA와 마찬가지로 Spring Data JDBC 도 Auditing을 제공합니다.  
따라서 엔티티의 생성시각과 수정시각을 `@CreateDate`와 `@LastModifiedDate`를 이용해서 자동으로 관리해 줄 수 있습니다.

생성일과 수정일로 관리할 컬럼에 아래와 같이 어노테이션을 작성합니다.

```java
public class Article {
    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "article_id", keyColumn = "article_key")
    private List<Comment> comments;

    private Article() {
    }

    public Article(final List<Comment> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
```

그 다음 `@EnableJdbcAuditing`을 이용해서 Audit 기능을 활성화시켜주면 끝입니다.

```java
@EnableJdbcAuditing
@Configuration
public class ApplicationListenerConfiguration {

}
```

아래는 테스트 코드입니다.

```java
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ArticleTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @DisplayName("컬럼기본전략과 데이터베이스의 컬럼이 상이한 경우에도 가능하다")
    @Test
    void save() {
        //given
        Comment comment1 = new Comment("asdf");
        Comment comment2 = new Comment("qwer");
        Comment comment3 = new Comment("zxcv");

        Article article1 = new Article(Arrays.asList(comment1, comment2, comment3));

        //when
        Article save = articleRepository.save(article1);
        Article load = articleRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);

        //then
        System.out.println(save.getCreatedAt());
        System.out.println(load.getCreatedAt());
        assertThat(save.getCreatedAt().equals(load.getCreatedAt()));
        assertThat(load.getComments()).hasSize(3);
    }
}
```

표준출력으로 출력한 두줄이 있는데요.  
이 두줄을 확인하면 다른 점이 있습니다.

![image](https://user-images.githubusercontent.com/13347548/82149661-1731ac80-9892-11ea-90b2-0fcb8acefbe4.png)

코드상에서 자동으로 생성된 LocalDateTime과 DB에서 조회한 LocalDateTime이 조금 다른 것을 확인할 수 있습니다.  
DB에는 시간 데이터가 조금 절삭되어 저장이 되기때문에 윈도우 환경에서는 위 테스트가 실패할 수 있습니다.

이때 자식 엔티티에게도 생성일과 수정일을 관리하고 싶어서 `@CreatedDate`와 `@LastModifiedDate`를 사용 할 수 있는데요.  
Spring Data JDBC의 컨셉상 자식 엔티티에서는 Auditing기능을 사용할 수 없었습니다.

```java
public class Comment {
    @Id
    private Long id;

    private Long articleId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Comment() {
    }

    public Comment(final String content) {
        this.articleId = articleId;
        this.content = content;
    }

   // ... getter

}
```

위와 같이 Comment 엔티티를 만들고 아래 테스트 코드를 실행해보면 자식인 Comment 엔티티에겐 Audit 정보가 들어가지 않는 것을 확인 할 수 있습니다.

```java
    @DisplayName("자식 Entity는 Audit 기능을 사용할 수 없다.")
    @Test
    void save2() {
        //given
        Comment comment1 = new Comment("1");
        Comment comment2 = new Comment("2");

        Article article = new Article(Arrays.asList(comment1, comment2));

        //when
        article = articleRepository.save(article);

        //then
        assertThat(article.getComments().get(0).getCreatedAt()).isNull();
        assertThat(article.getComments().get(0).getUpdatedAt()).isNull();
        System.out.println(gson.toJson(article));
    }
```

![image](https://user-images.githubusercontent.com/13347548/82150396-9a9fcd80-9893-11ea-9b06-09ae15371f2f.png)

출력결과에서도 Audit 정보가 null이라 출력되지 않는 것을 볼 수 있습니다.

Spring Data JDBC에서 자식 엔티티는 부모 엔티티의 생명주기를 그대로 따라가도록 설계가 되어있습니다.

따라서 부모 엔티티에 새로운 자식 엔티티를 추가한다면 INSERT 쿼리가 한번 발생하는 것이 아니라 부모에게 속해있던 자식 엔티티를 전부 DELETE 후 다시 전부 INSERT하는 쿼리가 발생하는 것을 확인 할 수 있습니다.

`application.yml`에 쿼리 확인을 위해 다음 설정값을 추가하여 확인해 보도록 하겠습니다.

```yml
logging:
  level:
    org:
      springframework:
        jdbc:
          core:
            JdbcTemplate: debug
```

쿼리를 확인하기 위해 간단히 자식 엔티티를 추가후 잘 추가되었는지 확인하는 테스트를 작성해 보겠습니다.

```java
    @DisplayName("자식 Entity가 추가될때 전부 DELETE 후 전부 INSERT를 진행한다. ")
    @Test
    void save3() {
        //given
        Comment comment1 = new Comment("1");
        Comment comment2 = new Comment("2");

        Article article = new Article(new ArrayList<>(Arrays.asList(comment1, comment2)));
        article = articleRepository.save(article);

        //when
        System.out.println("\n##################\n");
        Comment comment3 = new Comment("3");
        article.addComment(comment3);

        article = articleRepository.save(article);

        //then
        List<Comment> comments = article.getComments();
        assertThat(comments).hasSize(3);
    }
```

테스트는 정상적으로 잘 작동하는 것을 확인 할 수 있고 이제 디버그 로그를 통해 쿼리가 어떻게 발생했는지 확인해 보겠습니다.

![image](https://user-images.githubusercontent.com/13347548/82151246-8fe63800-9895-11ea-9f3c-172bd7118795.png)

`###` 을 기준으로 아래로 발생한 쿼리를 확인하시면 됩니다.

로그를 보면 알수 있듯 

```sql
[DELETE FROM comment WHERE comment.article_id = ?]
```

DELETE 쿼리가 먼저 실행된 후

```sql
[INSERT INTO comment (article_id, content, created_at, updated_at, id, article_key) VALUES (?, ?, ?, ?, ?, ?)]
```

INSERT 쿼리가 3번 발생하는 것을 확인할 수 있습니다.

따라서 자식 엔티티에서 `@CreatedDate`과 `@LastModifiedDate`의 Audit은 사용할 수 가 없고 자식 엔티티의 생성 시간은 부모 엔티티의 `@LastModifiedDate` 와 동일하게 여기면 됩니다.

