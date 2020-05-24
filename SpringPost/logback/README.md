# Spring Boot - logback 설정하기

## 시작하며

아주아주 세세한 부분까지 짚어가며 하지는 않고

사용한 설정에 대해서 하나씩 짚으며 작성해보도록 하겠습니다.

## 작성하기

`classpath`인 `resources` 디렉토리 밑에 `logback-spring.xml` 파일을 작성하면 됩니다.

먼저 틀을 작성합니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration>


</configuration>
```

이제 `configuration` 블럭 안쪽에 모든 설정을 해주면 됩니다.

먼저 중복으로 쓰이는 값들을 변수로 설정해서 사용하도록 준비합니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration>

    <timestamp key="BY_DATE" datePattern="yyyy-MM-dd"/>
    <property name="LOG_PARENT_PATH" value="../log"/>
    <property name="LOG_CHILD_INFO" value="info"/>
    <property name="LOG_CHILD_WARN" value="warn"/>
    <property name="LOG_CHILD_ERROR" value="error"/>
    <property name="LOG_BACKUP" value="../log/backup"/>
    <property name="MAX_HISTORY" value="30"/>
    <property name="LOG_PATTERN"
              value="[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) [%C.%M:%line] - %msg%n"/>

</configuration>
```

`timestamp` 는 시간을 나타냅니다.  
`key`가 변수의 이름이 되고,  `datePattern`을 이용해서 년-월-일 을 나타냈습니다.  
나중에 파일로 로그를 저장 할 때 사용하기 위해 미리 선언해 두었습니다.

`property`는 `name`을 이용해서 변수의 이름을 나타냅니다. `value`로 해당 `property`의 값을 지정합니다.  
각 변수들의 의미는 아래 설정에서 하나씩 짚도록 하겠습니다.

### Profile

logback 또한 Spring profile별로 그 설정을 다르게 할 수 있습니다.

`configuration` 블럭 내부에서 `springProfile`블럭으로 활성화된 profile별로 로깅 설정을 다르게 해줄수 있습니다.

간단하게 console로 출력되는 예제를 살펴보겠습니다.

```xml
<springProfile name="!prod">
  
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>${LOG_PATTERN}
                </pattern>
            </encoder>
        </appender>

        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>

</springProfile>
```

`springProfile`블럭의 `name`  옵션에 profile을 선택할 수 있습니다.

`!prod`는 prod 환경을 **제외한** 모든 환경에서 활성화 됨을 의미합니다.  
반대로 `prod`라고 적어두면 prod 환경에서만 적용되는 옵션임을 의미합게됩니다.

### appender

`Appender`는 로그를 어떻게 처리할지 나타냅니다.

#### ConsoleAppender

콘솔 어펜더는 말그대로 발생하는 로그를 console에 출력합니다.

`name`옵션에 굳이 CONSOLE이라고 적어줄 필요는 없습니다. 해당 appender의 변수명이라고 생각하면 됩니다.

`class`는 반드시 `ch.qos.logback.core.ConsoleAppender`를 적어 주도록 합니다.  
해당 appender가 어떠한 타입의 appender인지 지정해줍니다.

`encoder` 는 인코더는 로그 이벤트를 바이트 배열로 변환하고 OutputStream으로 작성합니다.  
다시 말해서 발생한 로그를 우리가 원하는 방식으로 작성하는데 도움을 줍니다.

`pattern`에 해당 로그를 어떠한 형식으로 적을지 정할 수 있습니다.  
`${LOG_PATTERN}` 는 처음에 `property`로 설정한

`[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) [%C.%M:%line] - %msg%n`를 의미합니다.

로그 패턴에 대해서는 아래에서 다시 다루도록 하겠습니다.

`root` 는 로그 설정의 기반 설정을 의미합니다.

`level`은 Spring Boot에서 설정할 로그 레벨을 의미합니다.

`appender-ref`  에 있는 `ref`에 우리가 작성한 `appender`의 `name`에 해당하는 값을 똑같이 적어주면 적용이 됩니다.

#### RollingFileAppender

