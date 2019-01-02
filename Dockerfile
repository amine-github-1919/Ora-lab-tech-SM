FROM oracle/graalvm-ce:1.0.0-rc10
MAINTAINER amine.achergui@mail.com

RUN gu install  -c python
RUN gu rebuild-images polyglot libpolyglot js llvm python

COPY ./build/libs/notebook-0.0.1-SNAPSHOT.jar /notebook.jar

ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/notebook.jar"]

EXPOSE 8080
