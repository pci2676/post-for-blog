# LocalStack 으로 SQS 사용해보기

## 목적

Docker와 LocalStack, AWS CLI을 이용해서 AWS SQS를 구축해보자.

## 환경

- Docker
  - Engine : 19.03.8
  - compose : 1.25.4
- Mac os

## docker-compose.yml

```yaml
version: "3"

services:
  localstack:
    container_name: localstack
    image: localstack/localstack:0.12.11
    ports:
      - "4566-4576:4566-4576"
    environment:
      - SERVICES=sqs
      - DEFAULT_REGION=${DEFAULT_REGION- }
      - DEBUG=0
      - DOCKER_HOST=unix:///var/run/docker.sock
      - "/var/run/docker.sock:/var/run/docker.sock"
      - "${TMPDIR:-/tmp/localstack}:/tmp/localstack"

  setup-aws-resources:
    image: amazon/aws-cli:2.2.7
    volumes:
      - ./dev_env:/project/dev_env
      - ./aws:/aws
    environment:
      - AWS_ACCESS_KEY_ID=${ACCESS_KEY_ID- }
      - AWS_SECRET_ACCESS_KEY=${SECRET_ACCESS_KEY- }
      - AWS_DEFAULT_REGION=${DEFAULT_REGION- }
    entrypoint: /bin/sh -c
    command: >
      "
        echo wait for the state of localstack to become ready...
        sleep 10
        aws sqs create-queue --queue-name local-signup-request --endpoint-url=http://localstack:4566 --attributes file://local-signup-request.json
        echo set up aws resources is complete
      "
    depends_on:
      - localstack
```

위 스크립트는 두개의 컨테이너를 생성한다.

- localstack: AWS 리소스를 사용하기 위해 띄워주는 컨테이너이다. sqs, sns, s3등 AWS 리소스를 로컬환경에서 사용할 수 있게 해준다.
- setup-aws-resources: localstack 만으로 AWS 리소스를 생성할 수 없기 때문에 AWS CLI를 사용해야한다. AWS CLI 를 이용해서 AWS 리소스를 생성해주도록 하자.

### 주의점

컨테이너의 이미지들의 버전은 꼭 명시해주도록하자 안그러면 자동으로 최신 버전을 가져와서 호환성 문제로 컨테이너가 제대로 생성되지 않을 수도 있다.

`localstack.environment.SERVICES` 에 [AWS CLI의 서비스 이름을 콤마로 구분해서 적어주면 된다.](https://docs.aws.amazon.com/cli/latest/reference/#available-services)

AWS CLI 를 사용해서 리소스를 생성하는 부분을 보면 `--attributes file://local-signup-request.json` 이 있다. 

SQS queue의 상세 설정을 json 파일로 작성해두고 생성할 때 참고하여 생성한다. 따라서 host의 로컬에서 json 파일을 작성하고 컨테이너에서 실행할 수 있어야하므로 volume을 마운트 해주도록 한다.

AWS CLI 명령어의 실행 위치는 `/aws` 이다. `docker-compose.yml` 파일 경로에 `aws` 폴더를 생성하고 json 파일을 작성해주도록 한다.

### 팁

위 스크립트에는 환경변수 4개가 필요하다.

- DEFAULT_REGION: AWS 리젼
- ACCESS_KEY_ID: AWS 접근 ID
- SECRET_ACCESS_KEY: AWS 접근 비밀번호
- TMPDIR: compose 를 실행하는 host의 공유 폴더

매번 환경 변수를 커맨드라인으로 넣어주기 귀찮으니 간단한 스크립트(`run.sh`)를 작성해서 사용하도록 하자.

```yaml
#!/bin/bash

# Localstack
export SERVICES=s3
export TMPDIR=/private$TMPDIR
export DEBUG=0
export DEFAULT_REGION=ap-northeast-2
export ACCESS_KEY_ID=accesskey
export SECRET_ACCESS_KEY=secretkey

docker-compose up -d --remove-orphans
```

`./run.sh` 명령어로 스크립트를 실행하면 문제없이 로컬스택이 구축이 된다.

## 확인

위 스크립트에서는 SQS를 생성했다. 잘 생성되었는지 아래 커맨드로 확인을 해본다.

`aws --endpoint-url=http://localhost:4566 sqs list-queues`

생성한 큐가 잘 보인다면 localstack을 이용한 AWS local 환경 구축은 잘 된것이다!

