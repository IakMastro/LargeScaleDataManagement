FROM maven

COPY my_files /my_files

WORKDIR /my_files/Covid19/

EXPOSE 8080
CMD ["./start_server.sh"]