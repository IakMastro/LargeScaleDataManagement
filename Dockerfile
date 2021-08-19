FROM maven

COPY my_files /my_files

WORKDIR /my_files/Covid19/

RUN mvn install && mvn compile

CMD ["tail", "-f", "/dev/null"]