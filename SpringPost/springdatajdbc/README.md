# Spring Data JDBC 사용하기

## 개요

우아한 테크코스 Lv2를 진행하며 Spring Data JDBC를 사용하게 되었습니다.

MyBatis와 Spring Data JPA는 사용해보았지만 Spring Data JDBC는 처음 사용해 보았습니다.  
때문에 많은 시행착오를 거쳤습니다.

이 글은 [공식문서](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#reference)의 일부분을 참고하여 작성되었습니다.  
목 마른 사람이 우물을 판다고 한글 문서가 별로 없는 것 같아 고생하다가 Spring Data JDBC를 처음 사용하는 분들에게 도움이 되길 바라면서 글을 작성하였습니다.

만약 더 자세한 내용을 알고 싶으시다면 공식문서를 참고하시면 좋을 것 같습니다.

## 테스트 환경

데이터 베이스는 메모리 DB인 H2를 사용하여 테스트 하였습니다.  
스프링 부트 버전은 2.2.6 RELEASE 입니다.

게시글에 사용된 예제코드와 테스트코드는 [GitHub]()에서 확인하실 수 있습니다.

## 1. 엔티티(Entity) 생성

Spring Data JDBC에서 엔티티 객체를 생성하는 알고리즘은 3가지입니다.

1. 기본 생성자가 있는 경우 기본생성자를 사용합니다.
   - 다른 생성자가 존재해도 무시하고 최우선 기본생성자를 사용합니다.
2. 매개변수가 존재하는 단일 생성자가 있다면 단일 생성자를 사용합니다.
3. 매개변수가 존재하는 생성자가 여러개 있다면 `@PersistenceConstructor` 어노테이션이 적혀있는 생성자를 사용합니다.

여기서 Spring Data JDBC는 `Reflection` 을 이용해서 엔티티 객체를 복사하기 때문에 생성자의 접근제어자는 `private` 이면 안됩니다. `protected` 혹은 `public` 으로 선언후 사용해야 합니다.

### 엔티티 내부 멤버 변수 값 주입 과정

Spring Data JDBC는 생성자로 인해 채워지지 않은 필드들에 대해 자동으로 생성된 프로퍼티 접근자(`Property Accessor`)가 다음과 같은 순서로 멤버변수의 값을 채워넣습니다.

1. 엔티티의 식별자를 주입합니다.
2. 엔티티의 식별자를 이용해 내부적으로 참조중인 순환 객체에 대한 값을 주입합니다.
3. `transient` 로 선언된 필드가 아닌 멤버변수에 대한 값을 주입합니다.

엔티티 맴버변수는 다음과 같은 순서로 주입됩니다.

1. 멤버변수에 `final` 예약어가 있다면 `wither` 메서드를 이용하여 값을 주입합니다.
2. 해당 멤버변수에 대한 `getter`와 `setter`가 정의되어 있다면 `setter`를 사용하여 주입합니다.
3. 직접 멤버변수에 주입합니다.



### 엔티티 생성 시 가이드 라인

Spring Data JDBC 문서를 보면 JDBC를 이용해서 만드는 객체들에 대한 몇가지 [가이드라인](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#mapping.general-recommendations)이 있습니다.

가이드 라인은 다음과 같습니다.

- **불변객체**를 만드려고 노력하라.
- **모든 매개변수를 가진 생성자**(All Arguments Contructor)를 제공하라
- 생성자 오버로딩으로 인해 `@PersistenceConstructor`을 사용하는 것을 피하기 위해 **팩토리 매서드 패턴**을 사용하라
- 생성자와 자동 생성된 프로퍼티 접근자(`Property Accessor`)가 객체를 생성할 수 있도록 규약을 지켜라
- 자동 생성될 식별자 필드(ex. id)를 `final`과 함께 사용하기 위해 `wither`메서드를 사용하라
- 보일러 플레이트 코드를 피하기 위해 `Lombok`을 사용하라
  - 모든 매개변수를 가진 생성자의 경우 `Lombok`의 `@AllArgsConstructor`가 좋은 방법이 될 수 있다.



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

저는 아래와 같은 방법이 가장 단순하고 사용하기 편한 방법인것 같습니다.

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



## 2. 엔티티에서 사용하는 멤버변수

- 기본적으로 원시타입과 그 참조타입은 모두 사용 가능합니다.

- Enum또한 사용가능하며 Enum의 name이 저장되게 됩니다.

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
      TRUE("Y", true),
      FALSE("N", false);
  
      private final String message;
      private final boolean value;
  
      Active(final String message, final boolean value) {
          this.message = message;
          this.value = value;
      }
  
      public String getMessage() {
          return message;
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

  ```java
      @DisplayName("enum을 필드로 사용하고 저장, 꺼낼수 있다.")
      @Test
      void save() {
          //given
          EnumEntity enumEntity = new EnumEntity(Active.TRUE);
          EnumEntity save = enumEntityRepository.save(enumEntity);
  
          //when
          EnumEntity entity = enumEntityRepository.findById(save.getId()).orElseThrow(NoSuchElementException::new);
  
          //then
          assertThat(entity.getActive()).isEqualTo(Active.TRUE);
      }
  
      @DisplayName("Enum의 name으로 저장되어 있다.")
      @Test
      void load() {
          //given
          EnumEntity enumEntity = new EnumEntity(Active.TRUE);
          EnumEntity save = enumEntityRepository.save(enumEntity);
  
          //when
          List<EnumEntity> allByActive = enumEntityRepository.findAllByActive(Active.TRUE.name());
  
          //then
          assertThat(allByActive).hasSize(1);
      }
  ```

- String 또한 사용가능합니다.

- `java.util.Date`, `java.time.LocalDate`, `java.time.LocalDateTime`, 그리고 `java.time.LocalTime`이 사용가능합니다.

- 사용하는 데이터베이스가 지원을 한다면 위에 언급한 타입들의 배열 혹은 Collection 타입을 사용할 수 도 있습니다.

- 다른 entity를 참조하는 경우, 1:1 연관 관계 혹은 `embedded` 타입인 경우가 있습니다.  
  1:1 연관 관계인 경우 참조 되는 엔티티의 `id`는 선택사항(optional)입니다.  
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

  `embedded` 엔티티의 경우 `id` 필드는 사용할 수 없습니다.

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
  
      @Embedded.Nullable
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
   * embedded Entity 이다.
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

  

- `Map<some type, entity>` 과 `List<entity>`는 두개의 추가 Column이 필요하다.  
  fk의 대상이 되는 Column의 이름으로 [참조하는 테이블의 이름]이 필요하다.  
  그리고 Map과 List의 Key로 사용될 Column으로 [참조하는 테이블 이름]_KEY 로 되어있는 Column이 필요하다.  

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

- `List` 의 경우 `Map<Integer, Entity>` 처럼 동작한다고 보면된다.

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

### 일대다 연관관계에서 Collection을 사용 할 때

데이터베이스에 Spring Data JDBC가 지정한대로 Column 이름이 저장되어 있으면 그리 어렵지 않게 사용 할 수 있다.

그런데 누가 참조키 Column이름을 [참조하는테이블이름]으로 짓겠는가?  
Column이름과 기본전략이 상이한 경우 다음과 같이 사용할 수 있다.

`@MappedCollection`를 이용하여 문제를 해결할 수 있다.

## CustomConversion 사용하기

사실 엔티티에 객체를 맵핑하는것은 그리 큰 문제가 아니다.

