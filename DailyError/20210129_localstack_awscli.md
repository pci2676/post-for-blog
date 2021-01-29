# [오늘의 에러] mesosphere/aws-cli Connection aborted

프로젝트를 하면서 로컬 환경을 개발 환경과 일치시키기 위해서 도커를 많이 쓰고있다.

근데 도커를 사용하니까 이게 내 로컬 환경 설정이 문제인지 도커 환경 설정(도커엔진)의 문제인지, 아니면 도커 이미지 혹은 컨테이너의 문제인지 알 길이 없더라.

오늘은 장장 4일에 걸쳐 해결한 어이없는 문제를 기록하고자 한다.

## localstack

먼저 문제의 상황에서 localstack을 사용하고 있었는데 나처럼 localstack을 처음 접한 사람을 위해 간단하게 얘기하면 aws의 컴포넌트들(sns, sqs, s3)등을 가상의 환경으로 띄워주는 녀석이다.

로컬 환경에서는 개발 환경 처럼 aws에 항상 sns, sqs 를 구동시켜놓기 힘든데 이렇게 localstack을 사용하면 그러한 문제를 쉽게 풀어갈 수 있다.

## mesosphere/aws-cli

나를 미치게한 주범인데 가상의 aws-cli 를 제공하는 이미지이다. 글을 쓰는 시점에서 무려 2년전에 마지막 업데이트가 되서 개발을 멈춘듯한 오픈소스인데 프로젝트에서 이 녀석을 사용하고 있었다.

docker-compose.yml 에는 다음과 같이 적혀있었다.

```yml
services:
  localstack:
    image: localstack/localstack
    ports:
      - 포트정보
    environment:
      - 환경변수 정보
    volumes:
      - 마운트 정보

  setup-resources:
    image: mesosphere/aws-cli
    volumes:
      - 마운트 정보
    environment:
      - 환경변수 정보
    entrypoint: /bin/sh -c
    command: >
      "
        sleep 10
        aws sns create-topic --name sns-1 --endpoint-url=http://localstack:4575
        aws sns create-topic --name sns-2 --endpoint-url=http://localstack:4575
        aws sns create-topic --name sns-3 --endpoint-url=http://localstack:4575
        aws sqs create-queue --queue-name sqs-1 --endpoint-url=http://localstack:4576
      "
    depends_on:
      - localstack
```

이런식으로 작성된 docker-comose.yml 를 실행하면 localstack 컨테이너를 생성후 depends_on 에 의해 setup_resources 컨테이너를 생성하는데 이놈의 컨테이너가 자꾸 아래와 같은 로그를 뱉고 죽었다. 

**('Connection aborted.', error(111, 'Connection refused'))**

![image](https://user-images.githubusercontent.com/13347548/106273349-d8078800-6275-11eb-9a06-18568fb58632.png)

나도 개발하고 싶고 로컬에서 환경 너무 띄워보고 싶은데 검색을해도 안나오고 원인을 못 찾았다. 심지어 2년넘게 관리가 안되고 있는 오픈소스라 issue 관리도 안되서 얻을 정보도 없었다..

그래서 사수분한테 헬프치고 한시간 정도 같이 보다가 사수분이 차이점을 찾았는데 localstack 의 spec 을 보니 나의 localstack은 java 11로 구동되어있고 사수의 localstack 은 jdk8로 구동을 하고있었다!!!

compose 스크립트를 보면 알겠지만 버전 명시가 되어있지 않아 처음으로 localstack 을 pull 받아온 나는 latest 버전으로 가져왔는데 이게 0.12.x 버전이었다. 그래서 버전으로 내리다가 0.11.5 다음 버전부터 java11 로 구동하는걸 발견했고 0.11.5 버전으로 compose 스크립트에 명시를 하면서 문제는 해결됐다.

## 맺으며

사수분이 도커로 환경설정하면 꼭 팀에 한명씩은 문제가 발생하는게 너무 아쉽다고 했는데 진짜 죽을맛이었다. 

도커와 같이 로컬 환경설정을 맞춰놓는다면 반드시 버전을 명시해서 누가 언제 실행하더라도 모두가 동일한 환경에서 실행해서 성공할수 있도록 해주는게 좋을 것 같다.