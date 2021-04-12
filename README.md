[![Build Status](https://dev.azure.com/ebiz-project/uob_ajo/_apis/build/status/Maven%20with%20SonarCloud-CI?branchName=development)](https://dev.azure.com/ebiz-project/uob_ajo/_build/latest?definitionId=13&branchName=development)

# Getting Started

### Setting
The following guides illustrates how to use this module correctly:

Setting MySQL Credential in System Environment Variable:
- export MYSQL_HOSTNAME={{MYSQL_HOSTNAME}}
- export MYSQL_PORT={{MYSQL_PORT}}
- export MYSQL_DATABASE={{MYSQL_DATABASE}}
- export MYSQL_USERNAME={{MYSQL_USERNAME}}
- export MYSQL_PASSWORD={{MYSQL_PASSWORD}}
- export SECURITY_REALM={{SECURITY_REALM}}
- export KEYSTORE_PASSWORD={{KEYSTORE_PASSWORD}}
- export JWT_CLIENT_ID={{JWT_CLIENT_ID}}
- export JWT_CLIENT_SECRET={{JWT_CLIENT_SECRET}}
- export JWT_RESOURCE_IDS={{JWT_RESOURCE_IDS}}
- export APP_NAME={{APP_NAME}}
- export APP_NAME_DESC={{APP_NAME_DESC}}
- export AUTHOR_NAME={{AUTHOR_NAME}}
- export AUTHOR_EMAIL={{AUTHOR_EMAIL}}
- export SERVER_PORT={{SERVER_PORT}}

Setting Environment Variable in IntelliJ:
1. Go to Run Menu -> Edit Configuration
2. Choose Maven Profile -> Runner Tab
3. Set Environment Variables, example :
   - MYSQL_HOSTNAME={{hostname}}
   - MYSQL_PORT={{port}}
   - MYSQL_DATABASE={{database_name}}
   - MYSQL_USERNAME={{username}}
   - MYSQL_PASSWORD={{password}}
   - SECURITY_REALM={{SECURITY_REALM}}
   - KEYSTORE_PASSWORD={{KEYSTORE_PASSWORD}}
   - JWT_CLIENT_ID={{JWT_CLIENT_ID}}
   - JWT_CLIENT_SECRET={{JWT_CLIENT_SECRET}}
   - JWT_RESOURCE_IDS={{JWT_RESOURCE_IDS}}
   - APP_NAME={{APP_NAME}}
   - APP_NAME_DESC={{APP_NAME_DESC}}
   - AUTHOR_NAME={{AUTHOR_NAME}}
   - AUTHOR_EMAIL={{AUTHOR_EMAIL}}
   - SERVER_PORT={{SERVER_PORT}}

Swagger UI
- {{server}}/swagger-ui.html
- Disable when production mode

Application Info
- Set application info in yaml file

Monitoring using Prometheus
- We using docker for running prometheus
- Please re-check prometheus.yaml in targets configuration

Command line running Prometheus using docker :

`
docker run -d --name=prometheus -p 9090:9090 -v <PATH_TO_prometheus.yml_FILE>:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
`

Install Oracle JDBC :
mvn install:install-file -Dfile=jar/ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar