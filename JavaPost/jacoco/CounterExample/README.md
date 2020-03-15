# Jacoco Counter Line vs Instruction

Jacoco 커버리지 종류에 여러가지가 있지만 Line와 Instruction은 비슷하지만 엄청난 차이를 일으킨다.  
커버리지 측정방식이 다른데 Line의 경우 테스트 스레드가 읽고 지나간 **소스 코드의 Line**을 측정한다.  
하지만 Instruction의 경우 지나간 **바이트 코드**를 측정한다.

위 차이에 대한 이해가 잘 안될수도 있으니 바로 예제를 통해 확인해 보도록 하자.


### 설정

`build.gradle`에 설정은 다음과 같이 하였다.  
Junit5와 assertJ를 사용하기위해 의존성을 추가하였다.

```groovy
plugins {
    id 'java'
    id 'jacoco'
}

group 'org.javabom'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    testCompile('org.junit.jupiter:junit-jupiter:5.6.0')
    testCompile group: 'org.assertj', name: 'assertj-core', version: '3.15.0'
}

test {
    useJUnitPlatform()
    finalizedBy 'jacocoTestReport'
}

jacocoTestReport {
    reports {
        html.enabled true
        xml.enabled false
        csv.enabled false
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
                minimum = 0.90
            }

        }

    }
}
```

커버리지 violation의 최소 값을 90%의 커버리지를 충족하도록 설정하였다.  

### 비교

비교를 위해 먼저 Line으로 설정한뒤 다음 코드의 테스트를 진행하였다.

```java
public class Counter {

    private String counter;

    public Counter(String counter) {
        this.counter = counter;
    }

    public int getCounter() {
        if ("INSTRUCTION".equals(counter)) {
            return 0;
        } else if ("LINE".equals(counter)) {
            return 1;
        } else {
            return -1;
        }
    }
}
```

여기서 두 counter의 차이점을 알아보기 위한 메서드는 `getCounter()` 이다.  
다음과 같은 테스트 코드를 작성하자.

```java
class CounterTest {
    @Test
    void getCounter() {
        Counter counter = new Counter("LINE");

        int value = counter.getCounter();

        assertThat(value).isEqualTo(1);
    }
}
```

그리고 gradle test를 진행하면

![image](https://user-images.githubusercontent.com/13347548/76698110-45eca480-66e2-11ea-98af-67134584bfe3.png)

테스트는 성공했지만 90%를 만족하지 못했기때문에 build는 실패하는 것을 확인할 수 있다.  
`build.gradle` 에서 counter 를 Instruction으로 아래와 같이 수정하고 진행해도 마찬가지로 실패하는 것을 확인할 수 있다.

```groovy
jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = 'CLASS'

            limit {
                counter = 'INSTRUCTION'
                value = 'COVEREDRATIO'
                minimum = 0.90
            }

        }

    }
}
```

![image](https://user-images.githubusercontent.com/13347548/76698125-7df3e780-66e2-11ea-80b5-ba8645bc6523.png)

counter를 instruction으로 바꾸니까 커버리지 비율이 0.81로 상승한 것을 확인 할 수 있다.  
이때 어느정도 차이가 발생한 것을 눈치를 챌 수 있다.

이러한 차이가 만들어 낼수 있는 결과를 확인하려면 소스 코드의 포맷을 바꿔보면 된다.

기존 `getCounter()` 코드를 아래와 같이 소스 코드의 '포맷'만 바꾸도록 하자.

```java
public int getCounter() {
        if ("INSTRUCTION".equals(counter)) { return 0; } else if ("LINE".equals(counter)) { return 1; } else { return -1; }
    }
```

단순하게 한 줄로 기존 코드의 포맷을 변경했을 뿐이다.

이러한 상태에서 counter가 Instruction인 상태로 다시 테스트를 진행해 보면

![image](https://user-images.githubusercontent.com/13347548/76698171-2f931880-66e3-11ea-83c3-1d603170355d.png)

소스 코드의 포맷을 변경하기 전과 후의 커버리지 측정 값이 다름이 없음을 확인할 수 있다.

하지만 counter를 Line으로 변경하고 테스트를 진행하면

![image](https://user-images.githubusercontent.com/13347548/76698201-679a5b80-66e3-11ea-850f-a3889e69a01a.png)

정상적으로 테스트 통과후 빌드까지 성공하는 것을 확인할 수 있다!

### 결과

jacoco 공식 문서에 적혀있듯 counter의 설정이 Instruction인 경우 바이트 코드를 기준으로 한 측정이 이루어지는 반면  
Line의 경우 소스 코드의 한 줄을 측정하는 방식으로 이루어진다.  

이는 곧  
**Line**은 **소스 코드의 포맷에 대한 영향을 받는 측정** 방식이지만,  
**Instruction**의 경우 **소스 코드의 포맷에 영향을 받지 않는 측정**임을 의미한다.  

Instruction의 경우 소스 코드가 한 줄로 이루어져 있어도 class로 컴파일 하면서 바이트 코드로 나누어 지기 때문이다.

따라서 보다 정확한 커버리지 측정을 위해서는 Instruction을 이용하는 것이 더 바람직한 것 같다.