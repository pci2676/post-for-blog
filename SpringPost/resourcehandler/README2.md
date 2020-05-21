# 로컬 환경에서 정적 리소스 리로드 하기

## 개요

우테코 레벨 2를 진행하면서 웹 페이지, 특히 js를 많이 작성하게 됐습니다.

그러다 보니 정적 파일(특히 js)을 수정하면 Spring boot에서 실행중이던 app을 재시작해서  
수정된 정적 정보를 다시 불러와야 했습니다.

Spring devtools 의존성을 추가해서 리로드 하는 방식이 있지만  
빌드를 느려지게 하는데다 개발환경 외에 사용되지 않을 의존성을 추가하고 싶지 않았습니다.  
~~게다가 필자는 설정해도 됬다 말았다;;~~

얼마전 `WebMvcConfigurer`에서 `resourceHandler`를 다뤘는데  
이를 이용하면 간단하게 해결이 된다는 것을 알게되어 공유하고자 합니다.

## 바라보는 정적 리소스의 위치 수정하기

아래는 이전에 작성한 `classpath` 아래의 자원을 바라보고 있는 설정입니다.  

```java
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/", "classpath:/static/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }
}

```

이때 `classpath`는 gradle로 빌드한 결과의 `resources` 디렉토리(ex. build/resources)를 의미합니다.

이제 바라보고 있는 디렉토리를 `file` 키워드를 이용해 아래와 같이 `src/main/resources`로 변경해 주도록 합니다.

```java
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/templates/", "file:src/main/resources/static/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }
}
```

이로써 매번 Spring boot를 재기동하지 않아도 정적 자원이 수정되는 것을 바로 바로 볼수 있게 되었습니다.

## 프로파일을 이용해 분리하기

Spring boot는 `profile` 별로 다른 설정을 가지고 실행을 할 수 있습니다.

현재 코드는 언제나 `file:src/main/resources/` 를 바라보고 있습니다.  
실제 서비스 환경에서는 다시 `classpath:`를 바라보아야 합니다.

이를 위해서  
 `local` 환경에서는 `file:` 을
`prod` 환경에서는 `classpath:` 를 바라보도록 환경설정을 `profile`을 이용해 분리해 주도록 합시다.

### Inner Class 활용하기

먼저 Inner Class 를 두개 만들어 주도록 합시다.

하나는 `prod` 환경에서 사용할 것이고 다른 하나는 `local` 환경에서 사용할 class 입니다.

```java
@Configuration
public class MvcConfiguration {

    @Profile("prod")
    @Configuration
    public static class ProdMvcConfiguration {

    }

    @Profile("local")
    @Configuration
    public static class LocalMvcConfiguration {

    }

}
```

`@Profile`에 value로 해당 환경에 대한 이름을 정해주도록 합니다.

이어서 각각 `WebMvcConfigurer` 를 `implements` 해주고  
각 환경에 알맞게 `resourceHandler`를 작성해 주도록 합니다.

```java
@Configuration
public class MvcConfiguration {

    @Profile("prod")
    @Configuration
    public static class ProdMvcConfiguration implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/templates/", "classpath:/static/")
                    .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
        }
    }

    @Profile("local")
    @Configuration
    public static class LocalMvcConfiguration implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("file:src/main/resources/templates/", "file:src/main/resources/static/")
                    .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
        }
    }

}
```

위와 같이 환경에 따라 분리가 끝났다면 환경을 선택해서 Spring boot를 실행하면 됩니다.

`application.properties` 혹은 `.yml`에서 사용할 `prod`환경을 활성화 시켜주도록 합시다.

#### .properties

```properties
spring.profiles.active=prod
```

#### .yml

```yml
spring:
  profiles:
    active: prod
```

이렇게 하면 Spring boot를 build 하여 생성되는 jar가 실행될 때는   
JVM 옵션으로 `acitve` 값을 주지 않으면 `prod` 환경으로 실행됩니다.

하지만 우리는 개발 할때 local 환경에서 실행하기를 원합니다.  
아래와 IntelliJ에서 JVM 옵션을 설정하고 Spring boot를 실행하도록 합시다.

![image](https://user-images.githubusercontent.com/13347548/82581258-41090d00-9bcb-11ea-9278-78b90aefe4f7.png)

![image](https://user-images.githubusercontent.com/13347548/82581302-59792780-9bcb-11ea-8a3f-e4fadac46768.png)

![image](https://user-images.githubusercontent.com/13347548/82581351-6e55bb00-9bcb-11ea-953e-bfcd9e7f5724.png)

이렇게 하면 jar 파일을 실행할때 `-Dspring.profiles.active=local` 옵션을 주고 실행한 것과 같습니다.

## 맺으며

딱히 이 방법이 정답이라고 생각하지는 않기 때문에  
더 좋은 방법이 있으면 피드백 부탁드리겠습니다!

감사합니다.
