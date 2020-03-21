# Docker with MariaDB

## image 받기

```bash
$ docker pull mariadb
```

## container 실행

``` bash
$ docker run -d -p 3306:3306 --name localmariadb -e MYSQL_ROOT_PASSWORD=root mariadb:10.4 
```

옵션

- -d : 백그라운드 모드
- -p : docker의 호스트에게 전달되는 3306(앞) 요청을 컨테이너의 3306(뒤)로 포트포워딩 한다.
- --name : 컨테이너의 이름
- -e : 환경변수 전달
  - MYSQL_ROOT_PASSWORD=root
    - 루트의 비밀번호를 root으로
  - mariadb:10.4 : 마리아 디비 10.4 버전 사용

## contianer 접속

```bash
$ docker exec -i -t mariadb bash
```

#### mariadb 접속

```bash
$ mysql -uroot -proot
```

## 설정

### 인코딩 설정

mariadb 10.4 기준, db에 접속하여 인코딩 설정값을 확인하면 다음과 같다.

`MariaDB [(none)]> show variables like 'c%';`

![image](https://user-images.githubusercontent.com/13347548/77228071-1220eb80-6bc8-11ea-8ec9-dafc50c6aaf0.png)

MariaDB는 기본적으로 latin이 설정되어있다.

MariaDB에서 나와 설정을 하자

`MariaDB [(none)]> exit`

`$ cd /etc/mysql`

이 위치에 `my.conf` 와 `mariadb.conf`가 있는데 `my.conf`에서 `mariadb.conf`를 include하여 사용한다.

`$ vi mariadb.conf` 하여 설정을 살펴보면 다음과 같은 부분이 있다.

![image](https://user-images.githubusercontent.com/13347548/77228136-97a49b80-6bc8-11ea-8091-e6627d465349.png)

빨간 박스 부분에 `#` 로 주석처리된 부분을 전부 지워주고 저장하자.

그리고 도커 컨테이너 밖으로 나가 컨테이너를 재시작(`$ docker restart localmariadb`)한뒤 다시 설정값을 확인하여 latin으로 설정되어있던 부분이 utf로 바뀌었는지 확인하면 끝!

![image](https://user-images.githubusercontent.com/13347548/77230400-4223bb00-6bd7-11ea-9eaa-161e994d90a0.png)



### 소켓 개수 설정

스레드 생성 갯수에 영향을 미친다.

```bash
$ vi /etc/security/limits.conf
이곳에 추가
```

```bash
*               soft    nofile          65535
*               hard    nofile          65535
```

컨테이너 재시작.

```bash
$ docker restart localmariadb
```



### 시간설정

```bash
$ rm /etc/localtime
$ ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
```

컨테이너 재시작 후 `date`로 시간 확인

```bash
$ docker restart localmariadb
$ date
```

