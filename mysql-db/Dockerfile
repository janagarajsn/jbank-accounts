# Build a mysql db image from latest mysql image
FROM mysql:8.0.33
#Set username
ENV MYSQL_USER=accounts
#Set password
ENV MYSQL_PASSWORD=accounts
# Set the root password
ENV MYSQL_ROOT_PASSWORD=root
# Set the database name
ENV MYSQL_DATABASE=accounts
# Copy the sql script
COPY schema.sql /docker-entrypoint-initdb.d/init.sql
# Expose the port
EXPOSE 3306
