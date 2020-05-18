# static 아닌 폴더에서 정적 정보 읽어오기

스프링 부트에 web 의존성을 추가하고 `localhost:8080`으로 접근하면  
기본적으로 `resources` 폴더에 있는 `static` 에 위치한 `index.html` 파일을 읽게됩니다.

프로젝트를 생성하고 다음과 같이 `index.html`을 생성하여 `static` 에 위치시키고

![image](https://user-images.githubusercontent.com/13347548/82197069-7a284f80-9935-11ea-81e3-39b306d28f72.png)

애플리케이션을 실행후 localhost:8080으로 접근하면 다음과 같이 index 페이지가 출력되는것을 확인할 수 있습니다.

![image](https://user-images.githubusercontent.com/13347548/82197119-86aca800-9935-11ea-94f7-81dee6f45b45.png)

`static` 에서 접근하는 것은 문제가 없습니다.  
그러나 html 파일들은 `static`이 아닌 `resources` 밑에 `templates` 폴더에서 관리를 하고 싶어진다면 문제가 발생합니다.

아래와 같이 `home.html`을 `resources` 밑 `templates`에 위치시키고

![image](https://user-images.githubusercontent.com/13347548/82197452-00dd2c80-9936-11ea-9e8a-7daad9d15c22.png)

간단한 컨트롤러를 통해 접근을 시도해 보겠습니다.

![image](https://user-images.githubusercontent.com/13347548/82197659-37b34280-9936-11ea-99b3-6506d35dfd34.png)

결과는 실패합니다. 404 not found가 발생합니다.

![image](https://user-images.githubusercontent.com/13347548/82197739-4f8ac680-9936-11ea-9677-10170351b35e.png)

## 해결하기

먼저 이를 해결하는 방법은 다음과 같습니다.

스프링 부트에서 제공하는 WebMvc 설정을 유지하며 기능을 확장하기 위해  
`WebMvcConfigurer`를 `implements`하도록 합니다.

그리고 `addResourceHandlers`를 오버라이드하여 다음과 같이 작성합니다.

![image](https://user-images.githubusercontent.com/13347548/82203320-1d7d6280-993e-11ea-9d0f-21f5987e98bd.png)

1. `/` 를 시작으로 하는 모든 요청을 다룬다는 것을 의미합니다.
2. 1번에 해당하는 요청을 처리할 자원을 찾을 위치를 나타냅니다.
3. 요청에 대한 Cache를 10분으로 설정한 것입니다. 이 부분은 신경쓰지 않으셔도 좋습니다.

설정을 마치고 다시 `localhost:8080/home`으로 접근해 보겠습니다.

![image](https://user-images.githubusercontent.com/13347548/82203500-5f0e0d80-993e-11ea-95fa-5964a2fd8e09.png)

정상적으로 접근이 되는군요!

그러나 다시 localhost:8080 로 접근하여 index 페이지를 보려한다면 실패하게 됩니다.

![image](https://user-images.githubusercontent.com/13347548/82203599-7f3dcc80-993e-11ea-91d5-bcab0cd0af81.png)

이는 우리가 모든 요청 `/**`에 대해 자원을 찾을 위치를 `classpath:/templates/` 로 설정하였기 때문에  
기존 `classpath:/static/` 에 위치한 index.html 을 찾지 못하여 발생하는 문제입니다.

`templates`를 리소스 경로에 추가해준것 처럼 `static` 경로를 추가하면 문제는 해결됩니다.

![image](https://user-images.githubusercontent.com/13347548/82206112-97afe600-9942-11ea-94eb-bde40754f6dd.png)



## 어째서?

먼저 Spring boot는 기본적으로 바라보는 정적 자원의 위치가 있습니다.

`ResourceProperties` 클래스를 보면 명시가 되어있는데요. 그 위치는 다음과 같습니다.

![image](https://user-images.githubusercontent.com/13347548/82206616-80bdc380-9943-11ea-89c3-7b9d442715d6.png)

처음  `addResourceHandlers` 를 오버라이드 하지 않았을때는 위 경로로만 정적 파일을 탐색하였기 때문에  
`templates` 폴더를 탐색하지 않아 우리가 원하는 `home.html`을 렌더링 하지 못하고 있었던 것입니다.

그런데 왜 핸들러를 추가했다고 `static` 경로에 있던 `index.html` 을 찾지 못하게 된 것 일까요?

## WebMvcConfiguration

조금 전 기본적으로 Spring boot 가 제공하는 기능을 위해 `WebMvcConfigurer`를 `implements` 를 했었습니다.  
이때 기본적으로 제공하는 기능은 `WebMvcAutoConfiguration`가 제공해줍니다.

그리고`WebMvcAutoConfiguration`의  `addResourceHandlers` 메서드를 오버라이드 하는 부분을 보면  
다음과 같이 코드가 작성되어 있습니다.

![image](https://user-images.githubusercontent.com/13347548/82207116-47d21e80-9944-11ea-8a34-3eecf2bfe348.png)

코드에서 볼 수 있듯 `/**` 경로로 아무런 핸들러가 설정되어 있지 않다면  
기본으로 제공하는 정적파일의 위치와 함께 핸들러를 제공하게됩니다.

그러나 우리가 만든 핸들러는 `/**`를 경로로 하였기때문에 `if`구문을 지나치게 되고  
`/static`를 비롯한 다른 정적 파일 위치를 등록하지 않아 탐색에서 벗어나게 됨으로써  
`index.html`에 접근하지 못한 것 입니다.



## .html suffix 설정

매번 .html을 써주기 귀찮고 보기에도 좋지 않습니다.

이때 application.properties 혹은 .yml을 이용해서 손쉽게 바꿀수 있습니다.

.properties 기준 다음과 같이 작성하면 되고

`spring.mvc.view.suffix=.html`

.yml 기준 다음과 같이 작성하면 됩니다.

```java
spring:
  mvc:
    view:
      suffix: .html
```

그리고 `Controller`에서 `return`하던 `String` 에서`.html`을 제거한뒤 사용하면 됩니다.



## 맺으며

우아한 테크코스 레벨2를 진행하며 templates에 위치한 html을 읽게 해주고 싶었는데 방법을 명확히 알지 못했습니다.  
사용하지도 않을 템플릿 엔진 의존성을 단지 templates 경로 탐색을 위해 추가해야 한다는 점에서  
handlebars나 thymeleaf 의존성을 추가하기 싫었습니다.

이왕 해결하는 김에 정확히 어떻게 동작하는지 천천히 확인해 봤는데 좋은 학습 방법을 찾은 것 같아 기분이 좋습니다.