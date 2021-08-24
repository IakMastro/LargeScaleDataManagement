FROM maven

COPY my_files /my_files

WORKDIR /my_files/Covid19/

CMD ["tail", "-f", "/dev/null"]