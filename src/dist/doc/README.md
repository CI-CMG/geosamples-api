# Geosamples API Service

## Installation
### Basic Installation Overview
1. Extract distribution bundle
1. Configure application properties
1. Install as a systemd service

### Java Installation and Distribution Bundle Extraction

1. Install the Java 1.8 or higher JRE
1. If running as a non-root user, set create that user ```useradd geosamples```. You may also want to increase the number of file descriptors for that user.
1. If not running as a service, it is recommended to set the JAVA_HOME environment variable
1. Download either the zip or the tar.gz archive from the repository and copy it to the install location
1. Run ```unzip geosamples-api-XXX.zip``` or ```tar -xvf geosamples-api-XXX.tar.gz```
1. If extracted as the root user, file ownership will be incorrect. Update file permissions with chown.  Ex. ```chown -R user:user service_dir```


### Configure Application
The service can be configured from multiple sources.  The primary two are a properties file and environment variables.  Between these two, environment variables 
have a higher priority and can override any values set in the property file.  This could be useful if it is desired that secrets be kept out of the
properties file.  This guide will cover configuring the application via properties.  If environment variables are desired, this link provides guidance on the
format for the environment variables: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html


The properties file is located at config/application.properties.  Most of the properties have sensible defaults.  However, some are blank and need to be 
configured.

The _${svc.home}_ placeholder can be used in properties files or environment variables and represents the absolute path to the service install location.


### Running the Service

To run the service manually, not as a systemd service, execute the _run.sh_ script in the directory where the application was extracted.


### Install As A Service

First, navigate to the svc directory.  Edit _install-service.properties_ and set the _USER_ and _JAVA_HOME_ properties.  Then run _install-service.sh_.

## Additional Configuration
### JVM Options
All JVM options passed to the application are located in _config/jvm.options_.  Lines starting with "#" are comments and will be ignored.
Sensible defaults have been selected.  The service will need to be restarted for changes to take effect.
