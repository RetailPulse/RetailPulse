# RetailPulse

Team Member:
Galvin Leow,
Evalyn Low Wei Xuan,
William Siling Tjhi,
Yap Chong Hwee Casper,

To run the **server-side applications**, please follow the steps below:
1. from Parent directory RetailPulse/
2. Run the following command to start the docker containers

   _docker compose -f rp-docker-compose.yml up --build_
3. to stop the docker containers

   _docker compose -f rp-docker-compose.yml down_

To run the **angular application**, please follow the steps below:
1. Open a new terminal. Change directory to ./rp-web-app
2. Execute:

   _npm install_

   _npm start_


[//]: # (To Run:)

[//]: # (1. Change directory to ./deploy/build_docker)

[//]: # (2. Execute:)

[//]: # (    docker rmi mysql-access:0.2.0)

[//]: # (    docker build -t mysql-access:0.2.0 -f build_access_sql.dockerfile .)

[//]: # (3. Change directory to ./deploy)

[//]: # (4. Execute:)

[//]: # (    docker compose -f 0_mysql_access.yaml up -d)

[//]: # (    docker compose -f 1_mysql_RP.yaml up -d)

[//]: # (5. Open a new terminal. Change directory to ./identity-access-management.)

[//]: # (6. Execute:)

[//]: # (  mvn clean install)

[//]: # (  mvn spring-boot:run)

[//]: # (7. Open a new terminal. Change directory to ./backend.)

[//]: # (8. Execute:)

[//]: # (  mvn clean install)

[//]: # (  mvn spring-boot:run)

[//]: # (9. Open a new terminal. Change directory to ./rp-web-app.)

[//]: # (10. Execute:)

[//]: # (  npm install)

[//]: # (  npm start)
