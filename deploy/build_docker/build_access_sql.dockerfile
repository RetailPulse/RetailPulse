FROM mysql:9.2.0

ENV MYSQL_ROOT_PASSWORD=password
ENV MYSQL_DATABASE=identity_access
ENV MYSQL_USER=user
ENV MYSQL_PASSWORD=password

COPY ../identity.init.sql /docker-entrypoint-initdb.d/init.sql

EXPOSE 3306

