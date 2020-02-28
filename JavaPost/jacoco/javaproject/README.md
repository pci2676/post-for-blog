# 좌충우돌 Jacoco 적용기

백기선님의 더 자바 강의를 보던 중 바이트 코드를 다루는 방법의 예시로 jacoco가 언급되었고 테스트 커버리지를 측정하여 build를 성공, 실패 시킬수 있다는 점을 알게되었습니다.

이를 이용하여 ''스터디에서 진행하는 장기 프로젝트와 토이 프로젝트에 적용하면 재미있겠다!' 라는 생각에 적용하며 겪은 내용을 정리해 보고자 합니다!

## Jacoco 적용하기

단순히 적용하는 것은 어렵지 않았습니다!  
[jacoco 유저 가이드](https://docs.gradle.org/current/userguide/jacoco_plugin.html#header)와 [우아한 형제들의 jacoco 포스팅](https://woowabros.github.io/experience/2020/02/02/jacoco-config-on-gradle-project.html)을 보면서 쉽게 적용할 수 있었습니다.

### jacoco plugin 추가

먼저 `plugins` 블록에 `id 'jacoco'` 를 추가해 주면   

```groovy
plugins {
    id 'jacoco'
}
```

<img src="https://user-images.githubusercontent.com/13347548/75556124-f3c04800-5a80-11ea-92d1-0ffbbc0c1f3f.png" alt="image" style="zoom:50%;" />

`jacocoTestReport` 와 `jacocoTestCoverageVerification` task가 gradle verification항목에 추가됩니다.

주의 해야 할 점은 생성된 두 개의 task는 `test` 가 먼저 실행된 다음에 실행이 되어야 합니다.  

<img src="https://user-images.githubusercontent.com/13347548/75570452-87524280-5a9a-11ea-827f-2541ea547acc.png" alt="image" style="zoom:50%;" />

> [jacoco 유저 가이드](https://docs.gradle.org/current/userguide/jacoco_plugin.html#header)에서

jacoco plugin이 성공적으로 적용되었다면 `jacoco` 로 이름이 붙은 `JacocoPluginExtension` 타입의 project extentions을 사용하여 빌드 파일에서 사용 될 기본적인 설정을 해줄수 있습니다.  
`JacocoPluginExtension` 에서 설정해 줄 수 있는 값은 아래 두가지 입니다.

1. toolVersion : Jacoco의 jar 버전
2. reportsDir : Jacoco report 결과물 디렉토리

우리는 reportsDir 은 `jacocoTestReport` 에서 설정해면 되니까 toolVersion만 다음과 같이 명시해 주도록 합시다. (작성일(2020.02.28) 기준 기본값이 0.8.5 이다.)

```groovy
jacoco {
    toolVersion = "0.8.5"
}
```

> [이곳](https://www.eclemma.org/jacoco/)에서 버전 정보를 확인 할 수 있다.

<img src="https://user-images.githubusercontent.com/13347548/75556225-223e2300-5a81-11ea-9216-06a5fe326460.png" alt="image" style="zoom:50%;" />

### jacocoTestReports 설정

이제 우리가 테스트 결과를 받는 부분을 설정해 주기 위해 jacocoReports 설정을 해야합니다.  
jacocoReport task는 html, csv, xml 형태로 커버리지 측정 결과를 알려주는 역할을 합니다.

```groovy
jacocoTestReport {
    reports {
            html.enabled true
            csv.enabled true
			      xml.enabled false
    }
}
```

위와 같이 설정한다면 html과 csv 형태로 report를 제공합니다.  

> csv 형태로 제공해야 Sonar Qube에서 커버리지 측정을 확인할 수 있습니다.
>
> 이 부분에서 추가적으로 설정해 줄 수 있는 설정 값은 [이곳](https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.tasks.JacocoReport.html)에서 확인 할 수 있습니다.

<img src="https://user-images.githubusercontent.com/13347548/75556289-44d03c00-5a81-11ea-87ce-f08444783d2b.png" alt="image" style="zoom:50%;" />

### jacocoTestCoverageVerification

jacoco의 꽃이라 생각하는 커버리지 검증 수준을 정의하는 부분입니다.  
이 부분에서 jacoco의 report 검사하여 설정한 최소 수준을 달성하지 못하면 task는 실패를 하게 됩니다.

여러 수준의 정의를 `violationRules` 에서 다수의 `rule` 에 정의하여 사용할 수 있습니다.

```groovy
jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = true
                element = 'CLASS'
                // includes = []

                limit {
                    counter = 'LINE'
                    value = 'COVEREDRATIO'
                    minimum = 0.60
                }

                excludes = []
            }
          
          	rule {
            	...
          	}

        }
    }
```

가장 중요한 부분이라 생각하는 만큼 각 설정값의 의미들에 대해 알아보도록 하겠습니다.

1. enabled : 해당 rule의 활성화 여부를 boolean으로 나타냅니다.  
   명시적으로 지정하지 않으면 default로 true입니다.

2. element : 측정의 큰 단위를 나타냅니다.  
   

3. includes : rule 적용 대상을 package 수준으로 정의합니다.  
   아무런 설정을 하지 않는다면 전체 적용됩니다.

4. limit : rule의 상세 설정을 나타내는 block 입니다.

   - [counter](https://www.eclemma.org/jacoco/trunk/doc/counters.html) : 커버리지 측정의 최소 단위를 나타냅니다.  
     이 때 측정은 java byte code가 실행된 것을 기준으로 counting됩니다.  
     counter의 종류는 CLASS, METHOD, LINE, BRANCH 등이 있습니다.  
     - CLASS : 클래스 내부 메소드가 한번이라도 실행된다면 실행된 것으로 간주됩니다.  
     - METHOD : 클래스와 마찬가지로 METHOD가 한번이라도 실행되면 실행된 것으로 간주됩니다.  
     - LINE : 한 라인이라도 실행되었다면 측정이 됩니다.  
       개인적으로 LINE이 커버리지 측정에 가장 도움이 되는것 같습니다.  
     - BRANCH : `if`, `switch` 구문에 대한 커버리지 측정을 합니다.

   - value : 측정한 counter의 정보를 어떠한 방식으로 보여줄지 정합니다.  
     value의 종류는 TOTALCOUNT, COVEREDCOUNT, MISSEDCOUNT, COVEREDRATIO, MISSEDRATIO 가 있습니다.  
     이름에서 충분히 그 뜻을 유추 할 수 있기때문에 따로 설명은 하지 않겠습니다.  
     커버리지 측정에서 우리는 얼마나 우리의 코드가 테스트 되었는지 확인하면 되기 때문에 커버된 비율을 나타내는 COVEREDRATIO를 사용합시다.
   - minimum : count값을 value에 맞게 표현했을때 최소 값을 나타냅니다.  
     이 값으로 jacoco coverage verification이 성공할지 못할지 판단합니다.  
     해당 값은 0.00 부터 1.00사이에 원하는 값으로 설정해주면 됩니다. 

5. excludes 

   - verify 에서 제외할 클래스를 지정할 수 있습니다.  
     패키지 레벨의 경로를 지정해주도록 합니다.  
     경로에는 와일드 카드로 `*`와 `?`를 사용할 수 있습니다.  
     이 부분은 아래에서 다시한번 다루겠습니다!

### jacoco 실행

이제 필요한 설정은 모두 끝마쳤으니 jacoco의 report를 받아봅시다!

테스트를 위해서 아래와 같이 양수 값을 나타내는 클래스를 만들고 테스트 코드를 작성해 봅시다.

```java
public final class PositiveNumber {
    private final long value;

    public PositiveNumber(long value) {
        validate(value);
        this.value = value;
    }

    private void validate(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%d 는 양수가 아닙니다.", value));
        }
    }

    public PositiveNumber add(PositiveNumber positiveNumber) {
        return new PositiveNumber(value + positiveNumber.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositiveNumber that = (PositiveNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
```

테스트 코드 (Junit 4)

```java
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PositiveNumberTest {

    @Test
    public void notPositiveValueThrowException() {
        long notPositive = 0;

        assertThatThrownBy(() -> new PositiveNumber(notPositive))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
```

처음에 언급했듯이 jacoco의 report를 받기 위해서는 task의 순서가 중요합니다.  
jacoco 문서에 따르면 test task를 먼저 실행한 다음 jacoco task 가 실행되어야 한다고 합니다.

**test -> jacocoTestReport -> jacocoTestCoverageVerification**

위 순서대로 task가 실행되도록 `finalizedBy`를 이용해서 아래와 같이 수정해 주도록 합시다.

<img src="https://user-images.githubusercontent.com/13347548/75558414-253b1280-5a85-11ea-8f5b-978e3fac4897.png" alt="image" style="zoom:50%;" />

그리고 `./gradlew test` 명령어로 test task를 실행하면..!

<img src="https://user-images.githubusercontent.com/13347548/75558541-51569380-5a85-11ea-8d7e-3cfee9a5f619.png" alt="image" style="zoom:50%;" />

빌드 실패합니다!  
우리가 기존에 설정한 LINE 커버리지는 60%인데 테스트 코드는 37% 이기 때문입니다.

이제 생성된 report를 확인해 봅시다.

<img src="https://user-images.githubusercontent.com/13347548/75558933-e5c0f600-5a85-11ea-9afd-0818fd74e480.png" alt="image" style="zoom:50%;" />

<img src="https://user-images.githubusercontent.com/13347548/75559325-9929ea80-5a86-11ea-829f-8dab7529abbf.png" alt="image" style="zoom:50%;" />

> 뒤늦게 equals , hashCode 메소드를 추가해서 이 부분에선 나타나지 않았습니다 ㅎㅎ..

초록색은 conter 기준에 통과한 부분, 빨간색은 통과하지 못한 부분, 노란색은 절반만 통과한 부분을 나타냅니다.

현재 테스트는 Junit4 기반으로 작성되어 있습니다.  
의존성에 Junit5를 추가하고 Junit5기반 테스트를 추가하여 부족한 커버리지를 올려보도록 합시다.

```groovy
testImplementation('org.junit.jupiter:junit-jupiter:5.6.0')
```

추가된 JUnit5 기반 테스트 코드

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositiveNumberJunit5Test {

    @DisplayName("양수 생성")
    @Test
    void createTest() {
        long positive = 1;
        PositiveNumber positiveNumber = new PositiveNumber(positive);

        assertThat(positiveNumber).isEqualTo(new PositiveNumber(1));
    }

    @DisplayName("양수 덧셈")
  	@Test
    void addTest() {
        //given
        PositiveNumber result = new PositiveNumber(2);

        PositiveNumber positiveNumber1 = new PositiveNumber(1);
        PositiveNumber positiveNumber2 = new PositiveNumber(1);

        //when
        PositiveNumber expect = positiveNumber1.add(positiveNumber2);

        
        //then
        assertThat(expect).isEqualTo(result);
    }
}
```

<img src="https://user-images.githubusercontent.com/13347548/75561940-266f3e00-5a8b-11ea-8503-7a365d2caef7.png" alt="image" style="zoom:50%;" />

가뿐하게 새로 작성한 테스트 코드가 통과하네요!  
이제 jacoco의 성공한 report를 받아보러 test task를 실행해 봅시다!

<img src="https://user-images.githubusercontent.com/13347548/75562254-a1385900-5a8b-11ea-95dc-bc5cfed44ded.png" alt="image" style="zoom:50%;" />

엥? 실패합니다;  
아무리 jacoco와 인텔리제이 커버리지 측정방식이 달라도 인텔리제이 테스트로 실행을 해보면 아래와 같이 92%의 라인 커버리지를 보여주는 데도 말이죠!

<img src="https://user-images.githubusercontent.com/13347548/75562343-c2994500-5a8b-11ea-8449-27937bd72a83.png" alt="image" style="zoom:50%;" />

이는 gradle이 junit4 테스트만 읽고 있었기 때문인데요!

이를 해결하기 위해서 test, dependencies block에 junit4와 junit5를 동시에 쓸수 있도록 다음과 같이 추가해 줍니다.

<img src="https://user-images.githubusercontent.com/13347548/75568829-58869d00-5a97-11ea-9256-0265ce27cc63.png" alt="image" style="zoom:50%;" />

그리고 다시 test task를 실행하면..!

<img src="https://user-images.githubusercontent.com/13347548/75569081-cb901380-5a97-11ea-94d8-48435f02ed51.png" alt="image" style="zoom:50%;" />

ㅠㅠ 드디어 통과했습니다!

<img src="https://user-images.githubusercontent.com/13347548/75569128-e4002e00-5a97-11ea-9ef6-58b17c8feabf.png" alt="image" style="zoom:50%;" />

전부 통과하지는 못했지만 우리의 기준치인 60%는 넘겼네요!!!

### 기본 설정 바꾸기

jacoco에 기본적으로 설정되어 있는 값으로도 사용하는데에는 크게 문제가 없었습니다.  
만약 개발환경 혹은 테스트 설정을 바꿔야 하는경우 test block 내의 jacoco block에서 설정값을 바꿔주면 됩니다.  
아래는 전부 기본설정 값이 세팅되어 있는 경우입니다.

```groovy
test {
        useJUnitPlatform ()
        jacoco {
            enabled = true
	    address = "localhost"
            classDumpDir = null
            destinationFile = file("$buildDir/jacoco/${name}.exec")
            dumpOnExit = true
            excludeClassLoaders = []
            excludes = []
            includes = []
            jmx = false
            output = JacocoTaskExtension.Output.FILE
            port = 6300
            sessionId = "<auto-generated value>"
        }
        finalizedBy 'jacocoTestReport'
    }
```



## Spring boot Multi Module 에서 적용하기

이제 우리의 스프링 부트 프로젝트에 적용을 하러 갑시다!

간단합니다!

우리는 jacoco를 모든 프로젝트의 테스트에 적용하고 싶기 때문에 `subprojects` block에 설정값을 추가하고  
`plugins` block 대신 `apply plugin` 으로 jacoco 를 추가해주면 됩니다!

멀티 모듈 프로젝트의 뼈대를 만든다음 적용하면 다음과 같이 `build.gradle`이 완성됩니다!

```
subprojects {
    group = 'com.javabom'
    version = '1.0.0'

    apply plugin: 'java'
    apply plugin: 'io.spring.dependency-management'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'jacoco'

    sourceCompatibility = '1.8'

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation 'org.springframework.boot:spring-boot-starter-test'

        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'
    }

    jacoco {
        toolVersion = '0.8.5'
    }

    test {
        useJUnitPlatform()
        finalizedBy 'jacocoTestReport'
    }

    jacocoTestReport {
        reports {
            html.enabled true
            xml.enabled false
            csv.enabled true
        }
        finalizedBy 'jacocoTestCoverageVerification'
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                element = 'CLASS'

                limit {
                    counter = 'LINE'
                    value = 'COVEREDRATIO'
                    minimum = 0.60
                }

                excludes = []
            }

        }
    }

}
```

## 측정하지 않을 클래스 설정하기

jacoco 적용하기에서 한번 언급을 했던 측정하지 않을 클래스를 설정하는 부분입니다.

JPA를 쓰고 QuertDSL을 쓴다면 Q domain이 생기는데 이 부분은 테스트할 필요가 없습니다.  
그리고 각종 Configuration 클래스는 현재 저희 프로젝트에서는 굳이 test 커버리지에서 추가할 필요가 없다고 느껴졌습니다.

하지만 jacoco는 위 클래스들을 전부 테스트 대상으로 파악하여 프로젝트 build를 실패하게 만듭니다!

<img src="https://user-images.githubusercontent.com/13347548/75577553-8ffc4600-5aa5-11ea-9bd3-3fcf8ddcf01e.png" alt="image" style="zoom:50%;" />

> Config 클래스의 테스트 커버리지가 0이라서 실패한 모습...

이를 해결 하기 위해 아래와 같이 와일드 카드를 이용해서 coverage 대상에서 제외시키도록 하였습니다!

```groovy
jacocoTestCoverageVerification {
        violationRules {
            rule {
                element = 'CLASS'

                limit {
                    counter = 'LINE'
                    value = 'COVEREDRATIO'
                    minimum = 0.60
                }

                excludes = [
                        '*.Q*',
                        '*.*Config*'
                ]
            }

        }
    }
```

이제 build가 성공적으로 됩니다!!

### jacocoReport에서 수집되지 않도록 제외하기

하지만 한가지 문제점이 남아있었습니다.

jacoco의 테스트 커버리지 대상에서 벗어나도록 설정은 했지만 report 대상에는 그대로 남아 있어 프로젝트 커버리지 비율을 떨어뜨리고 있었습니다..!!

<img src="https://user-images.githubusercontent.com/13347548/75577823-2597d580-5aa6-11ea-95f2-0f596673dea6.png" alt="image" style="zoom:50%;" />

이를 해결하기 위해 report block에서 우리가 설정한 디렉토리는 report 결과에 포함하지 않도록 해줍시다!

```groovy
jacocoTestReport {
        reports {
            html.enabled true
            xml.enabled false
            csv.enabled true
        }

        afterEvaluate {
            classDirectories.setFrom(files(classDirectories.files.collect {
                fileTree(dir: it,
                        exclude: [
                                '**/generated', // Q도메인이 위치한 디렉토리입니다.
                                '**/*Config*.*'
                        ])
            }))
        }
        finalizedBy 'jacocoTestCoverageVerification'
    }
```

<img src="https://user-images.githubusercontent.com/13347548/75578022-71e31580-5aa6-11ea-8d75-45c235d0fe69.png" alt="image" style="zoom:50%;" />

이제 한층 깔끔한 report가 생성되는군요!!

### 과제

모든것이 해결된 것 처럼 보이지만 해결해야할 과제가 남아있습니다.  

1. Q도메인을 커버리지 측정대상에서 지우기위해 와일드 카드(`*`)를 사용했기때문에  
   Q로 시작하는 모든 클래스는 커버리지 측정대상에서 제외됩니다.
2. 커버리지 측정 제외 대상과 report 제외 대상이 1:1로 일치하지 않습니다.  
   커버리지 측정 제외 대상은 **패키지+클래스**로 지정한 반면  
   report 제외 대상은 **디렉토리** 기준으로 설정되어 있습니다.

위 두가지 문자를 해결할 방법을 찾아서 글을 수정하도록 하겠습니다.



## jacoco 사용시 테스트에서 주의점

jacoco를 적용한뒤 test code를 작성할 때 주의해야할 점이 있습니다.

jacoco를 적용하면 test를 할 때 jacoco가 테스트 정보를 수집하기 위해   
**Synthetic 필드와 메소드를 각각 1개씩 추가**한다는 것인데요.

> 관련 [jacoco issue](https://github.com/jacoco/jacoco/issues/168) 링크
>
> * Synthetic 필드와 메서드는 컴파일러에 의해 생성된 녀석들입니다.

이를 확인하기 위해 테스트 코드를 작성해 보도록 하겠습니다.

```java
		@Test
    void reflectionTest() {
        Field[] declaredFields = PositiveNumber.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.isSynthetic()) {
                System.out.println(field + " jacoco가 넣은 필드");
            } else {
                System.out.println(field + " 기존 필드");
            }
        }
        
        assertThat(declaredFields).hasSize(2);
    }
```

**2개의 필드**를 가지고 있을 것이라는 테스트가 성공한것을 확인하고 console 창을 보면

<img src="https://user-images.githubusercontent.com/13347548/75574877-6beb3580-5aa2-11ea-89c7-210b47051e1d.png" alt="image" style="zoom:50%;" />

위와 같이 필드가 추가되어 있는 것을 확인할 수 있습니다.

만약 filed의 갯수나 reflection이 필요한 테스트 코드를 작성할 경우 위와 같은 부분을 주의하고  
`field`의 `isSynthetic()` 메소드를 이용하여 필터링을 거친뒤에 사용해 주어야 합니다.
