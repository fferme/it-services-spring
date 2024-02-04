FROM maven

WORKDIR /it-services-spring
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run