# TODO App
## 문제해결 전략
* Task간의 관련 참조는 참조 객체에서 추가 속성(이를테면 연결 일시 등)이 필요하지 않으므로 Join class 없이 @ManyToMany 활용
* 할일에 대하여 Close/Reopen 기능을 제공함
* Close시에는 하위 할일의 상태를 체크하지만, 반대로 Reopen시에는 상위 일감에 대한 상태 변경(Closed -> Open)에 대한 처리는 하지 않음
* API에 입출력에 관한 클래스와 Domain Model을 위한 클래스를 분리(VO/Domain)
* API 명세를 위하여 Swagger 적용
* 실환경에서는 Swagger를 사용하지 않기 위하여 프로퍼티로 사용여부를 설정 가능하게 함
* DB는 메모리 DB로 H2를 사용함

## 사용 기술
* JAVA 8, Spring Boot(WEB, JPA, H2)), Swagger
* jQuery, Bootstrap
 
## 필요 프로그램
[Java 8](https://www.azul.com/downloads/azure-only/zulu), [Git](https://git-scm.com/downloads/)

## 실행하기

### STEP 1 - 소스 내려받기 

```
git clone https://github.com/jhkim105/demo-todolist.git
cd demo-todolist
```  
    
### STEP 2 - 실행하기

빌드 없이 바로 시작하기
* Maven이 설치되어 있는 경우 mvn을 사용
* Maven이 설치되어 있지 않은 경우 mvnw (Windows에서는 mvnw.cmd) 사용 
```
./mvnw spring-boot:run
```
빌드 후 jar 파일 실행하기
```
./mvnw package
java -jar target/demo-todolist-0.0.1-SNAPSHOT.jar
```

### STEP 3- 접속하기
* TODO App 접속하기: [http://localhost:8080/](http://localhost:8080/)
* Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* h2-console:[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