`RollingFileAppender`는 발생하는 로그를 파일로 관리하는 appender입니다.

단순히 file로 관리하는 것이 아니라 특정 정책을 토대로 파일을 말아서(roll) 백업을 해줍니다.

아래 예시로 하나씩 살펴보겠습니다.

```xml
<springProfile name="prod">
        <appender name="FILE-INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${LOG_PARENT_PATH}/${LOG_CHILD_INFO}/info-${BY_DATE}.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>INFO</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>${LOG_PATTERN}</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${LOG_BACKUP}/${LOG_CHILD_INFO}/info-%d{yyyy-MM-dd}.zip</fileNamePattern>
                <maxHistory>${MAX_HISTORY}</maxHistory>
            </rollingPolicy>
        </appender>

  ...

</springProfile>
```

ConsoleAppender와 다르게 `class`에 `ch.qos.logback.core.rolling.RollingFileAppender` 를 적어주어야 합니다.

`file` 블럭에 적힌 경로와 이름으로 로그가 발생할때마다 file에 추가하여 써내려갑니다(append).

`${LOG_PARENT_PATH}/${LOG_CHILD_INFO}/info-${BY_DATE}.log` 는 에서

위에서 선언한 `property`를 사용하고 있습니다.

${LOG_PARENT_PATH} : ../log

${LOG_CHILD_INFO} : info

${BY_DATE} : yyyy-MM-dd

합치면 `../log/info/info-[발생 년월일].log` 라는 이름으로 로그 파일이 작성되는 것을 의미합니다.

`filter` 는 발생하는 로그들의 레벨에 따라 파일에 작성할지 말지를 결정해줍니다.  
`class`에 `ch.qos.logback.classic.filter.LevelFilter` 를 적어서 LevelFilter를 적용할 것을 명시해야 합니다.

```xml
<level>INFO</level>
<onMatch>ACCEPT</onMatch>
<onMismatch>DENY</onMismatch>
```

위 설정은 로그가 필터에 도착했을 때 레벨이 `INFO` 에 일치(`onMatch`)하면 파일에 쓰고`(ACCEPT)` 일치하지 않는다`(onMismatch)`면 쓰지않음`(DENY)` 할 것 임을 명시하고 있습니다.

`rollingPolicy`에서 `class`에 `ch.qos.logback.core.rolling.TimeBasedRollingPolicy`를 적용해서 시간 단위로 로그를 백업할 것임을 명시합니다.

`fileNamePattern`는 `file`과 마찬가지로 백업되는 로그의 위치를 나타냅니다.

`maxHistory`는 백업한 로그를 보관하는 기간을 정합니다.  
30을 적으면 30일 이후에 삭제됩니다.

