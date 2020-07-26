# Jekins이용 멀티모듈 CI/CD 구축하기

![image](https://user-images.githubusercontent.com/13347548/88481410-59642200-cf96-11ea-9385-ac09d97a39fa.png)

## 개발 환경

2개의 Ubuntu 서버 (Jenkins 서버, jar 구동 서버)

GitHub으로 관리되는 Java multi module project (+ Submodule, + Node)

Github Branch Source (Jenkins plugin)

Publish over SSH (Jenkins plugin)

## Ubuntu jenkins 설치

패키지 업데이트

`sudo apt-get update`

자바 설치

`sudo apt-get install openjdk-8-jdk`

젠킨스 키 등록

`sudo wget -q -O - https://pkg.jenkins.io/debian/jenkins-ci.org.key | sudo apt-key add -`

젠킨스 패키지 등록

`echo deb https://pkg.jenkins.io/debian-stable binary/ | sudo tee /etc/apt/sources.list.d/jenkins.list`

패키지 업데이트

`sudo apt-get update`

젠킨스 설치

`sudo apt-get install jenkins`

젠킨스 기동

`sudo systemctl start jenkins`

젠킨스 상태 확인

`sudo systemctl status jenkins`

## Jenkins 설치

Jenkins를 처음으로 설치한 다음 해당 public ip의 8080 포트로 접근하면 다음과 같은 화면이 나온다

![image](https://user-images.githubusercontent.com/13347548/76682692-86481600-6641-11ea-9583-ffa528f63df6.png)

`cat /var/jenkins_home/secrets/initialAdminPassword`

위 커맨드를 ubuntu에서 입력해서 나오는 값을 입력해 주자. 

다음으로 Customize Jenkins 화면이 나오는데 플러그인을 어떻게 설치할 건지를 묻는다.

![image](https://user-images.githubusercontent.com/13347548/76682807-649b5e80-6642-11ea-9c77-18c991ff9612.png)

일단 추천해 주는대로 설치하자.

설치가 끝나면 계정을 생성하고 넘어가자.

## PR에 대한 CI 설정

[Github Branch Source](https://docs.cloudbees.com/docs/cloudbees-ci/latest/cloud-admin-guide/github-branch-source-plugin)라는 플러그인을 사용했다.

PR을 생성하고 생성된 PR에 대해 push가 발생하면 자동으로 build를 실행하도록 설정하자.

아래와 같이 PR을 생성한 feature branch와 PR이 머지가 되었을 때 문제가 없는지 확인할 수 있다.

![image](https://user-images.githubusercontent.com/13347548/88480990-23be3980-cf94-11ea-9397-f9bd207c4bdd.png)

굳이 이 설정은 걸어둔 이유는 개발자가 로컬에서 테스트를 실행해서 문제가 없는 상태로 PR을 생성하면 문제가 되지 않지만  
깜빡하고 테스트를 미처 실행하지 못 했을 때 발생할 수 있는 문제를 방지하고 싶었다.

### Github Organization

이제 Jenkins home에서 new Item(새로운 아이템)을 틀릭해서 Github Organization으로 프로젝트를 생성하자.

![image](https://user-images.githubusercontent.com/13347548/88481189-f45bfc80-cf94-11ea-9dd1-ef1e2e3bfbcc.png)

그리고 다음 설정을 해주자.

1. Credentials 설정
2. Organization 이름 설정
3. PR에 대해서만 CI 작업 설정
4. GitHub 특정 레포지토리 스캔 설정
5. Submodule 설정
6. Jenkinsfile 작성

![image](https://user-images.githubusercontent.com/13347548/88481175-e3ab8680-cf94-11ea-9f66-7911df5c0ae7.png)

#### 1. Credentials 설정

**깃헙에서 키를 만들어서 사용하자** 라고 잘 못 써놨다.. **깃헙 아이디를 이용해서 키를 만들자!** 이다.

Add 버튼을 누르면 다음과 같은 형태로 입력하고 `- none -`으로 되어있는 부분을 바꿔주면 된다.

![image](https://user-images.githubusercontent.com/13347548/88481512-dd1e0e80-cf96-11ea-8044-1429aca02f27.png)

Username with password로 Credential을 만들면 다음과 같은 질문이 나올 수 있다.

*엥? github에서 Personal Access token 만들어서 쓰면 안되나?*

답은 안된다. 

**Username with password로 만든 Credential만 사용**하라고 명시되어 있다.

#### 2. Organization 이름 설정

스캔할 Organization 이름을 똑같이 적어주면 된다.

끝이다.

#### 3. PR에 대해서만 CI 작업 설정

Behaviours 항목을 보면 Discover branches 가 있고 전략을 정할수 있는데 default 설정이 Exclude...이다.

이 전략을 Only... 로 바꿔주면 PR에 대한 CI 작업만 수행하게 된다.

각 전략이 어떤 작업을 하는지 궁금하다면 옆에 있는 **? 아이콘** 을 누르면 상세하게 나온다!

![image](https://user-images.githubusercontent.com/13347548/88481342-df339d80-cf95-11ea-9ae2-751614594a85.png)

#### 4. GitHub 특정 레포지토리 스캔 설정

아무것도 적지 않으면 전체 레포지토리를 스캔해서 불필요하다.

우리가 원하는 특정 레포지토리만 filter로 잡아주도록 하자.

![image](https://user-images.githubusercontent.com/13347548/88481299-7e0bca00-cf95-11ea-8042-7f5fff549cdf.png)

![image](https://user-images.githubusercontent.com/13347548/88481278-5b79b100-cf95-11ea-9674-cb61ed6d2a0e.png)

#### 5. Submodule 설정

이 과정은 submodule을 사용한다는 가정하에 추가하는 설정이기 때문에 사용하지 않는다면 하지 않아도 무방하다.

Submodule이 같은 organization내에 있다는 가정하에 진행된다. ~~보통 그렇지 않을까?~~

Grade task로 submodule을 update하게 하려 했는데 보통 submodule을 secret key를 담아두는 용도로 사용한다면  
권한의 문제가 발생하기 때문에 task 방식으로 처리하기가 어려웠다.

다행히 submodule을 알아서 clone 하고 update해주는 좋은 기능이 있었다.

다음과 같이 따라하면 된다.

 ![image](https://user-images.githubusercontent.com/13347548/88481702-e2c82400-cf97-11ea-90d7-9e814b08ecc3.png)

![image](https://user-images.githubusercontent.com/13347548/88481710-f5425d80-cf97-11ea-844f-7037d9eb4c7b.png)

이렇게 하면 깔끔하게 Jenkinsfile 을 실행하기 전에 먼저 submodule에 대한 clone update 작업을 선행해 준다.

#### 6. Jenkinsfile 작성

아래와 같이 파이프라인 스크립트를 작성하자.

```
node {
    stage ('clone') {
        checkout scm
    }
    stage('build') {
        sh './gradlew clean build'
    }
}
```

`stage ('clone')` 에서 [checkout scm](https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/) 명령어가 credential 설정도 해주고 branch도 잡아주고 fetch도 해준다.

`stage('build')` 에서 빌드 작업을 실시한다.

Directive Script로 작성되서 여러 단점이 존재하는데 Script에 대해서 더 알고 싶다면 [jojoldu 님의 포스트](https://jojoldu.tistory.com/356)를 참고하자!

#### Scan Organization Triggers

위 옵션은 우리가 뚝딱뚝딱한 Projects 설정 바로 아래에 있는 설정인데 아무런 트리거가 발생하지 않아도 주기적으로 CI작업을 하는 옵션이다.

PR에 대해서만 CI 작업을 하기때문에 이 옵션은 꺼두도록 하자.

### Github Hook 설정

이제 PR이 생성되고 생성된 PR에서 push 이벤트가 발생하는 것을 jenkins가 받도록 설정해줘야한다.

Github > Settings > Personal access tokens > Generate new token 순으로 진행한다.

![image](https://user-images.githubusercontent.com/13347548/88483049-50784e00-cfa0-11ea-9306-b1f7abd1beaf.png)

항목은 repo, admin:repo:hook 만 선택하자.

![image](https://user-images.githubusercontent.com/13347548/88483089-99c89d80-cfa0-11ea-9f01-c7961d07e9bb.png)



이렇게 생성하면 `Private Key`가 한번 노출되는데 잘 저장해서 관리하도록 하자.

이제 키를 jenkins에 등록하도록 한다.

Jenkins관리 > 시스템 설정 에서 Github 탭에서 add GitHubSever를 누르고

credential을 생성하고 등록하여 서버를 등록한다.

![image](https://user-images.githubusercontent.com/13347548/88483268-b6190a00-cfa1-11ea-9c7e-fb3ce339b631.png)

아래와 같이 Secret text로 Credential을 생성하도록 하자.

![image](https://user-images.githubusercontent.com/13347548/88483276-bca78180-cfa1-11ea-9b01-38f78aaa1465.png)

이렇게 하고 설정을 save하면 PR생성과 push 이벤트가 jenkins에게 전송된다.

만약 전송되지 않다면 jenkins의 ip와 repository의 webhook 항목에 적힌 jenkins의 payload ip가 같은지 확인해주자.

webhook이 등록되어 있지 않다면 고급(advanced) 항목의 를 클릭해서 webhook을 재등록 해주면 된다.

![image](https://user-images.githubusercontent.com/13347548/88483348-3475ac00-cfa2-11ea-947d-097c2953b149.png)

![image](https://user-images.githubusercontent.com/13347548/88483351-37709c80-cfa2-11ea-915a-885ad52ef832.png)

## 자동 배포 설정

### 어떻게?

멀티모듈 구조로 프로젝트를 구성했기 때문에 master에 머지가 된다고 바로 배포작업을 진행할 수는 없었다.

특정 모듈(ex. web, admin)만 각각 따로 배포할 수 있도록 해야했다.

또 AWS Code Deploy와 AWS S3를 사용하지 말아야 하는 제약조건이 있었기 때문에  
Publish over SSH 라는 Jenkins의 Plugin을 사용해서 sftp로 jar 파일과 배포용 쉘 스크립트를 전송해야 했다.

### Publish over SSH 설치

Jenkins 관리 > 플러그인 관리 에서 설치하면 된다

![image](https://user-images.githubusercontent.com/13347548/88483798-355c0d00-cfa5-11ea-9a91-0f9a73b8722f.png)

플러그인을 설치하고

Jenkins 관리 > 환경설정 > Publish over SSH 항목으로 이동해서 다음과 같이 적어준다.

![image](https://user-images.githubusercontent.com/13347548/88483963-2e81ca00-cfa6-11ea-9a7a-8836c45705a3.png)

Passphrase 항목은 비어있을텐데 설정하면 생긴다. 빈채로 넘어가자

Key 부분은 비밀키를 적어주어야 하는데 jenkins의 .ssh 경로로 가서 ssh-keygen을 이용해서 공개키와 비밀키를 만들어 두고 비밀키를 옮겨 적어주도록 한다.

공개키(.pub)는 Remote Server(우리의 서비스가 배포될 서버)의 .ssh 경로에 있는 auth 파일에 추가해서 적어주도록 하자.

SSH SERVER 옵션에서 주의할 점은 Hostname(IP)과 Username(user)을 제대로 적어주어야한다.

이렇게 하고 오른쪽 아래에 Test Configuration을 눌러서 제대로 연결되었는지 확인해 주도록 하자. (캡쳐에는 화면에서 짤려서 안보인다.)

### Jenkins FreeStyle Project 만들기

새로운 아이템 만들기를 클릭하고 FreeStyle Project를 선택해서 새로운 Jenkins 프로젝트를 생성하자.

멀티모듈이라 모듈마다 서로 다른 배포가 필요하기 때문에 배포가 가능한 모듈별로 프로젝트를 생성하게 될 것이다.  
여기서는 하나의 모듈을 예시로 설명하도록 하겠다.

#### 소스 코드 관리

FreeStyle Project를 생성하고 `소스 코드 관리` 탭으로 이동해서 Git을 클릭하도록 하자.

그리고 아래와 같이 설정을 해주자.  
위에서 설정했던 내용이랑 크게 다른 부분이 없다.

![image](https://user-images.githubusercontent.com/13347548/88484105-44dc5580-cfa7-11ea-973d-eeadd98faa84.png)

Webhook을 설정해두면 이제 수신하기로 한 이벤트가 발생할때마다 polling을 이용해서 빌드를 자동화 할 수 있는데  
master에 있는 브랜치를 모듈별로 배포해야하기 때문에 빌드 유발 항목은 아무것도 체크하지 않았다.

#### Build

다음으로 Build 항목을 보면된다.

![image](https://user-images.githubusercontent.com/13347548/88484303-989b6e80-cfa8-11ea-9cf1-7a80453ae374.png)

빌드 수행후 빌드된 파일(jar)와 미리 작성해둔 배포용 쉘 스크립트(deploy.sh)을 tar로 묶는다.

tar는 Publish over SSH를 통해 remote server로 전송하여 사용 할 것이다.

#### 빌드 후 조치

빌드 바로 아래 항목인 빌드 후 조치에서 우리가 완성시킨 tar를 전송하자.

그리고 tar를 압축해제하고 jar파일과 deploy파일을 이동시켜서 실행하도록 스크립트를 작성하면 된다.

![image](https://user-images.githubusercontent.com/13347548/88484494-bd441600-cfa9-11ea-8920-b322f5a2fb19.png)

![image](https://user-images.githubusercontent.com/13347548/88484495-bf0dd980-cfa9-11ea-80d4-edc6d453519b.png)

이렇게 자동 배포 작업을 마무리한다.