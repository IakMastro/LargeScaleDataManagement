FROM maven

COPY my_files /my_files

WORKDIR /my_files/OlympicGamesTokyo/

RUN mvn install

CMD ["tail", "-f", "/dev/null"]