> 더 자세한 appender 의 종류와 정책을 보고싶으시다면  
> [logback appender docs](http://logback.qos.ch/manual/appenders.html)를 참고하시는 것을 추천드립니다.

### Log Pattern

`[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) [%C.%M:%line] - %msg%n`

를 하나씩 쪼개면서 살펴보겠습니다.

`[%d{yyyy-MM-dd HH:mm:ss}:%-4relative]`
`%d{yyyy-MM-dd HH:mm:ss}`에서 `%d`는 date를 의미하면 중괄호 들어간 문자열은 dateformat을 의미합니다.  
따라서 `2020-05-25 02:47:42`과 같이 날짜가 로그에 출력됩니다.

`%-4relative`에서 `%relative`는 초 아래 단위의 시간을 나타냅니다.  
`-4`를 했기때문에 4칸의 출력폼을 고정으로 가지고 출력합니다.  

다시 합쳐서 표현하면

`[2020-05-25 02:47:41:484 ]` 혹은 `[2020-05-25 02:47:42:1106]`와 같이 표현이 됩니다. 

`%green([%thread])`에서 `%green`은 초록색 폰트를 `%thread`는 해당 로그를 발생시킨 쓰레드를 의미합니다.  
따라서 `[main]`와 같이 출력 됩니다.

`%highlight(%-5level)`에서 먼저 `%level`은 발생 로그의 level을 의미합니다.  
`%highlight`는 ERROR 로그는 굵은 빨간색으로, WARN 로그는 빨간색으로, INFO 로그는 파란색으로 표시해줍니다.  
따라서 `INFO`, `WARN`, `ERROR`가 각자의 폰트 스타일을 가지고 출력됩니다.

`[%C.%M:%line]`

`%C` 는 로그를 발생시킨 클래스의 풀패키지 이름을 의미합니다.  
`%M`는 로그를 발생시킨 메서드의 이름을 의미합니다.  
`%line` 은 로그가 발생한 라인을 표시합니다.  
따라서 `[org.springframework.boot.StartupInfoLogger.logStarted:61] `와 같이 출력됩니다.

`%msg%n`에서 `%msg`는 로그로 입력한 메세지를 `%n`은 개행을 의미합니다.

##### 모든 포맷을 합치면 다음과 같이 출력됩니다.

`[2020-05-25 02:47:43:2339] [main] INFO  [org.springframework.boot.StartupInfoLogger.logStarted:61] - Started LetsMergeApplication in 2.237 seconds (JVM running for 2.838)`

> 더 자세한 Pattern 의 종류를 찾아보고 싶으시다면  
> [logback layout docs](http://logback.qos.ch/manual/layouts.html)와 [PatternLayout](http://logback.qos.ch/xref/ch/qos/logback/classic/PatternLayout.html)을 참고하시는 것을 추천드립니다.

### 맺으며

이렇게 작성한 logback.xml의 모습은 다음과 같습니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration>

    <timestamp key="BY_DATE" datePattern="yyyy-MM-dd"/>
    <property name="LOG_PARENT_PATH" value="../log"/>
    <property name="LOG_CHILD_INFO" value="info"/>
    <property name="LOG_CHILD_WARN" value="warn"/>
    <property name="LOG_CHILD_ERROR" value="error"/>
    <property name="LOG_BACKUP" value="../log/backup"/>
    <property name="MAX_HISTORY" value="30"/>
    <property name="LOG_PATTERN"
              value="[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %green([%thread]) %highlight(%-5level) %C.%M :%msg%n"/>

    <springProfile name="!prod">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>${LOG_PATTERN}
                </pattern>
            </encoder>
        </appender>

        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>

    </springProfile>

    <springProfile name="prod">
        <appender name="FILE-INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${LOG_PARENT_PATH}/${LOG_CHILD_INFO}/info-${BY_DATE}.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>INFO</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>${LOG_PATTERN}</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${LOG_BACKUP}/${LOG_CHILD_INFO}/info-%d{yyyy-MM-dd}.zip</fileNamePattern>
                <maxHistory>${MAX_HISTORY}</maxHistory>
            </rollingPolicy>
        </appender>

        <appender name="FILE-WARN" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${LOG_PARENT_PATH}/${LOG_CHILD_WARN}/warn-${BY_DATE}.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>WARN</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>${LOG_PATTERN}</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${LOG_BACKUP}/${LOG_CHILD_WARN}/warn-%d{yyyy-MM-dd}.zip</fileNamePattern>
                <maxHistory>${MAX_HISTORY}</maxHistory>
            </rollingPolicy>
        </appender>

        <appender name="FILE-ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${LOG_PARENT_PATH}/${LOG_CHILD_ERROR}/error-${BY_DATE}.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>ERROR</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>${LOG_PATTERN}</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${LOG_BACKUP}/${LOG_CHILD_ERROR}/error-%d{yyyy-MM-dd}.zip</fileNamePattern>
                <maxHistory>${MAX_HISTORY}</maxHistory>
            </rollingPolicy>
        </appender>

        <root level="INFO">
            <appender-ref ref="FILE-INFO"/>
            <appender-ref ref="FILE-WARN"/>
            <appender-ref ref="FILE-ERROR"/>
        </root>

    </springProfile>


</configuration>
```

예전에 프로젝트를 같이한 친구가 해줘서 기술부채로 남아있어서 청산하고 싶었는데

어느정도 해소가 된 것 같아 기분이 좋습니다.

