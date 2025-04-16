# geosamples-api
The geosamples-api is a web application that provides a REST API for public read-only access to the IMLGS database.

## Running the application

### Runtime Requirements
- Java 8+
- Oracle 18c+ Database

## Installation
1. Ensure JRE / JDK 8+ in installed and
2. Optionally, but recommended, set the JAVA_HOME environment variable
2. Unzip geosamples-api-X.X.X.zip
2. In the unzipped directory, edit config/application.properties and set parameters for the database connection and TLS keystore.
3. Run the application by executing run.sh to run in the foreground or start-background.sh to run in the background.

## Flyway Database Schema Migration
The database schema is defined in a shared library, geosamples-ingest-jpa, which this project depends on.  The Flyway tool can
be used to set up the schema.  To enable this feature, start the application with the following property set:
```properties
spring.flyway.enabled=true
```


## Development

### Development Requirements
- JDK 8+
- Maven 3.6.0+
- Docker

### Building From Source
```bash
mvn clean install
```

### Building From Source And Running Database Tests (Integration Tests)
```bash
mvn clean install -Pit
```