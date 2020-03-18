# Docker with MariaDB

## image 받기

`$ docker pull mariadb`

## container 실행

` $ docker run -d -p 3306:3306 --name localmariadb -e MYSQL_ROOT_PASSWORD=root mariadb:10.4 `

옵션

- -d : 백그라운드 모드
- -p : docker의 호스트에게 전달되는 3306(앞) 요청을 컨테이너의 3306(뒤)로 포트포워딩 한다.
- --name : 컨테이너의 이름
- -e : 환경변수 전달
  - MYSQL_ROOT_PASSWORD=root
    - 루트의 비밀번호를 root으로
  - mariadb:10.4 : 마리아 디비 10.4 버전 사용

