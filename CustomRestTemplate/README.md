# Spring 의존성 없이 RestTemplate Test 하기

`RestTemplate`를 처음 사용하면서 Spring 의존성 없이 테스트 할 수 있도록 구조를 설계하여 테스트 코드를 작성한 방법에 대해 공유하고자 합니다.

## 사용된 외부 API

리그오브레전드를 운영하는 [Riot에서 제공하는 API](https://developer.riotgames.com)를 사용하였습니다.

## 구조 설계

먼저 `RestTemplate`을 한번 감싸서 사용하는 프로젝트만의 `RestTemplate`의 기능을 만들어 둡니다.  
아래 코드에서는 `findSummonerByName(String name)`이 되겠습니다.

```java
public class SummonerRestTemplate {
    private static final String SUMMONER_V4_FIND_BY_NAME_URL = "/lol/summoner/v4/summoners/by-name/{summonerName}";

    private final RestTemplate restTemplate;

    public SummonerRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SummonerDto findSummonerByName(String name) {
        return restTemplate.getForObject(SUMMONER_V4_FIND_BY_NAME_URL, SummonerDto.class, name);
    }

}

// 통신을 통해 받을 객체
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummonerDto {
    @JsonAlias("id")
    private String summonerId;
    private String accountId;
    private String name;

    @Builder
    public SummonerDto(String summonerId, String accountId, String name) {
        this.summonerId = summonerId;
        this.accountId = accountId;
        this.name = name;
    }
}
```

이제 `SummonerRestTemplate`에서 사용 할 레스트 템플릿을 위해 필요한 공통적인 설정을 위한 레스트템플릿 빌더를 만들어 둡니다.  
바로 레스트 템플릿을 반환하는게 아닌 빌더를 반환하는 이유는 가장 공통적인 설정만 하고 다른 곳에서 입맛에 맞게 나머지 설정을 할 수 있게 하기위해서 입니다. (ex.`READ_TIME_OUT`)

```java
public class RiotRestTemplateBuilder {
    private static final String HOST = "https://kr.api.riotgames.com";

    public static RestTemplateBuilder get(RiotProperties riotProperties) {
        return new RestTemplateBuilder()
                .rootUri(HOST)
                .additionalInterceptors(new RiotTokenInterceptor(riotProperties))
                .errorHandler(new RiotErrorHandler());
    }
}
```

> RiotPerperties는 Riot API와 통신할때 인증을 위한 토큰입니다.
> 무시하셔도 됩니다.

위와 같이 `Interceptor`와 `ErrorHandler`와 같은 공통적인 설정만 해주도록 합시다.

> Interceptor와 ErrorHanlder의 본 게시글의 성격상 중요하지 않기 때문에 자세한 구현 내용은 넘어가도록 하겠습니다.

이제 우리가 만든 `SummonerRestTemplate`를 사용하는 클라이언트를 위해 Bean으로 등록을 해주도록 합니다.

```java
@Configuration
@RequiredArgsConstructor
public class SummonerRestTemplateConfiguration {

    private static final Duration READ_TIME = Duration.ofSeconds(30);
    private static final Duration CONN_TIME = Duration.ofSeconds(30);

    private final RiotProperties riotProperties;

    @Bean
    public SummonerRestTemplate summonerRestTemplate() {
        RestTemplate restTemplate = RiotRestTemplateBuilder.get(riotProperties)
                .setReadTimeout(READ_TIME)
                .setConnectTimeout(CONN_TIME)
                .build();
        return new SummonerRestTemplate(restTemplate);
    }
}
```

이제 모든 준비가 끝났으니 실제 통신이 제대로 동작하는지 테스트를 작성합니다.  
테스트는 Junit5, assertJ 를 사용했습니다.

```java
@SpringBootTest
@ExtendWith(SpringExtension.class)
class SummonerRestTemplateTest {

    @Autowired
    private SummonerRestTemplate summonerRestTemplate;

    @DisplayName("이름으로 사용자 찾기")
    @Test
    void findSummonerByName() {
        //given
        String name = "이상한새기";

        //when
        SummonerDto summonerDto = summonerRestTemplate.findSummonerByName(name);

        //then
        assertThat(summonerDto.getName()).isEqualTo(name);
    }

}
```

<center>정상적으로 테스트가 통과하는 것을 확인할 수 있습니다.  </center>

<img src="https://user-images.githubusercontent.com/13347548/73444197-7202cf00-439b-11ea-9e19-9f87e187464f.png" alt="image" style="zoom:50%;" />

이렇게 실제 통신을 하는 테스트만 만들어 두면 세 가지 문제점이 있습니다.

1. 만약 API 통신에 요금이 든다면 매번 **과금**이 될 수 있다.  
2. **통신이 불가능한 상황**(인터넷 연결 X)이라면 테스트를 할 수 없다.
3. 스프링이 올라간다음 테스트를 진행하기 때문에 속도가 **느립니다**.

따라서, 우리가 만든 `SummonerRestTemplate`가 어떠한 동작을 하는지 실제 통신을 통하지 않고 빠르게 테스트 코드를 통해 확인할 수 있도록 만들어야합니다.  


## MockRestServiceServer

`MockRestServiceServer`는 RestTemplate를 테스트 하기 위한 Spring 라이브러리 입니다.  
따라서 RestTemplate의 response 값을 Mock할 수 있는 기능을 제공합니다.  
Spring을 띄워 통신 결과를 반환하는 것이 아니라 localhost를 통해 테스트 하기 때문에 속도가 굉장히 빠릅니다!

`MockRestServiceServer`에게 Mock할 RestTemplate 객체를 넘겨주고 넘겨준 RestTemplate에게 특정 요청에 대한 행동을 지정해주도록 해야합니다.  
먼저 `MockRestServiceServer`를 만들어 두도록 합니다.

```java
    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = RiotRestTemplateBuilder.get(new RiotProperties()).build();
        summonerRestTemplate = new SummonerRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
```

위에서 만들어 두었던 `RiotRestTemplateBuilder`를 통해 Mock할 RestTemplate를 만들어 줍니다.  
그리고 우리가 테스트하기 위한 `SummonerRestTemplate`와 MockServer에 주입해주도록 합니다.  
이로 인해 우리가 테스트 하는 `SummonerRestTemplate`에서 동작하는 기능은 MockServer에서 정의한 대로 동작을 하게 됩니다.

이제 MockServer에서 특정 요청에 대한 행동을 지정해 두도록 합니다.

```java
this.mockServer.expect(requestTo(SUMMONER_BY_NAME_URL + encodedName))
                .andRespond(withSuccess(expectBody, MediaType.APPLICATION_JSON));
```

요청 주소(`requestTo`)가 `SUMMONER_BY_NAME_URL + encodedName`인 곳에 대한 응답(`andRespond`)으로  
성공(`withSuccess`)과 함께 응답 값(`expectBody, MediaType.APPLICATION_JSON`)을 내려주도록 지정해 둡니다.  
그리고 테스트 마지막에 **반드시** `verify()` 메소드를 호출하도록 합니다. `verify` 메소드는 mockserver에서 사용된 resttemplate가 호출 됬는지 확인하여 테스트가 올바른지 확인합니다.

```java
this.mockServer.verify();
```

완성된 테스트 코드는 다음과 같습니다.

```java
class SummonerRestTemplateMockTest {
    private static final String SUMMONER_BY_NAME_URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/";

    private SummonerRestTemplate summonerRestTemplate;

    private MockRestServiceServer mockServer;


    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = RiotRestTemplateBuilder.get(new RiotProperties()).build();
        summonerRestTemplate = new SummonerRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("이름으로 사용자 찾기")
    @Test
    void findSummonerByName() {
        //given
        String name = "이상한새기";
        String encodedName = UriEncoder.encode(name);

        String expectBody = "{\n" +
                "    \"profileIconId\": 2095,\n" +
                "    \"name\": \"이상한새기\",\n" +
                "    \"puuid\": \"0unup92CswoD3CGo6qwydHzATD4pePmpi3XhZA-kX2urduks6nJke6nlnSpmJn0hEUPgHzuo0d5Tgg\",\n" +
                "    \"summonerLevel\": 98,\n" +
                "    \"accountId\": \"w94qxPIxhJ2ALZoRItVSwyN6R-CNMXOE1VJwesmrZdAv\",\n" +
                "    \"id\": \"zN1v1n2XlkIY9cYKj9XydSSKItQNRtDLVdJHEWIkVhN5fQ\",\n" +
                "    \"revisionDate\": 1570881947000\n" +
                "}";

        this.mockServer.expect(requestTo(SUMMONER_BY_NAME_URL + encodedName))
                .andRespond(withSuccess(expectBody, MediaType.APPLICATION_JSON));

        //when
        SummonerDto summonerDto = summonerRestTemplate.findSummonerByName(name);

        //then
        assertThat(summonerDto.getName()).isEqualTo(name);
      	this.mockServer.verify();
    }
}
```

<img src="https://user-images.githubusercontent.com/13347548/73444307-aecec600-439b-11ea-87e3-ff240ef6276a.png" alt="image" style="zoom:50%;" />

<center>정상적으로 통과하고 Spring을 띄운 테스트 보다 훨씬 빠른 속도인것을 확인 할 수 있습니다.</center>

이어서 RestTemplate에 미리 등록한 `ErrorHanlder`에서 발생하는 `exception`을 테스트하기 위해 통신 실패에 따른  테스트를 작성해 보도록 하겠습니다.

```java
    @DisplayName("이름으로 사용자 찾기 실패")
    @Test
    public void findSummonerByName2() {
        //given
        String summonerName = "이상한새기";
        String encodedName = UriEncoder.encode(summonerName);

        String exceptBody = "{\n" +
                "    \"status\": {\n" +
                "        \"status_code\": 400,\n" +
                "        \"message\": \"BadRequest\"\n" +
                "    }\n" +
                "}";

        this.mockServer.expect(requestTo(SUMMONER_BY_NAME_URL + encodedName))
                .andRespond(withBadRequest()
                        .body(exceptBody)
                        .contentType(MediaType.APPLICATION_JSON));

        //when
        //then
        assertThatThrownBy(() -> summonerRestTemplate.findSummonerByName(summonerName))
                .isInstanceOf(NoSuchElementException.class);
				this.mockServer.verify();
    }
```

<img src="https://user-images.githubusercontent.com/13347548/73444349-c443f000-439b-11ea-9c7d-10e02deb3d95.png" alt="image" style="zoom:50%;" />

<center>실패 테스트도 정상적으로 통과하였습니다.</center>

바뀐 부분은 `andRespond()` 블럭 내 `withBadRequest`가 있고 따로 `body`, `contentType`을 지정하는 것임을 확인 할 수 있습니다.  
만약 BadRequest가 아닌 다른 `status code`를 테스트 하고 싶다면 `withStatus`를 사용하면 됩니다.

## 구조 개선

현재 상태로는 한 가지 문제점이 있습니다.  
바로 `SummonerRestTemplate`를 사용하는 쪽에서 테스트 코드를 작성할 경우 실제 통신이 이루어져야 하기 때문에 Spring이 반드시 떠야한다는 문제점이 있습니다.  
이를 해결하기 위해 `SummonerRestTemplate`를 `Interface`로 변경하고 `Configuration` 코드를 변경해 주도록 합니다.  
다시 말해, profile 별로 실행되는 `SummonerRestTemplate`를 만들어 주도록 합시다.

```java
public interface SummonerRestTemplate {
    SummonerDto findSummonerByName(String name);
}

public class MajorSummonerRestTemplate implements SummonerRestTemplate {
    private static final String SUMMONER_V4_FIND_BY_NAME_URL = "/lol/summoner/v4/summoners/by-name/{summonerName}";

    private final RestTemplate restTemplate;

    public MajorSummonerRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SummonerDto findSummonerByName(String name) {
        return restTemplate.getForObject(SUMMONER_V4_FIND_BY_NAME_URL, SummonerDto.class, name);
    }

}

public class StubSummonerRestTemplate implements SummonerRestTemplate {
    @Override
    public SummonerDto findSummonerByName(String name) {
        return SummonerDto.builder()
                .name(name)
                .accountId("stubAccountId")
                .summonerId("stubSummonerId")
                .build();
    }
}
```

<img src="https://user-images.githubusercontent.com/13347548/73446489-34547500-43a0-11ea-9942-aeecdea3aac9.png" alt="image" style="zoom:50%;" />

<center>변경된 구조<center>

`StubSummonerRestTemplate`는 통신을 하지 않고 항상 고정된 `SummonerDto`를 반환도록 합니다.

이제 Profile에 따라 다른 Bean을 띄우도록 `SummonerRestTemplateConfiguration`을 수정하도록 합니다.

```java
public class SummonerRestTemplateConfiguration {

    private static final Duration READ_TIME = Duration.ofSeconds(30);
    private static final Duration CONN_TIME = Duration.ofSeconds(30);

    @Profile("major")
    @Configuration
    @RequiredArgsConstructor
    public static class MajorConfig {
        private final RiotProperties riotProperties;

        @Bean
        public SummonerRestTemplate summonerRestTemplate() {
            RestTemplate restTemplate = RiotRestTemplateBuilder.get(riotProperties)
                    .setReadTimeout(READ_TIME)
                    .setConnectTimeout(CONN_TIME)
                    .build();
            return new MajorSummonerRestTemplate(restTemplate);
        }
    }

    @Profile("local")
    @Configuration
    public static class LocalConfig {
        @Bean
        public SummonerRestTemplate summonerRestTemplate() {
            return new StubSummonerRestTemplate();
        }
    }

}
```

이제 profile active의 상태가 `major`라면 `MajorConfig`가 `local`라면 `TestConfig`가 실행되어 상황에 알맞게 `SummonerRestTemplate` Bean이 생성되게 되었습니다.

## 의존성 분리

Spring 의존성 없이 테스트 코드를 작성하기 위해서는 커스텀하게 사용하는 `SummonerRestTeamplate`에서 사용하는 `RestTemplate`가 외부에서 주입받는 형식으로 의존성을 바깥으로 분리해야 했습니다.  
처음 코드를 작성할때는 RestTemplate를 내부적으로 생성해서 사용하였기 때문에 Spring 이 반드시 필요한 테스트 밖에 작성하지 못 했습니다.

테스트 하기 어렵고 원하는대로 테스트가 되지 않는다면 구조가 잘못 되었는지 의심하는 생각을 해야 할 것 같습니다.
