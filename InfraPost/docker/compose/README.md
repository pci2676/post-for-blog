# Docker Compose 입문기

## 개요

Docker Compose를 사이드 프로젝트하며 인프라 설정을 도와준 친구가 작성해준 것과  
이번에 우아한 테크코스 레벨 1을 진행하면서 코치님이 작성한 것까지 두번 마주했습니다.

처음 봤을때 하나도 모르는 상태였는데 두번째 마주했을때도 잘 모르는 상태라는 것을 깨닫고  
기술 부채가 쌓이기 전에 해소하고자 글을 작성하였습니다.

## 도커 컴포즈

도커 컴포즈는 여러개의 컨테이너를 정의하고 실행시키기 위해 작성하는 YAML 형태의 파일입니다.

하지만 꼭 여러개의 컨테이너를 정의, 실행 할 때만 사용하는 것은 아니고 한 가지 컨테이너만을 사용 할 때도  
충분히 유용하게 사용할 수 있는것 같습니다.

예를 들면 Maria-db, jenkins, reddis 를 동시에 생성, 실행할수 있도록 해주는 것입니다.

### 도커 컴포즈 준비하기

도커 컴포즈는 3단계의 진행 순서를 가지고 있습니다.

1. `Dockerfile`에 구동시킬 앱 환경을 정의합니다.  
   `docker-compose.yml`에서 service에 명시한 build 하위옵션인 dockerfile에서 사용할 수 있습니다.

   ```dockerfile
   # Dockerfile example
   FROM openjdk:8-jdk-alpine
   ADD ./professorlol-web/build/libs/*.jar professorlol.jar
   ENTRYPOINT ["java", "-jar", "/professorlol.jar"]
   ```

2.  `docker-compose.yml` 에 앱을 구성할 서비스들을 정의합니다. 정의된 서비스들은 독립된 환경에서 동시에 실행됩니다.

3.  `docker-compose up` 명령어를 이용해서 작성한 컴포즈를 실행시킵니다.  
   `-d`옵션을 줘서 백그라운드에서 실행되게 할 수 있습니다.

### 도커 컴포즈 명령어 종류

- 서비스 시작, 중지 및 rebuild 하기
- 실행중인 서비스 상태보기
- 실행중인 서비스의 로그 출력하기
- 서비스에서 일회성 명령 실행하기

### 도커 컴포즈의 특징

#### 하나의 호스트에서 독립된 여러개의 환경 생성

컴포즈는 프로젝트 이름을 이용해서 서로 독립된 환경을 만듭니다.  
프로젝트 이름은 서로 다른 여러 컨텍스트에서 사용이 가능합니다.

- dev 호스트에서는 하나의 환경을 여러개로 복사해 생성할 수 있습니다. 예를 들어 프로젝트의 feature branch의 안정적인 복사본을 여러개 실행할 수 있습니다.
- CI 서버에서는 서로가 간섭하는 환경속에서 계속해서 build 작업을 유지할 수 있습니다. 프로젝트 이름을 unique한 빌드 번호로 설정할 수 있습니다.
- 공유된 호스트나 dev 호스트에서 같은 서비스 이름을 사용하고 있는프로젝트가 서로를 간섭하지 않게 할 수 있습니다.

default로 설정되는 프로젝트 이름은 프로젝트 디렉토리의 이름을 기반으로 합니다.  
프로젝트 이름을 custom하게 설정하려면 `-p` 옵션을 이용하여 프로젝트 이름을 부여하면 됩니다.

#### 컨테이너 생성시 volume data를 보존

 `docker-compose up`명령어를 이용해서 실행하면 기존 volume data를 모두 보존합니다.  
만약 이전에 실행된 컨테이너 였다면 이전 컨테이너의 data를 새로운 컨테이너로 모두 복사해서 옮겨옵니다.  

#### 컨테이너는 변경이 발생했을때만 재 생성

컴포즈는 컨테이너를 생성할때 사용되는 설정들을 캐시해놓고 사용합니다.  
별다른 변경없이 서비스를 재 실행하는 경우 기존에 존재하던 컨테이너를 재사용합니다.  

#### 변수와 구성을 다른 환경에게 전달가능

컴포즈는 컴포즈 파일에서 변수들을 지원합니다.  
변수들은 조합하여 커스텀하게 사용할수 있고 다른 환경에서도 사용가능합니다.  
또한 컴포즈 파일은 `extends`를 이용해 상속을 사용할 수 있습니다. 

## 참고

https://docs.docker.com/compose/



# Docker Compose 작성하기

아래는 Maria Database를 사용하기 위해 작성한 `docker-compose.yml` 입니다.

```yml
version: '3'
services:
  maria:
    image: mariadb
    container_name: local-maria
    restart: always
    ports:
      - 13306:3306
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: project-ward
      TZ: Asia/Seoul
    volumes:
      - ./docker/maria/data:/var/lib/mysql
      - ./docker/maria/init:/docker-entrypoint-initdb.d
```

Version : compose 파일의 버전을 명시합니다.

Services : 바로 하위에 나오는 이름들이 각 service의 이름이 됩니다. 따라서 maria는 하나의 이름입니다.

Image : 해당 서비스에서 사용될 이미지의 이름을 명시합니다.

Container_name : 해당 이미지를 이용해서 컨테이너를 만들때 노출되는 컨테이너의 이름을 지정합니다.

Restart : (always)특정 문제로 service가 중단되면 다시 재기동합니다.

Ports : 외부포트:내부포트

Environment : 환경변수를 의미합니다. 각 환경변수는 해당 이미지를 제공하는 도커 사이트에서 확인하면 됩니다.

Volumes : 해당 컨테이너를 실행 할 때 사용되는 volume data의 마운트 위치를 지정합니다. 



# DockerFile 작성하기

Docker에서 제공하는 image(ex. Maria-db, jenkins)는 docker hub에서 제공합니다. 

DockerFile은 docker hub에서 제공하는 공식 이미지에 우리 입맛에 맞도록 커스텀한 설정을 하여 이미지를 빌드 할 수 있도록 해줍니다.

따라서, DockerFile은 이미지에 특정 명령을 실행할 수 있도록 해줍니다.

### 명령어

**FROM**: 어떠한 이미지를 사용할지 명시해줍니다. (ex. FROM openjdk:8-jdk-alpine)

**ADD**: docker를 띄우는 host에 있는 파일을 컨테이너로 추가하는 명령어입니다. (ex. ADD ./professorlol-web/build/libs/*.jar professorlol.jar)

**ENTRYPOINT:** docker start, docker run 하면 실행되는 명령어를 의미합니다. (ex. ENTRYPOINT ["java", "-jar", "/professorlol.jar"])   

