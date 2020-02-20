FROM openjdk:8-jre
ADD target/netflix-zuul-api-gateway-server-0.0.1-SNAPSHOT.jar zuul.jar
EXPOSE 8765
ENTRYPOINT ["java","-jar","/zuul.jar"]
