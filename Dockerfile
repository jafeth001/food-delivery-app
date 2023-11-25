FROM openjdk:17-jdk
WORKDIR /app
EXPOSE 8080
COPY target/food-project-0.0.1-SNAPSHOT.jar /app/food.jar
CMD ["java","-jar","food.jar"]