
# Building the project

The project is based on spring-boot 1.5.18 and uses GraalVM EE 1.0.0-rc10 as the Java SDK.
Redis (latest version) is used for storing session information and Gradle 4.10.2 for dependency management.
You have to install GraalVM EE and set GRAALVM_HOME variable to point GraalVM's Contents/Home dir.
If you are using an IDE you should choose GraalVM as the SDK instead of the standard JDK and if you are using 
command line you have to set JAVA_HOME env variable to point to have the same value as GRAALVM_HOME.
Python component should be installed (using `gu` ) and a rebuild of the images will be asked after.

The command to be used for building the project:
 ```
 gradle clean build
  ```

# Testing
Under src/tests resides unit and integration tests. Integration tests use `IntTests` suffix, they need Docker compose
 to be installed for running properly. Integration tests will use a Redis instance launched through docker compose.
The command for running unit tests

 ```
 gradle test
  ```  
The command for running integration tests
```
gradle integrationTest
 ```
The global code coverage (unit and integration tests included) is:  **94,4%**
Code coverage for unit tests can be generated using:
```
gradle test jacocoTestReport
 ```


# Docker

Although a docker file and a docker compose are shipped, they are based on GraalVM-CE docker image.
The docker file builds a Docker image of the application using a modified version of GraalVM: python component
should be installed and a rebuild of all other images should be  done (though not tested because of demanding 
resources 
for the  rebuild).

After a build of the application, the following command will build and start the containers of the application:
```
docker-compose up
 ```
The application will be exposed at port 8080 and the `execute` operation will be at:

```
http://localhost:8080/api/interpreters/execute
```


# Design

The application is based on the Polyglot API of GraalVM for Python for implementing the Notebook execute feature. The 
current implementation supports python version 3.7.
The User session is managed via the `sessionId` request parameter (optional):
*  If not provided a new session is created and the response will contain a `SESSION-ID` header containing the id of 
the newly created Session in Redis.
*  If provided and its corresponding session exists, the previous state from the session is used to initialize the 
context before executing the new code. 
