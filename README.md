# summarystats-service

- To run application from command line, go to the project root and run the following commands:
    - mvn clean
    - mvn compile
    - mvn spring-boot:run
- [Problem Statement](https://docs.google.com/document/d/1VLeLbYSCdOmZzjNmKIcpguEtABO8aeQSmmnq0LOSmC8/edit)
- [Swagger Link](http://localhost:8500/swagger-ui.html) (for documentation)
- [H2 Console](http://localhost:8500/h2-console)
- Environment variables with default values:
```properties
server.port=8500
spring.application.name=summarystats-service
logging.level.com.clipboardhealth.summarystatsservice=INFO

spring.datasource.url=jdbc:h2:mem:rilldb
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
