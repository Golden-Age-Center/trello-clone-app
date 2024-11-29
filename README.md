#  Spring Trello 프로젝트

## 📖 프로젝트 소개
내일배움캠프를 진행하면서 배웠던 Spring에 대한 모든 기능을 적극 활용하여 Trello를 구현합니다.

## ⌚ 프로젝트 기간
* 24.10.14 - 24.10.17

## 📚 기술 스택

### 💻 Developers
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Spring](https://img.shields.io/badge/Spring%20JPA-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Spring](https://img.shields.io/badge/Spring%20JWT-FBBA00?style=for-the-badge&logo=Spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![ELK](https://img.shields.io/badge/ELK-%230377CC.svg?style=for-the-badge&)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS%20S3-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![YAML](https://img.shields.io/badge/yaml-%23ffffff.svg?style=for-the-badge&logo=yaml&logoColor=151515)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
[![Prometheus](https://img.shields.io/badge/prometheus-E6522C.svg?style=for-the-badge&logo=prometheus&logoColor=white)](https://prometheus.io/)
[![Elasticsearch](https://img.shields.io/badge/elasticsearch-005571.svg?style=for-the-badge&logo=elasticsearch&logoColor=white)](https://www.elastic.co/elasticsearch/)
[![Kibana](https://img.shields.io/badge/kibana-005571.svg?style=for-the-badge&logo=kibana&logoColor=white)](https://www.elastic.co/kibana/)
[![Grafana](https://img.shields.io/badge/grafana-F46800.svg?style=for-the-badge&logo=grafana&logoColor=white)](https://grafana.com/)

### 🎛️ 환경
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![macOS](https://img.shields.io/badge/mac%20os-000000?style=for-the-badge&logo=macos&logoColor=F0F0F0)
![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)

## 🏠 멤버 구성 및 기능 구현
|                 김나람                  |                 나유화                  |                   정지윤                    |                     조준호                      |                       황호진                        |
|:------------------------------------:|:------------------------------------:|:----------------------------------------:|:--------------------------------------------:|:------------------------------------------------:|
| [@kim-na-ram](https://github.com/kim-na-ram) | [@fargoe](https://github.com/fargoe) | [@jiyumi00](https://github.com/jiyumi00) | [@JUNO0432](https://github.com/JUNO0432) | [@ballqs](https://github.com/ballqs) |

## 🤝 역할 분담
* 김나람 : Spring Security , JWT 인증/인가 , 유저 , 리스트 , CI/CD
* 나유화 : 보드 , ELK
* 정지윤 : 댓글 , 알람(SLACK BOT 활용) , SSE
* 조준호 : 카드 , 담당자 , 인덱싱
* 황호진 : 워크스페이스 , Redis 동시성 제어
  <br>

## 🚩 기능 구현

<details>
<summary>유저</summary>

* 회원가입
* 회원탈퇴
* 로그인
* 권한변경

</details>

<details>
<summary>워크스페이스</summary>

* 워크스페이스 조회
* 워크스페이스 생성
* 워크스페이스 수정
* 워크스페이스 삭제
* 워크스페이스 멤버초대

</details>

<details>
<summary>보드</summary>

* 보드 생성
* 보드 수정
* 보드 다건 조회
* 보드 단건 조회
* 보드 삭제

</details>

<details>
<summary>리스트</summary>

* 리스트 생성
* 리스트 수정
* 리스트 순서 조정
* 리스트 삭제

</details>

<details>
<summary>카드</summary>

* 카드 생성
* 카드 수정
* 카드 다건 조회
* 카드 상세 조회
* 카드 삭제
* 카드 상태 업데이트

</details>

<details>
<summary>담당자</summary>

* 매니저 등록

</details>

<details>
<summary>댓글</summary>

* 댓글 등록
* 댓글 수정
* 댓글 삭제

</details>

## ☁ 와이어프레임
https://www.figma.com/design/qQXpAfUTnn0wtCwxr0giZ5/team19_%ED%94%8C%EB%9F%AC%EC%8A%A4%EC%A3%BC%EC%B0%A8?node-id=0-1&node-type=canvas&t=rBTdZvIv9bdM5NKS-0

## ☁ ERD 다이어그램
![image](https://github.com/user-attachments/assets/d9367381-5f80-4a8a-9b77-db6bbf390802)

## 📑 API 명세서
![image](https://github.com/user-attachments/assets/9fad27cf-ed1d-48ce-ab2a-052c019dfa4c)
![image](https://github.com/user-attachments/assets/bb1529ab-f646-4708-be0d-6fbe4320453b)
![image](https://github.com/user-attachments/assets/c5c221f5-0497-4663-9c44-832ac2875230)
![image](https://github.com/user-attachments/assets/9caf4236-0c3c-46b2-9c95-60e3598d8bdd)
![image](https://github.com/user-attachments/assets/9d90e969-17fd-454c-89b5-dcb3b1a97a91)
![image](https://github.com/user-attachments/assets/1ed70796-c140-4d7a-bedf-714c24cd008e)

## 아키텍쳐

### Redis Cluster Architecture - 황호진

이 프로젝트에 사용된 Redis는 Cluster를 사용하여 **데이터 분산**, **고가용성**, **동시성 제어**를 구현하였습니다.
Redis 클러스터는 여러 개의 노드로 구성되어 있으며, 각 노드는 서로 다른 데이터의 파티션을 저장합니다. 
이를 통해 데이터 처리 성능을 극대화하고, 장애 발생 시 **Failover**를 통해 데이터를 안전하게 관리합니다.

#### 주요 특징
- **데이터 샤딩**: Redis 클러스터는 데이터를 여러 노드에 분산 저장하여 데이터 저장 및 조회 성능을 향상시킵니다.
- **고가용성**: Master-Slave 구조로 구성된 클러스터에서 Master 노드가 장애가 발생하면 Slave 노드가 자동으로 Master로 승격되어 지속적인 서비스를 보장합니다.
- **자동 Failover**: 노드 간 통신을 통해 노드 상태를 모니터링하고, 장애 시 자동으로 Failover가 발생하여 고가용성을 유지합니다.



#### 주요 설정

```yaml
# Redis 클러스터 설정
spring:
  redis:
    cluster:
      nodes: 127.0.0.1:7000, 127.0.0.1:7001, 127.0.0.1:7002, 127.0.0.1:7003, 127.0.0.1:7004, 127.0.0.1:7005
      max-redirects: 3
```

```java
// RedisConfig 설정
@Bean
public RedisConnectionFactory redisConnectionFactory() {
    List<RedisNode> redisNodes = nodes.stream()
            .map(node -> {
                String[] parts = node.split(":");
                return new RedisNode(parts[0], Integer.parseInt(parts[1]));
            }).toList();
    RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
    redisClusterConfiguration.setClusterNodes(redisNodes);
    return new LettuceConnectionFactory(redisClusterConfiguration);
}

@Bean
public RedissonClient redissonClient() {
    final Config config = new Config();

    ClusterServersConfig csc = config.useClusterServers()
            .setScanInterval(2000)
            .setConnectTimeout(100)
            .setTimeout(3000)
            .setRetryAttempts(3)
            .setRetryInterval(1500);

    nodes.forEach(node -> csc.addNodeAddress("redis://" + node));

    return Redisson.create(config);
}
```

#### 고가용성 테스트
![7](https://github.com/user-attachments/assets/3d4920c0-3de5-4c7e-a4c4-07dec240ca40)
![4](https://github.com/user-attachments/assets/4d65ff0c-d5c1-437f-bc15-cb477d1b72e0)

### 인덱싱 - 조준호
이 프로젝트에서는 검색 성능을 개선하기 위해 인덱스를 사용하였습니다.

데이터가 저장될 때, 검색 속도를 높이기 위해 데이터의 일부를 미리 정해두는 구조를 인덱싱이라고 합니다. 

이를 통해 전체 데이터를 탐색하지 않고도 원하는 데이터를 빠르게 찾을 수 있도록 돕습니다.

#### 주요 특징
빠른 검색 속도

인덱스를 사용하면 대용량 데이터셋에서 특정 데이터를 빠르게 찾을 수 있습니다.

여러 개의 컬럼을 결합한 복합 인덱스를 생성할 수 있습니다. 두 개 이상의 컬럼을 자주 조합해 검색할 때 유용합니다.

#### 우리 프로젝트의 인덱싱
분석: 모든 조회의 실행 계획을 분석한 결과, 성능이 낮은 컬럼을 식별하고, 최적화 전략으로 인덱싱을 구성하고. 활용도와 카디널리티를 고려하여 제목과 내용 컬럼의 성능을 최적화하기로 결정했습니다.

설계: 인덱스의 후보로 제목 및 내용의 복합 인덱스, 마감일 단일 인덱스, 제목과 마감일의 복합 인덱스를 고려하여 설계하기로 결정했습니다.

검증: 인덱스를 너무 많이 만들면 오히려 성능 저하가 우려되므로, 성능 최적화를 위해 자주 사용되는 인덱스를 따로 선정했습니다. 

팀원들과 협의한 결과, 제목과 내용의 복합 인덱스로만 구성하기로 결정. 그 결과, 최대 98%의 성능 개선이 이루어졌습니다.

![output (2)](https://github.com/user-attachments/assets/81d2d505-8951-4015-9165-813b59047a8b)

### 알림 - 정지윤
워크 스페이스 초대, 댓글 저장, 카드 수정을 했을 때 SlackBot으로 알림을 확인할 수 있습니다

#### 주요 특징
- **SSE (Server-Sent Events)**: 클라이언트가 서버에 구독을 요청하면 서버와 연결을 유지하고, 알림이 있을 때 마다 `SseEmitter`를 통해 클라이언트로 실시간 알림을 전송합니다
- **다양한 이벤트 처리**: 댓글 저장, 워크스페이스 초대, 카드 수정 등 다양한 이벤트를 처리하여 알림을 전송합니다.
- **SlackBot 연동**: SlackBotService와 연동하여 득정 알림을 Slack채널로 보낼 수 있도록 구성되어 있습니다.
 <br>  

### ELK / Grafana - 나유화
ELK 스택과 Grafana를 사용하여 애플리케이션 로그 및 성능 모니터링 환경을 구성했습니다.
- Elasticsearch: 데이터 인덱싱 및 검색을 담당. 7.17.3 버전을 사용하며, 싱글 노드로 실행됨.
- Logstash: 로그를 수집하여 Elasticsearch로 전송하는 파이프라인. 병렬 처리 워커 수 및 배치 크기와 지연 시간을 설정하여 성능을 조정.
- Kibana: Elasticsearch 데이터를 시각화하기 위한 대시보드. 5601 포트를 통해 접근 가능.
- Prometheus/Grafana: 시스템 및 애플리케이션 성능을 모니터링.

#### Kibana 로그 모니터링
![log.png](/images/log.png)

#### 부하 테스트
- 애플리케이션의 성능 및 안정성을 검증하기 위해 여러 개의 시나리오로 성능 테스트를 진행했습니다. 
- 주요 목표는 애플리케이션이 다양한 요청 수에 대해 일정한 성능을 유지할 수 있는지, 오류가 발생하지 않는지 확인하는 것입니다.

#### 테스트 환경 시나리오

| 요청 항목          | 요청 수  | 테스트 종류             | 측정 항목                    |
|-------------------|---------|-----------------------|-----------------------------|
| 회원가입 요청 1    | 1,000회 | 회원가입 처리 성능 테스트 | 최대 응답 시간, 평균 응답 시간, 오류율 |
| 회원가입 요청 2    | 3,000회 | 회원가입 처리 성능 테스트 | 최대 응답 시간, 평균 응답 시간, 오류율 |
| 회원가입 요청 3    | 5,000회 | 회원가입 처리 성능 테스트 | 최대 응답 시간, 평균 응답 시간, 오류율 |

#### 테스트 결과
![grafana.png](/images/grafana.png)

![jmeterhtml.png](/images/jmeterhtml.png)

## 트러블슈팅
<details>
  <summary>조준호</summary>
  <p> 1. 이미지와 DTO를 함께 보낼 때 415 에러 문제</p>
  <p>DTO 값을 보내면서 이미지 파일까지 추가해 보낼 때 문제가 발생하였습니다.<br>
  multipart/form-data로 전송했으나, @RequestBody는 application/json 형식으로 보내도록 되어 있어 충돌이 발생한 것입니다.<br>
  따라서 DTO를 @ModelAttribute 방식으로 바꾸어야 했고, DTO는 따로 전송하되 파일은 body에 작성하는 방법으로 문제를 해결했습니다.</p>
  
  <p> 2. 인덱싱 문제</p>
  <p>인덱싱을 했음에도 불구하고 성능이 개선되지 않는 문제를 발견했습니다.<br>
  인덱스는 B-트리를 사용해 정렬하므로, %ab%와 같은 패턴의 검색은 인덱스 적용이 불가능함을 확인하고.<br>
  트렐로 프로젝트를 참고해보니, 검색 시 ab%와 같은 형식의 검색을 지원하고 있었습니다.<br>
  따라서, 우리도 이를 적용해 검색을 ab% 방식으로 변경.<br>
  또한, 대소문자 구분을 하게 되면 쿼리 비용과 시간이 크게(최대 44%p) 증가하는 점을 고려해, 대소문자를 구분하지 않기로 결정하였습니다.</p>
  
  <p> 3. 매니저 인덱싱 조회 문제</p>
  <p>현 테이블 구조상 매니저를 인덱싱할 방법이 없다는 문제가 있었습니다.<br>
  추후 인덱싱을 원한다면 중간 테이블인 매니저에 유저의 이름을 넣어 인덱싱하는 방식이 가장 적합해 보입니다.</p>
</details>

<details>
  <summary>정지윤</summary>
  <p>1. 이벤트 처리 도중 오류가 발생해도 롤백이 되지 않는 문제</p>
  <p>댓글은 정상적으로 저장되지만 알림 전송 과정에서 발생한 에러가 있더라도 데이터가 롤백되지 않았습니다<br>
  이벤트 리스너에서 트랜잭션 관리가 제대로 이루어 지지 않으면, 댓글 저장 로직과 알림 전송 로직이 별도로 처리되어 문제가 발생할 수 있습니다.<br>
  따라서 `@EventListener`가 아니라 `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`로 변경하여 트랜잭션이 
  성공적으로 커밋 된 후 이벤트가 발생하도록 하여 해결하였습니다.</p>
</details>

<details>
  <summary>황호진</summary>
  <p>1. 로컬호스트에 Redis Cluster 구현 후 Spring boot에 적용시 에러</p>
  <p>에러명 : Error creating bean with name 'redissonClient' defined in class path resource<br>
  일반적인 RedisConfig 설정과는 다른 점이 있었고 Redis Cluster 용 설정하는 방법이 있었습니다. 그방법을 적용하여 해결했습니다.</p>

  <p>2. Redis Cluster 구축된거 Docker로 이관 작업시 에러</p>
  <p>클러스터가 제대로 생성안되는 에러가 발생했습니다.<br>
  이유가 뭔지 찾아가면서 도커 네트워크 ,Redis.conf을 수정하면서 Docker에 Redis Cluster에 구축에 성공하였습니다.</p>

  <p>3. Docker에 구축된 Redis Cluster를 Spring boot에 연결하는 작업에 에러</p>
  <p>Spring boot에서 redis cluster에 연결이 안되는 에러가 발생하였습니다.<br>
  이또한 2번 에러와 덩달아 수정을 수없이 하면서 진행해보았고 도달한 결론은 Spring boot 도 같은 Docker compose에 올려서
  동일 Docker network에 두어야지 연결이 가능하다는 점이였습니다. 이를 통해 해결했습니다.</p>
</details>


## 📑 프로젝트 후기

### 김나람
Github Actions의 CI를 통해 코드가 잘 돌아가는지 확인하고 머지할 수 있어서 좋았습니다.

### 나유화
ELK 및  Grafana를 통한 시스템 모니터링 방법을 습득할 수 있었습니다.


### 정지윤
SSE 기반의 실시간 통신과 이벤트에 관한 내용을 알게 되었습니다.

### 조준호
많은걸 배워가는 느낌이었습니다. 접근방식과 트러블슈팅과 여러 회의를통해 좋은내용을 알아가는 시간이었습니다.

### 황호진
Redis Cluster를 하면서 많이 힘들었지만 많은 것을 알게 되었습니다. 덕분에 docker-compose 또한 완벽하게 터득할수 있었습니다.
