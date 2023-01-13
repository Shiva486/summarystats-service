# summarystats-service

> **user**: ssuser, **password**: ss@Pass1

- To run application with docker, go to the project root and run the following commands:
  - docker build --tag=summarystats-server:latest .
  - docker-compose up
- To run application without docker, go to the project root and run the following commands:
    - mvn clean
    - mvn compile
    - mvn spring-boot:run
- To run tests:
  - mvn test
- Application port: 8600
- [Problem Statement](https://docs.google.com/document/d/1VLeLbYSCdOmZzjNmKIcpguEtABO8aeQSmmnq0LOSmC8/edit)
- [Swagger Link](http://localhost:8600/swagger-ui.html) (for documentation, api contracts and api description)
- [H2 Console](http://localhost:8600/h2-console)
- Environment variables with default values:
```properties
server.port=8600
spring.application.name=summarystats-service
logging.level.com.clipboardhealth.summarystatsservice=INFO

spring.datasource.url=jdbc:h2:mem:clipboarddb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.show-sql=false
spring.jpa.open-in-view=false
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.trace=false
spring.h2.console.settings.web-allow-others=false

# ----------------------------------
# ---- Tomcat container config  ----
# ----------------------------------
server.tomcat.accept-count=500
server.tomcat.max-connections=10000
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=80
```
<br>

#### Good coding practices followed in this application

- Have structured the code in a readable manner, separating business logic, controllers, services etc
- Have used querydsl for generating summary statistics instead of native sql or criteria api. Both have few limitations in terms of adding new constraints to the existing queries and type safety
- Have defined interfaces, abstract classes etc. wherever it was necessary
- Have defined currency as enum instead of String for type safety
- Have implemented a global exception handler for better error messages
