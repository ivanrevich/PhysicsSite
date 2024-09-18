FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]


FROM node:18-alpine

WORKDIR /frontend

EXPOSE 3005

COPY ["/frontend/package.json", "/frontend/package-lock.json*", "./frontend"]

RUN npm install

COPY . .

CMD ["npm", "start"]