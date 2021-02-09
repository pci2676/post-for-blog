# [오늘의 에러] Kotlin Internal Test 에러 해결

## 문제 상황

인텔리제이에서 코틀린에서 테스트를 만들면 기본적으로 internal 메서드로 된 테스트를 만들어 주는데 이 테스트를 돌리면 자꾸 에러가 발생한다.

<br/>

![image](https://user-images.githubusercontent.com/13347548/107380937-aa51f700-6b31-11eb-971d-9c2c4b00681f.png)

<br/>

```
Internal Error occurred.
org.junit.platform.commons.JUnitException: TestEngine with ID 'junit-jupiter' failed to discover tests
	at org.junit.platform.launcher.core.DefaultLauncher.discoverEngineRoot(DefaultLauncher.java:189)
	at org.junit.platform.launcher.core.DefaultLauncher.discoverRoot(DefaultLauncher.java:168)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:132)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:71)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:220)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:53)
Caused by: org.junit.platform.commons.JUnitException: MethodSelector [className = 'com.javabom.bomkotlin.chap2.ExprTest', methodName = 'exprTest1$bomkotlin_kotlin_in_action_test', methodParameterTypes = ''] resolution failed
	at org.junit.platform.launcher.listeners.discovery.AbortOnFailureLauncherDiscoveryListener.selectorProcessed(AbortOnFailureLauncherDiscoveryListener.java:39)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolveCompletely(EngineDiscoveryRequestResolution.java:102)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.run(EngineDiscoveryRequestResolution.java:82)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver.resolve(EngineDiscoveryRequestResolver.java:113)
	at org.junit.jupiter.engine.discovery.DiscoverySelectorResolver.resolveSelectors(DiscoverySelectorResolver.java:45)
	at org.junit.jupiter.engine.JupiterTestEngine.discover(JupiterTestEngine.java:69)
	at org.junit.platform.launcher.core.DefaultLauncher.discoverEngineRoot(DefaultLauncher.java:181)
	... 6 more
Caused by: org.junit.platform.commons.PreconditionViolationException: Could not find method with name [exprTest1$bomkotlin_kotlin_in_action_test] in class [com.javabom.bomkotlin.chap2.ExprTest].
	at org.junit.platform.engine.discovery.MethodSelector.lambda$lazyLoadJavaMethod$2(MethodSelector.java:176)
	at java.base/java.util.Optional.orElseThrow(Optional.java:408)
	at org.junit.platform.engine.discovery.MethodSelector.lazyLoadJavaMethod(MethodSelector.java:174)
	at org.junit.platform.engine.discovery.MethodSelector.getJavaMethod(MethodSelector.java:149)
	at org.junit.jupiter.engine.discovery.MethodSelectorResolver.resolve(MethodSelectorResolver.java:69)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.lambda$resolve$2(EngineDiscoveryRequestResolution.java:146)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
	at java.base/java.util.ArrayList$ArrayListSpliterator.tryAdvance(ArrayList.java:1632)
	at java.base/java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
	at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
	at java.base/java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base/java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolve(EngineDiscoveryRequestResolution.java:185)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolve(EngineDiscoveryRequestResolution.java:125)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolveCompletely(EngineDiscoveryRequestResolution.java:91)
	... 11 more
```

<br/>

## 해결법

로그를 보아하니 테스트 메서드를 찾지 못해서 그러는 것 같다. 

코드는 죄가 없으니 내가 설정을 뭔가 잘못했겠지.. 싶어서 gradle 설정을 다시 살펴봤다.

kotlin dsl 을 사용하면서 기존 test tasks를 선언하는 방법이 여러가지가 있었는데 내 경우에는 아래와 같이 설정되어 있었다.

### AS-IS

```groovy
tasks.withType(Test::class){
    useJUnitPlatform()
}
```

`withType` 을 이용해서 test tasks 를 정의해놨었다.

테스트에 문제가 있으니 이 부분에 문제가 있겠거니.. 하고 공식문서를 다시 살펴봤는데 아니 이게 뭐람!! `named` 로 공식문서에는 정의해두는 것이 아닌가?

그래서 아래와 같이 고쳤더니 해결되었다.

### TO-BE

```groovy
tasks.named<Test>("test"){
    useJUnitPlatform()
}
```



<br/>

## 이유

모자란 영어실력으로 gradle 문서를 뒤져봤는데 `named`는 기존 task 를 이용하는 반면 `withType`은 새로운 task 를 생성해버리는 것 같다. 그래서 아마 기존 test task 에는 internal 을 처리할 수 있도록 설정이 되어있는데 새롭게 생성하면서 관련된 부분이 초기화되고 에러가 발생하지 않았을까 싶다.

~~어디까지나 추측이다.~~

> https://docs.gradle.org/current/userguide/task_configuration_avoidance.html#sec:how_do_i_defer_configuration
