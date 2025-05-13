# mysql/Dockerfile
FROM mysql:8.0

ENV MYSQL_DATABASE=qima_db \
    MYSQL_USER=qima \
    MYSQL_PASSWORD=qima123 \
    MYSQL_ROOT_PASSWORD=root123

# Copy initialization SQL scripts
COPY ./init.sql /docker-entrypoint-initdb.d/data.sql