# Jenkins with Docker

예전에 대외활동을 할때 CI/CD를 하기 위해 Jenkins를 이용한 적이 있었다.  
사용해본지가 너무 오래되기도 했고 Travis말고 Jenkins를 연습해보고 싶었는 차에
친구가 Docker를 이용해서 Local 환경에서 연습 할 수 있다고 알려줬다.

## 설치

Docker의 설치는 쉬우니 넘어가도록 하고 바로 Jenkins 설치로 넘어가도록 하자.

다음과 같은 과정을 거치면 Local 환경에서 Jenkins를 사용 할 수 있다.

1. jenkins 이미지 설치
2. jenkins 이미지를 이용하여 container 생성
3. jenkins 잠금해제
4. jenkins install

### 1. jenkins 이미지 설치

터미널에서 다음과 같은 커맨드를 입력하도록 하자.

`$ docker pull jenkins/jenkins:lts`

jenkins의 image를 설치하는 커맨드이다.  
lts는 Long Term Support 버전을 의미한다.  
일반 jenkins를 설치했다가 패키지 설치 단계에서 에러가 발생해서 이유를 찾아보니  
최신 버전을 받아오면 설치가 실패하는 issue가 있었다.

설치가 완료된 image id는 아래 커맨드를 이용해서 확인 할 수 있다.

`$ docker images`

참고로 image 삭제 커맨드는 `$ docker rmi [image id]` 이다.

### 2. containor 생성

image와 containor가 어떠한 관계인지 이해 할 때 java에 빗대어 표현한 글을 보니 이해가 쉽게 되었다.  
image는 class로 containor는 해당 class의 instance 객체럼 생각하면 된다.

컨테이너 생성은 다음과 같이 하면 된다.

`$ docker run -d -p 9090:8080 -v ~/localJenkins:/var/jenkins_home --name jenkinslocal -u root jenkins/jenkins:lts`

커맨드의 의미는 다음과 같다.

- -d : 백그라운드 모드로 실행함을 의미한다.
- -p : 포트 포워딩을 의미한다.  
  위 예시는 localhost:9090으로 접근하면 8080포트로 포워딩해준다.
- -v : 파일 마운트를 위한 커맨드이다.  
  `~/localJenkins` 위치에 containor가 설치되는 파일들을 마운트 시켜준다.
- --name : 우리가 사용할 containor의 이름을 지정한다. 객체의 변수명이라 생각하면 된다.
- -u : 실행 할 때 어떠한 사용자로 실행할지 지정한다.
- 마지막 jenkins/jenkins:lts 는 containor를 만들때 사용할 image를 의미한다.

생성된 container는 아래 커맨드로 확인 할 수 있다.

`$ docker ps -a`

참고로 container 삭제 커맨드는 `$ docker rm [container id]` 이다.

### 3. jenkins 잠금해제

컨테이너 생성이 끝나면 jenkins에 접속을 합니다.

`localhost:9090`으로 접근하면 

![image](https://user-images.githubusercontent.com/13347548/76682677-687ab100-6641-11ea-9f07-2ece97403a71.png)

위와 같은 화면에서 잠시 대기를 하면 아래와 같은 화면을 볼 수 있습니다.

![image](https://user-images.githubusercontent.com/13347548/76682692-86481600-6641-11ea-9583-ffa528f63df6.png)

화면에 적혀 있는 경로를 docker container 내부에서 확인하여 입력합니다.

![image](https://user-images.githubusercontent.com/13347548/76682761-03738b00-6642-11ea-8ccc-b57e7b10056c.png)

`$ docker exec -it jenkinslocal /bin/bash`

옵션!

- -i : 표준 입출력을 사용한다는 것을 의미한다.
- -t : 가상 tty를 통해 접속함을 의미한다.

`$ cat /var/jenkins_home/secrets/initialAdminPassword`

암호를 입력하여 잠금을 풀면 다음과 같은 화면을 볼 수 있습니다. 참고로 containor에서 나가는 커맨드는 `$ exit` 이다.

![image](https://user-images.githubusercontent.com/13347548/76682807-649b5e80-6642-11ea-9c77-18c991ff9612.png)

Install suggested plugins 을 통해 설치합니다.

![image](https://user-images.githubusercontent.com/13347548/76682875-da9fc580-6642-11ea-8675-ec06c0f115a3.png)

설치가 끝난뒤 계정을 생성하면 Jenkins의 설치는 마무리가 된다.

계정 생성을 하자! 그럼 끝이다!

![image](https://user-images.githubusercontent.com/13347548/76684470-a41c7780-664f-11ea-944d-d9efde9da4de.png)

그런데 모든일이 원하는 대로 되지는 않는다고, 간혹 설치하다가 error가 발생하기도 한다. 열받게.

![image](https://user-images.githubusercontent.com/13347548/76684366-edb89280-664e-11ea-9381-43b570cd008e.png)

의존성 에러가 발생했다고 jenkins가 친절하게 알려준다.

![image](https://user-images.githubusercontent.com/13347548/76684388-13de3280-664f-11ea-9283-fa15d8e7ebb2.png)

설치가 안되어있으면 설치를 해준 다음 재시작 하면되고,  
설치가 되어있다고 나온다면 바로 재시작 해주면 해결된다.

![image](https://user-images.githubusercontent.com/13347548/76684442-759e9c80-664f-11ea-857d-9250876dfa16.png)

편안~

설치가 끝났다!