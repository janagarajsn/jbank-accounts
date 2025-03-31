# Build a mysql db image from latest mysql image
FROM mysql:8.0.33
# Copy the sql script
COPY schema.sql /docker-entrypoint-initdb.d/init.sql
# Expose the port
EXPOSE 3306
