# JWT-authentication-demo

This is simple spring boot application with JWT token authorization

How to run

1) clone repo

2) mvn clean package (I use Apache Maven 3.6.0)

3) java -jar JWT-demo-0.0.1-SNAPSHOT.jar (I use jdk 1.8.0_202)

4) open http://localhost:8080/signin?redirect_url=/my-app/home/1

spring data automatically creates tables with users, and then inserts the default user from the import.sql script

after that you may signin by default user (username:admin password:admin)



